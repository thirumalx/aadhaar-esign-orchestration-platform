package io.github.thirumalx.repository;

import io.github.thirumalx.model.ProviderConfiguration;
import java.util.List;

/**
 * @author Thirumal M
 *         Provider configuration repository interface.
 */
public interface ProviderConfigurationRepository {
    Long save(ProviderConfiguration providerConfiguration);

    ProviderConfiguration findById(Long id);

    List<ProviderConfiguration> findAll();

    int update(ProviderConfiguration providerConfiguration);

    int delete(Long id);
}
