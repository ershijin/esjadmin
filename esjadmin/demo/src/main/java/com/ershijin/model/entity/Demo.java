package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
* @description /
* @author ershijin
* @date 2020-10-15
**/
@Data
@TableName("demo")
public class Demo implements Serializable {

    @TableId
    @ApiModelProperty(value = "id")
    private Long id;

    @NotBlank(message="名称 不能为空")
    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @NotBlank(message="链接地址 不能为空")
    @ApiModelProperty(value = "链接地址")
    private String link;

    @ApiModelProperty(value = "状态：-1,删除；0，待审核；1，正常")
    private Integer status;

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "createTime")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "updateTime")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "uni")
    private Integer uni;

}