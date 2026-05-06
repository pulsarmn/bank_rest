package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.pulsar.bank.entity.RefreshToken;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    // TODO: add Clock field
    private final HashService hashService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken create(User user, String rawRefreshToken) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(rawRefreshToken);

        String refreshTokenHash = hashService.hash(rawRefreshToken);
        Instant currentTime = Instant.now();
        Instant expirationTime = currentTime.plusMillis(jwtProperties.getRefreshToken().getExpirationMillis());
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(refreshTokenHash)
                .user(user)
                .issuedAt(currentTime)
                .expiresAt(expirationTime)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}
