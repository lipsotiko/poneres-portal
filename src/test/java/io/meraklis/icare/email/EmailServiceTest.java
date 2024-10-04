package io.meraklis.icare.email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailServiceTest {

    @Test
    public void testTokenReplacement() {
        TestEmailService emailService = new TestEmailService();

        emailService.send("hello@world.io", "Hello", "sample.html");

        assertEquals("Hello", emailService.getSentSubject());
        assertEquals("hello@world.io", emailService.getSentTo());
        assertEquals("<a href=\"https://hello.com/login\">Log in</a>\n<h1>Hello World</h1>", emailService.getSentTemplate());
    }

}
