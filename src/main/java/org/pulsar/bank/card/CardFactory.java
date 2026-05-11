package org.pulsar.bank.card;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.card.domain.Card;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.crypto.service.HmacService;
import org.pulsar.bank.crypto.service.CryptoService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.Base64;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class CardFactory {

    private final HmacService hmacService;
    private final CryptoService cryptoService;
    private final CardNumberGenerator cardNumberGenerator;

    private static final int NUMBER_OF_YEARS_TO_EXPIRE = 10;

    public Card create(User user) {
        Objects.requireNonNull(user);

        String cardNumber = cardNumberGenerator.generate();
        String encryptedPan = encryptPanToBase64(cardNumber);
        String panHash = hmacService.hash(cardNumber);
        YearMonth expiresAt = YearMonth.now().plusYears(NUMBER_OF_YEARS_TO_EXPIRE);

        return Card.builder()
                .owner(user)
                .encryptedPan(encryptedPan)
                .panHash(panHash)
                .expiresAt(expiresAt)
                .build();
    }

    private String encryptPanToBase64(String cardNumber) {
        byte[] encryptedPanBytes = cryptoService.encrypt(cardNumber.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedPanBytes);
    }
}
