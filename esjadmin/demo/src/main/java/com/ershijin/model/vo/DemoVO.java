package com.ershijin.model.vo;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @description /
* @author ershijin
* @date 2020-09-17
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

    private Timestamp createTime;

    private Timestamp updateTime;
}