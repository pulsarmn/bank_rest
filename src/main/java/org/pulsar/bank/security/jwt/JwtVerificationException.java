package org.pulsar.bank.security.jwt;


public class JwtVerificationException extends RuntimeException {

    public JwtVerificationException(String message) {
        super(message);
    }
}
