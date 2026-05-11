package org.pulsar.bank.card.domain;

import jakarta.persistence.*;
import lombok.*;
import org.pulsar.bank.auth.domain.User;

import java.math.BigDecimal;
import java.time.YearMonth;
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

    @Column(name = "encrypted_pan")
    private String encryptedPan;

    @Column(name = "pan_hash")
    private String panHash;

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Builder.Default
    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "expires_at")
    private YearMonth expiresAt;

    public enum Status {
        ACTIVE,
        BLOCKED,
        EXPIRED
    }
}
