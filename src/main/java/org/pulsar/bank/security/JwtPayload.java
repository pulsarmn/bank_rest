package org.pulsar.bank.security;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;


public record JwtPayload(
        JWSHeader header,
        JWTClaimsSet claims
) {
}
