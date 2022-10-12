package com.tb.api.tbapiserver.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.tb.api.tbapiserver.board.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAll(@Nullable Specification<Board> specification, Pageable pageable);
    Page<Board> findByType(int type, Pageable pageable);
}