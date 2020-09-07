package com.ershijin.esjadmin.model.entity;

import com.ershijin.esjadmin.model.BaseEntity;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 系统配置信息
 */
@Data
@Alias("Config")
public class Config extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String value;

    private String name;

    private String description;



}
