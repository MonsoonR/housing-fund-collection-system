package com.housingfund.collection.util;

public final class AccountNumberUtil {

    public static final int ACCOUNT_NUMBER_LENGTH = 12;

    private AccountNumberUtil() {
    }

    public static String formatAccountNumber(Long sequenceValue) {
        if (sequenceValue == null) {
            throw new IllegalArgumentException("账号序号不能为空");
        }
        if (sequenceValue < 0) {
            throw new IllegalArgumentException("账号序号不能为负数");
        }

        String rawValue = String.valueOf(sequenceValue);
        if (rawValue.length() > ACCOUNT_NUMBER_LENGTH) {
            throw new IllegalArgumentException("账号序号超过12位");
        }
        return String.format("%0" + ACCOUNT_NUMBER_LENGTH + "d", sequenceValue);
    }

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{12}");
    }
}
