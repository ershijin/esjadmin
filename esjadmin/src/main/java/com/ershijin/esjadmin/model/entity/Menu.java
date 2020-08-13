package com.ershijin.esjadmin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ershijin.esjadmin.validation.groups.Update;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Alias("Menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = {Update.class}, message = "id 不能为空")
    @Min(value = 1, groups = {Update.class}, message = "id参数错误")
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long parentId;

    /**
     * 菜单类型：0:目录；1：菜单；2：按钮
     */
    private int type;

    private String path;

    private String redirect;

    private String component;

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
