package com.tb.api.tbapiserver.board.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "")
public class Board {
    public static final String SINGULAR = "board";
    public static final String MULTIPLE = "boards";

    @Id
    private int id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "longtext", nullable = false)
    private String contents;

    @Column(length = 50, nullable = false)
    private String author;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "image_path")
    private String image_path;
}
