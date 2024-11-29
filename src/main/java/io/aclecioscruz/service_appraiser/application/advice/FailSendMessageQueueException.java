package io.aclecioscruz.service_appraiser.application.advice;

public class FailSendMessageQueueException extends RuntimeException{

    public FailSendMessageQueueException(String message) {
        super(message);
    }
}
