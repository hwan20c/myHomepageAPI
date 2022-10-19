package com.tb.api.tbapiserver.board.service;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import com.tb.api.tbapiserver.board.model.File;
import com.tb.api.tbapiserver.board.repository.FileRepository;

@Service
public class FileService {
  private final FileRepository fileRepository;

  public FileService(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public File create(File file) {
    return fileRepository.save(file);
  }

  public int removeByNoticeId(int noticeId) {
    return fileRepository.removeByBoardId(noticeId);
  }

  public File findById(int id) {
    return fileRepository.findById(id).orElseThrow(NoResultException::new);
  }
  
}
