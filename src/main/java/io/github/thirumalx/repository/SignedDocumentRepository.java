package io.github.thirumalx.repository;

import io.github.thirumalx.model.SignedDocument;
import java.util.List;

/**
 * @author Thirumal M
 *         Signed document repository interface.
 */
public interface SignedDocumentRepository {
    Long save(SignedDocument signedDocument);

    SignedDocument findById(Long id);

    List<SignedDocument> findAll();

    int update(SignedDocument signedDocument);

    int delete(Long id);
}
