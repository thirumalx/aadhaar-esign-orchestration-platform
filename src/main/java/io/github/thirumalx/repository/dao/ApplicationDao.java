package io.github.thirumalx.repository.dao;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.Application;
import io.github.thirumalx.repository.ApplicationRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationDao extends GenericDao implements ApplicationRepository {

    ApplicationDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "application_id";

    @Override
    public Long save(Application application) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.application (application_name, application_code, webhook_url, update_info) VALUES (:application_name, :application_code, :webhook_url, :update_info)")
                .param("application_name", application.applicationName())
                .param("application_code", application.applicationCode())
                .param("webhook_url", application.webhookUrl())
                .param("update_info", application.updateInfo())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .longValue();
    }

    @Override
    public Optional<Application> findById(Long id) {
        return Optional.ofNullable(jdbcClient.sql("SELECT * FROM public.application WHERE application_id = :application_id")
                .param(PK, id)
                .query(Application.class)
                .single());
    }

    @Override
    public Optional<Application> findByCode(String applicationCode) {
        return Optional.ofNullable(jdbcClient.sql("SELECT * FROM public.application WHERE application_code = :application_code")
                .param("application_code", applicationCode)
                .query(Application.class)
                .single());
    }

    @Override
    public List<Application> findAll() {
        return jdbcClient.sql("SELECT * FROM public.application ORDER BY application_id DESC")
                .query(Application.class)
                .list();
    }

    @Override
    public int update(Application application) {
        return jdbcClient.sql("UPDATE public.application SET application_name = :application_name, application_code = :application_code, webhook_url = :webhook_url, update_info = :update_info, updated_at = current_timestamp WHERE application_id = :application_id")
                .param("application_name", application.applicationName())
                .param("application_code", application.applicationCode())
                .param("webhook_url", application.webhookUrl())
                .param("update_info", application.updateInfo())
                .param(PK, application.applicationId())
                .update();
    }

    @Override
    public int delete(Long id) {
        return jdbcClient.sql("DELETE FROM public.application WHERE application_id = :application_id")
                .param(PK, id)
                .update();
    }
}
