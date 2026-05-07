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


@Component
@RequiredArgsConstructor
public class NimbusJwtVerifier implements JwtVerifier {

    private final ECPublicKey accessTokenPublicKey;

    @Override
    public JwtClaims verify(String accessToken) throws JwtVerificationException {
        try {
            JWSVerifier verifier = new ECDSAVerifier(accessTokenPublicKey);
            SignedJWT token = SignedJWT.parse(accessToken);

            boolean isValid = token.verify(verifier);
            if (isValid) {
                JWTClaimsSet jwtClaimsSet = token.getJWTClaimsSet();

                JwtClaims.Builder claimsBuilder = JwtClaims.builder();
                jwtClaimsSet.getClaims().forEach(claimsBuilder::claim);

                return claimsBuilder.build();
            }
            throw new JwtVerificationException("Invalid JWT");
        } catch (JOSEException | ParseException e) {
            throw new JwtVerificationException("Invalid JWT");
        }
    }
}
