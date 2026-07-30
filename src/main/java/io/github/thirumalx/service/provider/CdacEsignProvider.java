package io.github.thirumalx.service.provider;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.service.EsignProvider;

/**
 * @author Thirumal
 *         CdacEsignProvider
 */
@Service
public class CdacEsignProvider implements EsignProvider {

    private final Logger logger = LoggerFactory.getLogger(CdacEsignProvider.class);

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
            String txn = "ASP-" + esignDto.signId();
            
            String s1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            String s2 = "<Esign AuthMode=\"" + authMode + "\" aspId=\"" + aspId + "\" ekycId=\"\" ekycIdType=\"" + ekycIdType + "\" responseSigType=\"" + responseSigType + "\" responseUrl=\"" + responseUrl + "\" sc=\"" + sc + "\" ts=\"" + ts + "\" txn=\"" + txn + "\" ver=\"2.1\">";
            String docHash = "<Docs>\n<InputHash docInfo=\"" + docInfo + "\" hashAlgorithm=\"" + hashAlgorithm + "\" id=\"1\">" + sha256hex + "</InputHash>\n</Docs>\n</Esign>";
            String eSignXmlStr = s1 + "\n" + s2 + docHash;

            // 3. Sign the XML
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                    null, null);

            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (InputStream ksIs = keystorePath.getInputStream()) {
                ks.load(ksIs, keystorePassword.toCharArray());
            }
            String alias = ks.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keystorePassword.toCharArray());
            X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

            KeyInfoFactory kif = fac.getKeyInfoFactory();
            List<Object> x509Content = new ArrayList<>();
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);
            X509Data xd = kif.newX509Data(x509Content);
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document docToSign = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(eSignXmlStr.getBytes(StandardCharsets.UTF_8)));

            DOMSignContext dsc = new DOMSignContext(privateKey, docToSign.getDocumentElement());
            XMLSignature signature = fac.newXMLSignature(si, ki);
            signature.sign(dsc);

            StringWriter writer = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.transform(new DOMSource(docToSign), new StreamResult(writer));
            String signedXml = writer.getBuffer().toString();

            return new EsignResponseDto(signedXml, txn, "application/xml", esignFormUrl, null);
        } catch (Exception e) {
            logger.error("Error during CDAC eSign initiation", e);
            return new EsignResponseDto(null, null, null, null, "Error preparing request: " + e.getMessage());
        }
    }
}
