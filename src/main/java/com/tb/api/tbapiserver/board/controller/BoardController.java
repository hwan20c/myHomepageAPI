package com.tb.api.tbapiserver.board.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<Page<Board>> list(HttpServletRequest request, 
																					@RequestParam(value = "searchKey", required = false, defaultValue = "")  Optional<String> searchKey,
																					@RequestParam(value = "searchValue", required = false, defaultValue = "")  Optional<String> searchValue,
																					@RequestParam(value = "page", required = false, defaultValue = "")  Optional<Integer> page) {
		BoardSearchRequest boardSearchRequest = new BoardSearchRequest();
		boardSearchRequest.setPage(page.orElse(0));
		boardSearchRequest.setSearchKey(searchKey.orElse(""));
		boardSearchRequest.setSearchValue(searchValue.orElse(""));
		Page<Board> boardList = boardService.listAll(boardSearchRequest);
		return new ResponseEntity<>(boardList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Board> detail(HttpServletRequest request, @PathVariable int id) {
		Optional<Board> board = boardService.findById(id);
    board.get().increaseViewCount();
		boardService.save(board.get());
		return new ResponseEntity<Board>(board.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Board> create(@RequestBody Board board) {
		boardService.save(board);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Board> update(@RequestBody Board board) {
		boardService.save(board);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Board> delete(@RequestBody Board board) {
		boardService.removeBoardById(board.getId());
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

}