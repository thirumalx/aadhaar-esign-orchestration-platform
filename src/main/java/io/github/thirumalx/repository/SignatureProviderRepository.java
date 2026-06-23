package io.github.thirumalx.repository;

import io.github.thirumalx.model.SignatureProvider;
import java.util.List;

/**
 * @author Thirumal M
 *         Signature provider repository interface.
 */
public interface SignatureProviderRepository {
    Short save(SignatureProvider signatureProvider);

    SignatureProvider findById(Short id);

    List<SignatureProvider> findAll();

    int update(SignatureProvider signatureProvider);

    int delete(Short id);
}
