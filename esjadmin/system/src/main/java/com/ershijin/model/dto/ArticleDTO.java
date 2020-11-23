package com.ershijin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDTO implements Serializable {

    private static final long serialVersionUID = 6432189344672135977L;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String title;
    private Long categoryId;
    private String coverPicture;
    private String link;
    private String summary;
    private String detail;

    private List<ArticlePictureDTO> pictures;

}
