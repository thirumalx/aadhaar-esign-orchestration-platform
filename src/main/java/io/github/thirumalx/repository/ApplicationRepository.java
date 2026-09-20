package io.github.thirumalx.repository;

import io.github.thirumalx.model.Application;
import java.util.List;
import java.util.Optional;

/**
 * @author Thirumal M
 *         Application repository interface.
 */
public interface ApplicationRepository {
    Long save(Application application);

    Optional<Application> findById(Long id);

    List<Application> findAll();

    int update(Application application);

    int delete(Long id);

    Optional<Application> findByCode(String applicationCode);
}
