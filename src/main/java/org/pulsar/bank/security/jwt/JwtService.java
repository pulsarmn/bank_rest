package org.pulsar.bank.security.jwt;


public interface JwtService {

    String generateAccessToken(JwtClaims claims);

    String generateRefreshToken();
}
