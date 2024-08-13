package com.bangIt.blended.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUploadUtil {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${spring.cloud.aws.s3.upload-temp}")
    private String temp;
    @Value("${spring.cloud.aws.s3.upload-src}")
    private String upload;

    public FileUploadUtil(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Map<String, String> s3TempUpload(MultipartFile file) throws IOException {
        String orgFileName = file.getOriginalFilename();
        String newFileName = newFileName(orgFileName);
        String bucketKey = temp + newFileName;

        try (InputStream is = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(bucketKey)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            RequestBody requestBody = RequestBody.fromInputStream(is, file.getSize());
            s3Client.putObject(putObjectRequest, requestBody);
        }

        String url = s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(bucketKey))
                .toString().substring(6); // https://이미지주소

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", url);
        resultMap.put("bucketKey", bucketKey);
        resultMap.put("orgName", orgFileName);
        return resultMap;
    }

    private String newFileName(String orgFileName) {
        int index = orgFileName.lastIndexOf(".");
        return UUID.randomUUID().toString() + orgFileName.substring(index);
    }
}