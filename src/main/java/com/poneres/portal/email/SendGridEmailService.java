package com.poneres.portal.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
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
    public void send(String to, String subject, String template, String attachmentName, byte[] attachment) {
        String tokenizedTemplateHtml = replaceTokens(template);
        fullSend(getPoneresNoReply(), to, subject, tokenizedTemplateHtml, attachmentName, attachment);
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSend(to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String from, String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSend(from, to, subject, tokenizedTemplateHtml, null, null);
    }

    private void fullSend(String from, String to, String subject, String tokenizedTemplateHtml, String attachmentName, byte[] attachment) {
        String[] toEmails = to.split(",");
        for (String toEmail : toEmails) {
            try {
                Content content = new Content("text/html", tokenizedTemplateHtml);

                Mail mail = new Mail(new Email(from), subject, new Email(toEmail), content);

                if (attachment != null && attachmentName != null) {
                    String attachmentContent = Base64.getEncoder().encodeToString(attachment);
                    addAttachment(mail, attachmentName, attachmentContent);
                }

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

    void addAttachment(Mail mail, String attachmentName, String attachmentContent) {
        final Attachments attachments = new Attachments
                .Builder(attachmentName, attachmentContent)
                .withType("application/pdf")
                .build();
        mail.addAttachments(attachments);
    }

    private void fullSend(String to, String subject, String tokenizedTemplateHtml) {
        fullSend(getPoneresNoReply(), to, subject, tokenizedTemplateHtml, null, null);
    }
}
