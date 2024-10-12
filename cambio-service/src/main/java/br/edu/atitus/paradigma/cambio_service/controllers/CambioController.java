package br.edu.atitus.paradigma.cambio_service.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.paradigma.cambio_service.clients.CambioBcbClient;
import br.edu.atitus.paradigma.cambio_service.clients.ListaCambioBcbResponse;
import br.edu.atitus.paradigma.cambio_service.entities.CambioEntity;
import br.edu.atitus.paradigma.cambio_service.repositories.CambioRepository;

@RestController
@RequestMapping("cambio-service")
public class CambioController {

	private final CambioRepository cambioRepository;
	private final CambioBcbClient cambioBcb;
	
	public CambioController(CambioRepository cambioRepository, CambioBcbClient cambioBcb) {
		super();
		this.cambioRepository = cambioRepository;
		this.cambioBcb = cambioBcb;
	}
	
	@Value("${server.port}")
	private int porta;
	
	@GetMapping("/{valor}/{origem}/{destino}")
	public ResponseEntity<CambioEntity> getCambio(
			@PathVariable double valor,
			@PathVariable String origem,	
			@PathVariable String destino) throws Exception{
		
		CambioEntity cambio = cambioRepository.findByOrigemAndDestino(origem, destino).orElseThrow(() -> new Exception("Moedas não existentes"));
		
		ListaCambioBcbResponse cotacao = cambioBcb.getCambioBcb("USD", "10-10-2024");
		
		//cambio.setFator(cotacao.getValue().get(0).getCotacaoVenda());
		
		cambio.setValorConvertido(valor * cambio.getFator());
		cambio.setAmbiente("Produto-Service run in port: " + porta);
		return ResponseEntity.ok(cambio);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		String cleanMessage = e.getMessage().replaceAll("[\\r\\n]", " ");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cleanMessage);
	}
}
