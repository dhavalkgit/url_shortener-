package com.example.service.urlservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;

@Slf4j
public class HashGenerator {
    private static final String BASE62_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = BASE62_CHARSET.length();

    public static String generateHash(@NonNull String longURL) {
        String newLongURL = longURL + String.valueOf(Instant.now().toEpochMilli());
        MessageDigest messageDigest = null;
        try {
             messageDigest = MessageDigest.getInstance("MD5");
        }
        catch (Exception e){
            log.info(e.getMessage());
        }
        byte[] digest = messageDigest.digest(newLongURL.getBytes(StandardCharsets.UTF_8));
        return encode(digest);
    }
    private static String encode(byte[] input) {
        BigInteger number = new BigInteger(1, input);  // Convert bytes to a positive BigInteger
        StringBuilder encoded = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            int remainder = number.mod(BigInteger.valueOf(BASE)).intValue();
            encoded.append(BASE62_CHARSET.charAt(remainder));
            number = number.divide(BigInteger.valueOf(BASE));
        }

        return encoded.reverse().substring(0, 7);  // Reverse the result to get correct encoding
    }
}
