package io.aclecioscruz.service_appraiser.application.advice;


public class DataConsumerNotFoundException extends RuntimeException {

    public DataConsumerNotFoundException(String message) {
        super(message);
    }
}
