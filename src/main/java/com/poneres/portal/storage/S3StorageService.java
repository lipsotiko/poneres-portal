package com.poneres.portal.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3StorageService implements StorageService {

    @Value("${s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3 s3Client;

    public void save(String key, byte[] document, String fileName, String uploadedBy) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(document.length);

        if (fileName.endsWith(".pdf")) {
            meta.setContentType("application/pdf");
        }

        Map<String, String> userMeta = new HashMap<>();
        userMeta.put("file-name", fileName);
        userMeta.put("uploaded-by", uploadedBy);

        meta.setUserMetadata(userMeta);

        ByteArrayInputStream stream = new ByteArrayInputStream(document);
        PutObjectRequest putRequest = new PutObjectRequest(bucket, key, stream, meta);
        s3Client.putObject(putRequest);
    }

    public StorageResponse get(String key) {
        S3Object object = s3Client.getObject(bucket, key);
        S3ObjectInputStream objectContent = object.getObjectContent();
        String fileName = object.getObjectMetadata().getUserMetadata().get("file-name");
        try {
            return new StorageResponse(fileName, objectContent.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(new DeleteObjectRequest(bucket, key));
    }
}
