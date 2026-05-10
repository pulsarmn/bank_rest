package org.pulsar.bank.service.crypto;

import java.util.Objects;


public class SimpleKeyMetadata implements KeyMetadata {

    private final String keyId;

    public SimpleKeyMetadata(String keyId) {
        this.keyId = Objects.requireNonNull(keyId);
    }

    @Override
    public String getKeyId() {
        return keyId;
    }
}
