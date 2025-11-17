package com.example.tcc_gateway.infrastructure.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Bean
    public S3Client s3Client(
            @Value("${amazon.region}") String region,
            @Value("${amazon.accessKey}") String accessKey,
            @Value("${amazon.secretKey}") String secretKey,
            @Value("${amazon.sessionToken}") String sessionToken
    ) {
        final AwsSessionCredentials credentials = AwsSessionCredentials.create(
                accessKey,
                secretKey,
                sessionToken
        );

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
