package com.dyns.persevero.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class EmailValidator {

    public final static String EMAIL_REGEX = "^(?!\\.)([a-zA-Z0-9+_-](?:[a-zA-Z0-9+._-]{0,62}[a-zA-Z0-9+])?)@" +
            "([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";

    private EmailValidator() {
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.length() > 254) return false;

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

}
