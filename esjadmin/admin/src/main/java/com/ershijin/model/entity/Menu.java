package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ershijin.model.BaseEntity;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

@Data
@Alias("Menu")
public class Menu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parentId;

    /**
     * 菜单类型：0:目录；1：菜单；2：按钮
     */
    private int type;

    private String path;

    private String name;

    private String component;

    @TableField("is_no_cache")
    private boolean noCache;

    private String title;

    private String icon;

    private Integer priority;

    // 权限标识
    private String permission;

    private String remark;

    @TableField("is_hidden")
    private boolean hidden;

    @TableField("is_enabled")
    private boolean enabled;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Menu> children;

}
