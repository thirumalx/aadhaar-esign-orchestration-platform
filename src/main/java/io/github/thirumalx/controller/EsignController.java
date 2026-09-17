package io.github.thirumalx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.dto.EsignResponseDto;
import io.github.thirumalx.service.EsignOrchestratorService;

@RestController
@RequestMapping("/esign")
public class EsignController {

    private final EsignOrchestratorService orchestratorService;

    public EsignController(EsignOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping(value = "/initiate", version = "1.0.0")
    public ResponseEntity<EsignResponseDto> initiateEsign(@RequestBody EsignDto esignDto) {
        EsignResponseDto response = orchestratorService.initiateEsign(esignDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Receives the callback from eMudhra after the user has completed the e-Sign process.
     * The payload is expected to be application/x-www-form-urlencoded.
     */
    @PostMapping(value = "/callback/emudhra", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> emudhraCallback(@RequestParam("XML") String signedXmlResponse) {
        // Here we would extract the PKCS7 signature from the AspSignResponse XML,
        // Verify eMudhra's signature, and update the SignedDocument record in DB.

        // Return a simple success page or redirect back to the client application
        return ResponseEntity.ok("e-Sign callback received successfully.");
    }
}
