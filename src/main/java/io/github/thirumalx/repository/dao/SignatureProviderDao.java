package io.github.thirumalx.repository.dao;

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

    SignatureProviderDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "signature_provider_id";

    @Override
    public Short save(SignatureProvider signatureProvider) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.signature_provider (signature_provider_id, provider_code, provider_name, priority) VALUES (:signature_provider_id, :provider_code, :provider_name, :priority)")
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
        return jdbcClient.sql("SELECT * FROM public.signature_provider WHERE signature_provider_id = :signature_provider_id")
                .param(PK, id)
                .query(SignatureProvider.class)
                .single();
    }

    @Override
    public List<SignatureProvider> findAll() {
        return jdbcClient.sql("SELECT * FROM public.signature_provider ORDER BY signature_provider_id DESC")
                .query(SignatureProvider.class)
                .list();
    }

    @Override
    public int update(SignatureProvider signatureProvider) {
        return jdbcClient.sql("UPDATE public.signature_provider SET provider_code = :provider_code, provider_name = :provider_name, priority = :priority, updated_at = current_timestamp WHERE signature_provider_id = :signature_provider_id")
                .param("provider_code", signatureProvider.providerCode())
                .param("provider_name", signatureProvider.providerName())
                .param("priority", signatureProvider.priority())
                .param(PK, signatureProvider.signatureProviderId())
                .update();
    }

    @Override
    public int delete(Short id) {
        return jdbcClient.sql("DELETE FROM public.signature_provider WHERE signature_provider_id = :signature_provider_id")
                .param(PK, id)
                .update();
    }

    @Override
    public SignatureProvider findTopPriority() {
        return jdbcClient.sql("SELECT * FROM public.signature_provider ORDER BY priority ASC LIMIT 1")
                .query(SignatureProvider.class)
                .single();
    }

    @Override
    public Optional<SignatureProvider> findByApplicationId(Long applicationId) {
        return Optional.ofNullable(jdbcClient.sql("SELECT sp.* FROM public.signature_provider sp INNER JOIN public.provider_configuration pc ON sp.signature_provider_id = pc.signature_provider_id INNER JOIN public.application a ON a.application_id = pc.application_id WHERE a.application_code = :application_id LIMIT 1")
                .param("application_id", applicationId)
                .query(SignatureProvider.class)
                .stream()
                .findFirst()
                .orElse(null));
    }
}
