package com.ershijin.esjadmin.model.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统配置信息
 */
@Data
@Alias("Config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createTime;

    private Date updateTime;


    private String code;

    private String value;

    private String name;

    private String description;



}
