package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.component.Config;
import com.ershijin.config.security.bean.SecurityProperties;
import com.ershijin.converter.UserConverter;
import com.ershijin.dao.UserMapper;
import com.ershijin.dao.UserRoleMapper;
import com.ershijin.exception.ApiException;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.OnlineUserDTO;
import com.ershijin.model.dto.UserDTO;
import com.ershijin.model.dto.UserPrincipal;
import com.ershijin.model.entity.Role;
import com.ershijin.model.entity.User;
import com.ershijin.model.entity.UserRole;
import com.ershijin.model.query.UserQuery;
import com.ershijin.util.*;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private SecurityProperties properties;

    /**
     * 从数据库或者缓存中取出用户信息，详见接口注释
     *
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
    public String saveLoginInfo(User user, HttpServletRequest request) {

        String token = TokenUtils.generateTokenCode();

        List<String> permissions = new ArrayList<>();
        user.getAuthorities().forEach(i -> {
            permissions.add(i.getAuthority());
        });

        // 将登录信息保存到redis
        String ip = RequestUtils.getIp(request);
        LocalDateTime lastLoginTime = LocalDateTime.now();

        OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
        onlineUserDTO.setId(user.getId());
        onlineUserDTO.setUsername(user.getUsername());
        // 随机生成10位字符串作为salt,构造UserDetails的password用
        onlineUserDTO.setSalt(RandomStringUtils.randomGraph(10));
        onlineUserDTO.setAuthorities(JsonUtils.toJsonString(permissions));
        onlineUserDTO.setKey(token);
        onlineUserDTO.setName(user.getName());
        onlineUserDTO.setIp(ip);
        onlineUserDTO.setAddress(RequestUtils.getCityInfo(ip));
        onlineUserDTO.setBrowser(RequestUtils.getBrowser(request));
        onlineUserDTO.setLoginTime(lastLoginTime);
        RedisUtils.set(properties.getOnlineKey() + token, onlineUserDTO, properties.getTokenValidityInSeconds());

        // 更新用户登录时间和ip
        userMapper.update(null,
                Wrappers.<User>lambdaUpdate().eq(User::getId, user.getId()).set(User::getLastIp, ip).set(User::getLastLoginTime,
                lastLoginTime));
        return token;
    }

    /**
     * 获取用户登录信息
     *
     * @param onlineUserDTO 凭证信息，保存在数据库中的
     * @return
     */
    public UserDetails getLoginInfo(OnlineUserDTO onlineUserDTO) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<String> authoritieList = JsonUtils.toList(onlineUserDTO.getAuthorities());
        authoritieList.forEach(i -> {
            authorities.add(new SimpleGrantedAuthority(i));
        });
        // 给每一个登录用户添加普通权限
        authorities.add(new SimpleGrantedAuthority(Config.GENERAL_PERMISSION));
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(onlineUserDTO.getId());
        userPrincipal.setUsername(onlineUserDTO.getUsername());
        userPrincipal.setPassword(onlineUserDTO.getSalt());
        userPrincipal.setAuthorities(authorities);
        return userPrincipal;
    }

    /**
     * 清除数据库或者缓存中登录salt
     *
     * @param token
     */
    public void deleteLoginInfo(String token) {
        RedisUtils.del(properties.getOnlineKey() + token);
    }

    public PageResult list(UserQuery userQuery, Page<User> page) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userQuery.getKeyword())) {
            queryWrapper.and(i -> i.like("username", userQuery.getKeyword()).or().like("name", userQuery.getKeyword()));
        }
        queryWrapper.eq(userQuery.getEnabled() != null, "is_enabled", userQuery.getEnabled());
        queryWrapper.apply(userQuery.getRoleId() != null, "id IN (SELECT user_id FROM user_role WHERE " +
                "role_id={0})", userQuery.getRoleId());
        queryWrapper.select(User.class, i -> !i.getColumn().equals("password"));

        page.setOrders(OrderItem.descs("id"));

        IPage<User> result = userMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), userConverter.toDto(result.getRecords()));
    }

    @Transactional
    public void save(User user) {
        // 判断账号是否重复
        // 加密password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        // 保存user信息
        userMapper.insert(user);
        // 保存 user_role 信息
        if (!user.getRoles().isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            Set<UserRole> userRoles = new HashSet<>();
            user.getRoles().forEach(role -> {
                UserRole userRole = new UserRole();
                userRole.setCreateTime(now);
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoles.add(userRole);
            });
            userRoleMapper.batchInsert(userRoles);
        }
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @Transactional
    public void update(User user) {
        if (!StringUtils.isEmpty(user.getPassword())) {
            // 加密password
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        // 保存user信息
        userMapper.updateById(user);
        // 删除 user_role 信息
        userRoleMapper.deleteByUserId(user.getId());
        // 保存新 user_role 信息
        if (!user.getRoles().isEmpty()) {
            Set<UserRole> userRoles = new HashSet<>();
            LocalDateTime now = LocalDateTime.now();
            user.getRoles().forEach(role -> {
                UserRole userRole = new UserRole();
                userRole.setCreateTime(now);
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoles.add(userRole);
            });
            userRoleMapper.batchInsert(userRoles);
        }
    }

    /**
     * 更新密码
     *
     * @param user
     */
    public void updatePassword(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.updateById(user);
    }


    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    public User get(Long id) {
        User user = userMapper.getById(id);
        return user;
    }

    /**
     * 通过用户id查询用户的角色列表
     *
     * @param id
     * @return
     */
    public List<Role> listRolesById(Long id) {
        return userMapper.listRolesById(id);
    }

    public void enableById(Long id) {
        User user = new User();
        user.setId(id);
        user.setEnabled(true);
        userMapper.updateById(user);
    }

    public void disableById(Long id) {
        User user = new User();
        user.setId(id);
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

    /**
     * 保存头像
     *
     * @param avatar
     * @return
     * @throws IOException
     * @throws ApiException
     */
    public String saveAvatar(MultipartFile avatar, User oldUserInfo) throws IOException, ApiException {
        String uploadPath = ResourceUtils.getURL("classpath:").getPath() + "upload/avatar/";
        try {
            String avatarFile = FileUtils.upload(avatar, uploadPath);

            User user = new User();
            // 存入数据库
            user.setId(oldUserInfo.getId());
            user.setEnabled(oldUserInfo.isEnabled());
            user.setAvatar(avatarFile);
            userMapper.updateById(user);

            // 删除老头像
            FileUtils.del(uploadPath + oldUserInfo.getAvatar());

            return avatarFile;
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
