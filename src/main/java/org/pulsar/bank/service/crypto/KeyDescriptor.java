package org.pulsar.bank.service.crypto;

import javax.crypto.SecretKey;


public interface KeyDescriptor {

    SecretKey getKey();

    KeyMetadata getKeyMetadata();
}
