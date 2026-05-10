package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.exception.CryptoException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;


@Service
@RequiredArgsConstructor
public class HmacService {

    private static final String ALGORITHM = "HmacSHA256";
    private static final SecretKey secretKey = new SecretKeySpec("I8KOq6SJY+FkPLna5goGPA22IN+T1fYFahxeTkAOoXs=".getBytes(), ALGORITHM);

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
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);
            byte[] hashBytes = mac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
            String hash = HexFormat.of().formatHex(hashBytes);
            return hash.equals(hashData);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }
}
