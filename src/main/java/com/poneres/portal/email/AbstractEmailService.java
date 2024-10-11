package com.poneres.portal.email;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
abstract class AbstractEmailService {

    @Value("${email.no-reply}")
    private String noReplyEmail;

    @Value("${webapp.url}")
    private String webappUrl;

    public String replaceTokens(String template, Map<String, String> additionalTokens) {
        Map<String, String> tokens = new HashMap<>();
        tokens.putAll(getDefaultTokens());
        tokens.putAll(additionalTokens);
        return replacement(template, tokens);
    }

    public String replaceTokens(String template) {
        return replacement(template, getDefaultTokens());
    }

    private String replacement(String template, Map<String, String> tokens) {
        try {
            String templateHtml = new String(new ClassPathResource("email_templates/" + template).getInputStream().readAllBytes());


            String patternString = "%(" + StringUtils.join(tokens.keySet(), "|") + ")%";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(templateHtml);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
            }
            matcher.appendTail(sb);

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getDefaultTokens() {
        return new HashMap<>() {
            {
                put("WEBAPP_URL", getWebappUrl());
                put("LOGIN_URL", getWebappUrl() + "/oauth2/authorization/okta");
            }
        };
    }

}
