package com.poneres.portal.signatures;

import com.poneres.portal.agreements.SignatureRecipient;

import java.util.List;
import java.util.Map;

public interface SignatureService {

    String create(String name, Boolean isDraft, Boolean withSignaturePage, String base64File, List<SignatureRecipient> recipients, List<Map<String, Object>> signatureFields);

    void delete(String ssdId);

    String status(String ssdId);

    String fileUrl(String ssdId);

    void sendReminder(String ssdId);
}
