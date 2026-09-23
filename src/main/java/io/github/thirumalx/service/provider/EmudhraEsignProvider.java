package io.github.thirumalx.service.provider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.exception.ResourceNotFoundException;
import io.github.thirumalx.repository.ProviderConfigurationRepository;
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
    private final ProviderConfigurationRepository providerConfigurationRepository;
    private final Environment environment;

    public EmudhraEsignProvider(XmlSignerService xmlSignerService,
                                ProviderConfigurationRepository providerConfigurationRepository,
                                Environment environment) {
        this.xmlSignerService = xmlSignerService;
        this.providerConfigurationRepository = providerConfigurationRepository;
        this.environment = environment;
    }

    @Override
    public String getProviderCode() {
        return "eMudhra";
    }

    @Override
    public EsignResponseDto initiateSign(EsignDto esignDto) {
        logger.debug("Initiating eSign with Emudra for application: {}", esignDto.applicationId());
        try {
            // Fetch configuration dynamically from DB based on environment
            var config = providerConfigurationRepository
                    .findByProviderCodeAndEnvironment(getProviderCode(), getEnvironmentCd(environment))
                    .orElseThrow(() -> new ResourceNotFoundException("Provider configuration not found for eMudhra in current environment"));
            String aspId = config.aspId();
            String actionUrl = config.apiUrl();

            // Construct raw XML using the unified EsignDto
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String responseUrl = esignDto.successUrl() != null ? esignDto.successUrl() : "https://your-app-domain.com/esign/callback/emudhra";
            String txn = esignDto.signId() != null ? esignDto.signId() : "AS" + System.currentTimeMillis();
            String authMode = esignDto.authMode() != null ? esignDto.authMode() : "1"; // Default to OTP
            String consent = esignDto.consent() != null ? esignDto.consent() : "Y";
            
            // Compute document hash (SHA-256) of the file at esignDto.fileSourcePath()
            // Placeholder hash logic since file reading is not implemented yet
            String docHash = "4fd11688bf1aae8b964eccf96bd3ea363c8c259a960bd871d12dde36c7339a8f";

            // Mapping to the CCA / C-DAC standard <Esign> tag
            String rawXml = String.format(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<Esign AuthMode=\"%s\" aspId=\"%s\" ekycIdType=\"A\" responseSigType=\"PKCS7complete\" " +
                    "responseUrl=\"%s\" sc=\"%s\" ts=\"%s\" txn=\"%s\" ver=\"2.1\">" +
                    "<Docs><InputHash docInfo=\"IND_ESIGN_REGISTRATION\" hashAlgorithm=\"SHA256\" id=\"1\">%s</InputHash></Docs>" +
                    "</Esign>", 
                    authMode, aspId, responseUrl, consent, timestamp, txn, docHash);

            // Sign XML natively
            String signedXml = xmlSignerService.signXml(rawXml);

            // Return signed XML string and action details
            return new EsignResponseDto(signedXml, txn, "application/xml", actionUrl, null);

        } catch (Exception e) {
            logger.error("Failed to generate and sign XML for eMudhra", e);
            throw new RuntimeException("XML Signing failed", e);
        }
    }   

    
}
