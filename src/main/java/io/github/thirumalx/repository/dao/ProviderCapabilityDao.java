package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.ProviderCapability;
import io.github.thirumalx.repository.ProviderCapabilityRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProviderCapabilityDao extends GenericDao implements ProviderCapabilityRepository {

    ProviderCapabilityDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "provider_capability_id";
    private static final String CREATE = "ProviderCapability.create";
    private static final String GET = "ProviderCapability.get";
    private static final String LIST = "ProviderCapability.list";
    private static final String UPDATE = "ProviderCapability.update";
    private static final String DELETE = "ProviderCapability.delete";

    @Override
    public Integer save(ProviderCapability providerCapability) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
                .param("signature_provider_id", providerCapability.signatureProviderId())
                .param("provider_cd", providerCapability.providerCd())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .intValue();
    }

    @Override
    public ProviderCapability findById(Integer id) {
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(ProviderCapability.class)
                .single();
    }

    @Override
    public List<ProviderCapability> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(ProviderCapability.class)
                .list();
    }

    @Override
    public int update(ProviderCapability providerCapability) {
        return jdbcClient.sql(getSql(UPDATE))
                .param("signature_provider_id", providerCapability.signatureProviderId())
                .param("provider_cd", providerCapability.providerCd())
                .param(PK, providerCapability.providerCapabilityId())
                .update();
    }

    @Override
    public int delete(Integer id) {
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
