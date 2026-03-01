package com.example.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for formatting currency in Vietnamese format
 * Example: 10000000 becomes "10.000.000"
 */
public class CurrencyFormatter {

    private static final DecimalFormatSymbols VIETNAMESE_SYMBOLS = new DecimalFormatSymbols(new Locale("vi", "VN"));
    private static final DecimalFormat VIETNAMESE_FORMAT;

    static {
        // Vietnamese format: period for thousands separator, comma for decimal
        VIETNAMESE_SYMBOLS.setGroupingSeparator('.');
        VIETNAMESE_SYMBOLS.setDecimalSeparator(',');
        
        VIETNAMESE_FORMAT = new DecimalFormat("#,##0.##", VIETNAMESE_SYMBOLS);
        VIETNAMESE_FORMAT.setGroupingUsed(true);
        VIETNAMESE_FORMAT.setMaximumFractionDigits(2);
        VIETNAMESE_FORMAT.setMinimumFractionDigits(0);
    }

    /**
     * Format a value in Vietnamese currency format
     * @param value the number to format
     * @return formatted string (e.g., "10.000.000" for 10000000)
     */
    public static String formatCurrency(Number value) {
        if (value == null) {
            return "0";
        }
        
        try {
            return VIETNAMESE_FORMAT.format(value);
        } catch (Exception e) {
            return value.toString();
        }
    }

    /**
     * Format a BigDecimal in Vietnamese currency format
     */
    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        return formatCurrency((Number) value);
    }

    /**
     * Format a Double in Vietnamese currency format
     */
    public static String formatCurrency(Double value) {
        if (value == null) {
            return "0";
        }
        return formatCurrency((Number) value);
    }

    /**
     * Format a Long in Vietnamese currency format
     */
    public static String formatCurrency(Long value) {
        if (value == null) {
            return "0";
        }
        return formatCurrency((Number) value);
    }

    /**
     * Format a Integer in Vietnamese currency format
     */
    public static String formatCurrency(Integer value) {
        if (value == null) {
            return "0";
        }
        return formatCurrency((Number) value);
    }
}
