package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *         Represents signature attempts.
 */
public record SignatureAttempt(
                Long signatureAttemptId,
                Long signatureRequestId,
                Long providerConfigurationId,
                Short statusCd,
                OffsetDateTime requestSentAt,
                OffsetDateTime responseReceivedAt,
                String providerTransactionId,
                String requestPayload,
                String responsePayload,
                Short httpStatus,
                Short providerStatusCode,
                String providerStatusMessage,
                String errorCode,
                String errorMessage,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {
}
