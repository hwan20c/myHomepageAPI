package com.tb.api.tbapiserver.board.cotroller;

import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
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
		@RequestParam(value = "name", required = false, defaultValue = "") Optional<String> name,
		@RequestParam(value = "search", required = false, defaultValue = "") Optional<String> search,
		@RequestParam(value = "page", required = false, defaultValue = "0") Optional<Integer> page,
		@RequestParam(value = "size", required = false, defaultValue = "9") Optional<Integer> size) {

	}
}