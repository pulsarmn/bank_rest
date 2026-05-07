package org.pulsar.bank.exception;


public class JwtVerificationException extends RuntimeException {

    public JwtVerificationException(String message) {
        super(message);
    }
}
