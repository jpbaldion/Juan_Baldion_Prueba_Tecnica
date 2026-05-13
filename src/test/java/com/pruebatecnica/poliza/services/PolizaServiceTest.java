package com.pruebatecnica.poliza.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.pruebatecnica.poliza.entities.PolizaColectivaEntity;
import com.pruebatecnica.poliza.entities.PolizaEntity;
import com.pruebatecnica.poliza.entities.PolizaIndividualEntity;
import com.pruebatecnica.poliza.entities.RiesgoEntity;
import com.pruebatecnica.poliza.enums.EstadoPoliza;
import com.pruebatecnica.poliza.enums.EstadoRiesgo;
import com.pruebatecnica.poliza.repositories.PolizaRepository;
import com.pruebatecnica.poliza.repositories.RiesgoRepository;

@ExtendWith(MockitoExtension.class)
class PolizaServiceTest {

    @InjectMocks
    private PolizaService polizaService;

    @Mock
    private PolizaRepository polizaRepository;

    @Mock
    private RiesgoRepository riesgoRepository;

    @Mock
    private RestTemplate restTemplate;

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = PolizaService.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(polizaService, value);
    }

    @Test
    void listarPolizas_filtraPorTipoYEstado() {
        PolizaIndividualEntity ind = new PolizaIndividualEntity();
        ind.setId("p1");
        ind.setEstado(EstadoPoliza.ACTIVA);

        PolizaColectivaEntity col = new PolizaColectivaEntity();
        col.setId("p2");
        col.setEstado(EstadoPoliza.ACTIVA);

        when(polizaRepository.findAll()).thenReturn(List.of(ind, col));

        List<PolizaEntity> resultado = polizaService.listarPolizas("INDIVIDUAL", EstadoPoliza.ACTIVA);

        assertThat(resultado).hasSize(1).first().isInstanceOf(PolizaIndividualEntity.class);
    }

    @Test
    void renovarPoliza_aumentaValoresYNotifica() throws Exception {
        PolizaIndividualEntity pol = new PolizaIndividualEntity();
        pol.setId("p1");
        pol.setEstado(EstadoPoliza.ACTIVA);
        pol.setValorCanonMensual(100f);
        pol.setValorPrima(50f);

        when(polizaRepository.findById("p1")).thenReturn(Optional.of(pol));
        when(polizaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        setPrivateField("ipc", new BigDecimal("0.10"));
        setPrivateField("coreMockUrl", "http://core-mock/test");
        setPrivateField("coreMockApiKey", "key");

        PolizaEntity resultado = polizaService.renovarPoliza("p1");

        assertThat(resultado.getEstado()).isEqualTo(EstadoPoliza.RENOVADA);
        assertThat(resultado.getValorCanonMensual()).isGreaterThan(100f);
        assertThat(resultado.getValorPrima()).isGreaterThan(50f);
        verify(polizaRepository).save(pol);
        verify(restTemplate).postForObject(any(String.class), any(), any(Class.class));
    }

    @Test
    void cancelarPoliza_cancelaRiesgoYNotifica() throws Exception {
        PolizaIndividualEntity pol = new PolizaIndividualEntity();
        pol.setId("p1");
        pol.setEstado(EstadoPoliza.ACTIVA);

        RiesgoEntity riesgo = new RiesgoEntity();
        riesgo.setId("r1");
        riesgo.setEstado(EstadoRiesgo.ACTIVO);
        pol.setRiesgo(riesgo);

        when(polizaRepository.findById("p1")).thenReturn(Optional.of(pol));
        when(polizaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        setPrivateField("coreMockUrl", "http://core-mock/test");
        setPrivateField("coreMockApiKey", "key");

        PolizaEntity resultado = polizaService.cancelarPoliza("p1");

        assertThat(resultado.getEstado()).isEqualTo(EstadoPoliza.CANCELADA);
        assertThat(pol.getRiesgo().getEstado()).isEqualTo(EstadoRiesgo.CANCELADO);
        verify(polizaRepository).save(pol);
        verify(restTemplate).postForObject(any(String.class), any(), any(Class.class));
    }

    @Test
    void agregarRiesgo_agregaEnColectiva() throws Exception {
        PolizaColectivaEntity pol = new PolizaColectivaEntity();
        pol.setId("pC");
        pol.setEstado(EstadoPoliza.ACTIVA);

        when(polizaRepository.findById("pC")).thenReturn(Optional.of(pol));
        when(polizaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        setPrivateField("coreMockUrl", "http://core-mock/test");
        setPrivateField("coreMockApiKey", "key");

        RiesgoEntity nuevo = new RiesgoEntity();
        nuevo.setId("rNew");
        RiesgoEntity resultado = polizaService.agregarRiesgo("pC", nuevo);

        assertThat(resultado.getEstado()).isEqualTo(EstadoRiesgo.ACTIVO);
        assertThat(pol.getRiesgos()).contains(nuevo);
        verify(polizaRepository).save(pol);
        verify(restTemplate).postForObject(any(String.class), any(), any(Class.class));
    }

    @Test
    void agregarRiesgo_enIndividual_lanzaBadRequest() {
        PolizaIndividualEntity pol = new PolizaIndividualEntity();
        pol.setId("p1");
        when(polizaRepository.findById("p1")).thenReturn(Optional.of(pol));

        assertThrows(ResponseStatusException.class, () -> polizaService.agregarRiesgo("p1", new RiesgoEntity()));
    }

    @Test
    void cancelarRiesgo_cambiaEstado() {
        RiesgoEntity riesgo = new RiesgoEntity();
        riesgo.setId("r1");
        riesgo.setEstado(EstadoRiesgo.ACTIVO);

        when(riesgoRepository.findById("r1")).thenReturn(Optional.of(riesgo));

        RiesgoEntity res = polizaService.cancelarRiesgo("r1");
        assertThat(res.getEstado()).isEqualTo(EstadoRiesgo.CANCELADO);
    }
}
