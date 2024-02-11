package io.meraklis.icare.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class StorageConfiguration {

    @Value("${s3.region}")
    private String region;

    @Value("${s3.access-key}")
    private String accessKey;

    @Value("${s3.access-secret}")
    private String accessSecret;

    @Bean
    public AmazonS3 client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                accessKey,
                                accessSecret
                        )))
                .withRegion(region)
                .build();
    }
}
