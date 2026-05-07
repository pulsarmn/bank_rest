package org.pulsar.bank.security.jwt;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.pulsar.bank.exception.JwtVerificationException;
import org.springframework.stereotype.Component;

import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;


@Component
@RequiredArgsConstructor
public class NimbusJwtVerifier implements JwtVerifier {

    private final Clock clock;
    private final ECPublicKey accessTokenPublicKey;

    @Override
    public JwtClaims verify(String accessToken) throws JwtVerificationException {
        try {
            JWSVerifier verifier = new ECDSAVerifier(accessTokenPublicKey);
            SignedJWT token = SignedJWT.parse(accessToken);

            boolean isInvalid = !token.verify(verifier);
            if (isInvalid) {
                throw new JwtVerificationException("Invalid JWT signature");
            } else if (isExpired(token)) {
                throw new JwtVerificationException("The token has expired");
            }
            return buildClaims(token);
        } catch (JOSEException | ParseException e) {
            throw new JwtVerificationException("Invalid JWT");
        }
    }

    private boolean isExpired(SignedJWT token) throws ParseException {
        Instant currentTime = Instant.now(clock);
        Instant expirationTime = getExpirationTime(token);
        return currentTime.isAfter(expirationTime);
    }

    private Instant getExpirationTime(SignedJWT token) throws ParseException {
        return token.getJWTClaimsSet()
                .getExpirationTime()
                .toInstant();
    }

    private JwtClaims buildClaims(SignedJWT token) throws ParseException {
        JWTClaimsSet jwtClaimsSet = token.getJWTClaimsSet();

        JwtClaims.Builder claimsBuilder = JwtClaims.builder();
        jwtClaimsSet.getClaims().forEach(claimsBuilder::claim);

        return claimsBuilder.build();
    }
}
