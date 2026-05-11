package org.pulsar.bank.crypto;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Component
@Profile("dev")
public class StaticKeyProvider implements KeyProvider {

    private final SecretKey secretKey;
    private final KeyDescriptorFactory keyDescriptorFactory;

    private static final String DEFAULT_ALGORITHM = "AES";

    public StaticKeyProvider(@Value("${app.encryption.key}") String key, KeyDescriptorFactory keyDescriptorFactory) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.secretKey = new SecretKeySpec(decodedKey, DEFAULT_ALGORITHM);
        this.keyDescriptorFactory = keyDescriptorFactory;
    }

    @Override
    public KeyDescriptor getKeyDescriptor() {
        return keyDescriptorFactory.create(secretKey);
    }
}
