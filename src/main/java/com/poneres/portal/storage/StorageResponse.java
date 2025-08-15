package com.poneres.portal.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
public class StorageResponse {
    private String fileName;
    private byte[] content;

    public MediaType getMediaType() {
        if (fileName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        }

        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }

        if (fileName.endsWith(".jpg")) {
            return MediaType.IMAGE_JPEG;
        }

        return null;
    }
}
