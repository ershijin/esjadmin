package com.ershijin.esjadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.esjadmin.dao.UserMapper;
import com.ershijin.esjadmin.dao.UserRoleMapper;
import com.ershijin.esjadmin.model.PageResult;
import com.ershijin.esjadmin.model.entity.Authentication;
import com.ershijin.esjadmin.model.entity.Role;
import com.ershijin.esjadmin.model.entity.User;
import com.ershijin.esjadmin.model.entity.UserRole;
import com.ershijin.esjadmin.model.query.UserQuery;
import com.ershijin.esjadmin.model.vo.UserVO;
import com.ershijin.esjadmin.util.JsonUtils;
import com.ershijin.esjadmin.util.MyBeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 从数据库或者缓存中取出用户信息，详见接口注释
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不对");
        }
        return user;
    }

    /**
     * 保存用户登录信息，并返回 token
     *
     * @param user
     * @return
     */
    public String saveLoginInfo(User user) {

        String token = UUID.randomUUID().toString();
        Authentication authentication = new Authentication();
        authentication.setUsername(user.getUsername());
        authentication.setToken(token);
        authentication.setCreateTime(new Date());
        authentication.setExpireTime(new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7));
        // 随机生成10位字符串作为salt,构造UserDetails的password用
        authentication.setSalt(RandomStringUtils.randomGraph(10));

        List<String> permissions = new ArrayList<>();
        user.getAuthorities().forEach(i -> {
            permissions.add(i.getAuthority());
        });
        authentication.setPermissions(JsonUtils.toJsonString(permissions));

        authenticationService.insertAuthentication(authentication);
        return token;
    }

    /**
     * 获取用户登录信息
     * @param authentication 凭证信息，保存在数据库中的
     * @return
     */
    public UserDetails getLoginInfo(Authentication authentication) {
//        return loadUserByUsername(username);


//        Authentication authentication = authenticationService.getAuthenticationByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> authoritieList = JsonUtils.toList(authentication.getPermissions());
        authoritieList.forEach(i -> {
            authorities.add(new SimpleGrantedAuthority(i));
        });
        // 从数据库中取出token生成时用的salt，将salt放到password字段返回
        return org.springframework.security.core.userdetails.User.builder()
                .username(authentication.getUsername())
                .password(authentication.getSalt())
                .authorities(authorities)
                .build();
    }

    /**
     * 清除数据库或者缓存中登录salt
     * @param token
     */
    public void deleteLoginInfo(String token) {
        authenticationService.deleteByToken(token);
    }

    public PageResult list(int pageNum, int pageSize, UserQuery query) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(query.getKeyword())) {
            queryWrapper.and(i->i.like("username", query.getKeyword()).or().like("name", query.getKeyword()));
        }
        if (query.getEnabled() != null) {
            queryWrapper.eq("is_enabled", query.getEnabled());
        }
        if (query.getRole_id() != null) {
            queryWrapper.apply("id IN (SELECT user_id FROM user_role WHERE role_id={0})", query.getRole_id());
        }
        queryWrapper.select(User.class, i -> !i.getColumn().equals("password"));
        IPage<User> result = userMapper.selectPage(page, queryWrapper);
        result.setRecords(MyBeanUtils.convert(result.getRecords(), UserVO.class));
        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Transactional
    public void save(User user) {
        // 判断账号是否重复
        user.setCreateTime(new Date());
        // 加密password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        // 保存user信息
        userMapper.insert(user);
        // 保存 user_role 信息
        if (!user.getRoles().isEmpty()) {
            Set<UserRole> userRoles = new HashSet<>();
            user.getRoles().forEach(role -> {
                UserRole userRole = new UserRole();
                userRole.setCreateTime(new Date());
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoles.add(userRole);
            });
            userRoleMapper.batchInsert(userRoles);
        }
    }

    /**
     * 更新用户信息
     * @param user
     */
    @Transactional
    public void update(User user) {
        user.setUpdateTime(new Date());
        if (!StringUtils.isEmpty(user.getPassword())) {
            // 加密password
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        // 保存user信息
        userMapper.update(user);
        // 删除 user_role 信息
        userRoleMapper.deleteByUserId(user.getId());
        // 保存新 user_role 信息
        if (!user.getRoles().isEmpty()) {
            Set<UserRole> userRoles = new HashSet<>();
            user.getRoles().forEach(role -> {
                UserRole userRole = new UserRole();
                userRole.setCreateTime(new Date());
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoles.add(userRole);
            });
            userRoleMapper.batchInsert(userRoles);
        }
    }

    /**
     * 更新密码
     * @param user
     */
    public void updatePassword(User user) {
        user.setUpdateTime(new Date());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.updateById(user);
    }


    /**
     * 获取用户信息
     * @param id
     * @return
     */
    public User get(Long id) {
        User user = userMapper.getById(id);
        return user;
    }

    /**
     * 通过用户id查询用户的角色列表
     * @param id
     * @return
     */
    public List<Role> listRolesById(Long id) {
        return userMapper.listRolesById(id);
    }

    public void enableById(Long id) {
        User user = new User();
        user.setId(id);
        user.setUpdateTime(new Date());
        user.setEnabled(true);
//        userMapper.enable(user);
        userMapper.updateById(user);
    }
    public void disableById(Long id) {
        User user = new User();
        user.setId(id);
        user.setUpdateTime(new Date());
        user.setEnabled(false);
        userMapper.updateById(user);
    }

    @Transactional
    public void removeById(Long id) {
        // 删除用户角色关系
        userRoleMapper.deleteByUserId(id);
        // 删除用户记录
        userMapper.deleteById(id);
    }
}
