package com.dyns.persevero.utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class Validate {

    public final static String EMAIL_REGEX = "^(?!\\.)([a-zA-Z0-9+_-](?:[a-zA-Z0-9+._-]{0,62}[a-zA-Z0-9+])?)@" +
            "([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";

    private static final String NOT_BLANK_MESSAGE = "Provided value cannot be null or empty.";
    private static final String NOT_NULL_MESSAGE = "Provided object cannot be null.";

    private Validate() {
    }

    public static String notBlank(final String value) {
        return notBlank(value, NOT_BLANK_MESSAGE);
    }

    public static String notBlank(final String value, final String message) {
        if (Condition.isBlank(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <T> T notNull(final T obj) {
        return notNull(obj, NOT_NULL_MESSAGE);
    }

    public static <T> T notNull(final T obj, final String message) {
        return Objects.requireNonNull(obj, message);
    }

    public static boolean isEmailValid(String value) {
        return isEmailValid(value, EMAIL_REGEX);
    }

    public static boolean isEmailValid(String value, String regex) {
        if (Condition.isLengthGreaterThan(value, 254)) return false;

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(value).matches();
    }

}
