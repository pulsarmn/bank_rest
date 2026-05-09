package org.pulsar.bank.service.crypto;


public interface KeyProvider {

    KeyDescriptor getKeyDescriptor();
}
