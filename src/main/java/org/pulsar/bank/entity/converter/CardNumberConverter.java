package org.pulsar.bank.entity.converter;

import jakarta.persistence.AttributeConverter;
import org.pulsar.bank.entity.CardNumber;


public class CardNumberConverter implements AttributeConverter<CardNumber, String> {

    @Override
    public String convertToDatabaseColumn(CardNumber cardNumber) {
        return cardNumber.getValue();
    }

    @Override
    public CardNumber convertToEntityAttribute(String number) {
        return new CardNumber(number);
    }
}
