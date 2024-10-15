package com.poneres.portal.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.*;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RestApiService {

    @Autowired
    private ObjectMapper jackson;

    public <T> T post(String uri, Map<String, String> headers, Object payload, Class<T> responseType) {
        try {
            CloseableHttpResponse response = post2(uri, headers, payload);
            handleSuccess(response, uri);
            String responseEntity = EntityUtils.toString(response.getEntity());
            return jackson.readValue(responseEntity, responseType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CloseableHttpResponse post2(String uri, Map<String, String> headers, Object payload) {
        CloseableHttpResponse response = request(new HttpPost(uri), headers, payload);
        handleSuccess(response, uri);
        return response;
    }

    public void patch(String uri, String token, Object payload) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        CloseableHttpResponse response = request(new HttpPatch(uri), headers, payload);
        handleSuccess(response, uri);
    }

    private CloseableHttpResponse request(HttpEntityEnclosingRequestBase httpRequest, Map<String, String> headers, Object payload) {
        if (headers != null) {
            headers.forEach(httpRequest::setHeader);
        }

        try {
            StringEntity params = new StringEntity(jackson.writeValueAsString(payload));
            httpRequest.setEntity(params);
            httpRequest.addHeader("content-type", "application/json");

            CloseableHttpClient client = HttpClients.createDefault();
            return client.execute(httpRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T post(String uri, Object request, Class<T> responseType) {
        return post(uri, null, request, responseType);
    }

    public void post(String uri, String token, Object payload) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        post2(uri, headers, payload);
    }

    public <T> T get(String uri, Map<String, String> headers, Class<T> responseType) {
        try {
            final HttpGet httpRequest = new HttpGet(uri);
            if (headers != null) {
                headers.forEach(httpRequest::setHeader);
            }
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpRequest);
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
            throw new RuntimeException("API Request failed.");
        }
    }

    public static String urlEncode(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    public void delete(String uri, Map<String, String> headers) {
        HttpDelete httpRequest = new HttpDelete(uri);

        if (headers != null) {
            headers.forEach(httpRequest::setHeader);
        }

        try {
            CloseableHttpResponse response = HttpClients.createDefault().execute(httpRequest);
            handleSuccess(response, uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
