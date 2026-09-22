package io.github.thirumalx.repository.dao;

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

    ProviderCapabilityDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "provider_capability_id";

    @Override
    public Integer save(ProviderCapability providerCapability) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.provider_capability (signature_provider_id, provider_cd) VALUES (:signature_provider_id, :provider_cd)")
                .param("signature_provider_id", providerCapability.signatureProviderId())
                .param("provider_cd", providerCapability.providerCd())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .intValue();
    }

    @Override
    public ProviderCapability findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM public.provider_capability WHERE provider_capability_id = :provider_capability_id")
                .param(PK, id)
                .query(ProviderCapability.class)
                .single();
    }

    @Override
    public List<ProviderCapability> findAll() {
        return jdbcClient.sql("SELECT * FROM public.provider_capability ORDER BY provider_capability_id DESC")
                .query(ProviderCapability.class)
                .list();
    }

    @Override
    public int update(ProviderCapability providerCapability) {
        return jdbcClient.sql("UPDATE public.provider_capability SET signature_provider_id = :signature_provider_id, provider_cd = :provider_cd, updated_at = current_timestamp WHERE provider_capability_id = :provider_capability_id")
                .param("signature_provider_id", providerCapability.signatureProviderId())
                .param("provider_cd", providerCapability.providerCd())
                .param(PK, providerCapability.providerCapabilityId())
                .update();
    }

    @Override
    public int delete(Integer id) {
        return jdbcClient.sql("DELETE FROM public.provider_capability WHERE provider_capability_id = :provider_capability_id")
                .param(PK, id)
                .update();
    }
}
