package io.github.thirumalx.model;

import java.time.OffsetDateTime;

/**
 * @author Thirumal M
 *         Represents an application.
 */
public record Application(
                Long applicationId,
                String applicationName,
                String applicationCode,
                String webhookUrl,
                OffsetDateTime startTime,
                OffsetDateTime endTime,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt,
                String updateInfo) {
}
