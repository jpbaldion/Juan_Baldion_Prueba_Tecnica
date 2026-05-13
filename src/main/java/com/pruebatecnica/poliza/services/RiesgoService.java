package com.pruebatecnica.poliza.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.pruebatecnica.poliza.entities.RiesgoEntity;
import com.pruebatecnica.poliza.enums.EstadoRiesgo;
import com.pruebatecnica.poliza.repositories.RiesgoRepository;

@Service
@Transactional
public class RiesgoService {

	@Autowired
	private RiesgoRepository riesgoRepository;

	public RiesgoEntity cancelarRiesgo(String riesgoId) {
		RiesgoEntity riesgo = riesgoRepository.findById(riesgoId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riesgo no encontrado"));
		riesgo.setEstado(EstadoRiesgo.CANCELADO);
		return riesgoRepository.save(riesgo);
	}
    
}
