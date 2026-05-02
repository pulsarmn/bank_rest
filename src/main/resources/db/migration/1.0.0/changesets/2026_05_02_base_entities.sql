--liquibase formatter sql

--changeset pulsar:1
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    login VARCHAR(64) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(16) NOT NULL CHECK (role IN ('USER', 'ADMIN')) DEFAULT 'USER',
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
--rollback DROP TABLE IF EXISTS users;

--changeset pulsar:2
CREATE TABLE IF NOT EXISTS cards (
    id UUID PRIMARY KEY,
    encrypted_number VARCHAR(128) NOT NULL,
    last_four_digits CHAR(4) NOT NULL,
    owner_id UUID REFERENCES users (id) ON DELETE RESTRICT NOT NULL,
    status VARCHAR(16) NOT NULL CHECK (status IN ('ACTIVE', 'BLOCKED', 'EXPIRED')) DEFAULT 'ACTIVE',
    balance DECIMAL(15, 2) NOT NULL CHECK (balance >= 0) DEFAULT 0.00,
    expires_at DATE NOT NULL
);

CREATE INDEX idx_cards_owner_id ON cards (owner_id);
--rollback DROP TABLE IF EXISTS cards;

--changeset pulsar:3
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    token_hash VARCHAR(128) NOT NULL,
    issued_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX idx_refresh_tokens_token_hash ON refresh_tokens (token_hash);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens (expires_at);
--rollback DROP TABLE IF EXISTS refresh_tokens;
