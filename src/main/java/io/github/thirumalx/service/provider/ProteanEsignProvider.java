package io.github.thirumalx.service.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.service.EsignProvider;
import io.github.thirumalx.service.XmlSignerService;
import io.github.thirumalx.repository.ProviderConfigurationRepository;

/**
 * @author Thirumal
 *         ProteanEsignProvider
 */
@Service
public class ProteanEsignProvider implements EsignProvider {

    private final Logger logger = LoggerFactory.getLogger(ProteanEsignProvider.class);

    private final io.github.thirumalx.service.XmlSignerService xmlSignerService;
    private final io.github.thirumalx.repository.ProviderConfigurationRepository providerConfigurationRepository;
    private final org.springframework.core.env.Environment environment;
    private final XmlSignerService xmlSignerService;
    private final ProviderConfigurationRepository providerConfigurationRepository;
    private final Environment environment;

    public ProteanEsignProvider(io.github.thirumalx.service.XmlSignerService xmlSignerService,
                                io.github.thirumalx.repository.ProviderConfigurationRepository providerConfigurationRepository,
                                org.springframework.core.env.Environment environment) {
    public ProteanEsignProvider(XmlSignerService xmlSignerService,
                                ProviderConfigurationRepository providerConfigurationRepository,
                                Environment environment) {
        this.xmlSignerService = xmlSignerService;
        this.providerConfigurationRepository = providerConfigurationRepository;
        this.environment = environment;
    }

    @Override
    public String getProviderCode() {
        return "protean";
    }

    private Short getEnvironmentCd() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0].toLowerCase();
            if (profile.contains("prod")) return 3;
            if (profile.contains("uat")) return 2;
        }
        return 1; // Default to DEV
    }

    @Override
    public EsignResponseDto initiateSign(EsignDto esignDto) {
        logger.info("Initiating eSign with Protean for {}", esignDto.signId());
        try {
            // Fetch configuration dynamically from DB based on environment
            io.github.thirumalx.model.ProviderConfiguration config = providerConfigurationRepository
                    .findByProviderCodeAndEnvironment(getProviderCode(), getEnvironmentCd());
                    .findByProviderCodeAndEnvironment(getProviderCode(), getEnvironmentCd(environment));
            
            if (config == null) {
                throw new RuntimeException("Provider configuration not found for Protean in current environment");
            }

            String aspId = config.aspId();
            String actionUrl = config.apiUrl();

            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String responseUrl = esignDto.successUrl() != null ? esignDto.successUrl() : "https://your-app-domain.com/esign/callback/protean";
            String txn = esignDto.signId() != null ? esignDto.signId() : "ASP-" + System.currentTimeMillis();
            String authMode = esignDto.authMode() != null ? esignDto.authMode() : "1"; // Default to OTP
            String consent = esignDto.consent() != null ? esignDto.consent() : "Y";
            
            String docHash = "4fd11688bf1aae8b964eccf96bd3ea363c8c259a960bd871d12dde36c7339a8f";

            // Mapping to the CCA / C-DAC standard <Esign> tag
            String rawXml = String.format(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<Esign AuthMode=\"%s\" aspId=\"%s\" ekycIdType=\"A\" responseSigType=\"PKCS7complete\" " +
                    "responseUrl=\"%s\" sc=\"%s\" ts=\"%s\" txn=\"%s\" ver=\"2.1\">" +
                    "<Docs><InputHash docInfo=\"IND_ESIGN_REGISTRATION\" hashAlgorithm=\"SHA256\" id=\"1\">%s</InputHash></Docs>" +
                    "</Esign>", 
                    authMode, aspId, responseUrl, consent, timestamp, txn, docHash);

            String signedXml = xmlSignerService.signXml(rawXml);

            return new EsignResponseDto(signedXml, txn, "application/xml", actionUrl, null);

        } catch (Exception e) {
            logger.error("Failed to generate and sign XML for Protean", e);
            return new EsignResponseDto(null, null, null, null, "XML Signing failed: " + e.getMessage());
        }
    }
}
