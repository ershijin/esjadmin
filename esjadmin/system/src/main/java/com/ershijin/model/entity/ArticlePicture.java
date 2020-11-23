package com.ershijin.model.entity;

import com.ershijin.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticlePicture extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6629423304492834468L;

    private Long articleId;
    private String url;
    private Long sortId;

}
