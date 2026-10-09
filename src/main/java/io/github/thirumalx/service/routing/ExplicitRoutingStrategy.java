package io.github.thirumalx.service.routing;

import org.springframework.stereotype.Component;
import io.github.thirumalx.dto.EsignDto;

@Component
public class ExplicitRoutingStrategy implements RoutingStrategy {

    @Override
    public String determineProvider(EsignDto esignDto) {
        if (esignDto.providerCode() != null && !esignDto.providerCode().trim().isEmpty()) {
            return esignDto.providerCode();
        }
        return null;
    }

    @Override
    public String getStrategyName() {
        return "EXPLICIT";
    }
}

