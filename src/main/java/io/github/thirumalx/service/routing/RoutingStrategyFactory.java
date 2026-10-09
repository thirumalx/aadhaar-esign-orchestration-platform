package io.github.thirumalx.service.routing;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class RoutingStrategyFactory {

    private final Map<String, RoutingStrategy> strategies;

    public RoutingStrategyFactory(List<RoutingStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(RoutingStrategy::getStrategyName, Function.identity()));
    }

    public RoutingStrategy getStrategy(String strategyName) {
        if (strategyName == null || strategyName.trim().isEmpty()) {
            return null;
        }
        return strategies.get(strategyName.toUpperCase());
    }
}

