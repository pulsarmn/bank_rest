package org.pulsar.bank.security.jwt;


import org.pulsar.bank.exception.JwtVerificationException;

public interface JwtVerifier {

    JwtClaims verify(String accessToken) throws JwtVerificationException;
}
