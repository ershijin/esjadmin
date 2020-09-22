package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ershijin.model.BaseEntity;
import com.ershijin.validation.groups.Update;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@TableName("sys_dict_detail")
//@Alias("DictDetail")
public class DictDetail extends BaseEntity implements Serializable {

    // 字典
    private Long dictId;

    // 字典标签
    private String label;

    // 字典值
    private String value;

    // 排序
    private Integer dictSort = 999;
}