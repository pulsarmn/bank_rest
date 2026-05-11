package org.pulsar.bank.card.dto;


import jakarta.validation.constraints.NotBlank;

public record CardBlockRequest(

        @NotBlank
        String cardPan
) {
}
