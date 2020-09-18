package com.ershijin.model.entity;

import com.ershijin.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class Article extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8923925437345403830L;

    private String title;
    private Long categoryId;
    private String coverPicture;
    private String link;
    private String summary;
    private String detail;

}
