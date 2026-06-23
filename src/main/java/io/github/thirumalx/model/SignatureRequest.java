package io.github.thirumalx.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author Thirumal M
 *         Represents signature requests.
 */
public record SignatureRequest(
                Long signatureRequestId,
                Long applicationId,
                Short statusCd,
                UUID signatureRequestUuid,
                String referenceNo,
                String originalDocumentHash,
                String originalDocumentPath,
                String requestPayload,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt,
                String updateInfo) {
}
