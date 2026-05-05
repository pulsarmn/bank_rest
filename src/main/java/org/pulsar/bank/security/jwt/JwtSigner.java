package org.pulsar.bank.security.jwt;


public interface JwtSigner {

    String sign(JwtPayload payload);
}
