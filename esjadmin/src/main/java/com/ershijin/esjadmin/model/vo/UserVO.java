package com.ershijin.esjadmin.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Alias("UserVO")
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String name;

    private boolean enabled;

    private String username;

    private String remark;

    private String avatar;

}
