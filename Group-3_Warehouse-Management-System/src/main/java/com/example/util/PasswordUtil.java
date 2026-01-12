package com.example.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

public class PasswordUtil {
    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";

    private static final SecureRandom random = new SecureRandom();

    // Generate random string
    public static String generateRandomPassword(int passwordLength) {
        String combinedChars = ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL;
        // Xác định luôn capacity ban đầu cho StringBuilder = passwordLength
        StringBuilder sb = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(combinedChars.length());
            sb.append(combinedChars.charAt(index));
        }
        return sb.toString();
    }

    // Hash password with Bcrypt
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    // Check password for testing
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
