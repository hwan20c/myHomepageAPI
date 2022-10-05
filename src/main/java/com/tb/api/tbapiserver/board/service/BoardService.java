package com.tb.api.tbapiserver.board.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tb.api.tbapiserver.board.model.Board;
import com.tb.api.tbapiserver.board.repository.BoardRepository;

@Service
public class BoardService {
	private final BoardRepository boardRepository;

	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public Board create(Board board) {
		return boardRepository.save(board);
	}

	public Page<Board> BoardSearch(Optional<String> name, Optional<String> search, Optional<Integer> page, Optional<Integer> size) {
			Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(9));
			Page<Board> boardlist = 
	}
}
