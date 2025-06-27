package com.poneres.portal.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@Profile({"local", "local-no-auth"})
public class LocalEmailService extends AbstractEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String template) {
        String tokenizedTemplateHtml = replaceTokens(template);
        printIt(getPoneresNoReply(), to, null, subject, tokenizedTemplateHtml, null);
    }

    @Override
    public void send(String to, String cc, String subject, String template, String attachmentName, byte[] attachment, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        printIt(getPoneresNoReply(), to, cc, subject, tokenizedTemplateHtml, attachmentName);
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        printIt(getPoneresNoReply(), to, null, subject, tokenizedTemplateHtml, null);
    }

    @Override
    public void send(String from, String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        printIt(from, to, null, subject, tokenizedTemplateHtml, null);
    }

    private void printIt(String from, String to, String cc, String subject, String tokenizedTemplateHtml, String attachmentName) {
        log.info("--------------------------------------------------------");
        log.info("Email sent!");

        String[] toEmails = to.split(",");
        for (String toEmail : toEmails) {
            log.info("To: {}", toEmail);
        }

        if (cc != null) {
            log.info("CC: {}", cc);
        }
        log.info("From: {}", from);
        log.info("Subject: {}", subject);
        log.info("Content: {}", tokenizedTemplateHtml);

        if (attachmentName != null) {
            log.info("Attachment Name: {}", attachmentName);
        }

        log.info("--------------------------------------------------------");
    }

}
