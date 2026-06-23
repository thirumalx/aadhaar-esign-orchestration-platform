package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *         Represents provider configurations.
 */
public record ProviderConfiguration(
                Long providerConfigurationId,
                Short signatureProviderId,
                Short environmentCd,
                String apiUrl,
                String healthUrl,
                Integer timeoutMs,
                Short retryCount,
                String apiKey,
                String secret,
                String certificateReference,
                OffsetDateTime startTime,
                OffsetDateTime endTime,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt,
                String updateInfo) {
}
