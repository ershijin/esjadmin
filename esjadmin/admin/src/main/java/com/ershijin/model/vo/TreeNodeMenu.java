package com.ershijin.model.vo;

import com.ershijin.model.entity.MenuMeta;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

@Alias("TreeNodeMenu")
@Data
public class TreeNodeMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String path;

    private String name;

    private String component;

    private boolean noCache;

    private String title;

    private String icon;

    private Long parentId;

    private int type;

    private Integer priority;

    private boolean hidden;

    private boolean enabled;

    private String remark;

    private MenuMeta meta;

    private String permission;

    private List<TreeNodeMenu> children;

}
