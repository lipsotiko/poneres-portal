package com.poneres.portal.signatures;

public interface SignatureService {

    String create(String name, Boolean isDraft, Boolean withSignaturePage, String base64File);

    void delete(String ssdId);

    String status(String ssdId);

    String fileUrl(String ssdId);
}
