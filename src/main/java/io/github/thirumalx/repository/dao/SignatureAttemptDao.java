package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
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

    SignatureAttemptDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "signature_attempt_id";
    private static final String CREATE = "SignatureAttempt.create";
    private static final String GET = "SignatureAttempt.get";
    private static final String LIST = "SignatureAttempt.list";
    private static final String UPDATE = "SignatureAttempt.update";
    private static final String DELETE = "SignatureAttempt.delete";

    @Override
    public Long save(SignatureAttempt signatureAttempt) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
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
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(SignatureAttempt.class)
                .single();
    }

    @Override
    public List<SignatureAttempt> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(SignatureAttempt.class)
                .list();
    }

    @Override
    public int update(SignatureAttempt signatureAttempt) {
        return jdbcClient.sql(getSql(UPDATE))
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
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
