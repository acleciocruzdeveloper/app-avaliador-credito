package io.aclecioscruz.service_appraiser.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.novo-cartao-solicitado}")
    private String emissorCartaoQueue;

    @Bean
    public Queue queue() {
        return new Queue(emissorCartaoQueue, true);
    }
}
