package org.pulsar.bank.auth.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.crypto.service.HashService;
import org.pulsar.bank.auth.dto.response.AuthResponse;
import org.pulsar.bank.auth.dto.request.RefreshRequest;
import org.pulsar.bank.auth.domain.RefreshToken;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.auth.exception.InvalidRefreshTokenException;
import org.pulsar.bank.auth.exception.RefreshTokenNotFoundException;
import org.pulsar.bank.auth.repository.RefreshTokenRepository;
import org.pulsar.bank.security.jwt.JwtClaims;
import org.pulsar.bank.security.jwt.JwtService;
import org.pulsar.bank.security.jwt.JwtClaimsFactory;
import org.pulsar.bank.auth.RefreshTokenFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;
    private final HashService hashService;
    private final JwtClaimsFactory jwtClaimsFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final RefreshTokenValidator refreshTokenValidator;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken create(User user, String rawRefreshToken) {
        if (user == null || rawRefreshToken == null) {
            throw new IllegalArgumentException();
        }

        String refreshTokenHash = hashService.hash(rawRefreshToken);
        RefreshToken refreshToken = refreshTokenFactory.create(user, refreshTokenHash);

        return refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse generateNewTokenPair(RefreshRequest refreshRequest) {
        if (refreshRequest == null) {
            throw new IllegalArgumentException("RefreshRequest must not be null");
        }

        RefreshToken lastRefreshToken = find(refreshRequest);
        User user = lastRefreshToken.getUser();
        JwtClaims claims = jwtClaimsFactory.create(user);

        String accessToken = jwtService.generateAccessToken(claims);
        String newRawRefreshToken = jwtService.generateRefreshToken();

        String refreshTokenHash = hashService.hash(newRawRefreshToken);
        RefreshToken newRefreshToken = refreshTokenFactory.create(user, refreshTokenHash);

        lastRefreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(newRefreshToken);
        return new AuthResponse(accessToken, newRawRefreshToken, newRefreshToken.getId().toString());
    }

    private RefreshToken find(RefreshRequest refreshRequest) {
        UUID id = extractRefreshTokenId(refreshRequest);
        RefreshToken refreshToken = refreshTokenRepository.findById(id)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        String rawRefreshToken = refreshRequest.refreshToken();
        refreshTokenValidator.validate(refreshToken, rawRefreshToken);

        return refreshToken;
    }

    private UUID extractRefreshTokenId(RefreshRequest refreshRequest) {
        try {
            return UUID.fromString(refreshRequest.refreshTokenId());
        } catch (IllegalArgumentException e) {
            throw new InvalidRefreshTokenException("Invalid refresh token id");
        }
    }
}
