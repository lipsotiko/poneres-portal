package io.meraklis.icare.email;

import java.util.Map;

public interface EmailService {
    void send(String to, String subject, String template);

    void send(String to, String subject, String template, Map<String, String> additionalTokens);
}
