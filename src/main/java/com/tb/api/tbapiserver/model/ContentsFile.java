package com.tb.api.tbapiserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "contents_files")
public class ContentsFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column
  private String name;
  @Column(name = "bucket_name")
  private String bucketName;
  @Column
  private String path;
  @Column(name = "board_id")
  private int boardId;
}
