package com.tb.api.tbapiserver.board.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tb.api.tbapiserver.board.model.Board;
import com.tb.api.tbapiserver.board.repository.BoardRepository;
import com.tb.api.tbapiserver.board.search.BoardSearchRequest;
import com.tb.api.tbapiserver.specification.BoardSpecification;

@Service
@ComponentScan(value = "BoardSpecification")
public class BoardService {
	private final BoardRepository boardRepository;

	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public Board create(Board board) {
		return boardRepository.save(board);
	}

	public Page<Board> listAll(BoardSearchRequest boaredSearchRequest) {
		Pageable pageable = PageRequest.of(boaredSearchRequest.getPage(), boaredSearchRequest.getSize(), Sort.by("id").descending());
		// boaredSearchRequest.setType(3);
		if (boaredSearchRequest.getSearchKey() == null) {
			// return boardRepository.findByType(String.valueOf(boaredSearchRequest.getType()), pageable);
			return boardRepository.findByType(boaredSearchRequest.getType(), pageable);
		} else if (boaredSearchRequest.getSearchKey().isEmpty()) {
			return boardRepository.findAll(Specification
			.where(BoardSpecification.searchType(boaredSearchRequest.getType()))
			.and(BoardSpecification.searchTitle(boaredSearchRequest.getSearchValue()))
				.or(BoardSpecification.searchContent(boaredSearchRequest.getSearchValue())), pageable);
		} else {
			return boardRepository.findAll(Specification
			.where(BoardSpecification.searchType(boaredSearchRequest.getType()))
			.and(BoardSpecification.searchLike(boaredSearchRequest)), pageable);
		}
	}

}
