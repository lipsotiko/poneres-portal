package io.meraklis.icare.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("develop")
public class DevelopStorageService implements StorageService {

    private final Map<String, StorageResponse> responseMap = new HashMap<>();

    public void save(String key, byte[] document, String fileName, String uploadedBy, String applicationId) {
        ByteArrayInputStream stream = new ByteArrayInputStream(document);
        responseMap.put(key, new StorageResponse(fileName, stream.readAllBytes()));
    }

    public StorageResponse get(String key) {
        return responseMap.get(key);
    }

    @Override
    public void delete(String key) {
        responseMap.remove(key);
    }
}
