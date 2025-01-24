package com.umc7th.a1grade.global.config;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.Getter;

@Getter
@Configuration
public class S3Config {

  private AWSCredentials awsCredentials;

  @Value("${aws.accessKeyId}")
  private String accessKey;

  @Value("${aws.secretAccessKey}")
  private String secretKey;

  @Value("${aws.region.static}")
  private String region;

  @Value("${aws.s3.bucket}")
  private String bucket;

  @Value("${aws.s3.path.question}")
  private String questionPath;

  @Value("${aws.s3.path.question.user}")
  private String userPath;

  @Value("${aws.s3.path.question.ai}")
  private String aiPath;

  @Value("${aws.s3.path.memo}")
  private String memoPath;

  @Value("${aws.s3.path.note}")
  private String notePath;

  @Value("${aws.s3.path.character}")
  private String characterPath;

  @PostConstruct
  public void init() {
    this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
  }

  @Bean
  public AmazonS3 amazonS3() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }

  @Bean
  public AWSCredentialsProvider awsCredentialsProvider() {
    return new AWSStaticCredentialsProvider(awsCredentials);
  }

  public String getUserQuestionPath() {
    return questionPath + '/' + userPath;
  }

  public String getAiQuestionPath() {
    return questionPath + '/' + aiPath;
  }
}
