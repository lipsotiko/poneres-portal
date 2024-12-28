package com.poneres.portal.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@Profile("development")
public class SendGridEmailService extends AbstractEmailService implements EmailService {

    @Autowired
    private SendGrid sendGrid;

    public void send(String to, String subject, String template) {
        String tokenizedTemplateHtml = replaceTokens(template);
        fullSend(to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSend(to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String from, String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSend(from, to, subject, tokenizedTemplateHtml);
    }

    private void fullSend(String from, String to, String subject, String tokenizedTemplateHtml) {
        String[] toEmails = to.split(",");
        for (String toEmail : toEmails) {
            try {
                Content content = new Content("text/html", tokenizedTemplateHtml);

                Mail mail = new Mail(new Email(from), subject, new Email(toEmail), content);

                Request request = new Request();

                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                sendGrid.api(request);
            } catch (IOException ex) {
                log.error("Add error occurred when attempting to send an email to: {}", toEmail, ex);
            }
        }
    }

    private void fullSend(String to, String subject, String tokenizedTemplateHtml) {
        fullSend(getPoneresNoReply(), to, subject, tokenizedTemplateHtml);
    }
}
