package org.pulsar.bank.crypto.service;


import org.pulsar.bank.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HexFormat;


@Service
public class HmacService {

    private final SecretKey secretKey;

    private static final String ALGORITHM = "HmacSHA256";

    public HmacService(@Value("${app.hmac.key}") String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String hash(String rawData) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);
            byte[] hash = mac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new CryptoException("Failed to generate PAN hash", e);
        }
    }

    public boolean matches(String rawData, String hashData) {
        try {
            String computedHash = hash(rawData);
            return MessageDigest.isEqual(
                    computedHash.getBytes(StandardCharsets.UTF_8),
                    hashData.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }
}
