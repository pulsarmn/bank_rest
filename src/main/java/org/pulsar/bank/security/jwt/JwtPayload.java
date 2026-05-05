package org.pulsar.bank.security.jwt;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;


public record JwtPayload(
        JWSHeader header,
        JWTClaimsSet claims
) {
}
