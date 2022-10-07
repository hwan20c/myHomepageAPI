package com.tb.api.tbapiserver.board.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tb.api.tbapiserver.board.model.Board;
import com.tb.api.tbapiserver.board.service.BoardService;
import com.tb.api.tbapiserver.constants.Constants;

@RestController
@RequestMapping(Constants.ROOT_PATH)
public class BoardController {

	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@GetMapping("/board")
	public ResponseEntity<Page<Board>> list(
		@RequestParam(value = "title", required = false, defaultValue = "") Optional<String> title,
		@RequestParam(value = "contents", required = false, defaultValue = "") Optional<String> contents,
		@RequestParam(value = "page", required = false, defaultValue = "0") Optional<Integer> page,
		@RequestParam(value = "size", required = false, defaultValue = "9") Optional<Integer> size) {
			Page<Board> boardList = boardService.BoardSearch(title, contents, page, size);
		return new ResponseEntity<>(boardList, HttpStatus.OK);
	}
}