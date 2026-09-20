package io.github.thirumalx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.model.Application;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.ApplicationRepository;
import io.github.thirumalx.repository.SignatureProviderRepository;
/**
 * @author Thirumal
 * Service class responsible for orchestrating the eSign process.
 * EsignOrchestratorService
 */
@Service
public class EsignOrchestratorService {

    private Logger logger = LoggerFactory.getLogger(EsignOrchestratorService.class);

    private final EsignProviderFactory providerFactory;
    private final SignatureProviderRepository signatureProviderRepository;
    private final ApplicationRepository applicationRepository;

    public EsignOrchestratorService(EsignProviderFactory providerFactory,
            SignatureProviderRepository signatureProviderRepository,
            ApplicationRepository applicationRepository) {
        this.providerFactory = providerFactory;
        this.signatureProviderRepository = signatureProviderRepository;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Initiates eSign process.
     *
     * @param esignDto The eSign request details.
     * @return The result of the eSign process.
     */
    public EsignResponseDto initiateEsign(EsignDto esignDto) {
        logger.debug("Initiate eSign request for application: {}", esignDto.applicationId());
        String providerCode = esignDto.providerCode();
        Long applicationId = esignDto.applicationId();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for ID: " + applicationId));
        logger.debug("Application found: {}", application.applicationName());
        // If providerCode is not passed, fetch based on applicationId or fallback to
        // highest priority
        if (providerCode == null || providerCode.trim().isEmpty()) {
            SignatureProvider preferredProvider = null;
            if (esignDto.applicationId() != null) {
                preferredProvider = signatureProviderRepository.findByApplicationId(applicationId)
                        .orElse(null);
            }
            if (preferredProvider == null) {
                preferredProvider = signatureProviderRepository.findTopPriority();
            }

            if (preferredProvider == null) {
                throw new IllegalStateException("No default eSign provider configured.");
            }
            providerCode = preferredProvider.providerCode();
        }

        // Get the specific provider strategy
        EsignProvider provider = providerFactory.getProvider(providerCode);
        logger.debug("Signature will be assigned to {}", provider.getProviderCode());
        // Execute the strategy
        return provider.initiateSign(esignDto);
    }
}
