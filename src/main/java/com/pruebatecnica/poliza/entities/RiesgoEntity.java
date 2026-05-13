package com.pruebatecnica.poliza.entities;

import com.pruebatecnica.poliza.enums.EstadoRiesgo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RiesgoEntity extends BaseEntity {
    @NotBlank(message = "El identificador del inmueble es obligatorio")
    private String identificadorImueble;

    @Positive(message = "El valor canon del riesgo debe ser mayor que cero")
    private float valorCanonRiesgo;

    @Enumerated(EnumType.STRING)
    private EstadoRiesgo estado;
    
}
