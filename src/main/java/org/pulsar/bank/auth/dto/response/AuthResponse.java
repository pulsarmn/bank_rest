package org.pulsar.bank.auth.dto.response;


public record AuthResponse(
        String accessToken,
        String refreshToken,
        String refreshTokenId
) {
}
