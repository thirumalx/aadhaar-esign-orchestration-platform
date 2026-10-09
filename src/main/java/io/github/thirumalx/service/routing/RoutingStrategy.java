package io.github.thirumalx.service.routing;

import io.github.thirumalx.dto.EsignDto;

/**
 * Strategy interface for routing eSign requests to a provider.
 */
public interface RoutingStrategy {

    /**
     * Determines the provider code based on the strategy.
     * @param esignDto The eSign request.
     * @return providerCode if applicable, else null.
     */
    String determineProvider(EsignDto esignDto);

    /**
     * Unique name of the strategy (e.g. EXPLICIT, CHEAPEST, WEIGHTED).
     */
    String getStrategyName();
}

