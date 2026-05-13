package com.pruebatecnica.poliza.entities;

import java.util.Date;

import com.pruebatecnica.poliza.enums.EstadoPoliza;


public abstract class PolizaEntity extends BaseEntity {
	private String numeroPoliza;
    private Date fechaInicio;
    private Date fechaFin;
    private float valorCanonMensual;
    private float valorPrima;
    private EstadoPoliza estado;
}
