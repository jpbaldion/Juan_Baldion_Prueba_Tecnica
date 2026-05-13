package com.pruebatecnica.poliza.entities;

import java.util.Date;

import com.pruebatecnica.poliza.enums.EstadoPoliza;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PolizaEntity extends BaseEntity {
	private String numeroPoliza;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;

    private float valorCanonMensual;

    private float valorPrima;

    @Enumerated(EnumType.STRING)
    private EstadoPoliza estado;
}
