package com.tb.api.tbapiserver.dto;

import lombok.Data;

@Data
public class BoardSaveRequestDto {
  private int id;
  private String title;
  private String content;
  private String imagePath;
  private int viewCount;
  private int type;
}
