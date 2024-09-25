package io.meraklis.icare.email;

import com.jayway.jsonpath.JsonPath;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private SendGrid sendGrid;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<Request> requestArgumentCaptor;

    @Test
    @SneakyThrows
    public void testTokenReplacement() {
        when(sendGrid.api(any())).thenReturn(new Response());
        emailService.setTokens(new HashMap<>() {
            {
                put("LOGIN_URL", "https://hello.com/login");
                put("ANOTHER_TOKEN", "Hello World");
            }
        });
        emailService.send("hello@world.io", "Hello", "email_templates/sample.html");
        verify(sendGrid).api(requestArgumentCaptor.capture());

        Request request = requestArgumentCaptor.getValue();
        String body = request.getBody();
        assertEquals("Hello", JsonPath.read(body, "$.subject"));
        assertEquals("hello@world.io", JsonPath.read(body, "$.personalizations[0].to[0].email"));
        assertEquals(
                "<a href=\"https://hello.com/login\">Log in</a>\n<h1>Hello World</h1>",
                JsonPath.read(body, "$.content[0].value"));
    }

}
