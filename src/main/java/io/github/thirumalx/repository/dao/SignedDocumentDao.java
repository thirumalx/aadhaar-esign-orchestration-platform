package io.github.thirumalx.repository.dao;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.github.thirumalx.model.SignedDocument;
import io.github.thirumalx.repository.SignedDocumentRepository;
import io.github.thirumalx.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public class SignedDocumentDao extends GenericDao implements SignedDocumentRepository {

    SignedDocumentDao(JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    private static final String PK = "signed_document_id";

    @Override
    public Long save(SignedDocument signedDocument) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO public.signed_document (signature_request_id, storage_path, signed_hash, signed_time, signer_name, signer_yob, signer_gender, signer_pin, signer_aadhaar_suffix) VALUES (:signature_request_id, :storage_path, :signed_hash, :signed_time, :signer_name, :signer_yob, :signer_gender, :signer_pin, :signer_aadhaar_suffix)")
                .param("signature_request_id", signedDocument.signatureRequestId())
                .param("storage_path", signedDocument.storagePath())
                .param("signed_hash", signedDocument.signedHash())
                .param("signed_time", signedDocument.signedTime())
                .param("signer_name", signedDocument.signerName())
                .param("signer_yob", signedDocument.signerYob())
                .param("signer_gender", signedDocument.signerGender())
                .param("signer_pin", signedDocument.signerPin())
                .param("signer_aadhaar_suffix", signedDocument.signerAadhaarSuffix())
                .update(holder, PK);
        return Optional.ofNullable(holder.getKey()).orElseThrow(() -> new ResourceNotFoundException(primaryKeyErr))
                .longValue();
    }

    @Override
    public SignedDocument findById(Long id) {
        return jdbcClient.sql("SELECT * FROM public.signed_document WHERE signed_document_id = :signed_document_id")
                .param(PK, id)
                .query(SignedDocument.class)
                .single();
    }

    @Override
    public List<SignedDocument> findAll() {
        return jdbcClient.sql("SELECT * FROM public.signed_document ORDER BY signed_document_id DESC")
                .query(SignedDocument.class)
                .list();
    }

    @Override
    public int update(SignedDocument signedDocument) {
        return jdbcClient.sql("UPDATE public.signed_document SET signature_request_id = :signature_request_id, storage_path = :storage_path, signed_hash = :signed_hash, signed_time = :signed_time, signer_name = :signer_name, signer_yob = :signer_yob, signer_gender = :signer_gender, signer_pin = :signer_pin, signer_aadhaar_suffix = :signer_aadhaar_suffix, updated_at = current_timestamp WHERE signed_document_id = :signed_document_id")
                .param("signature_request_id", signedDocument.signatureRequestId())
                .param("storage_path", signedDocument.storagePath())
                .param("signed_hash", signedDocument.signedHash())
                .param("signed_time", signedDocument.signedTime())
                .param("signer_name", signedDocument.signerName())
                .param("signer_yob", signedDocument.signerYob())
                .param("signer_gender", signedDocument.signerGender())
                .param("signer_pin", signedDocument.signerPin())
                .param("signer_aadhaar_suffix", signedDocument.signerAadhaarSuffix())
                .param(PK, signedDocument.signedDocumentId())
                .update();
    }

    @Override
    public int delete(Long id) {
        return jdbcClient.sql("DELETE FROM public.signed_document WHERE signed_document_id = :signed_document_id")
                .param(PK, id)
                .update();
    }
}
