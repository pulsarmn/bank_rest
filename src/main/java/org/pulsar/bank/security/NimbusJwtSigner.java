package org.pulsar.bank.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NimbusJwtSigner implements JwtSigner {

    private final JWSSigner signer;

    @Override
    public String sign(JwtPayload payload) {
        try {
            SignedJWT signedToken = new SignedJWT(payload.header(), payload.claims());
            signedToken.sign(signer);
            return signedToken.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
