package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码
 */
@Data
@Alias("AuthCode")
public class AuthCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String code;

    private LocalDateTime createTime;

    private LocalDateTime expireTime;


}
