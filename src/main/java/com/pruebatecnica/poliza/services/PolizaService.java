package com.pruebatecnica.poliza.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.pruebatecnica.poliza.entities.PolizaColectivaEntity;
import com.pruebatecnica.poliza.entities.PolizaEntity;
import com.pruebatecnica.poliza.entities.PolizaIndividualEntity;
import com.pruebatecnica.poliza.entities.RiesgoEntity;
import com.pruebatecnica.poliza.enums.EstadoPoliza;
import com.pruebatecnica.poliza.enums.EstadoRiesgo;
import com.pruebatecnica.poliza.repositories.PolizaRepository;
import com.pruebatecnica.poliza.repositories.RiesgoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PolizaService {
    @Autowired
    private PolizaRepository polizaRepository;

    @Autowired
    private RiesgoRepository riesgoRepository;

    @Value("${poliza.ipc:0.05}")
    private BigDecimal ipc;

    public List<PolizaEntity> listarPolizas(String tipo, EstadoPoliza estado) {
        List<PolizaEntity> polizas = polizaRepository.findAll();
        return polizas.stream()
                .filter(poliza -> tipo == null || coincideTipo(poliza, tipo))
                .filter(poliza -> estado == null || estado.equals(poliza.getEstado()))
                .toList();
    }

    public List<RiesgoEntity> listarRiesgosPorPoliza(String polizaId) {
        PolizaEntity poliza = obtenerPoliza(polizaId);
        if (poliza instanceof PolizaIndividualEntity polizaIndividual) {
            RiesgoEntity riesgo = polizaIndividual.getRiesgo();
            return riesgo == null ? List.of() : List.of(riesgo);
        }

        if (poliza instanceof PolizaColectivaEntity polizaColectiva) {
            List<RiesgoEntity> riesgos = polizaColectiva.getRiesgos();
            return riesgos == null ? List.of() : List.copyOf(riesgos);
        }

        return List.of();
    }

    public PolizaEntity renovarPoliza(String polizaId) {
        PolizaEntity poliza = obtenerPoliza(polizaId);
        if (EstadoPoliza.CANCELADA.equals(poliza.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede renovar una poliza cancelada");
        }

        float factorIncremento = BigDecimal.ONE.add(ipc).floatValue();
        poliza.setValorCanonMensual(redondear(poliza.getValorCanonMensual() * factorIncremento));
        poliza.setValorPrima(redondear(poliza.getValorPrima() * factorIncremento));
        poliza.setEstado(EstadoPoliza.RENOVADA);
        return polizaRepository.save(poliza);
    }

    public PolizaEntity cancelarPoliza(String polizaId) {
        PolizaEntity poliza = obtenerPoliza(polizaId);
        cancelarRiesgos(poliza);
        poliza.setEstado(EstadoPoliza.CANCELADA);
        return polizaRepository.save(poliza);
    }

    public RiesgoEntity agregarRiesgo(String polizaId, RiesgoEntity riesgo) {
        Objects.requireNonNull(riesgo, "El riesgo es obligatorio");
        PolizaEntity poliza = obtenerPoliza(polizaId);
        riesgo.setEstado(EstadoRiesgo.ACTIVO);

        if (poliza instanceof PolizaColectivaEntity polizaColectiva) {
            List<RiesgoEntity> riesgos = polizaColectiva.getRiesgos();
            if (riesgos == null) {
                riesgos = new ArrayList<>();
                polizaColectiva.setRiesgos(riesgos);
            }
            riesgos.add(riesgo);
            polizaRepository.save(polizaColectiva);
            return riesgo;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo una poliza colectiva puede agregar riesgos");
    }

    public RiesgoEntity cancelarRiesgo(String riesgoId) {
        RiesgoEntity riesgo = obtenerRiesgo(riesgoId);
        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        return riesgo;
    }

    private void cancelarRiesgos(PolizaEntity poliza) {
        if (poliza instanceof PolizaIndividualEntity polizaIndividual) {
            RiesgoEntity riesgo = polizaIndividual.getRiesgo();
            if (riesgo != null) {
                riesgo.setEstado(EstadoRiesgo.CANCELADO);
            }
            return;
        }

        if (poliza instanceof PolizaColectivaEntity polizaColectiva && polizaColectiva.getRiesgos() != null) {
            polizaColectiva.getRiesgos().forEach(riesgo -> riesgo.setEstado(EstadoRiesgo.CANCELADO));
        }
    }

    private PolizaEntity obtenerPoliza(String polizaId) {
        return polizaRepository.findById(polizaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poliza no encontrada"));
    }

    private RiesgoEntity obtenerRiesgo(String riesgoId) {
        return riesgoRepository.findById(riesgoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riesgo no encontrado"));
    }

    private boolean coincideTipo(PolizaEntity poliza, String tipo) {
        String tipoNormalizado = tipo.toUpperCase(Locale.ROOT);
        return switch (tipoNormalizado) {
            case "INDIVIDUAL" -> poliza instanceof PolizaIndividualEntity;
            case "COLECTIVA" -> poliza instanceof PolizaColectivaEntity;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de poliza invalido");
        };
    }

    private float redondear(float valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
