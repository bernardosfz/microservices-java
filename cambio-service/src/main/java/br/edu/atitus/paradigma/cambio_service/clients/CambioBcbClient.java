package br.edu.atitus.paradigma.cambio_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bcb-olinda", url = "https://olinda.bcb.gov.br")
public interface CambioBcbClient {
	
	
	@GetMapping("/CotacaoMoedaDia(moeda={moeda},dataCotacao={dataCotacao})?@moeda='{moeda}'&@dataCotacao='{dataCotacao}'&$format=json")
	ListaCambioBcbResponse getCambioBcb(
			@PathVariable String moeda,
			@PathVariable String dataCotacao);
}
