package io.github.thirumalx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.SignatureProviderRepository;

@Service
public class EsignOrchestratorService {

    private Logger logger = LoggerFactory.getLogger(EsignOrchestratorService.class);

    private final EsignProviderFactory providerFactory;
    private final SignatureProviderRepository signatureProviderRepository;

    public EsignOrchestratorService(EsignProviderFactory providerFactory,
            SignatureProviderRepository signatureProviderRepository) {
        this.providerFactory = providerFactory;
        this.signatureProviderRepository = signatureProviderRepository;
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
        // If providerCode is not passed, fetch based on applicationId or fallback to
        // highest priority
        if (providerCode == null || providerCode.trim().isEmpty()) {
            SignatureProvider preferredProvider = null;
            if (esignDto.applicationId() != null && !esignDto.applicationId().trim().isEmpty()) {
                preferredProvider = signatureProviderRepository.findByApplicationId(esignDto.applicationId());
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
