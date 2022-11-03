package com.tb.api.tbapiserver.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tb.api.tbapiserver.constants.Constants;
import com.tb.api.tbapiserver.model.Board;
import com.tb.api.tbapiserver.model.ContentsFile;
import com.tb.api.tbapiserver.search.BoardSearchRequest;
import com.tb.api.tbapiserver.service.BoardService;
import com.tb.api.tbapiserver.service.ContentsFileService;
import com.tb.api.tbapiserver.service.ObjectStorageService;

@RestController
@RequestMapping(Constants.ROOT_PATH+Constants.API+Constants.ROOT_PATH+Constants.BOARDS_PATH)
public class BoardController {

	private final BoardService boardService;
	private final ObjectStorageService objectStorageService;
	private final ContentsFileService contentsFileService;

	public BoardController(BoardService boardService, ObjectStorageService objectStorageService, ContentsFileService contentsFileService) {
		this.boardService = boardService;
		this.objectStorageService = objectStorageService;
		this.contentsFileService = contentsFileService;
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
	public ResponseEntity<Board> create(@RequestParam ("board") String requestBoard, 
																			@RequestParam (required = false, name = "attachedFiles") List<MultipartFile> attachedFiles, 
																			@RequestPart(required = false, name="mainImageFile") MultipartFile mainImageFile,
																			@RequestParam (required = false, name = "contentFileNames", defaultValue = "") Optional<String> contentFileNames) throws Exception{

		System.out.println("@@@@@@@@@@@ 1" + requestBoard);
		List<String> refinedContentFileName = new ArrayList<>();

		if(!contentFileNames.get().equals("")) {
			System.out.println("@@@@@@@@ 123123 : " + contentFileNames.get());
			String replaceStr = contentFileNames.get().replaceAll("^\\[|]$", "");
			replaceStr = replaceStr.replaceAll("\"", "");
			refinedContentFileName = new ArrayList<String>(Arrays.asList(replaceStr.split(",")));
			System.out.println("@@@@@@@@@@ : " + replaceStr);
			System.out.println("@@@@@@@@@@@ : " + refinedContentFileName);
		} 		

		ObjectMapper objectMapper = new ObjectMapper();
		Board refinedBoard = objectMapper.readValue(requestBoard, Board.class);
		System.out.println("@@@@@@@@@@@@ 2 : " + refinedBoard.toString());

		//update
		if(refinedBoard.getId() != 0) {
			Board board = boardService.findById(refinedBoard.getId()).get();
			board.setId(refinedBoard.getId());
			board.setTitle(refinedBoard.getTitle());
			board.setContent(refinedBoard.getContent());
			if(refinedBoard.getImagePath() != null) board.setImagePath(refinedBoard.getImagePath());
			board.setType(refinedBoard.getType());

			if(mainImageFile != null) {
				objectStorageService.removeViewImageFile(board.getId());
				board = boardService.boardImages(board, mainImageFile);
				if(refinedBoard != null && refinedBoard.getImagePath() != null && mainImageFile.isEmpty()) {
					board.setImagePath(refinedBoard.getImagePath());
				}
			}
			System.out.println("@@@@@@@@@@@@ 3 : " + board.toString());
			boardService.save(board);

			if(attachedFiles != null) {
				boardService.setMultiFiles(board, attachedFiles);
			}

			if(!refinedContentFileName.isEmpty()) {
				for(int i=0; i<refinedContentFileName.size(); i++) {
					ContentsFile contentsFile = new ContentsFile();
					int searchFinalBackslach = refinedContentFileName.get(i).lastIndexOf("/");
					contentsFile.setName(refinedContentFileName.get(i).substring(searchFinalBackslach + 1));
					contentsFile.setBucketName(objectStorageService.getBucketName());
					contentsFile.setPath(refinedContentFileName.get(i).substring(48));
					contentsFile.setBoardId(board.getId());
					contentsFileService.create(contentsFile);
				}
			}

			return new ResponseEntity<>(board, HttpStatus.OK);
			//create
		} else {
			Board board = new Board();
			board.setTitle(refinedBoard.getTitle());
			board.setContent(refinedBoard.getContent());
			board.setType(refinedBoard.getType());
			if(mainImageFile != null) {
				board = boardService.boardImages(board, mainImageFile);
			}
			System.out.println("@@@@@@@@@@@@ 4 : " + board.toString());
			boardService.save(board);
			if(attachedFiles != null) {
				boardService.setMultiFiles(board, attachedFiles);
			}
			if(!refinedContentFileName.isEmpty()) {
				for(int i=0; i<refinedContentFileName.size(); i++) {
					ContentsFile contentsFile = new ContentsFile();
					int searchFinalBackslach = refinedContentFileName.get(i).lastIndexOf("/");
					contentsFile.setName(refinedContentFileName.get(i).substring(searchFinalBackslach + 1));
					contentsFile.setBucketName(objectStorageService.getBucketName());
					contentsFile.setPath(refinedContentFileName.get(i).substring(48));
					contentsFile.setBoardId(board.getId());
					contentsFileService.create(contentsFile);
				}
			}
			return new ResponseEntity<>(board, HttpStatus.OK);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		int removeFilesCount = contentsFileService.removeByBoardId(id);
		if(removeFilesCount != 0) {
			objectStorageService.removeDirectoryFiles(id);
		}
		boardService.removeBoardById(id);
		return new ResponseEntity<>("Delete Success", HttpStatus.OK);
	}

}