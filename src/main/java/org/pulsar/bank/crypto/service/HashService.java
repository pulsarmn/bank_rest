package org.pulsar.bank.crypto.service;


import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


@Service
public class HashService {

   private final SecureRandom secureRandom = new SecureRandom();

    private static final int DEFAULT_SALT_LENGTH = 16;
    private static final String DEFAULT_ALGORITHM = "SHA-512";
    private static final String SEPARATOR = "$";

    public String hash(String rawData) {
        if (rawData == null) {
            throw new IllegalArgumentException("Data is required");
        }

        byte[] saltBytes = generateSalt(DEFAULT_SALT_LENGTH);
        byte[] hashBytes = computeHash(rawData, saltBytes);

        String salt = Base64.getEncoder().encodeToString(saltBytes);
        String hash = Base64.getEncoder().encodeToString(hashBytes);

        return salt + SEPARATOR + hash;
    }

    private byte[] generateSalt(int length) {
        byte[] salt = new byte[DEFAULT_SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public boolean matches(String rawData, String hash) {
        if (rawData == null || hash == null) {
            return false;
        }

        String[] saltAndHash = hash.split("\\" + SEPARATOR);
        if (saltAndHash.length != 2) {
            return false;
        }

        byte[] saltBytes = Base64.getDecoder().decode(saltAndHash[0]);
        byte[] expectedHashBytes = Base64.getDecoder().decode(saltAndHash[1]);
        byte[] actualHashBytes = computeHash(rawData, saltBytes);

        return MessageDigest.isEqual(actualHashBytes, expectedHashBytes);
    }

    private byte[] computeHash(String rawData, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DEFAULT_ALGORITHM);

            messageDigest.update(salt);
            messageDigest.update(rawData.getBytes(StandardCharsets.UTF_8));

            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hashing algorithm is not available: " + DEFAULT_ALGORITHM);
        }
    }
}
