package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
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

    ApplicationDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "application_id";
    private static final String CREATE = "Application.create";
    private static final String GET = "Application.get";
    private static final String LIST = "Application.list";
    private static final String UPDATE = "Application.update";
    private static final String DELETE = "Application.delete";

    @Override
    public Long save(Application application) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
                .param("application_name", application.applicationName())
                .param("application_code", application.applicationCode())
                .param("webhook_url", application.webhookUrl())
                .param("update_info", application.updateInfo())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .longValue();
    }

    @Override
    public Application findById(Long id) {
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(Application.class)
                .single();
    }

    @Override
    public List<Application> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(Application.class)
                .list();
    }

    @Override
    public int update(Application application) {
        return jdbcClient.sql(getSql(UPDATE))
                .param("application_name", application.applicationName())
                .param("application_code", application.applicationCode())
                .param("webhook_url", application.webhookUrl())
                .param("update_info", application.updateInfo())
                .param(PK, application.applicationId())
                .update();
    }

    @Override
    public int delete(Long id) {
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
