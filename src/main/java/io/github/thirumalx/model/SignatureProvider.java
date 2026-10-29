package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *  It's a record class associated with the signature_providers table in the database.
 *  Holds details about each signature provider (i.e., ESP like Protean, CDAC, etc.), 
 *      including its unique ID, code, name, priority, cost, weight, and timestamps for creation and last update.
 */
public record SignatureProvider(
                Short signatureProviderId,
                String providerCode,
                String providerName,
                Short priority,
                Double cost,
                Integer weight,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {
}
