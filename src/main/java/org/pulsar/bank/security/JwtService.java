package org.pulsar.bank.security;


public interface JwtService {

    String generateAccessToken(JwtClaims claims);

    String generateRefreshToken();
}
