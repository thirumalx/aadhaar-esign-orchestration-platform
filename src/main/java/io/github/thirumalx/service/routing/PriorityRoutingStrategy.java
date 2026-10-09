package io.github.thirumalx.service.routing;

import org.springframework.stereotype.Component;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.SignatureProviderRepository;

@Component
public class PriorityRoutingStrategy implements RoutingStrategy {

    private final SignatureProviderRepository repository;

    public PriorityRoutingStrategy(SignatureProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public String determineProvider(EsignDto esignDto) {
        SignatureProvider topPriority = repository.findTopPriority();
        return topPriority != null ? topPriority.providerCode() : null;
    }

    @Override
    public String getStrategyName() {
        return "PRIORITY";
    }
}

