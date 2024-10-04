package io.meraklis.icare.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Profile("local")
public class LocalEmailService extends AbstractEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String template) {
        String tokenizedTemplateHtml = replaceTokens(template);
        printIt(to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        printIt(to, subject, tokenizedTemplateHtml);
    }

    private void printIt(String to, String subject, String tokenizedTemplateHtml) {
        log.info("--------------------------------------------------------");
        log.info("Email sent!");
        log.info("To: {}", to);
        log.info("From: {}", getNoReplyEmail());
        log.info("Subject: {}", subject);
        log.info("Content: {}", tokenizedTemplateHtml);
        log.info("--------------------------------------------------------");
    }

    @Override
    public Map<String, String> getDefaultTokens() {
        return new HashMap<>() {
            {
                put("LOGIN_URL", getWebappUrl() + "/oauth2/authorization/okta");
            }
        };
    }
}
