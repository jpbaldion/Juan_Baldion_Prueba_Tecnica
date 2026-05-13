package com.pruebatecnica.poliza.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class PolizaColectivaEntity extends PolizaEntity {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RiesgoEntity> riesgos;

}
