package org.pulsar.bank.crypto;

import javax.crypto.SecretKey;


public interface KeyDescriptor {

    SecretKey getKey();

    KeyMetadata getKeyMetadata();
}
