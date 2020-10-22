package com.ershijin.model.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OnlineUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String authorities;

    private String salt;


}
