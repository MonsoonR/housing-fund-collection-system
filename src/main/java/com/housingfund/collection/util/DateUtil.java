package com.housingfund.collection.util;

import java.time.LocalDate;

public final class DateUtil {

    private DateUtil() {
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate defaultLastPayDate() {
        return LocalDate.of(1899, 12, 1);
    }
}
