package com.poneres.portal.storage;

public interface StorageService {
    void save(String key, byte[] document, String fileName, String uploadedBy);

    StorageResponse get(String key);

    void delete(String key);
}
