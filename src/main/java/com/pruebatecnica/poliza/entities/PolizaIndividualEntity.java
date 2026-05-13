package com.pruebatecnica.poliza.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PolizaIndividualEntity extends PolizaEntity {
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "riesgo_id")
    private RiesgoEntity riesgo;
}