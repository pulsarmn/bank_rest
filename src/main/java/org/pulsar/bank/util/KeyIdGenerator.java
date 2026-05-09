package org.pulsar.bank.util;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KeyIdGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
