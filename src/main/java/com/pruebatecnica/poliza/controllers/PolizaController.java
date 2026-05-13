package com.pruebatecnica.poliza.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pruebatecnica.poliza.entities.PolizaEntity;
import com.pruebatecnica.poliza.entities.RiesgoEntity;
import com.pruebatecnica.poliza.enums.EstadoPoliza;
import com.pruebatecnica.poliza.services.PolizaService;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polizas")
public class PolizaController {

    private final PolizaService polizaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PolizaEntity> listarPolizas(@RequestParam(required = false) String tipo,
            @RequestParam(required = false) EstadoPoliza estado) {
        return polizaService.listarPolizas(tipo, estado);
    }

    @GetMapping("/{id}/riesgos")
    @ResponseStatus(HttpStatus.OK)
    public List<RiesgoEntity> listarRiesgos(@PathVariable String id) {
        return polizaService.listarRiesgosPorPoliza(id);
    }

    @PostMapping("/{id}/riesgos")
    @ResponseStatus(HttpStatus.CREATED)
    public RiesgoEntity agregarRiesgo(@PathVariable String id, @Valid @RequestBody RiesgoEntity riesgo) {
        return polizaService.agregarRiesgo(id, riesgo);
    }

    @PostMapping("/{id}/renovar")
    @ResponseStatus(HttpStatus.OK)
    public PolizaEntity renovar(@PathVariable String id) {
        return polizaService.renovarPoliza(id);
    }

    @PostMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public PolizaEntity cancelar(@PathVariable String id) {
        return polizaService.cancelarPoliza(id);
    }
}