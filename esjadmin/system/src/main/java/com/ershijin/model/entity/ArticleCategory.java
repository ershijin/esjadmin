package com.ershijin.model.entity;

import com.ershijin.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleCategory extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3693301080470881594L;

    private String name;

    private Integer sortId;

}
