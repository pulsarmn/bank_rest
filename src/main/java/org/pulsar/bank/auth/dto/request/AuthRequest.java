package org.pulsar.bank.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record AuthRequest(

        @NotBlank
        @Size(min = 4, max = 64)
        String login,

        @NotBlank
        @Size(min = 8, max = 64)
        String password
) {

    @Override
    public String toString() {
        return "AuthRequest{login=%s}".formatted(login);
    }
}
