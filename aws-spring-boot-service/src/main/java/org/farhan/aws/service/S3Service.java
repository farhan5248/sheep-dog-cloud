package org.farhan.aws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public List<String> listBuckets() {
        ListBucketsResponse response = s3Client.listBuckets();
        return response.buckets().stream()
                .map(Bucket::name)
                .collect(Collectors.toList());
    }

    public List<String> listObjects(String bucketName) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        return response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    public void uploadObject(String bucketName, String key, byte[] data) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(data));
    }

    public byte[] downloadObject(String bucketName, String key) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        return response.readAllBytes();
    }

    public void deleteObject(String bucketName, String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(request);
    }
}
