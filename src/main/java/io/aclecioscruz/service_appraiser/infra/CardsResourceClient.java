package io.aclecioscruz.service_appraiser.infra;

import io.aclecioscruz.service_appraiser.domain.DataCard;
import io.aclecioscruz.service_appraiser.domain.model.Cards;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-card", path = "/cards")
public interface CardsResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<List<Cards>> getCardForCustomer(
            @RequestParam("cpf") String cpf);

    @GetMapping(params = "income")
    ResponseEntity<List<DataCard>> getCardsIncome(@RequestParam("income") Long income);
}
