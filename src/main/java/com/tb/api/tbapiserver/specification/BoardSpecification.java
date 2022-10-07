package com.tb.api.tbapiserver.specification;

import org.springframework.data.jpa.domain.Specification;

import com.tb.api.tbapiserver.board.model.Board;
import com.tb.api.tbapiserver.board.search.BoaredSearchRequest;

public class BoardSpecification {

    private BoardSpecification() {
        throw new IllegalStateException("Utility : Specification Class");
    }

    public static Specification<Board> searchLike(BoaredSearchRequest boaredSearchRequest) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get(boaredSearchRequest.getSearchKey()), "%" + boaredSearchRequest.getSearchValue() + "%");
    }

    public static Specification<Board> searchTitle(String searchValue) {
        return (root, query, criteriabuilder) ->
            criteriabuilder.like(root.get("title"), "%" + searchValue + "%");
    }

    public static Specification<Board> searchContent(String searchValue) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get("content"), "%" + searchValue + "%");
      }
    
      public static Specification<Board> searchType(int type) {
        return ((root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("type"), type));
      }
    
}
