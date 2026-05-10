package org.pulsar.bank.service.crypto;


import org.pulsar.bank.exception.CryptoException;
import javax.crypto.SecretKey;


public interface CryptoService {

    byte[] encrypt(byte[] plainData) throws CryptoException;

    byte[] encrypt(byte[] plainData, SecretKey secretKey) throws CryptoException;

    byte[] decrypt(byte[] encodedData) throws CryptoException;

    byte[] decrypt(byte[] encodedData, SecretKey secretKey) throws CryptoException;
}
