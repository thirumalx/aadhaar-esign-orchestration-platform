package io.github.thirumalx.service;

import org.springframework.stereotype.Service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.SignatureProviderRepository;

@Service
public class EsignOrchestratorService {

    private final EsignProviderFactory providerFactory;
    private final SignatureProviderRepository signatureProviderRepository;

    public EsignOrchestratorService(EsignProviderFactory providerFactory, 
                                    SignatureProviderRepository signatureProviderRepository) {
        this.providerFactory = providerFactory;
        this.signatureProviderRepository = signatureProviderRepository;
    }

    public String initiateEsign(EsignDto esignDto) {
        String providerCode = esignDto.providerCode();
        
        // If providerCode is not passed, fetch the highest priority provider from the database
        if (providerCode == null || providerCode.trim().isEmpty()) {
            SignatureProvider preferredProvider = signatureProviderRepository.findTopPriority();
            if (preferredProvider == null) {
                throw new IllegalStateException("No default eSign provider configured.");
            }
            providerCode = preferredProvider.providerCode();
        }

        // Get the specific provider strategy
        EsignProvider provider = providerFactory.getProvider(providerCode);

        // Execute the strategy
        return provider.initiateSign(esignDto);
    }
}
