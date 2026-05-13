package com.pruebatecnica.poliza.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    private static final Logger logger = LoggerFactory.getLogger(CoreMockController.class);
    private static final String REQUIRED_API_KEY = "123456";

    @PostMapping("/evento")
    public ResponseEntity<?> evento(
            @RequestHeader(value = "x-api-key", required = true) String apiKey,
            @RequestBody CoreMockRequest request
    ) {
        if (apiKey == null || !REQUIRED_API_KEY.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid x-api-key");
        }

        logger.info("Attempted to send to CORE: evento={}, polizaId={}", request.getEvento(), request.getPolizaId());

        return ResponseEntity.ok(java.util.Collections.singletonMap("status", "logged"));
    }
}
