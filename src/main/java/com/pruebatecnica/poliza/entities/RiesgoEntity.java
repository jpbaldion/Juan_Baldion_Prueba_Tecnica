package com.pruebatecnica.poliza.entities;

import com.pruebatecnica.poliza.enums.EstadoRiesgo;

import jakarta.persistence.Entity;

@Entity
public class RiesgoEntity extends BaseEntity {
    private String identificadorImueble;
    private float valorCanonRiesgo;
    private EstadoRiesgo estado;
    
}
