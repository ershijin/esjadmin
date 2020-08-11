package com.ershijin.esjadmin.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.esjadmin.model.entity.Role;
import com.ershijin.esjadmin.model.entity.User;
import com.ershijin.esjadmin.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    User getByUsername(String username);
    User getById(Long id);
    List<Role> listRolesById(Long id);
    int insert(User user);

    IPage<UserVO> selectPageVo(Page<?> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    void update(User user);

    int deleteById(Long id);
}
