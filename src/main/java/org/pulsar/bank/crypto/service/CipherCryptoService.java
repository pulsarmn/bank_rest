package org.pulsar.bank.crypto.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.crypto.KeyDescriptor;
import org.pulsar.bank.crypto.KeyProvider;
import org.pulsar.bank.crypto.CryptoException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;


@Service
@RequiredArgsConstructor
public class CipherCryptoService implements CryptoService {

    private final KeyProvider keyProvider;

    private static final String DEFAULT_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_SIZE_BYTES = 12;
    private static final int TAG_SIZE_BITS = 128;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public byte[] encrypt(byte[] plainData) throws CryptoException {
        KeyDescriptor keyDescriptor = keyProvider.getKeyDescriptor();
        SecretKey secretKey = keyDescriptor.getKey();
        return encrypt(plainData, secretKey);
    }

    @Override
    public byte[] encrypt(byte[] plainData, SecretKey secretKey) throws CryptoException {
        try {
            byte[] iv = new byte[IV_SIZE_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = getEncryptCipher(secretKey, iv);
            byte[] encodedData = cipher.doFinal(plainData);

            return combine(iv, encodedData);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    private byte[] combine(byte[] iv, byte[] encodedData) {
        byte[] result = new byte[iv.length + encodedData.length];
        System.arraycopy(iv, 0, result, 0, IV_SIZE_BYTES);
        System.arraycopy(encodedData, 0, result, IV_SIZE_BYTES, encodedData.length);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] encodedData) throws CryptoException {
        KeyDescriptor keyDescriptor = keyProvider.getKeyDescriptor();
        SecretKey secretKey = keyDescriptor.getKey();
        return decrypt(encodedData, secretKey);
    }

    @Override
    public byte[] decrypt(byte[] encodedData, SecretKey secretKey) throws CryptoException {
        if (encodedData == null || encodedData.length < IV_SIZE_BYTES) {
            throw new IllegalArgumentException();
        }

        try {
            byte[] iv = new byte[IV_SIZE_BYTES];
            System.arraycopy(encodedData, 0, iv, 0, IV_SIZE_BYTES);

            byte[] data = new byte[encodedData.length - IV_SIZE_BYTES];
            System.arraycopy(encodedData, IV_SIZE_BYTES, data, 0, encodedData.length - IV_SIZE_BYTES);
            Cipher cipher = getDecryptCipher(secretKey, iv);

            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    private Cipher getEncryptCipher(SecretKey secretKey, byte[] iv) throws Exception {
        return getCipher(secretKey, iv, Cipher.ENCRYPT_MODE);
    }

    private Cipher getDecryptCipher(SecretKey secretKey, byte[] iv) throws Exception {
        return getCipher(secretKey, iv, Cipher.DECRYPT_MODE);
    }

    private Cipher getCipher(SecretKey secretKey, byte[] iv, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_SIZE_BITS, iv);
        cipher.init(mode, secretKey, gcmParameterSpec);

        return cipher;
    }
}
