package org.pulsar.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.pulsar.bank.entity.converter.CardNumberConverter;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
@ToString(exclude = "owner")
@EqualsAndHashCode(exclude = "owner")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Convert(converter = CardNumberConverter.class)
    private CardNumber number;

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "balance")
    private BigDecimal balance;

    public enum Status {
        ACTIVE,
        BLOCKED,
        EXPIRED
    }
}
