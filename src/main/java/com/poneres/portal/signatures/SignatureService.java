package com.poneres.portal.signatures;

import com.poneres.portal.agreements.SignatureRecipient;

import java.util.List;

public interface SignatureService {

    String create(String name, Boolean isDraft, Boolean withSignaturePage, String base64File, List<SignatureRecipient> recipients);

    void delete(String ssdId);

    String status(String ssdId);

    String fileUrl(String ssdId);

    void sendReminder(String ssdId);
}
