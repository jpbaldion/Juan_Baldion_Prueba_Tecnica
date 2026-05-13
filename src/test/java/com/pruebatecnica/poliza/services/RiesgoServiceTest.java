package com.pruebatecnica.poliza.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.pruebatecnica.poliza.entities.RiesgoEntity;
import com.pruebatecnica.poliza.enums.EstadoRiesgo;
import com.pruebatecnica.poliza.repositories.RiesgoRepository;

@ExtendWith(MockitoExtension.class)
class RiesgoServiceTest {

    @InjectMocks
    private RiesgoService riesgoService;

    @Mock
    private RiesgoRepository riesgoRepository;

    @Test
    void cancelarRiesgo_guardaYCambiaEstado() {
        RiesgoEntity r = new RiesgoEntity();
        r.setId("r1");
        r.setEstado(EstadoRiesgo.ACTIVO);

        when(riesgoRepository.findById("r1")).thenReturn(Optional.of(r));
        when(riesgoRepository.save(r)).thenReturn(r);

        RiesgoEntity res = riesgoService.cancelarRiesgo("r1");
        assertThat(res.getEstado()).isEqualTo(EstadoRiesgo.CANCELADO);
        verify(riesgoRepository).save(r);
    }

    @Test
    void cancelarRiesgo_noEncontrado_lanza() {
        when(riesgoRepository.findById("x")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> riesgoService.cancelarRiesgo("x"));
    }
}
