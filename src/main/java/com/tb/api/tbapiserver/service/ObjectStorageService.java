package com.tb.api.tbapiserver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class ObjectStorageService {
  private AmazonS3 objectStorage;

  @Value("${cloud.aws.object-storage.endPoint}")
  private String endPoint;
  @Value("${cloud.aws.object-storage.bucketName}")
  private String bucketName;
  @Value("${cloud.aws.regionName}")
  private Regions regionName;
  @Value("${cloud.aws.credentials.accessKey}")
  private String accessKey;
  @Value("${cloud.aws.credentials.secretKey}")
  private String secretKey;
  @Value("${cloud.aws.object-storage.path.attachment}")
  private String attachmentPath;
  @Value("${cloud.aws.object-storage.path.viewImage}")
  private String viewImagePath;
  @Value("${cloud.aws.object-storage.path.boardInsertedImage}")
  private String boardInsertedImage;

  @PostConstruct
  private void initObjectStorageAws() {
    objectStorage = AmazonS3ClientBuilder.standard()
                  // .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("hwanobjectstorageaccess", regionName.getName()))
                  .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                  .withRegion(regionName)
                  .build();
  }

  public S3Object downloadFromS3(String bucketName, String filePath) {
    return objectStorage.getObject(bucketName, filePath);
  }

  public void uploadFile(String path, MultipartFile file) {
    if(!objectStorage.doesBucketExistV2(bucketName)) {
      objectStorage.createBucket(bucketName);
    }

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setHeader("x-amz-acl", "public-read");
    try {
      objectStorage.putObject(bucketName, path, file.getInputStream(), objectMetadata);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void removeDirectoryFiles(String path) {
    List<KeyVersion> keyVersions = new ArrayList<>();
    List<S3ObjectSummary> s3ObjectSummaries = objectStorage.listObjects(bucketName, path).getObjectSummaries();
    if(!s3ObjectSummaries.isEmpty()) {
      for(S3ObjectSummary file : s3ObjectSummaries) {
        keyVersions.add(new KeyVersion(file.getKey()));
      }
      DeleteObjectsRequest multipleObjectDeleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keyVersions).withQuiet(false);
      objectStorage.deleteObjects(multipleObjectDeleteRequest);
    }
  }

  public void removeDirectoryFiles(int id) {
    removeAttachmentFiles(id);
    removeViewImageFile(id);
  }

  public void removeViewImageFile(int id) {
    List<KeyVersion> keyVersions = new ArrayList<>();
    List<S3ObjectSummary> s3ObjectSummaries = objectStorage.listObjects(bucketName, getViewImagePath() + "/" + id).getObjectSummaries();
    if(!s3ObjectSummaries.isEmpty()) {
      for(S3ObjectSummary file : s3ObjectSummaries) {
        keyVersions.add(new KeyVersion(file.getKey()));
      }
      DeleteObjectsRequest multipleObjectDeleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keyVersions).withQuiet(false);
      objectStorage.deleteObjects(multipleObjectDeleteRequest);
    }
  }

  public void removeAttachmentFiles(int id) {
    List<KeyVersion> keyVersions = new ArrayList<>();
    System.out.println("@@@@@@@@@@@@@@@@@ " + getAttachmentPath() + "/" + id);
    List<S3ObjectSummary> s3ObjectSummaries = objectStorage.listObjects(bucketName, getAttachmentPath() + "/" + id).getObjectSummaries();
    if(!s3ObjectSummaries.isEmpty()) {
      for(S3ObjectSummary file : s3ObjectSummaries) {
        keyVersions.add(new KeyVersion(file.getKey()));
      }
      DeleteObjectsRequest multipleObjectDeleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keyVersions).withQuiet(false);
      objectStorage.deleteObjects(multipleObjectDeleteRequest);
    }
  }

  public void removeBoardInsertedFile(String fileURL) {
    List<KeyVersion> keyVersions = new ArrayList<>();
    System.out.println("@@@@@@@@@@@@@@ : " + fileURL);
    List<S3ObjectSummary> s3ObjectSummaries = objectStorage.listObjects(bucketName, fileURL).getObjectSummaries();
    if(!s3ObjectSummaries.isEmpty()) {
      for(S3ObjectSummary file : s3ObjectSummaries) {
        keyVersions.add(new KeyVersion(file.getKey()));
      }
      DeleteObjectsRequest multipleObjectDeleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keyVersions).withQuiet(false);
      objectStorage.deleteObjects(multipleObjectDeleteRequest);
    }
  }

  public String getEndpoint() {
    return endPoint;
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getAttachmentPath() {
    return attachmentPath;
  }

  public String getViewImagePath() {
    return viewImagePath;
  }

  public String getBoardInsertedImagePath() {
    return boardInsertedImage;
  }
 
}
