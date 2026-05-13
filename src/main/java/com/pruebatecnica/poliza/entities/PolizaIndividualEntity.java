package com.pruebatecnica.poliza.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class PolizaIndividualEntity extends PolizaEntity {
	@OneToOne
    private RiesgoEntity riesgo;
}