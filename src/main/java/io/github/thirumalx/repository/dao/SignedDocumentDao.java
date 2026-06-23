package io.github.thirumalx.repository.dao;

import org.springframework.core.env.Environment;
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

    SignedDocumentDao(JdbcClient jdbcClient, Environment environment) {
        super(jdbcClient, environment);
    }

    private static final String PK = "signed_document_id";
    private static final String CREATE = "SignedDocument.create";
    private static final String GET = "SignedDocument.get";
    private static final String LIST = "SignedDocument.list";
    private static final String UPDATE = "SignedDocument.update";
    private static final String DELETE = "SignedDocument.delete";

    @Override
    public Long save(SignedDocument signedDocument) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcClient.sql(getSql(CREATE))
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
        return jdbcClient.sql(getSql(GET))
                .param(PK, id)
                .query(SignedDocument.class)
                .single();
    }

    @Override
    public List<SignedDocument> findAll() {
        return jdbcClient.sql(getSql(LIST))
                .query(SignedDocument.class)
                .list();
    }

    @Override
    public int update(SignedDocument signedDocument) {
        return jdbcClient.sql(getSql(UPDATE))
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
        return jdbcClient.sql(getSql(DELETE))
                .param(PK, id)
                .update();
    }
}
