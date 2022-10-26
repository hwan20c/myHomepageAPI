package com.tb.api.tbapiserver.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tb.api.tbapiserver.model.Board;
import com.tb.api.tbapiserver.model.ContentsFile;
import com.tb.api.tbapiserver.repository.BoardRepository;
import com.tb.api.tbapiserver.search.BoardSearchRequest;
import com.tb.api.tbapiserver.specification.BoardSpecification;

@Service
@ComponentScan(value = "BoardSpecification")
public class BoardService {
	private final BoardRepository boardRepository;
	private final ObjectStorageService objectStorageService;
	private final ContentsFileService contentsFileService;

	public BoardService(BoardRepository boardRepository, ObjectStorageService objectStorageService, ContentsFileService contentsFileService) {
		this.boardRepository = boardRepository;
		this.objectStorageService = objectStorageService;
		this.contentsFileService = contentsFileService;
	}

	public Board save(Board board) {
		return boardRepository.save(board);
	}

	public Page<Board> listAll(Pageable pageable) {
    return boardRepository.findAll(pageable);
  }


	public Page<Board> listAll(BoardSearchRequest boardSearchRequest) {
		Pageable pageable = PageRequest.of(boardSearchRequest.getPage(), boardSearchRequest.getSize(), Sort.by("id").descending());

		if (boardSearchRequest.getSearchKey().equals("null")) {
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

	public Optional<Board> findById(int id) {
		return boardRepository.findById(id);
	}

	public void removeBoardById(int id) {
		boardRepository.removeBoardById(id);
	}

	public Board boardImages(Board board, MultipartFile mainImageFile) {
		if(!mainImageFile.isEmpty()) {
			board = save(board);
			board.setImagePath(objectStorageService.getEndpoint() + "/" + objectStorageService.getViewImagePath()+ "/" + board.getId() + "/" + mainImageFile.getOriginalFilename());
			objectStorageService.uploadFile(objectStorageService.getViewImagePath() + "/" + board.getId() + "/" + mainImageFile.getOriginalFilename(), mainImageFile);	
		}
		return board;
	}

	public void setMultiFiles(Board board, List<MultipartFile> multipartFiles) {
		boolean isDeleted = false;
		for (MultipartFile multipartFile : multipartFiles) {
			if(!multipartFile.isEmpty()) {
				if(!isDeleted) {
					int removeFilesCount = contentsFileService.removeByBoardId(board.getId());
					if(removeFilesCount != 0) {
						objectStorageService.removeAttachmentFiles(board.getId());
					}
					isDeleted = true;
				}
				ContentsFile contentsFile = new ContentsFile();
				contentsFile.setBoardId(board.getId());
				contentsFile.setName(multipartFile.getOriginalFilename());
				contentsFile.setBucketName(objectStorageService.getBucketName());
				contentsFile.setPath(objectStorageService.getAttachmentPath() + "/" + board.getId() + "/" + multipartFile.getOriginalFilename());
				board.setContentsFiles(Collections.singletonList(contentsFile));
				contentsFileService.create(contentsFile);
				objectStorageService.uploadFile(contentsFile.getPath(), multipartFile);
			}
		}
	}

}
