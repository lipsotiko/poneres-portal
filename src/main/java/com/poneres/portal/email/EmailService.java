package com.poneres.portal.email;

import java.util.Map;

public interface EmailService {
    void send(String to, String subject, String template);

    void send(String to, String subject, String template, String attachmentName, byte[] attachment);

    void send(String to, String subject, String template, Map<String, String> additionalTokens);

    void send(String from, String to, String subject, String template, Map<String, String> additionalTokens);
}
