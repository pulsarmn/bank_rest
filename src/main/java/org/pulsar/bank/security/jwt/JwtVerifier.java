package org.pulsar.bank.security.jwt;


public interface JwtVerifier {

    JwtClaims verify(String accessToken) throws JwtVerificationException;
}
