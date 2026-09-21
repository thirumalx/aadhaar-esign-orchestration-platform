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
     * For example, "protean", "cdac", "eMudhra", etc.
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

    /**
     * Determines the environment code based on the active Spring profile.
     * @param environment the Spring Environment
     * @return 1 for DEV, 2 for UAT, 3 for PROD
     */
    default Short getEnvironmentCd(org.springframework.core.env.Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0].toLowerCase();
            if (profile.contains("prod")) return 3;
            if (profile.contains("uat")) return 2;
        }
        return 1; // Default to DEV
    }
}
