package io.meraklis.icare.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
@Service
public class EmailService {

    @Autowired
    private SendGrid sendGrid;

    @Value("${email.no-reply}")
    private String noReplyEmail;

    @Value("${webapp.url}")
    private String webappUrl;

    private Map<String, String> tokens;

    @PostConstruct
    public void populateDefaultTokens() {
        tokens = new HashMap<>() {
            {
                put("LOGIN_URL", getWebappUrl() + "/oauth2/authorization/okta");
            }
        };
    }

    public void send(String toEmail, String subject, String template) {
        try {
            String templateHtml = new String(new ClassPathResource(template).getInputStream().readAllBytes());

            String tokenizedTemplateHtml = replaceTokens(tokens, templateHtml);

            Content content = new Content("text/html", tokenizedTemplateHtml);

            Mail mail = new Mail(new Email(noReplyEmail), subject, new Email(toEmail), content);

            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sendGrid.api(request);
        } catch (IOException ex) {
            log.error("Add error occurred when attempting to send an email to: {}", toEmail, ex);
        }
    }

    private String replaceTokens(Map<String, String> tokens, String template) {
        String patternString = "%(" + StringUtils.join(tokens.keySet(), "|") + ")%";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(template);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
