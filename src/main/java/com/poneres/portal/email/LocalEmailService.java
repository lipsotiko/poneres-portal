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
        printIt(getPoneresNoReply(), to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String to, String subject, String template, String attachmentName, byte[] attachment) {
        String tokenizedTemplateHtml = replaceTokens(template);
        printIt(getPoneresNoReply(), to, subject, tokenizedTemplateHtml);
        log.info("Attachment Name: {}", attachmentName);
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        printIt(getPoneresNoReply(), to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String from, String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        printIt(from, to, subject, tokenizedTemplateHtml);
    }

    private void printIt(String from, String to, String subject, String tokenizedTemplateHtml) {
        log.info("--------------------------------------------------------");
        log.info("Email sent!");

        String[] toEmails = to.split(",");
        for (String toEmail : toEmails) {
            log.info("To: {}", toEmail);
        }

        log.info("From: {}", from);
        log.info("Subject: {}", subject);
        log.info("Content: {}", tokenizedTemplateHtml);
        log.info("--------------------------------------------------------");
    }

}
