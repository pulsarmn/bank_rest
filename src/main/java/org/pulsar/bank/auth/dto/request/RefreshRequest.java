package org.pulsar.bank.auth.dto.request;

import jakarta.validation.constraints.NotBlank;


public record RefreshRequest(

        @NotBlank
        String refreshToken,

        @NotBlank
        String refreshTokenId
) {
}
