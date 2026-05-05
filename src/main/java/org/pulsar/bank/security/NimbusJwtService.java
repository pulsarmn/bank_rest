package org.pulsar.bank.security;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class NimbusJwtService implements JwtService {

    private final ECPrivateKey accessTokenPrivateKey;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final JWSAlgorithm DEFAULT_ALGORITHM = JWSAlgorithm.ES384;

    @Override
    public String generateAccessToken(JwtClaims claims) {
        JWSHeader jwtHeader = new JWSHeader(DEFAULT_ALGORITHM);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
        claims.getClaims().forEach(claimsBuilder::claim);

        JWTClaimsSet jwtClaims = claimsBuilder.build();
        try {
            JWSSigner signer = new ECDSASigner(accessTokenPrivateKey);
            SignedJWT token = new SignedJWT(jwtHeader, jwtClaims);
            token.sign(signer);

            return token.serialize();
        } catch (JOSEException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateRefreshToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getEncoder().encodeToString(tokenBytes);
    }
}
