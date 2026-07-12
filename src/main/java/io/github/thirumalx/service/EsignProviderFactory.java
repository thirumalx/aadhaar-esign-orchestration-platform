package io.github.thirumalx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class EsignProviderFactory {

    private final Map<String, EsignProvider> providers = new HashMap<>();

    public EsignProviderFactory(List<EsignProvider> providerList) {
        for (EsignProvider provider : providerList) {
            providers.put(provider.getProviderCode().toLowerCase(), provider);
        }
    }

    /**
     * Get the EsignProvider by provider code.
     * 
     * @param providerCode the provider code
     * @return the EsignProvider
     * @throws IllegalArgumentException if provider code is not found
     */
    public EsignProvider getProvider(String providerCode) {
        if (providerCode == null || providerCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider code cannot be null or empty");
        }
        
        EsignProvider provider = providers.get(providerCode.toLowerCase());
        
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported eSign provider: " + providerCode);
        }
        
        return provider;
    }
}
