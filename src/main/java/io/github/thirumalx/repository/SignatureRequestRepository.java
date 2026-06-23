package io.github.thirumalx.repository;

import io.github.thirumalx.model.SignatureRequest;
import java.util.List;

/**
 * @author Thirumal M
 *         Signature request repository interface.
 */
public interface SignatureRequestRepository {
    Long save(SignatureRequest signatureRequest);

    SignatureRequest findById(Long id);

    List<SignatureRequest> findAll();

    int update(SignatureRequest signatureRequest);

    int delete(Long id);
}
