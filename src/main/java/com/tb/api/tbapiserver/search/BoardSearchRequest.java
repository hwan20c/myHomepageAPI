package com.tb.api.tbapiserver.search;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardSearchRequest {
    private int page = 0;
    private int size = 9;
    private int type = 0;
    private String searchKey;
    private String searchValue;
}
