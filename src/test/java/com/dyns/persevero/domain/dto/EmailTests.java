package com.dyns.persevero.domain.dto;

import com.dyns.persevero.fixtures.impl.EmailFixture;
import com.dyns.persevero.utils.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Testing email validity")
public class EmailTests {

    private final EmailFixture fixture = new EmailFixture();
    private final List<String> validEmails = fixture.getValidEmails();
    private final List<String> invalidEmails = fixture.getInvalidEmails();

    @Test
    @DisplayName("Should assert that any emails are valid")
    public void shouldAssertThatAnyEmailsAreValid() {
        validEmails.forEach(
                email -> {
                    log.info(getInfo(email));
                    assertTrue(EmailValidator.isValidEmail(email));
                }
        );
    }

    @Test
    @DisplayName("Should assert that any emails are invalid")
    public void shouldAssertThatAnyEmailsAreInvalid() {
        invalidEmails.forEach(
                email -> {
                    log.info(getInfo(email));
                    assertFalse(EmailValidator.isValidEmail(email));
                }
        );
    }

    @Test
    @DisplayName("Should assert that first email is invalid and second is valid")
    public void shouldAssertThatGivenEmailIsInvalid() {
        log.info(getInfo(invalidEmails.getFirst()));
        log.info(getInfo(validEmails.getLast()));

        assertFalse(EmailValidator.isValidEmail(invalidEmails.getFirst()));
        assertTrue(EmailValidator.isValidEmail(validEmails.getLast()));
    }

    private String getInfo(String email) {
        return String.format("%s :  is valid ? %b",
                email,
                EmailValidator.isValidEmail(email)
        );
    }
}
