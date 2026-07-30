package io.github.thirumalx.service.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.service.EsignProvider;

/**
 * @author Thirumal
 *         ProteanEsignProvider
 */
@Service
public class ProteanEsignProvider implements EsignProvider {

    Logger logger = LoggerFactory.getLogger(ProteanEsignProvider.class);

    @Override
    public String getProviderCode() {
        return "protean";
    }

    @Override
    public EsignResponseDto initiateSign(EsignDto esignDto) {
        logger.info("Initiating eSign with Protean for {}", esignDto.signId());
        return new EsignResponseDto(null, null, null, null, "Not implemented");
    }

}
