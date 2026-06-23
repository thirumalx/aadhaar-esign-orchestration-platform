package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *         Represents signed documents.
 */
public record SignedDocument(
                Long signedDocumentId,
                Long signatureRequestId,
                String storagePath,
                String signedHash,
                OffsetDateTime signedTime,
                String signerName,
                String signerYob,
                String signerGender,
                String signerPin,
                String signerAadhaarSuffix,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {
}
