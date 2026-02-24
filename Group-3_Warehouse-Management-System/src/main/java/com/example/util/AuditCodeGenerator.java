package com.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuditCodeGenerator {

    public static String generateDailyCode() {
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datePart = today.format(formatter);

        return "AUD-" + datePart;
    }
}
