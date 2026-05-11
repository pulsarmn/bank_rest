package org.pulsar.bank.exception;


public class CardCreationException extends RuntimeException {

    public CardCreationException(String message) {
        super(message);
    }

    public CardCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardCreationException(Throwable cause) {
        super(cause);
    }
}
