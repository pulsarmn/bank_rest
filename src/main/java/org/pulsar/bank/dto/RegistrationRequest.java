package org.pulsar.bank.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record RegistrationRequest(

        @NotBlank
        @Size(min = 4, max = 64)
        String login,

        @NotBlank
        @Size(min = 8, max = 64)
        String password,

        @NotBlank
        @Size(min = 2, max = 32)
        String firstName,

        @NotBlank
        @Size(min = 1, max = 32)
        String lastName
) {

    @Override
    public String toString() {
        return "RegistrationRequest{login=%s, firstName=%s, lastName=%s}"
                .formatted(login, firstName, lastName);
    }
}
