package com.dyns.persevero.utils;

public class Condition {

    private Condition() {
    }

    public static boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    public static boolean isLengthGreaterThan(final String value, int maxLength) {
        return !isBlank(value) && value.length() > maxLength;
    }
}
