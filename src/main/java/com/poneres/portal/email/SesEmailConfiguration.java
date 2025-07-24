package com.poneres.portal.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SesEmailConfiguration {

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.access-secret}")
    private String accessSecret;

    @Value("${aws.region}")
    private String region;

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, accessSecret)
                ))
                .region(Region.of(region))
                .build();
    }
}
