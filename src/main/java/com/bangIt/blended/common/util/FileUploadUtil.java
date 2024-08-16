package com.bangIt.blended.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileUploadUtil {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.upload-temp}")
    private String temp;

    @Value("${spring.cloud.aws.s3.upload-src}")
    private String upload;

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

    public List<String> s3TempToImages(List<String> tempKeys) {
        List<String> uploadKeys = new ArrayList<>();
        tempKeys.forEach(tempKey -> {
            String[] str = tempKey.split("/");
            String destinationKey = upload + str[str.length - 1];

            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(bucket)
                    .sourceKey(tempKey)
                    .destinationBucket(bucket)
                    .destinationKey(destinationKey)
                    .build();

            s3Client.copyObject(copyObjectRequest);
            s3Client.deleteObject(builder -> builder.bucket(bucket).key(tempKey));

            String url = s3Client.utilities()
                    .getUrl(builder -> builder.bucket(bucket).key(destinationKey))
                    .toString().substring(6);
            uploadKeys.add(url);
        });

        return uploadKeys;
    }
}