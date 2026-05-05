package org.pulsar.bank.security;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.time.Instant;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final ECPrivateKey accessTokenPrivateKey;
    private final ECPrivateKey refreshTokenPrivateKey;

    private static final JWSAlgorithm DEFAULT_ALGORITHM = JWSAlgorithm.ES384;

    public String generateAccessToken(JwtClaims claims) {
        return generateToken(claims, jwtProperties.getAccessToken().getExpirationMillis(), accessTokenPrivateKey);
    }

    public String generateRefreshToken(JwtClaims claims) {
        return generateToken(claims, jwtProperties.getRefreshToken().getExpirationMillis(), refreshTokenPrivateKey);
    }

    private String generateToken(JwtClaims claims, long expirationMillis, ECPrivateKey privateKey) {
        JWSHeader header = new JWSHeader.Builder(DEFAULT_ALGORITHM).build();

        Date expirationTime = getExpirationTime(jwtProperties.getRefreshToken().getExpirationMillis());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(claims.getSubject())
                .claim("role", claims.getClaim("role"))
                .expirationTime(expirationTime)
                .build();

        try {
            ECDSASigner signer = new ECDSASigner(privateKey);
            SignedJWT signedToken = new SignedJWT(header, jwtClaimsSet);
            signedToken.sign(signer);

            return signedToken.serialize();
        } catch (JOSEException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    private Date getExpirationTime(long expirationMillis) {
        Instant currentTime = Instant.now();
        Instant expirationTime = currentTime.plusMillis(expirationMillis);
        return Date.from(expirationTime);
    }
}
