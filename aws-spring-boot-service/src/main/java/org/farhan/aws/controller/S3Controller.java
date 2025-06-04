package org.farhan.aws.controller;

import org.farhan.aws.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Autowired
    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/buckets")
    public ResponseEntity<List<String>> listBuckets() {
        List<String> buckets = s3Service.listBuckets();
        return new ResponseEntity<>(buckets, HttpStatus.OK);
    }

    @GetMapping("/buckets/{bucketName}/objects")
    public ResponseEntity<List<String>> listObjects(@PathVariable String bucketName) {
        List<String> objects = s3Service.listObjects(bucketName);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    @PostMapping("/buckets/{bucketName}/objects/{objectKey}")
    public ResponseEntity<Void> uploadObject(
            @PathVariable String bucketName,
            @PathVariable String objectKey,
            @RequestParam("file") MultipartFile file) {
        try {
            s3Service.uploadObject(bucketName, objectKey, file.getBytes());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buckets/{bucketName}/objects/{objectKey}")
    public ResponseEntity<byte[]> downloadObject(
            @PathVariable String bucketName,
            @PathVariable String objectKey) {
        try {
            byte[] data = s3Service.downloadObject(bucketName, objectKey);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", objectKey);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/buckets/{bucketName}/objects/{objectKey}")
    public ResponseEntity<Void> deleteObject(
            @PathVariable String bucketName,
            @PathVariable String objectKey) {
        s3Service.deleteObject(bucketName, objectKey);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
