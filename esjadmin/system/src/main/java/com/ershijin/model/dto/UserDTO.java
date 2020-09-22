package com.ershijin.model.dto;

import com.ershijin.model.BaseEntity;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("UserDTO")
@Data
public class UserDTO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private boolean enabled;

    private String username;

    private String remark;

    private String avatar;

}
