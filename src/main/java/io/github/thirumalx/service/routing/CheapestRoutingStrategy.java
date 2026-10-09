package io.github.thirumalx.service.routing;

import java.util.List;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.SignatureProviderRepository;

@Component
public class CheapestRoutingStrategy implements RoutingStrategy {

    private final Logger logger = LoggerFactory.getLogger(CheapestRoutingStrategy.class);
    private final SignatureProviderRepository repository;

    public CheapestRoutingStrategy(SignatureProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public String determineProvider(EsignDto esignDto) {
        List<SignatureProvider> providers = repository.findAll();
        if (providers == null || providers.isEmpty()) {
            return null;
        }

        SignatureProvider cheapest = providers.stream()
                .filter(p -> p.cost() != null)
                .min(Comparator.comparing(SignatureProvider::cost))
                .orElse(null);

        if (cheapest != null) {
            logger.debug("Cheapest routing strategy selected provider: {} with cost: {}", cheapest.providerCode(), cheapest.cost());
            return cheapest.providerCode();
        }

        return null;
    }

    @Override
    public String getStrategyName() {
        return "CHEAPEST";
    }
}

