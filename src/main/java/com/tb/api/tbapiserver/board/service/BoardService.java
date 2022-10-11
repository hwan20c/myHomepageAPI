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

	public Page<Board> listAll(BoardSearchRequest boardSearchRequest) {
		Pageable pageable = PageRequest.of(boardSearchRequest.getPage(), boardSearchRequest.getSize(), Sort.by("id").descending());
		// boardSearchRequest.setType(3);
		if (boardSearchRequest.getSearchKey() == null) {
			// return boardRepository.findByType(String.valueOf(boardSearchRequest.getType()), pageable);
			return boardRepository.findByType(boardSearchRequest.getType(), pageable);
		} else if (boardSearchRequest.getSearchKey().isEmpty()) {
			return boardRepository.findAll(Specification
			.where(BoardSpecification.searchType(boardSearchRequest.getType()))
			.and(BoardSpecification.searchTitle(boardSearchRequest.getSearchValue()))
				.or(BoardSpecification.searchContent(boardSearchRequest.getSearchValue())), pageable);
		} else {
			return boardRepository.findAll(Specification
			.where(BoardSpecification.searchType(boardSearchRequest.getType()))
			.and(BoardSpecification.searchLike(boardSearchRequest)), pageable);
		}
	}

}
