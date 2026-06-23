package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.ProviderConfiguration;
import io.github.thirumalx.repository.ProviderConfigurationRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProviderConfigurationDao extends GenericDao implements ProviderConfigurationRepository {

    ProviderConfigurationDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "provider_configuration_id";
    private static final String CREATE = "ProviderConfiguration.create";
    private static final String GET = "ProviderConfiguration.get";
    private static final String LIST = "ProviderConfiguration.list";
    private static final String UPDATE = "ProviderConfiguration.update";
    private static final String DELETE = "ProviderConfiguration.delete";

    @Override
    public Long save(ProviderConfiguration providerConfiguration) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
                .param("signature_provider_id", providerConfiguration.signatureProviderId())
                .param("environment_cd", providerConfiguration.environmentCd())
                .param("api_url", providerConfiguration.apiUrl())
                .param("health_url", providerConfiguration.healthUrl())
                .param("timeout_ms", providerConfiguration.timeoutMs())
                .param("retry_count", providerConfiguration.retryCount())
                .param("api_key", providerConfiguration.apiKey())
                .param("secret", providerConfiguration.secret())
                .param("certificate_reference", providerConfiguration.certificateReference())
                .param("update_info", providerConfiguration.updateInfo())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .longValue();
    }

    @Override
    public ProviderConfiguration findById(Long id) {
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(ProviderConfiguration.class)
                .single();
    }

    @Override
    public List<ProviderConfiguration> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(ProviderConfiguration.class)
                .list();
    }

    @Override
    public int update(ProviderConfiguration providerConfiguration) {
        return jdbcClient.sql(getSql(UPDATE))
                .param("signature_provider_id", providerConfiguration.signatureProviderId())
                .param("environment_cd", providerConfiguration.environmentCd())
                .param("api_url", providerConfiguration.apiUrl())
                .param("health_url", providerConfiguration.healthUrl())
                .param("timeout_ms", providerConfiguration.timeoutMs())
                .param("retry_count", providerConfiguration.retryCount())
                .param("api_key", providerConfiguration.apiKey())
                .param("secret", providerConfiguration.secret())
                .param("certificate_reference", providerConfiguration.certificateReference())
                .param("update_info", providerConfiguration.updateInfo())
                .param(PK, providerConfiguration.providerConfigurationId())
                .update();
    }

    @Override
    public int delete(Long id) {
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
