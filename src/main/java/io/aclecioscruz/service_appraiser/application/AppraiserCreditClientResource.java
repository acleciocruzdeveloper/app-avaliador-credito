package io.aclecioscruz.service_appraiser.application;

import io.aclecioscruz.service_appraiser.application.advice.DataConsumerNotFoundException;
import io.aclecioscruz.service_appraiser.application.advice.FailSendMessageQueueException;
import io.aclecioscruz.service_appraiser.domain.DadosParaSolicitacao;
import io.aclecioscruz.service_appraiser.domain.DataCustomerEvaluation;
import io.aclecioscruz.service_appraiser.domain.DataEvaluation;
import io.aclecioscruz.service_appraiser.domain.model.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("appraiser-clients")
@RequiredArgsConstructor
public class AppraiserCreditClientResource {

    private final AppraiserCreditService appraiserCreditService;

    @GetMapping
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok().body("App appraiser client OK!");
    }

    @GetMapping(value = "status-clients", params = "cpf")
    public ResponseEntity<ClientResponse> consultStatusClient(
            @RequestParam("cpf") String cpf) {
        return ResponseEntity.ok()
                .body(appraiserCreditService.seekCustomerSituation(cpf));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity conductCustomerEvaluation(
            @RequestBody DataCustomerEvaluation dataEvaluation) {
        try {
            DataEvaluation response = appraiserCreditService
                    .dataEvaluation(dataEvaluation.cpf(), dataEvaluation.renda());
            return ResponseEntity.ok().body(response);
        } catch (DataConsumerNotFoundException exception) {
            throw new RuntimeException(exception);
        }

    }

    @PostMapping("solicitacao-cortoes")
    public ResponseEntity solicitarCartao(@RequestBody DadosParaSolicitacao dados) {
        try {
            return ResponseEntity.ok().body(appraiserCreditService.solicitarEmissorCartao(dados));
        } catch (FailSendMessageQueueException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
