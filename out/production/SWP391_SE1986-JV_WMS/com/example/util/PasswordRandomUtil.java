package com.example.util;

import java.security.SecureRandom;

public class PasswordRandomUtil {
    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";

    private static final SecureRandom random = new SecureRandom();

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
}
