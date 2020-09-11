package com.ershijin.model.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @todo 与AuthenticationToken合并为一个模型???
 */
@Data
@Alias("Authentication")
public class Authentication implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime expireTime;

    private String username;

    private String token;

    private String permissions;

    private String salt;


}
