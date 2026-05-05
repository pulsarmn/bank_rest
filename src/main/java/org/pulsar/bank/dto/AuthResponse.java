package org.pulsar.bank.dto;


public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
