package io.github.thirumalx.repository.dao;

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

    SignatureRequestDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "signature_request_id";

    @Override
    public Long save(SignatureRequest signatureRequest) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.signature_request (application_id, status_cd, reference_no, original_document_hash, original_document_path, request_payload, update_info) VALUES (:application_id, :status_cd, :reference_no, :original_document_hash, :original_document_path, :request_payload::jsonb, :update_info)")
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
        return jdbcClient.sql("SELECT * FROM public.signature_request WHERE signature_request_id = :signature_request_id")
                .param(PK, id)
                .query(SignatureRequest.class)
                .single();
    }

    @Override
    public List<SignatureRequest> findAll() {
        return jdbcClient.sql("SELECT * FROM public.signature_request ORDER BY signature_request_id DESC")
                .query(SignatureRequest.class)
                .list();
    }

    @Override
    public int update(SignatureRequest signatureRequest) {
        return jdbcClient.sql("UPDATE public.signature_request SET application_id = :application_id, status_cd = :status_cd, reference_no = :reference_no, original_document_hash = :original_document_hash, original_document_path = :original_document_path, request_payload = :request_payload::jsonb, update_info = :update_info, updated_at = current_timestamp WHERE signature_request_id = :signature_request_id")
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
        return jdbcClient.sql("DELETE FROM public.signature_request WHERE signature_request_id = :signature_request_id")
                .param(PK, id)
                .update();
    }
}
