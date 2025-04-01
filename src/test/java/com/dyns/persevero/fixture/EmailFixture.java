package com.dyns.persevero.fixture;


import java.util.ArrayList;
import java.util.List;

public class EmailFixture {

    public List<String> getValidEmails() {
        List<String> validEmails = new ArrayList<>();

        validEmails.add("username@domain.com");
        validEmails.add("username@domain.co.in");
        validEmails.add("user.name@domain.io");
        validEmails.add("user-name@domain.fr");
        validEmails.add("user_name@domain.en");

        return validEmails;
    }

    public List<String> getInvalidEmails() {
        List<String> invalidEmails = new ArrayList<>();

        invalidEmails.add("usernamedomain.com");
        invalidEmails.add("username@domaincom");
        invalidEmails.add("user.name@");
        invalidEmails.add("@domain.fr");
        invalidEmails.add("user name@domain.com");
        invalidEmails.add("a_very_very_very_very_very_long_username_that_is_unreasonably_long@domain.com");
        invalidEmails.add("user@@domain.com");
        invalidEmails.add(".user@domain.com");
        invalidEmails.add("user!$%^&*()@domain.com");
        invalidEmails.add("user@domain.!$%^&*");

        return invalidEmails;
    }
}
