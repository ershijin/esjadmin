package com.ershijin.model.query;

import lombok.Data;

@Data
public class ArticleQuery {
    private String keyword;
    private Long categoryId;
}
