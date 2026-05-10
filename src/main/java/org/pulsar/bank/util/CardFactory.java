package org.pulsar.bank.util;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.entity.Card;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.service.HashService;
import org.pulsar.bank.service.HmacService;
import org.pulsar.bank.service.crypto.CryptoService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class CardFactory {

    private final HmacService hmacService;
    private final CryptoService cryptoService;

    private static final String MIR_PREFIX = "2200";
    private static final Random RANDOM = new Random();

    public Card create(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        String cardNumber = generateCardNumber();
        String lastFourDigits = getLastFourDigits(cardNumber);
        byte[] encryptedPanBytes = cryptoService.encrypt(cardNumber.getBytes(StandardCharsets.UTF_8));
        String encryptedPan = new String(encryptedPanBytes);
        String panHash = hmacService.hash(cardNumber);
        YearMonth expiresAt = YearMonth.now().plusYears(10);

        return Card.builder()
                .owner(user)
                .encryptedPan(encryptedPan)
                .panHash(panHash)
                .lastFourDigits(lastFourDigits)
                .expiresAt(expiresAt)
                .build();
    }

    private String generateCardNumber() {
        int digitsNeeded = 12;

        String numberWithoutCheck = MIR_PREFIX + Stream.generate(() -> RANDOM.nextInt(10))
                .limit(digitsNeeded)
                .map(String::valueOf)
                .collect(Collectors.joining());
        return numberWithoutCheck + calculateLastDigit(numberWithoutCheck);
    }

    private int calculateLastDigit(String numberWithoutCheck) {
        int sum = 0;
        boolean doubleDigit = true;

        for (int i = 0; i < numberWithoutCheck.length(); i++) {
            int digit = Character.getNumericValue(numberWithoutCheck.charAt(i));
            if (doubleDigit) {
                digit *= 2;
                digit = (digit > 9) ? digit - 9 : digit;
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (10 - (sum % 10)) % 10;
    }

    private String getLastFourDigits(String cardNumber) {
        return cardNumber.substring(cardNumber.length() - 4);
    }
}
