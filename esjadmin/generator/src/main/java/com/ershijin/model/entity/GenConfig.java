package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@TableName("code_gen_config")
public class GenConfig implements Serializable {

    private static final long serialVersionUID = 3257151075038644660L;

    public GenConfig(String tableName) {
        this.tableName = tableName;
    }

    private Long id;

    @NotBlank
    // 表名
    private String tableName;

    // 接口名称
    private String apiAlias;

    @NotBlank
    // 包路径
    private String pack;

    @NotBlank
    // 模块名
    private String moduleName;

    @NotBlank
    // 前端文件路径
    private String path;

    // 前端文件路径
    private String apiPath;

    // 作者
    private String author;

    // 表前缀
    private String prefix;

    // 是否覆盖
    private Boolean cover = false;
}
