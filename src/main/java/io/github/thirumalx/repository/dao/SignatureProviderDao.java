package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.SignatureProvider;
import io.github.thirumalx.repository.SignatureProviderRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class SignatureProviderDao extends GenericDao implements SignatureProviderRepository {

    SignatureProviderDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "signature_provider_id";
    private static final String CREATE = "SignatureProvider.create";
    private static final String GET = "SignatureProvider.get";
    private static final String LIST = "SignatureProvider.list";
    private static final String UPDATE = "SignatureProvider.update";
    private static final String DELETE = "SignatureProvider.delete";

    @Override
    public Short save(SignatureProvider signatureProvider) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
                .param("signature_provider_id", signatureProvider.signatureProviderId())
                .param("provider_code", signatureProvider.providerCode())
                .param("provider_name", signatureProvider.providerName())
                .param("priority", signatureProvider.priority())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .shortValue();
    }

    @Override
    public SignatureProvider findById(Short id) {
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(SignatureProvider.class)
                .single();
    }

    @Override
    public List<SignatureProvider> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(SignatureProvider.class)
                .list();
    }

    @Override
    public int update(SignatureProvider signatureProvider) {
        return jdbcClient.sql(getSql(UPDATE))
                .param("provider_code", signatureProvider.providerCode())
                .param("provider_name", signatureProvider.providerName())
                .param("priority", signatureProvider.priority())
                .param(PK, signatureProvider.signatureProviderId())
                .update();
    }

    @Override
    public int delete(Short id) {
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
