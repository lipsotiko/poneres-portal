package io.meraklis.icare.documents;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageResponse {
    private String fileName;
    private byte[] content;
}
