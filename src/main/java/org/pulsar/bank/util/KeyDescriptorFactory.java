package org.pulsar.bank.util;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.service.crypto.KeyDescriptor;
import org.pulsar.bank.service.crypto.KeyMetadata;
import org.pulsar.bank.service.crypto.SimpleKeyDescriptor;
import org.pulsar.bank.service.crypto.SimpleKeyMetadata;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class KeyDescriptorFactory {

    private final KeyIdGenerator keyIdGenerator;

    public KeyDescriptor create(SecretKey secretKey) {
        Objects.requireNonNull(secretKey);
        KeyMetadata keyMetadata = createKeyMetadata();
        return new SimpleKeyDescriptor(secretKey, keyMetadata);
    }

    private KeyMetadata createKeyMetadata() {
        String keyId = keyIdGenerator.generate();
        return new SimpleKeyMetadata(keyId);
    }
}
