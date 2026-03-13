/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.util;

/**
 *
 * @author PC
 */
public class ChipFormatter {

    public static String formatChipName(String input) {

        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String chip = input.trim().replaceAll("\\s+", " ");
        String lower = chip.toLowerCase();

        if (lower.matches("i[3579]\\s*-?\\s*\\d+[a-z]{0,3}")) {

            String cleaned = lower.replaceAll("\\s+", "")
                    .replace("-", "");

            String series = cleaned.substring(0, 2); // i3
            String model = cleaned.substring(2).toUpperCase(); // 1243F

            return series + "-" + model;
        }

        if (lower.startsWith("ryzen")) {

            String[] parts = chip.split(" ");

            if (parts.length >= 3) {
                return "Ryzen "
                        + parts[1] + " "
                        + parts[2].toUpperCase();
            }
        }

        return chip;
    }

}
