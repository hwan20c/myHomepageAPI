package com.tb.api.tbapiserver.service;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import com.tb.api.tbapiserver.model.ContentsFile;
import com.tb.api.tbapiserver.repository.ContentsFileRepository;

@Service
public class ContentsFileService {
  private final ContentsFileRepository fileRepository;

  public ContentsFileService(ContentsFileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public ContentsFile create(ContentsFile file) {
    return fileRepository.save(file);
  }

  public int removeByBoardId(int noticeId) {
    return fileRepository.removeByBoardId(noticeId);
  }

  public ContentsFile findById(int id) {
    return fileRepository.findById(id).orElseThrow(NoResultException::new);
  }
  
}
