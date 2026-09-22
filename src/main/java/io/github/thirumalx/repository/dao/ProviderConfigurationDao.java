package io.github.thirumalx.repository.dao;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.ProviderConfiguration;
import io.github.thirumalx.repository.ProviderConfigurationRepository;
import java.util.List;
import java.util.Optional;

@Repository
public class ProviderConfigurationDao extends GenericDao implements ProviderConfigurationRepository {

    ProviderConfigurationDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "provider_configuration_id";

    @Override
    public Long save(ProviderConfiguration providerConfiguration) {
        org.springframework.jdbc.support.KeyHolder holder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.provider_configuration (signature_provider_id, environment_cd, asp_id, api_url, health_url, timeout_ms, retry_count, api_key, secret, certificate_reference, update_info) VALUES (:signature_provider_id, :environment_cd, :asp_id, :api_url, :health_url, :timeout_ms, :retry_count, :api_key, :secret, :certificate_reference, :update_info)")
                .param("signature_provider_id", providerConfiguration.signatureProviderId())
                .param("application_id", providerConfiguration.applicationId())
                .param("environment_cd", providerConfiguration.environmentCd())
                .param("asp_id", providerConfiguration.aspId())
                .param("api_url", providerConfiguration.apiUrl())
                .param("health_url", providerConfiguration.healthUrl())
                .param("timeout_ms", providerConfiguration.timeoutMs())
                .param("retry_count", providerConfiguration.retryCount())
                .param("api_key", providerConfiguration.apiKey())
                .param("secret", providerConfiguration.secret())
                .param("certificate_reference", providerConfiguration.certificateReference())
                .param("update_info", providerConfiguration.updateInfo())
                .update(holder, PK);
        Long generatedKey = (Long) holder.getKey();
        if (generatedKey == null) {
            throw new io.github.thirumalx.exception.ResourceNotFoundException(primaryKeyErr);
        }
        return generatedKey.longValue();
    }

    @Override
    public ProviderConfiguration findById(Long id) {
        return jdbcClient.sql("SELECT * FROM public.provider_configuration WHERE provider_configuration_id = :provider_configuration_id")
                .param(PK, id)
                .query(ProviderConfiguration.class)
                .single();
    }

    @Override
    public Optional<ProviderConfiguration> findByProviderCodeAndEnvironment(String providerCode, Short environmentCd) {
        return Optional.ofNullable(jdbcClient.sql("SELECT pc.* FROM public.provider_configuration pc INNER JOIN public.signature_provider sp ON sp.signature_provider_id = pc.signature_provider_id WHERE sp.provider_code = :provider_code AND pc.environment_cd = :environment_cd LIMIT 1")
                .param("provider_code", providerCode)
                .param("environment_cd", environmentCd)
                .query(ProviderConfiguration.class)
                .single());
    }

    @Override
    public List<ProviderConfiguration> findAll() {
        return jdbcClient.sql("SELECT * FROM public.provider_configuration ORDER BY provider_configuration_id DESC")
                .query(ProviderConfiguration.class)
                .list();
    }

    @Override
    public int update(ProviderConfiguration providerConfiguration) {
        return jdbcClient.sql("UPDATE public.provider_configuration SET signature_provider_id = :signature_provider_id, environment_cd = :environment_cd, asp_id = :asp_id, api_url = :api_url, health_url = :health_url, timeout_ms = :timeout_ms, retry_count = :retry_count, api_key = :api_key, secret = :secret, certificate_reference = :certificate_reference, update_info = :update_info, updated_at = current_timestamp WHERE provider_configuration_id = :provider_configuration_id")
                .param("signature_provider_id", providerConfiguration.signatureProviderId())
                .param("application_id", providerConfiguration.applicationId())
                .param("environment_cd", providerConfiguration.environmentCd())
                .param("asp_id", providerConfiguration.aspId())
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
        return jdbcClient.sql("DELETE FROM public.provider_configuration WHERE provider_configuration_id = :provider_configuration_id")
                .param(PK, id)
                .update();
    }
}
