--liquibase formatted sql

--changeset pulsar:1
ALTER TABLE cards ADD COLUMN IF NOT EXISTS pan_hash VARCHAR(255) UNIQUE;

--changeset pulsar:2
ALTER TABLE cards RENAME COLUMN encrypted_number TO encrypted_pan;
