package org.pulsar.bank.util;


import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class CardNumberGenerator {

    private static final Random RANDOM = new Random();
    private static final int NUMBER_OF_DIGITS = 10;
    private static final String MIR_PREFIX = "2200";
    private static final int TOTAL_DIGITS_IN_CARD_NUMBER = 16;

    public String generate() {
        int digitsNeeded = TOTAL_DIGITS_IN_CARD_NUMBER - MIR_PREFIX.length() - 1; // minus last digit
        String numberWithoutCheck = MIR_PREFIX + Stream.generate(() -> RANDOM.nextInt(NUMBER_OF_DIGITS))
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
}
