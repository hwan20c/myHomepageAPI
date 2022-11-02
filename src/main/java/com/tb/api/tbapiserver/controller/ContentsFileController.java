package com.tb.api.tbapiserver.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.tb.api.tbapiserver.constants.Constants;
import com.tb.api.tbapiserver.model.ContentsFile;
import com.tb.api.tbapiserver.service.ContentsFileService;
import com.tb.api.tbapiserver.service.ObjectStorageService;
import com.tb.api.tbapiserver.service.RandomStringService;

@Controller
@RequestMapping(Constants.ROOT_PATH+Constants.API+Constants.ROOT_PATH+Constants.CONTENTSFILES_PATH)
public class ContentsFileController {
  private final ContentsFileService contentsFileService;
  private final ObjectStorageService objectStorageService;
  private final RandomStringService randomStringService;
  
  public ContentsFileController(ContentsFileService contentsFileService, ObjectStorageService objectStorageService, RandomStringService randomStringService) {
    this.contentsFileService = contentsFileService;
    this.objectStorageService = objectStorageService;
    this.randomStringService = randomStringService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<byte[]> download(@PathVariable int id) {
    ContentsFile contentsFile = contentsFileService.findById(id);
    S3Object s3Object = objectStorageService.downloadFromS3(contentsFile.getBucketName(), contentsFile.getPath());

    try(S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        ByteArrayOutputStream bas = new ByteArrayOutputStream()) {

      byte[] bytesArray = new byte[4096];
      int bytesRead = -1;
      while((bytesRead = s3ObjectInputStream.read(bytesArray, 0, bytesArray.length)) != -1) {
        bas.write(bytesArray, 0, bytesRead);
      }

      String encodingName = URLEncoder.encode(contentsFile.getName(), StandardCharsets.UTF_8.name());

      return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodingName + "\"")
          .body(bas.toByteArray());
    } catch (AmazonClientException | IOException e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @PostMapping
  public ResponseEntity<ContentsFile> upload(@RequestParam("file") MultipartFile multipartFile) {
    String randomString = randomStringService.createRandomString();
    String url = "dev/content-files/" + randomString + "_" + multipartFile.getOriginalFilename();
    System.out.println("@@@ 345 " + url);
    objectStorageService.uploadFile(url, multipartFile);
    ContentsFile contentsFile = new ContentsFile();
    contentsFile.setPath(objectStorageService.getEndpoint() + "/" + url);
    return ResponseEntity.ok().body(contentsFile);
  }

  @DeleteMapping
  public ResponseEntity<String> delete(@RequestParam("url") String url) {
    System.out.println("@@@@@@@@@@@@@@ 4 : "+ url);
    objectStorageService.removeBoardInsertedFile(url);
    return new ResponseEntity<>("Delete Success", HttpStatus.OK);
  }
}
