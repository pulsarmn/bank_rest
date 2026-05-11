--liquibase formatted sql

--changeset pulsar:1
ALTER TABLE cards DROP COLUMN last_four_digits;
