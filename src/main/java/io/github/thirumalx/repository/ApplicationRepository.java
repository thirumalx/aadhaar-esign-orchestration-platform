package io.github.thirumalx.repository;

import io.github.thirumalx.model.Application;
import java.util.List;

/**
 * @author Thirumal M
 *         Application repository interface.
 */
public interface ApplicationRepository {
    Long save(Application application);

    Application findById(Long id);

    List<Application> findAll();

    int update(Application application);

    int delete(Long id);
}
