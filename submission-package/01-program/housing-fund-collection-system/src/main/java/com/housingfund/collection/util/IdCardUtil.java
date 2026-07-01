package com.housingfund.collection.util;

import java.time.DateTimeException;
import java.time.LocalDate;

public final class IdCardUtil {

    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    private IdCardUtil() {
    }

    public static boolean isValid(String idCard) {
        if (idCard == null || !idCard.matches("\\d{17}[0-9Xx]")) {
            return false;
        }
        if (!isValidBirthDate(idCard.substring(6, 14))) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < WEIGHTS.length; i++) {
            sum += Character.digit(idCard.charAt(i), 10) * WEIGHTS[i];
        }
        return CHECK_CODES[sum % 11] == Character.toUpperCase(idCard.charAt(17));
    }

    private static boolean isValidBirthDate(String value) {
        try {
            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(4, 6));
            int day = Integer.parseInt(value.substring(6, 8));
            LocalDate birthDate = LocalDate.of(year, month, day);
            return !birthDate.isAfter(LocalDate.now());
        } catch (DateTimeException | NumberFormatException ex) {
            return false;
        }
    }
}
