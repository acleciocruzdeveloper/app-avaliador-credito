package io.aclecioscruz.service_appraiser.application.advice;

public class ServerErrorMicroserviceException extends RuntimeException {
    public ServerErrorMicroserviceException(String communicationFailureMicroservice, String message) {
        super(message);
    }
}
