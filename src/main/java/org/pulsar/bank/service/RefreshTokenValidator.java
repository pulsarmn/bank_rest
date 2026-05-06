package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.entity.RefreshToken;
import org.pulsar.bank.exception.InvalidRefreshTokenException;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;


@Component
@RequiredArgsConstructor
public class RefreshTokenValidator {

    private final Clock clock;
    private final HashService hashService;

    public void validate(RefreshToken refreshToken, String rawRefreshToken) {
        boolean hashesMatch = hashService.matches(rawRefreshToken, refreshToken.getTokenHash());
        if (!hashesMatch) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        if (isExpired(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
    }

    public boolean isExpired(RefreshToken refreshToken) {
        Instant currentTime = Instant.now(clock);
        Instant expirationTime = refreshToken.getExpiresAt();
        return expirationTime.isBefore(currentTime);
    }
}
