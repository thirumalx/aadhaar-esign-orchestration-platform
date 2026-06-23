package io.github.thirumalx.repository;

import io.github.thirumalx.model.SignatureAttempt;
import java.util.List;

/**
 * @author Thirumal M
 *         Signature attempt repository interface.
 */
public interface SignatureAttemptRepository {
    Long save(SignatureAttempt signatureAttempt);

    SignatureAttempt findById(Long id);

    List<SignatureAttempt> findAll();

    int update(SignatureAttempt signatureAttempt);

    int delete(Long id);
}
