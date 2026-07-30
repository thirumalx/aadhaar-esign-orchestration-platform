package io.github.thirumalx.dto;

/**
 * @author Thirumal
 * DTO to return the generated eSign request data to the client,
 * so the client can construct the auto-submitting form.
 */
public record EsignResponseDto(
    String eSignRequest,
    String aspTxnID,
    String contentType,
    String actionUrl,
    String errMsg
) {
}
