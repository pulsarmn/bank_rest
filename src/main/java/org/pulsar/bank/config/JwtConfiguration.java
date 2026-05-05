package org.pulsar.bank.config;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

    private final JwtProperties jwtProperties;

    private static final String DEFAULT_ALGORITHM = "EC";

    @Bean
    ECPrivateKey accessTokenPrivateKey() throws Exception {
        String rawPrivateKey = jwtProperties.getAccessToken().getPrivateKey();
        byte[] privateKeyBytes = Base64.getDecoder().decode(rawPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALGORITHM);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }
}
