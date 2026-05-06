package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.pulsar.bank.dto.AuthResponse;
import org.pulsar.bank.dto.RefreshRequest;
import org.pulsar.bank.entity.RefreshToken;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.exception.InvalidRefreshTokenException;
import org.pulsar.bank.exception.RefreshTokenNotFoundException;
import org.pulsar.bank.repository.RefreshTokenRepository;
import org.pulsar.bank.security.jwt.JwtClaims;
import org.pulsar.bank.security.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    // TODO: add Clock field
    private final JwtService jwtService;
    private final HashService hashService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken create(User user, String rawRefreshToken) {
        if (user == null || rawRefreshToken == null) {
            throw new IllegalArgumentException();
        }

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

    public AuthResponse getNewAccessToken(RefreshRequest refreshRequest) {
        if (refreshRequest == null) {
            throw new IllegalArgumentException("Refresh Request must not be null");
        }

        UUID refreshTokenId = UUID.fromString(refreshRequest.refreshTokenId());
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        boolean hashesMatch = hashService.matches(refreshRequest.refreshToken(), refreshToken.getTokenHash());
        if (!hashesMatch) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        boolean isExpired = refreshToken.getExpiresAt().isBefore(Instant.now());
        if (isExpired) {
            throw new InvalidRefreshTokenException("Refresh token is expired");
        }

        User user = refreshToken.getUser();
        JwtClaims claims = JwtClaims.builder()
                .subject(user.getLogin())
                .claim("iat", Instant.now().getEpochSecond())
                .claim("exp", Instant.now().plusMillis(jwtProperties.getAccessToken().getExpirationMillis()).getEpochSecond())
                .claim("roles", List.of(user.getRole().toString()))
                .build();

        String accessToken = jwtService.generateAccessToken(claims);
        String newRawRefreshToken = jwtService.generateRefreshToken();

        refreshToken.setRevokedAt(Instant.now());
        Instant currentTime = Instant.now();
        Instant expirationTime = currentTime.plusMillis(jwtProperties.getRefreshToken().getExpirationMillis());
        RefreshToken newRefreshToken = RefreshToken.builder()
                .tokenHash(newRawRefreshToken)
                .user(user)
                .issuedAt(currentTime)
                .expiresAt(expirationTime)
                .build();
        refreshTokenRepository.save(newRefreshToken);
        return new AuthResponse(accessToken, newRawRefreshToken, newRefreshToken.getId().toString());
    }
}
