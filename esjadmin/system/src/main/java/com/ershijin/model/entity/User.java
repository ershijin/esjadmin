package com.ershijin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ershijin.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Alias("User")
@Data
public class User extends BaseEntity implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String name;

    private String avatar;

    @TableField("is_enabled")
    private boolean enabled;

    private String remark;

    private String lastIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Menu> menus;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
//        if (roles != null) {
//            for (Role role : roles) {
//                authorities.add(new SimpleGrantedAuthority(role.getId().toString()));
//            }
//        }
        if (menus != null) {
            menus.forEach(menu -> {
                if (!StringUtils.isEmpty(menu.getPermission()) && menu.isEnabled()) {
                    authorities.add(new SimpleGrantedAuthority(menu.getPermission()));
                }
            });
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }




}
