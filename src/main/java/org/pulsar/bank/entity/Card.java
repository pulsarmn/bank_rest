package org.pulsar.bank.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "encrypted_number")
    private String encryptedNumber;

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
