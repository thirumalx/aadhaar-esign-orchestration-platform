package io.github.thirumalx.service.provider;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.exception.ResourceNotFoundException;
import io.github.thirumalx.model.ProviderConfiguration;
import io.github.thirumalx.repository.ProviderConfigurationRepository;
import io.github.thirumalx.service.EsignProvider;
import io.github.thirumalx.service.XmlSignerService;

/**
 * @author Thirumal
 *         CdacEsignProvider
 */
@Service
public class CdacEsignProvider implements EsignProvider {

    private final Logger logger = LoggerFactory.getLogger(CdacEsignProvider.class);
    private final XmlSignerService xmlSignerService;
    private final ProviderConfigurationRepository providerConfigurationRepository;
    private final Environment environment;

    public CdacEsignProvider(XmlSignerService xmlSignerService, ProviderConfigurationRepository providerConfigurationRepository,
                             Environment environment) {
        this.xmlSignerService = xmlSignerService;
        this.providerConfigurationRepository = providerConfigurationRepository;
        this.environment = environment;
    }

    @Value("${cdac.aspId}")
    private String aspId;
    @Value("${cdac.authMode}")
    private String authMode;
    @Value("${cdac.ekycIdType}")
    private String ekycIdType;
    @Value("${cdac.responseSigType}")
    private String responseSigType;
    @Value("${cdac.sc}")
    private String sc;
    @Value("${cdac.docInfo}")
    private String docInfo;
    @Value("${cdac.hashAlgorithm}")
    private String hashAlgorithm;
    @Value("${cdac.responseUrl}")
    private String responseUrl;
    @Value("${cdac.keystorePath}")
    private Resource keystorePath;
    @Value("${cdac.keystorePassword}")
    private String keystorePassword;
    @Value("${cdac.esignFormUrl}")
    private String esignFormUrl;

    @Override
    public String getProviderCode() {
        return "cdac";
    }

    @Override
    public EsignResponseDto initiateSign(EsignDto esignDto) {
        logger.info("Initiating eSign with CDAC for {}", esignDto.signId());
        try {
            // Fetch configuration dynamically from DB based on environment
            ProviderConfiguration config = providerConfigurationRepository
                    .findByProviderCodeAndEnvironment(getProviderCode(), getEnvironmentCd(environment))
                    .orElseThrow(() -> new ResourceNotFoundException("Provider configuration not found for CDAC in current environment"));
            String aspId = config.aspId();
            String esignFormUrl = config.apiUrl();

            // 1. Prepare PDF Signature Appearance and Hash
            PdfReader pdfReader = new PdfReader(esignDto.fileSourcePath());
            AcroFields acroFields = pdfReader.getAcroFields();
            List<String> signatureNames = acroFields.getSignatureNames();

            FileOutputStream os = new FileOutputStream(esignDto.fileDestinationPath());
            PdfStamper stamper1;
            if (!signatureNames.isEmpty()) {
                stamper1 = PdfStamper.createSignature(pdfReader, os, '\0', null, true);
            } else {
                stamper1 = PdfStamper.createSignature(pdfReader, os, '\0');
            }

            PdfSignatureAppearance appearance = stamper1.getSignatureAppearance();
            appearance.setVisibleSignature(new Rectangle(0, 0, 150, 90), pdfReader.getNumberOfPages(), "Esign_" + esignDto.signId());
            appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);

            PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
            appearance.setCryptoDictionary(dic);
            HashMap<PdfName, Integer> exc = new HashMap<>();
            exc.put(PdfName.CONTENTS, 8192 * 2 + 2);
            appearance.preClose(exc);

            InputStream pdfStreamAndDictionery = appearance.getRangeStream();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = pdfStreamAndDictionery.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            String sha256hex = hexString.toString();

            pdfStreamAndDictionery.close();
            // Note: we leave preClose active, the PDF will be closed when the CDAC response is received.

            // 2. Generate eSign XML
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String ts = sd.format(new Date());
            String txn = esignDto.signId() != null ? esignDto.signId() : "ASP-" + System.currentTimeMillis();
            String authMode = esignDto.authMode() != null ? esignDto.authMode() : "1"; // Default to OTP
            String consent = esignDto.consent() != null ? esignDto.consent() : "Y";
            String responseUrl = esignDto.successUrl() != null ? esignDto.successUrl() : "https://your-app-domain.com/esign/callback/cdac";
            String ekycIdType = "A";
            String responseSigType = "PKCS7complete";
            String docInfo = "IND_ESIGN_REGISTRATION";
            String hashAlgorithm = "SHA256";
            
            String s1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            String s2 = "<Esign AuthMode=\"" + authMode + "\" aspId=\"" + aspId + "\" ekycId=\"\" ekycIdType=\"" + ekycIdType + "\" responseSigType=\"" + responseSigType + "\" responseUrl=\"" + responseUrl + "\" sc=\"" + sc + "\" ts=\"" + ts + "\" txn=\"" + txn + "\" ver=\"2.1\">";
            String docHash = "<Docs>\n<InputHash docInfo=\"" + docInfo + "\" hashAlgorithm=\"" + hashAlgorithm + "\" id=\"1\">" + sha256hex + "</InputHash>\n</Docs>\n</Esign>";
            String eSignXmlStr = s1 + "\n" + s2 + docHash;
            // 3. Sign the XML natively using our central XmlSignerService
            String signedXml = xmlSignerService.signXml(eSignXmlStr);

            return new EsignResponseDto(signedXml, txn, "application/xml", esignFormUrl, null);
        } catch (Exception e) {
            logger.error("Error during CDAC eSign initiation", e);
            return new EsignResponseDto(null, null, null, null, "Error preparing request: " + e.getMessage());
        }
    }
}
