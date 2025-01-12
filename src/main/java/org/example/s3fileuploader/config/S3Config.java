package org.example.s3fileuploader.config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value(value = "${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value(value = "${aws.accessKey}")
    private String accessKey;

    @Bean
    public AmazonS3 s3() {
        // Storing the access and the secret key from AWS to access the services
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();


    }

}
