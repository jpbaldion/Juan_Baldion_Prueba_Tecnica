package com.pruebatecnica.poliza.entities;

import jakarta.persistence.Entity;

@Entity
public class PolizaEntity extends BaseEntity {
	private String name;
	private String description;
	private String image;
}
