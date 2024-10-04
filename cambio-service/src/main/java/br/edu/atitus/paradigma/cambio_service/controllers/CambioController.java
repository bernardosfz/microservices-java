package br.edu.atitus.paradigma.cambio_service.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.paradigma.cambio_service.entities.CambioEntity;
import br.edu.atitus.paradigma.cambio_service.repositories.CambioRepository;

@RestController
@RequestMapping("cambio-service")
public class CambioController {

	private final CambioRepository cambioRepository;
	
	public CambioController(CambioRepository cambioRepository) {
		super();
		this.cambioRepository = cambioRepository;
	}
	
	
	@GetMapping("/{fator}/{origem}/{destino}")
	public ResponseEntity<Optional<CambioEntity>> getCambio(
			@PathVariable double fator,
			@PathVariable String origem,	
			@PathVariable String destino) {
		
		Optional<CambioEntity> cambio = cambioRepository.findByOrigemAndDestino(origem, destino);
				
		return ResponseEntity.ok(cambio);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		String cleanMessage = e.getMessage().replaceAll("[\\r\\n]", " ");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cleanMessage);
	}
}
