package com.tb.api.tbapiserver.board.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tb.api.tbapiserver.board.model.Board;
import com.tb.api.tbapiserver.board.search.BoardSearchRequest;
import com.tb.api.tbapiserver.board.service.BoardService;
import com.tb.api.tbapiserver.constants.Constants;

@RestController
@RequestMapping(Constants.ROOT_PATH+Constants.API+Constants.ROOT_PATH+Constants.BOARDS_PATH)
public class BoardController {

	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@GetMapping
	public ResponseEntity<Page<Board>> list(HttpServletRequest request, BoardSearchRequest boaredSearchRequest) {
		System.out.println("@@@@@@@@@" + org.hibernate.Version.getVersionString());
		Page<Board> boardList = boardService.listAll(boaredSearchRequest);
		return new ResponseEntity<>(boardList, HttpStatus.OK);
	}
}