package com.poneres.portal.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class S3StorageService implements StorageService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Autowired
    private S3Client s3Client;

    public void save(String key, byte[] document, String fileName, String uploadedBy) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("file-name", fileName);
        metadata.put("uploaded-by", uploadedBy);

        PutObjectRequest.Builder putObjectRequestBuilder =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .metadata(metadata)
                        .contentLength((long) document.length);

        if (fileName.endsWith(".pdf")) {
            putObjectRequestBuilder.contentType("application/pdf");
        }

        if (fileName.endsWith(".png")) {
            putObjectRequestBuilder.contentType("image/png");
        }

        if (fileName.endsWith(".jpg")) {
            putObjectRequestBuilder.contentType("image/jpg");
        }

        s3Client.putObject(putObjectRequestBuilder.build(), RequestBody.fromBytes(document));
    }

    public StorageResponse get(String key) {
        ResponseBytes<GetObjectResponse> object = s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build(), ResponseTransformer.toBytes());
        byte[] data = object.asByteArray();
        String fileName = object.response().metadata().get("file-name");
        return new StorageResponse(fileName, data);
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }
}
