package com.pruebatecnica.poliza.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pruebatecnica.poliza.entities.RiesgoEntity;
import com.pruebatecnica.poliza.services.RiesgoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/riesgos")
public class RiesgoController {

    private final RiesgoService riesgoService;

    @PostMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public RiesgoEntity cancelar(@PathVariable String id) {
        return riesgoService.cancelarRiesgo(id);
    }
}