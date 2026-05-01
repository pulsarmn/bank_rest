package org.pulsar.bank.entity;


import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private UUID id;
    private User user;
    private String tokenHash;
    private Instant issuedAt;
    private Instant expiresAt;
    private Instant revokedAt;
}
