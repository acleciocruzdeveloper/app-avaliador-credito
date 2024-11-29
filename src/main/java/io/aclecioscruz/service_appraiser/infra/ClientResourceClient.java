package io.aclecioscruz.service_appraiser.infra;

import io.aclecioscruz.service_appraiser.domain.model.DadaClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-client", path = "/clients")
public interface ClientResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadaClient> dadosClient(@RequestParam("cpf") String cpf);

}
