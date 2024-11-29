package io.aclecioscruz.service_appraiser.application;

import feign.FeignException.FeignClientException;
import io.aclecioscruz.service_appraiser.application.advice.DataConsumerNotFoundException;
import io.aclecioscruz.service_appraiser.application.advice.FailSendMessageQueueException;
import io.aclecioscruz.service_appraiser.application.advice.ServerErrorMicroserviceException;
import io.aclecioscruz.service_appraiser.domain.DadosParaSolicitacao;
import io.aclecioscruz.service_appraiser.domain.DataCard;
import io.aclecioscruz.service_appraiser.domain.DataEvaluation;
import io.aclecioscruz.service_appraiser.domain.model.Cards;
import io.aclecioscruz.service_appraiser.domain.model.ClientResponse;
import io.aclecioscruz.service_appraiser.domain.model.DadaClient;
import io.aclecioscruz.service_appraiser.domain.model.Protocolo;
import io.aclecioscruz.service_appraiser.infra.CardsResourceClient;
import io.aclecioscruz.service_appraiser.infra.ClientResourceClient;
import io.aclecioscruz.service_appraiser.infra.mqueue.EmissorCartaoPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppraiserCreditService {

    private final ClientResourceClient dadaClient;
    private final CardsResourceClient cardsResourceClient;
    private final EmissorCartaoPublisher publisher;

    public ClientResponse seekCustomerSituation(String cpf) throws DataConsumerNotFoundException {

        try {
            ResponseEntity<DadaClient> dadaClientResponseEntity = dadaClient.dadosClient(cpf);
            ResponseEntity<List<Cards>> cardForCustomer = cardsResourceClient.getCardForCustomer(cpf);
            log.debug("DATA CUSTOMER {}", dadaClientResponseEntity);

            return new ClientResponse(dadaClientResponseEntity.getBody(), cardForCustomer.getBody());
        } catch (FeignClientException e) {
            if (HttpStatus.NOT_FOUND.value() == e.status()) {
                throw new DataConsumerNotFoundException("CONSUMER NOT FOUND!");
            }
            throw new ServerErrorMicroserviceException("COMMUNICATION FAILURE MICROSERVICE ", e.getMessage());
        }
    }

    public DataEvaluation dataEvaluation(String cpf, Long renda) throws DataConsumerNotFoundException, ServerErrorMicroserviceException {
        try {
            ResponseEntity<DadaClient> responseClient = dadaClient.dadosClient(cpf);
            ResponseEntity<List<DataCard>> cardsIncome = cardsResourceClient.getCardsIncome(renda);
            List<DataCard> cards = cardsIncome.getBody();
            assert cards != null;
            List<DataCard> dataCardList = cards.stream().map(dataCard -> {
                DadaClient responseClientBody = responseClient.getBody();

                BigDecimal limitedBasic = dataCard.limite();
                assert responseClientBody != null;
                BigDecimal ageClient = BigDecimal.valueOf(responseClientBody.idade());
                BigDecimal factor = ageClient.divide(BigDecimal.valueOf(10));
                BigDecimal limitedApproved = factor.multiply(limitedBasic);

                return new DataCard(dataCard.nome(), dataCard.flag(), limitedApproved);
            }).toList();

            return new DataEvaluation(dataCardList);
        } catch (FeignClientException e) {
            if (HttpStatus.NOT_FOUND.value() == e.status()) {
                throw new DataConsumerNotFoundException("Client not found!!");
            }
            throw new ServerErrorMicroserviceException("COMMUNICATION FAILURE!", e.getMessage());
        }

    }

    public Protocolo solicitarEmissorCartao(DadosParaSolicitacao dados) {
        try {
            publisher.solicitarCartao(dados);
            return new Protocolo(UUID.randomUUID().toString());
        } catch (Exception e) {
            throw new FailSendMessageQueueException("FAIL SEND MESSAGE REQUEST CARD!");
        }
    }
}
