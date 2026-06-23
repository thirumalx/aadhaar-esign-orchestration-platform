package io.github.thirumalx.repository;

import io.github.thirumalx.model.ProviderCapability;
import java.util.List;

/**
 * @author Thirumal M
 *         Provider capability repository interface.
 */
public interface ProviderCapabilityRepository {
    Integer save(ProviderCapability providerCapability);

    ProviderCapability findById(Integer id);

    List<ProviderCapability> findAll();

    int update(ProviderCapability providerCapability);

    int delete(Integer id);
}
