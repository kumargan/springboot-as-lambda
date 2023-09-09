package com.lambda.sample.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class S3Service {

    @Value("${s3.region}")
    String region;

    private AmazonS3 s3Client=null;

    private AmazonS3 setS3Client() {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(region).build();
        return s3Client;
    }

    public String putObject(File file, String bucketName, String objectKey) {
        AmazonS3  s3Client = setS3Client();
        try {
            PutObjectResult response = s3Client.putObject(bucketName,
                                   objectKey, file);
            return response.getETag();
        }catch(Exception e){
            log.error("Error pushing file in to s3 {} {}",e.getMessage(),e);
        }

        return "";
    }


}
