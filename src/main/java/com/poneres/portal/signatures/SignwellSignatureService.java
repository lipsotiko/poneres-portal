package com.poneres.portal.signatures;

import com.poneres.portal.agreements.SignatureRecipient;
import com.poneres.portal.helpers.RestApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class SignwellSignatureService implements SignatureService {

    @Autowired
    private RestApiService restApiService;

    @Value("${signwell.api-key}")
    private String apiKey;

    @Value("${signwell.api-url}")
    private String apiUrl;

    @Override
    public String create(String name, Boolean draft, Boolean withSignaturePage, String base64File, List<SignatureRecipient> recipients) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("test_mode", true);
        body.put("draft", draft);
        body.put("with_signature_page", withSignaturePage);

        body.put("recipients", IntStream.range(0, recipients.size())
                .mapToObj(i -> new HashMap<>() {{
                    put("id", i);
                    put("email", recipients.get(i).getEmail());
                    put("name", recipients.get(i).getName());
                }}).toList());

        List<Object> files = new ArrayList<>();
        files.add(new HashMap<>() {{
            put("name", name);
            put("file_base64", base64File);
        }});
        body.put("files", files);

        Map<String, Object> response = restApiService.post(apiUrl + "documents", headers, body, HashMap.class);
        return (String) response.get("id");
    }

    @Override
    public void delete(String ssdId) {
        if (ssdId == null) {
            return;
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);

        Map<String, Object> document = restApiService.get(apiUrl + "documents/" + ssdId, headers, HashMap.class);
        if ((Boolean) document.get("test_mode")) {
            restApiService.delete(apiUrl + "documents/" + ssdId, headers);
            return;
        }

        if (document.get("status").equals("Completed")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Complete documents may not be deleted in production.");
        }

        restApiService.delete(apiUrl + "documents/" + ssdId, headers);
    }

    @Override
    public String status(String ssdId) {
        if (ssdId == null) {
            return null;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);

        try {
            Map<String, Object> response = restApiService.get(apiUrl + "documents/" + ssdId, headers, HashMap.class);
            return (String) response.get("status");
        } catch (Exception ex) {
            // Do nothing;
        }

        return "None";
    }

    @Override
    public String fileUrl(String ssdId) {
        if (ssdId == null) {
            return null;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);

        String uri = apiUrl + "documents/" + ssdId + "/completed_pdf?url_only=true";
        Map<String, Object> response = restApiService.get(uri, headers, HashMap.class);
        return (String) response.get("file_url");
    }

    @Override
    public void sendReminder(String ssdId) {
        if (ssdId == null) {
            return;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);

        restApiService.post(apiUrl + "documents/" + ssdId + "/remind", headers, Collections.emptyMap());
    }

}
