package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ershijin.model.BaseEntity;
import com.ershijin.validation.groups.Update;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@TableName("sys_dict")
public class Dict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8665508394661986282L;

    @TableField(exist = false)
    private List<DictDetail> dictDetails;

    @NotBlank
    // 名称
    private String name;

    // 描述
    private String description;
}