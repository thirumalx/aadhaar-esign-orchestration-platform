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
    private final io.github.thirumalx.service.routing.RoutingStrategyFactory routingStrategyFactory;

    public EsignOrchestratorService(EsignProviderFactory providerFactory,
            SignatureProviderRepository signatureProviderRepository,
            ApplicationRepository applicationRepository,
            io.github.thirumalx.service.routing.RoutingStrategyFactory routingStrategyFactory) {
        this.providerFactory = providerFactory;
        this.signatureProviderRepository = signatureProviderRepository;
        this.applicationRepository = applicationRepository;
        this.routingStrategyFactory = routingStrategyFactory;
    }

    /**
     * Initiates eSign process.
     *
     * @param esignDto The eSign request details.
     * @return The result of the eSign process.
     */
    public EsignResponseDto initiateEsign(EsignDto esignDto) {
        logger.debug("Initiate eSign request for application: {}", esignDto.applicationId());
        Long applicationId = esignDto.applicationId();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for ID: " + applicationId));
        logger.debug("Application found: {}", application.applicationName());
        String providerCode = null;

        // 1. If consumer specifically requested a routing strategy (e.g. CHEAPEST, WEIGHTED)
        if (esignDto.routingStrategy() != null) {
            io.github.thirumalx.service.routing.RoutingStrategy requestedStrategy = routingStrategyFactory.getStrategy(esignDto.routingStrategy());
            if (requestedStrategy != null) {
                providerCode = requestedStrategy.determineProvider(esignDto);
                logger.debug("Routing strategy '{}' determined provider: {}", esignDto.routingStrategy(), providerCode);
            }
        }

        // 2. EXPLICIT: Consumer passed a specific providerCode
        if (providerCode == null) {
            io.github.thirumalx.service.routing.RoutingStrategy explicitStrategy = routingStrategyFactory.getStrategy("EXPLICIT");
            providerCode = explicitStrategy.determineProvider(esignDto);
            if (providerCode != null) {
                logger.debug("Explicit strategy determined provider: {}", providerCode);
            }
        }

        // 3. App-level Preferred Provider
        if (providerCode == null && applicationId != null) {
            SignatureProvider preferredProvider = signatureProviderRepository.findByApplicationId(applicationId).orElse(null);
            if (preferredProvider != null) {
                providerCode = preferredProvider.providerCode();
                logger.debug("Application preferred provider selected: {}", providerCode);
            }
        }

        // 4. Fallback: PRIORITY
        if (providerCode == null) {
            io.github.thirumalx.service.routing.RoutingStrategy priorityStrategy = routingStrategyFactory.getStrategy("PRIORITY");
            providerCode = priorityStrategy.determineProvider(esignDto);
            logger.debug("Priority strategy fallback determined provider: {}", providerCode);
        }

        if (providerCode == null || providerCode.trim().isEmpty()) {
            throw new IllegalStateException("No eSign provider could be determined via routing strategies.");
        }

        // Get the specific provider implementation
        EsignProvider provider = providerFactory.getProvider(providerCode);
        logger.debug("Signature will be assigned to {}", provider.getProviderCode());
        // Execute the strategy
        return provider.initiateSign(esignDto);
    }
}
