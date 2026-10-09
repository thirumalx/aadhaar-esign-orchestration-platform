package io.github.thirumalx.service.routing;

import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.SignatureProviderRepository;

@Component
public class WeightedRoutingStrategy implements RoutingStrategy {

    private final Logger logger = LoggerFactory.getLogger(WeightedRoutingStrategy.class);
    private final SignatureProviderRepository repository;
    private final SecureRandom random = new SecureRandom();

    public WeightedRoutingStrategy(SignatureProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public String determineProvider(EsignDto esignDto) {
        List<SignatureProvider> providers = repository.findAll();
        if (providers == null || providers.isEmpty()) {
            return null;
        }

        // Filter providers that have a defined weight > 0
        List<SignatureProvider> weightedProviders = providers.stream()
                .filter(p -> p.weight() != null && p.weight() > 0)
                .toList();

        if (weightedProviders.isEmpty()) {
            return null;
        }

        int totalWeight = weightedProviders.stream().mapToInt(SignatureProvider::weight).sum();
        int randomValue = random.nextInt(totalWeight);

        int currentWeight = 0;
        for (SignatureProvider provider : weightedProviders) {
            currentWeight += provider.weight();
            if (randomValue < currentWeight) {
                logger.debug("Weighted routing strategy selected provider: {} (random: {}, total: {})", provider.providerCode(), randomValue, totalWeight);
                return provider.providerCode();
            }
        }

        return null;
    }

    @Override
    public String getStrategyName() {
        return "WEIGHTED";
    }
}

