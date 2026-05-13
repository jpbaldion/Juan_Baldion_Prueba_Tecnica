package com.pruebatecnica.poliza.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @PodamExclude
    @Id
    @Generated(value = "uuid")
    private String id;
}
