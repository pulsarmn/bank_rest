package org.pulsar.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@ToString(exclude = {"cards", "refreshTokens", "passwordHash"})
@EqualsAndHashCode(exclude = {"cards", "refreshTokens"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login")
    private String login;

    @Column(name = "password_hash")
    private String passwordHash;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @CreationTimestamp
    private Instant createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    private List<Card> cards = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens = new ArrayList<>();
}
