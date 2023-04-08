package ru.installbiz.das_stroy.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import ru.installbiz.das_stroy.entity.Config;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class StorageService {
    public static String putFile(String file) {
        final String ENDPOINT = "storage.yandexcloud.net";

        List<String> fileList = List.of(file.split("/"));
        String fileName = fileList.get(fileList.size() - 1);

        BasicAWSCredentials bAWSc = new BasicAWSCredentials(Config.getAWS_ID(), Config.getAWS_KEY());
        AwsClientBuilder.EndpointConfiguration ep = new AwsClientBuilder
                .EndpointConfiguration(ENDPOINT, "ru-central1");
        final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(bAWSc))
                .withEndpointConfiguration(ep).build();

        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * 24 * 5;
        expiration.setTime(expTimeMillis);

        S3Objects.inBucket(s3client, Config.getBUCKET_NAME()).forEach((S3ObjectSummary objectSummary) -> {
            if (objectSummary.getKey().equals(fileName)) {
                s3client.deleteObject(new DeleteObjectRequest(Config.getBUCKET_NAME(), fileName));
            }
        });

        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd/");
        String partDate = DateFormat.format(Calendar.getInstance().getTime());

        s3client.putObject(Config.getBUCKET_NAME(), partDate + fileName, new File(file));
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(Config.getBUCKET_NAME(), partDate + fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL urlNew = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        return urlNew.toString();
    }
}
