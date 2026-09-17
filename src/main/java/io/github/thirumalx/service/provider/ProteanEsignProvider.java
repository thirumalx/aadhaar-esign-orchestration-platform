package io.github.thirumalx.service.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.service.EsignProvider;

/**
 * @author Thirumal
 *         ProteanEsignProvider
 */
@Service
public class ProteanEsignProvider implements EsignProvider {

    private final Logger logger = LoggerFactory.getLogger(ProteanEsignProvider.class);

    private final io.github.thirumalx.service.XmlSignerService xmlSignerService;

    @org.springframework.beans.factory.annotation.Value("${esign.protean.aspId:PRODNESLNEW}")
    private String aspId;

    public ProteanEsignProvider(io.github.thirumalx.service.XmlSignerService xmlSignerService) {
        this.xmlSignerService = xmlSignerService;
    }

    @Override
    public String getProviderCode() {
        return "protean";
    }

    @Override
    public EsignResponseDto initiateSign(EsignDto esignDto) {
        logger.info("Initiating eSign with Protean for {}", esignDto.signId());
        try {
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String responseUrl = "https://your-app-domain.com/esign/callback/protean";
            String txn = "ASP-" + System.currentTimeMillis();
            String docHash = "4fd11688bf1aae8b964eccf96bd3ea363c8c259a960bd871d12dde36c7339a8f";

            String rawXml = String.format(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<Esign AuthMode=\"1\" aspId=\"%s\" ekycIdType=\"A\" responseSigType=\"PKCS7complete\" " +
                    "responseUrl=\"%s\" sc=\"Y\" ts=\"%s\" txn=\"%s\" ver=\"2.1\">" +
                    "<Docs><InputHash docInfo=\"IND_ESIGN_REGISTRATION\" hashAlgorithm=\"SHA256\" id=\"1\">%s</InputHash></Docs>" +
                    "</Esign>", 
                    aspId, responseUrl, timestamp, txn, docHash);

            String signedXml = xmlSignerService.signXml(rawXml);
            String actionUrl = "https://esign.nsdl.com/AadhaareSign.jsp"; // Placeholder Protean URL

            return new EsignResponseDto(signedXml, txn, "application/xml", actionUrl, null);

        } catch (Exception e) {
            logger.error("Failed to generate and sign XML for Protean", e);
            return new EsignResponseDto(null, null, null, null, "XML Signing failed: " + e.getMessage());
        }
    }
}
