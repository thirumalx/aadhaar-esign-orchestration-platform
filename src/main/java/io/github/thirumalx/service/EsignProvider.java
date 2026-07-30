package io.github.thirumalx.service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;

/**
 * @author Thirumal
 *         Interface representing an eSign Provider Strategy.
 */
public interface EsignProvider {

    /**
     * Identifies the provider this implementation handles.
     * For example, "protean", "cdac", etc.
     * 
     * @return the provider code
     */
    String getProviderCode();

    /**
     * Initiates the eSign process for this specific provider.
     * 
     * @param esignDto the eSign request data
     * @return a response string or object depending on implementation needs
     */
    EsignResponseDto initiateSign(EsignDto esignDto);
}
