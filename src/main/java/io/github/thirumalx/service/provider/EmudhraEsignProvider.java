package io.github.thirumalx.service.provider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.service.EsignProvider;
import io.github.thirumalx.service.XmlSignerService;
/**
 * @author Thirumal
 *         Implementation of the EsignProvider interface for Emudra eSign service.
 * EmudraEsignProvider
 */
@Component
public class EmudhraEsignProvider implements EsignProvider {

    private static final Logger logger = LoggerFactory.getLogger(EmudhraEsignProvider.class);

    private final XmlSignerService xmlSignerService;

    @Value("${esign.emudhra.aspId}")
    private String aspId;

    public EmudhraEsignProvider(XmlSignerService xmlSignerService) {
        this.xmlSignerService = xmlSignerService;
    }

    @Override
    public String getProviderCode() {
        return "eMudhra";
    }

    @Override
    public EsignResponseDto initiateSign(EsignDto esignDto) {
        logger.debug("Initiating eSign with Emudra for application: {}", esignDto.applicationId());
        try {
            // Construct raw XML
            // We use standard placeholder values for timestamp and responseUrl for now
            // responseUrl should point to our AadhaarEsignApplication callback endpoint
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String responseUrl = "https://your-app-domain.com/esign/callback/emudhra";
            String txn = "AS" + System.currentTimeMillis();
            
            // Compute document hash (SHA-256) of the file at esignDto.fileSourcePath()
            // Placeholder hash logic since file reading is not implemented yet
            String docHash = "4fd11688bf1aae8b964eccf96bd3ea363c8c259a960bd871d12dde36c7339a8f";

            String rawXml = String.format(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<Esign AuthMode=\"1\" aspId=\"%s\" ekycIdType=\"A\" responseSigType=\"PKCS7complete\" " +
                    "responseUrl=\"%s\" sc=\"Y\" ts=\"%s\" txn=\"%s\" ver=\"2.1\">" +
                    "<Docs><InputHash docInfo=\"IND_ESIGN_REGISTRATION\" hashAlgorithm=\"SHA256\" id=\"1\">%s</InputHash></Docs>" +
                    "</Esign>", 
                    aspId, responseUrl, timestamp, txn, docHash);

            // Sign XML natively
            String signedXml = xmlSignerService.signXml(rawXml);

            // Return signed XML string and action details
            String actionUrl = "https://authenticate.e-mudhra.com/AadhaareSign.jsp";
            return new EsignResponseDto(signedXml, txn, "application/xml", actionUrl, null);

        } catch (Exception e) {
            logger.error("Failed to generate and sign XML for eMudhra", e);
            throw new RuntimeException("XML Signing failed", e);
        }
    }   

    
}
