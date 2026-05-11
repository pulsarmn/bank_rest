package org.pulsar.bank.security.jwt.nimbus;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.pulsar.bank.security.jwt.JwtClaims;
import org.pulsar.bank.security.jwt.JwtPayload;
import org.pulsar.bank.security.jwt.JwtService;
import org.pulsar.bank.security.jwt.JwtSigner;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class NimbusJwtService implements JwtService {

    private final JwtSigner jwtSigner;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final JWSAlgorithm DEFAULT_ALGORITHM = JWSAlgorithm.ES384;

    @Override
    public String generateAccessToken(JwtClaims claims) {
        JwtPayload jwtPayload = buildJwtPayload(claims);
        return jwtSigner.sign(jwtPayload);
    }

    private JwtPayload buildJwtPayload(JwtClaims claims) {
        JWSHeader jwtHeader = new JWSHeader(DEFAULT_ALGORITHM);
        JWTClaimsSet jwtClaims = buildJwtClaims(claims);

        return new JwtPayload(jwtHeader, jwtClaims);
    }

    private JWTClaimsSet buildJwtClaims(JwtClaims claims) {
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
        claims.getClaims().forEach(claimsBuilder::claim);
        return claimsBuilder.build();
    }

    @Override
    public String generateRefreshToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getEncoder().encodeToString(tokenBytes);
    }
}
