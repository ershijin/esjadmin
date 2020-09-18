package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

import java.sql.Timestamp;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @description /
* @author ershijin
* @date 2020-09-17
**/
@Data
@TableName("demo")
public class Demo implements Serializable {

    @TableId
    @TableField(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField(value = "title")
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String title;

    @TableField(value = "category_id")
    @NotNull
    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @TableField(value = "link")
    @NotBlank
    @ApiModelProperty(value = "链接地址")
    private String link;

    @TableField(value = "status")
    @NotNull
    @ApiModelProperty(value = "状态：-1,删除；0，待审核；1，正常")
    private Integer status;

    @NotNull
    @ApiModelProperty(value = "createTime")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "updateTime")
    private LocalDateTime updateTime;

}