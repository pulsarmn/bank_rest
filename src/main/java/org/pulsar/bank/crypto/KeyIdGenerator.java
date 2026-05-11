package org.pulsar.bank.crypto;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KeyIdGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
