package io.aclecioscruz.service_appraiser.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.aclecioscruz.service_appraiser.domain.DadosParaSolicitacao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmissorCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue emissorCartaoQueue;

    public void solicitarCartao(DadosParaSolicitacao data) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                emissorCartaoQueue.getName(), converterIntoJson(data));
    }

    private String converterIntoJson(DadosParaSolicitacao dados) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(dados);
    }
}
