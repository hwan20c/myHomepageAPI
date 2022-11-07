package com.tb.api.tbapiserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tb.api.tbapiserver.model.ContentsFile;

@Repository
public interface ContentsFileRepository extends JpaRepository<ContentsFile, Integer>{
  @Transactional
  @Modifying
  @Query("DELETE FROM ContentsFile f WHERE f.boardId = :boardId")
  int removeByBoardId(@Param("boardId") int boardId);
  int countByPathAndBoardId(String path, int boardId);
}
