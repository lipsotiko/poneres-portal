package io.meraklis.icare.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class RestApiService {

    @Autowired
    private ObjectMapper jackson;

    public <T> T post(String uri, String token, Object request, Class<T> responseType) {
        try {
            final HttpPost post = new HttpPost(uri);

            if (token != null) {
                post.setHeader("Authorization", token);
            }

            StringEntity params = new StringEntity(jackson.writeValueAsString(request));
            post.setEntity(params);
            post.addHeader("content-type", "application/json");

            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            handleSuccess(response, uri);

            String responseEntity = EntityUtils.toString(response.getEntity());

            if (responseType == null) {
                return null;
            } else {
                return jackson.readValue(responseEntity, responseType);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T post(String uri, Object request, Class<T> responseType) {
        return post(uri, null, request, responseType);
    }

    public void post(String uri, String token, Object request) {
        post(uri, token, request, null);
    }

    public <T> T get(String uri, String token, Class<T> responseType) {
        try {
            final HttpGet get = new HttpGet(uri);
            get.setHeader("Authorization", token);

            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(get);
            handleSuccess(response, uri);

            String responseEntity = EntityUtils.toString(response.getEntity());
            return jackson.readValue(responseEntity, responseType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSuccess(CloseableHttpResponse response, String uri) {
        int responseCode = response.getStatusLine().getStatusCode();
        if (!HttpStatus.valueOf(responseCode).is2xxSuccessful()) {
            log.error("API Request {} failed with status code {}", uri, response.getStatusLine().getStatusCode());
            throw new RuntimeException("API GET Request failed.");
        }
    }

    public static String urlEncode(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }
}
