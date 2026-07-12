package io.github.thirumalx.service.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.service.EsignProvider;

/**
 * @author Thirumal
 *         CdacEsignProvider
 */
@Service
public class CdacEsignProvider implements EsignProvider {

    Logger logger = LoggerFactory.getLogger(CdacEsignProvider.class);

    @Override
    public String getProviderCode() {
        return "cdac";
    }

    @Override
    public String initiateSign(EsignDto esignDto) {
        logger.info("Initiating eSign with CDAC for {}", esignDto.signId());
        return "Initiated eSign with CDAC for " + esignDto.signId();
    }

}
