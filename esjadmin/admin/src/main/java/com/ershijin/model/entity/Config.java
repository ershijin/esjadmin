package com.ershijin.model.entity;

import com.ershijin.model.BaseEntity;
import com.ershijin.validation.constraints.NotDuplicate;
import com.ershijin.validation.groups.Save;
import com.ershijin.validation.groups.Update;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统配置信息
 */
@Data
@Alias("Config")
public class Config extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(groups = {Save.class, Update.class}, message = "参数名不能为空")
    @NotDuplicate(groups = {Save.class}, table = "config", column = "code", message = "参数名已经被使用")
    private String code;

    @NotBlank(groups = {Save.class, Update.class}, message = "参数值不能为空")
    private String value;

    @NotBlank(groups = {Save.class, Update.class}, message = "配置名称不能为空")
    private String name;

    private String description;



}
