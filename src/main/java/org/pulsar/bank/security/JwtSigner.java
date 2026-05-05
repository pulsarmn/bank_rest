package org.pulsar.bank.security;


public interface JwtSigner {

    String sign(JwtPayload payload);
}
