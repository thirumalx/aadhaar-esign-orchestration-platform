package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *         Represents provider capabilities.
 */
public record ProviderCapability(
                Integer providerCapabilityId,
                Short signatureProviderId,
                Short providerCd,
                OffsetDateTime startTime,
                OffsetDateTime endTime,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {
}
