package com.tb.api.tbapiserver.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "boards")
public class Board {
    public static final String SINGULAR = "board";
    public static final String MULTIPLE = "boards";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", updatable = false)
    private List<ContentsFile> contentsFiles;

    public void increaseViewCount() {
        this.viewCount++;
    }
}
