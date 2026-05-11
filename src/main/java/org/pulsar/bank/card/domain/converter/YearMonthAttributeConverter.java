package org.pulsar.bank.card.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(YearMonth yearMonth) {
        return yearMonth != null ? yearMonth.atEndOfMonth() : null;
    }

    @Override
    public YearMonth convertToEntityAttribute(LocalDate date) {
        return date != null ? YearMonth.from(date) : null;
    }
}
