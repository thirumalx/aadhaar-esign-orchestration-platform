package io.github.thirumalx.repository.dao;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.SignatureAttempt;
import io.github.thirumalx.repository.SignatureAttemptRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class SignatureAttemptDao extends GenericDao implements SignatureAttemptRepository {

    SignatureAttemptDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "signature_attempt_id";

    @Override
    public Long save(SignatureAttempt signatureAttempt) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.signature_attempt (signature_request_id, provider_configuration_id, status_cd, request_sent_at, response_received_at, provider_transaction_id, request_payload, response_payload, http_status, provider_status_code, provider_status_message, error_code, error_message) VALUES (:signature_request_id, :provider_configuration_id, :status_cd, :request_sent_at, :response_received_at, :provider_transaction_id, :request_payload::jsonb, :response_payload::jsonb, :http_status, :provider_status_code, :provider_status_message, :error_code, :error_message)")
                .param("signature_request_id", signatureAttempt.signatureRequestId())
                .param("provider_configuration_id", signatureAttempt.providerConfigurationId())
                .param("status_cd", signatureAttempt.statusCd())
                .param("request_sent_at", signatureAttempt.requestSentAt())
                .param("response_received_at", signatureAttempt.responseReceivedAt())
                .param("provider_transaction_id", signatureAttempt.providerTransactionId())
                .param("request_payload", signatureAttempt.requestPayload())
                .param("response_payload", signatureAttempt.responsePayload())
                .param("http_status", signatureAttempt.httpStatus())
                .param("provider_status_code", signatureAttempt.providerStatusCode())
                .param("provider_status_message", signatureAttempt.providerStatusMessage())
                .param("error_code", signatureAttempt.errorCode())
                .param("error_message", signatureAttempt.errorMessage())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .longValue();
    }

    @Override
    public SignatureAttempt findById(Long id) {
        return jdbcClient.sql("SELECT * FROM public.signature_attempt WHERE signature_attempt_id = :signature_attempt_id")
                .param(PK, id)
                .query(SignatureAttempt.class)
                .single();
    }

    @Override
    public List<SignatureAttempt> findAll() {
        return jdbcClient.sql("SELECT * FROM public.signature_attempt ORDER BY signature_attempt_id DESC")
                .query(SignatureAttempt.class)
                .list();
    }

    @Override
    public int update(SignatureAttempt signatureAttempt) {
        return jdbcClient.sql("UPDATE public.signature_attempt SET signature_request_id = :signature_request_id, provider_configuration_id = :provider_configuration_id, status_cd = :status_cd, request_sent_at = :request_sent_at, response_received_at = :response_received_at, provider_transaction_id = :provider_transaction_id, request_payload = :request_payload::jsonb, response_payload = :response_payload::jsonb, http_status = :http_status, provider_status_code = :provider_status_code, provider_status_message = :provider_status_message, error_code = :error_code, error_message = :error_message, updated_at = current_timestamp WHERE signature_attempt_id = :signature_attempt_id")
                .param("signature_request_id", signatureAttempt.signatureRequestId())
                .param("provider_configuration_id", signatureAttempt.providerConfigurationId())
                .param("status_cd", signatureAttempt.statusCd())
                .param("request_sent_at", signatureAttempt.requestSentAt())
                .param("response_received_at", signatureAttempt.responseReceivedAt())
                .param("provider_transaction_id", signatureAttempt.providerTransactionId())
                .param("request_payload", signatureAttempt.requestPayload())
                .param("response_payload", signatureAttempt.responsePayload())
                .param("http_status", signatureAttempt.httpStatus())
                .param("provider_status_code", signatureAttempt.providerStatusCode())
                .param("provider_status_message", signatureAttempt.providerStatusMessage())
                .param("error_code", signatureAttempt.errorCode())
                .param("error_message", signatureAttempt.errorMessage())
                .param(PK, signatureAttempt.signatureAttemptId())
                .update();
    }

    @Override
    public int delete(Long id) {
        return jdbcClient.sql("DELETE FROM public.signature_attempt WHERE signature_attempt_id = :signature_attempt_id")
                .param(PK, id)
                .update();
    }
}
