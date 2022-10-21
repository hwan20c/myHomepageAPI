package com.tb.api.tbapiserver.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tb.api.tbapiserver.constants.Constants;
import com.tb.api.tbapiserver.model.Board;
import com.tb.api.tbapiserver.search.BoardSearchRequest;
import com.tb.api.tbapiserver.service.BoardService;

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
	public ResponseEntity<Board> create(@RequestBody Board requestBoard) {
		if(requestBoard.getId() != 0) {
			Board board = boardService.findById(requestBoard.getId()).get();
			board.setId(requestBoard.getId());
			board.setTitle(requestBoard.getTitle());
			board.setContent(requestBoard.getContent());
			board.setImagePath(requestBoard.getImagePath());
			board.setType(requestBoard.getType());
			boardService.save(board);
			return new ResponseEntity<>(board, HttpStatus.OK);
		} else {
			boardService.save(requestBoard);
			return new ResponseEntity<>(requestBoard, HttpStatus.OK);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable String id) {
		boardService.removeBoardById(Integer.parseInt(id));
		return new ResponseEntity<>("Delete Success", HttpStatus.OK);
	}

}