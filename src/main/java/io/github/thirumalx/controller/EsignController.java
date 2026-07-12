package io.github.thirumalx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.thirumalx.dto.EsignDto;
import io.github.thirumalx.service.EsignOrchestratorService;

@RestController
@RequestMapping("/esign")
public class EsignController {

    private final EsignOrchestratorService orchestratorService;

    public EsignController(EsignOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> initiateEsign(@RequestBody EsignDto esignDto) {
        String response = orchestratorService.initiateEsign(esignDto);
        return ResponseEntity.ok(response);
    }
}
