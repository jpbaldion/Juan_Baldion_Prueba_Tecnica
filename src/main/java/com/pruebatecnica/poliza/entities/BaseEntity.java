package com.pruebatecnica.poliza.entities;

import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @PodamExclude
    @Id
    private String id;

    @PrePersist
    void assignIdIfMissing() {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
    }
}
