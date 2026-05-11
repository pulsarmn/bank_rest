package org.pulsar.bank.crypto;

import javax.crypto.SecretKey;
import java.util.Objects;


public class SimpleKeyDescriptor implements KeyDescriptor {

    private final SecretKey secretKey;
    private final KeyMetadata keyMetadata;

    public SimpleKeyDescriptor(SecretKey secretKey, KeyMetadata keyMetadata) {
        this.secretKey = Objects.requireNonNull(secretKey);
        this.keyMetadata = Objects.requireNonNull(keyMetadata);
    }

    @Override
    public SecretKey getKey() {
        return secretKey;
    }

    @Override
    public KeyMetadata getKeyMetadata() {
        return keyMetadata;
    }
}
