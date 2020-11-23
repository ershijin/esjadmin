package com.ershijin.model.dto;

import lombok.Data;

@Data
public class ArticlePictureDTO {
    private Long id;
    private Long articleId;
    private String url;
    private Integer sortId;
}
