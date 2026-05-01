package org.pulsar.bank.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Card {

    private UUID id;
    private String number;
    private User owner;
    private Status status;
    private BigDecimal balance;

    public enum Status {
        ACTIVE,
        BLOCKED,
        EXPIRED
    }
}
