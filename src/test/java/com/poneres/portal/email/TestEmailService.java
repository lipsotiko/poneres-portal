package com.poneres.portal.email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@Data
@ActiveProfiles("local")
@EqualsAndHashCode(callSuper = true)
public class TestEmailService extends AbstractEmailService implements EmailService {

    private String sentTemplate;
    private String sentTo;
    private String sentSubject;

    private Map<String, String> defaultTokens = new HashMap<>() {
        {
            put("LOGIN_URL", "https://hello.com/login");
            put("ANOTHER_TOKEN", "Hello World");
        }
    };

    @Override
    public void send(String to, String subject, String template) {
        sentTemplate = replaceTokens(template);
        sentTo = to;
        sentSubject = subject;
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        sentTemplate = replaceTokens(template, additionalTokens);
        sentTo = to;
        sentSubject = subject;
    }
}
