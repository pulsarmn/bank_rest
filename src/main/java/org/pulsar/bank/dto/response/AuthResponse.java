package org.pulsar.bank.dto.response;


public record AuthResponse(
        String accessToken,
        String refreshToken,
        String refreshTokenId
) {
}
