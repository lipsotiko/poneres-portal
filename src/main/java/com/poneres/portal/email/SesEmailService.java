package com.poneres.portal.email;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@Profile("development")
public class SesEmailService extends AbstractEmailService implements EmailService {

    @Autowired
    private SesClient sesClient;

    @Override
    public void send(String to, String subject, String template) {
        String tokenizedTemplateHtml = replaceTokens(template);
        fullSend(getPoneresNoReply(), to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String to, String cc, String subject, String template, String attachmentName, byte[] attachment, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSendWithAttachment(getPoneresNoReply(), to, cc, subject, tokenizedTemplateHtml, attachmentName, attachment);
    }

    @Override
    public void send(String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSend(getPoneresNoReply(), to, subject, tokenizedTemplateHtml);
    }

    @Override
    public void send(String from, String to, String subject, String template, Map<String, String> additionalTokens) {
        String tokenizedTemplateHtml = replaceTokens(template, additionalTokens);
        fullSend(from, to, subject, tokenizedTemplateHtml);
    }

    private void fullSend(String from, String to, String subject, String tokenizedTemplateHtml) {
        Destination.Builder addressesBuilder = Destination.builder().toAddresses(to);

        Destination destination = addressesBuilder.build();

        Content content = Content.builder()
                .data(tokenizedTemplateHtml)
                .build();

        Content sub = Content.builder()
                .data(subject)
                .build();

        Body body = Body.builder()
                .html(content)
                .build();

        software.amazon.awssdk.services.ses.model.Message msg = software.amazon.awssdk.services.ses.model.Message.builder()
                .subject(sub)
                .body(body)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .message(msg)
                .source(from)
                .build();

        try {
            sesClient.sendEmail(emailRequest);
        } catch (SesException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }

    private void fullSendWithAttachment(String from, String to, String cc, String subject, String tokenizedTemplateHtml, String attachmentName, byte[] attachment) {
        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            if (cc != null) {
                message.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(tokenizedTemplateHtml, "text/html; charset=UTF-8");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName(attachmentName);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);

            RawMessage rawMessage = RawMessage.builder()
                    .data(SdkBytes.fromByteArray(outputStream.toByteArray()))
                    .build();

            SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder()
                    .rawMessage(rawMessage)
                    .build();

            sesClient.sendRawEmail(rawEmailRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
