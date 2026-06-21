package com.hotel.server.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
imjavax.crypto.SecretKey;
imjavax.crypto.spec.SecretKeySpec;
imjava.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final String ENCRYPTION_KEY = "hotel_secret_key_2026"; // 16 chars = 128-bit

    /**
     * Encrypt string using AES
     */
    public static String encrypt(String data) {
        try {
            SecretKey key = getKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            System.err.println("Error encrypting data: " + e.getMessage());
            return data; // Return original if encryption fails
        }
    }

    /**
     * Decrypt string using AES
     */
    public static String decrypt(String encryptedData) {
        try {
            SecretKey key = getKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            System.err.println("Error decrypting data: " + e.getMessage());
            return encryptedData; // Return original if decryption fails
        }
    }

    /**
     * Generate or retrieve encryption key
     */
    private static SecretKey getKey() {
        // Use fixed key for simplicity (in production, use KeyStore)
        byte[] decodedKey = Base64.getDecoder().decode(
            "qpwn6X8zL2mK9vC7B3gH8kJ4nM5pL9yQ="
        );
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    /**
     * Generate base64 encoded key (run once to get key)
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128);
            SecretKey key = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            System.err.println("Error generating key: " + e.getMessage());
            return null;
        }
    }
}
