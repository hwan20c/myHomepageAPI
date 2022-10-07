package com.tb.api.tbapiserver.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tb.api.tbapiserver.board.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {

    @Query("SELECT b FROM Board b WHERE b.title LIKE %?1% AND b.contents LIKE %?2%")
    Page<Board> findByBoardSearch(String title, String contents, Pageable pageable);
}