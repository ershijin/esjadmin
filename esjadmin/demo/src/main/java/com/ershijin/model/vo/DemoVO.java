package com.ershijin.model.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
* @description /
* @author ershijin
* @date 2020-10-26
**/
@Data
public class DemoVO implements Serializable {

    private Long id;

    /** 名称 */
    private String title;

    /** 分类id */
    private Integer categoryId;

    /** 链接地址 */
    private String link;

    /** 状态：-1,删除；0，待审核；1，正常 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer uni;
}