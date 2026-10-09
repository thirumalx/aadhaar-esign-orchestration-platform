package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *         Represents signature providers.
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
