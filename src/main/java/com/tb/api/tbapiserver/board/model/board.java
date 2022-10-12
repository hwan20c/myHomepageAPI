package com.tb.api.tbapiserver.board.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tb.api.tbapiserver.dto.BoardSaveRequestDto;

import lombok.Data;

@Data
@Entity
@Table(name = "boards")
public class Board {
    public static final String SINGULAR = "board";
    public static final String MULTIPLE = "boards";

    @Id
    private int id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "longtext", nullable = false)
    private String content;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "view_count")
    private int viewCount;

    @Column
    private int type;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Board() {}
    public Board(BoardSaveRequestDto boardSaveRequestDto) {
        this.id = boardSaveRequestDto.getId();
        this.title = boardSaveRequestDto.getTitle();
        this.content = boardSaveRequestDto.getContent();
        this.imagePath = boardSaveRequestDto.getImagePath();
        this.viewCount = boardSaveRequestDto.getViewCount();
        this.type = boardSaveRequestDto.getType();
     }
}
