package org.pulsar.bank.security.jwt;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.pulsar.bank.auth.domain.User;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtClaimsFactory {

    private final Clock clock;
    private final JwtProperties jwtProperties;

    public JwtClaims create(User user) {
        Instant currentTime = Instant.now(clock);
        Instant expirationTime = getExpirationTime(currentTime);
        return JwtClaims.builder()
                .subject(user.getLogin())
                .claim("iat", currentTime.getEpochSecond())
                .claim("exp", expirationTime.getEpochSecond())
                .claim("roles", List.of(user.getRole().toString()))
                .build();
    }

    private Instant getExpirationTime(Instant currentTime) {
        long expirationMillis = jwtProperties.getAccessToken().getExpirationMillis();
        return currentTime.plusMillis(expirationMillis);
    }
}
