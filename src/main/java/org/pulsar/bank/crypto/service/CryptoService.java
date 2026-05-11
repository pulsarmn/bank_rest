package org.pulsar.bank.crypto.service;


import org.pulsar.bank.crypto.CryptoException;
import javax.crypto.SecretKey;


public interface CryptoService {

    byte[] encrypt(byte[] plainData) throws CryptoException;

    byte[] encrypt(byte[] plainData, SecretKey secretKey) throws CryptoException;

    byte[] decrypt(byte[] encodedData) throws CryptoException;

    byte[] decrypt(byte[] encodedData, SecretKey secretKey) throws CryptoException;
}
