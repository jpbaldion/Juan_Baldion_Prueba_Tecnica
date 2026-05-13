package com.pruebatecnica.poliza.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebatecnica.poliza.entities.PolizaEntity;

@Repository
public interface PolizaRepository extends JpaRepository<PolizaEntity, String> {

}