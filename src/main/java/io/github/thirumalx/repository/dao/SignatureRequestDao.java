package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.SignatureRequest;
import io.github.thirumalx.repository.SignatureRequestRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class SignatureRequestDao extends GenericDao implements SignatureRequestRepository {

    SignatureRequestDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "signature_request_id";
    private static final String CREATE = "SignatureRequest.create";
    private static final String GET = "SignatureRequest.get";
    private static final String LIST = "SignatureRequest.list";
    private static final String UPDATE = "SignatureRequest.update";
    private static final String DELETE = "SignatureRequest.delete";

    @Override
    public Long save(SignatureRequest signatureRequest) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
                .param("application_id", signatureRequest.applicationId())
                .param("status_cd", signatureRequest.statusCd())
                .param("reference_no", signatureRequest.referenceNo())
                .param("original_document_hash", signatureRequest.originalDocumentHash())
                .param("original_document_path", signatureRequest.originalDocumentPath())
                .param("request_payload", signatureRequest.requestPayload())
                .param("update_info", signatureRequest.updateInfo())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .longValue();
    }

    @Override
    public SignatureRequest findById(Long id) {
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(SignatureRequest.class)
                .single();
    }

    @Override
    public List<SignatureRequest> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(SignatureRequest.class)
                .list();
    }

    @Override
    public int update(SignatureRequest signatureRequest) {
        return jdbcClient.sql(getSql(UPDATE))
                .param("application_id", signatureRequest.applicationId())
                .param("status_cd", signatureRequest.statusCd())
                .param("reference_no", signatureRequest.referenceNo())
                .param("original_document_hash", signatureRequest.originalDocumentHash())
                .param("original_document_path", signatureRequest.originalDocumentPath())
                .param("request_payload", signatureRequest.requestPayload())
                .param("update_info", signatureRequest.updateInfo())
                .param(PK, signatureRequest.signatureRequestId())
                .update();
    }

    @Override
    public int delete(Long id) {
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
