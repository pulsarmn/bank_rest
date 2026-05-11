package org.pulsar.bank.auth;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.pulsar.bank.auth.domain.RefreshToken;
import org.pulsar.bank.auth.domain.User;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;


@Component
@RequiredArgsConstructor
public class RefreshTokenFactory {

    private final Clock clock;
    private final JwtProperties jwtProperties;

    public RefreshToken create(User user, String refreshTokenHash) {
        Instant currentTime = Instant.now(clock);
        Instant expirationTime = getExpirationTime(currentTime);
        return RefreshToken.builder()
                .tokenHash(refreshTokenHash)
                .user(user)
                .issuedAt(currentTime)
                .expiresAt(expirationTime)
                .build();
    }

    private Instant getExpirationTime(Instant currentTime) {
        long expirationMillis = jwtProperties.getRefreshToken().getExpirationMillis();
        return currentTime.plusMillis(expirationMillis);
    }
}
