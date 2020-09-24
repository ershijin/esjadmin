package com.ershijin.controller;

import com.ershijin.model.Page;
import com.ershijin.annotation.Log;
import com.ershijin.exception.ApiException;
import com.ershijin.exception.NotFoundException;
import com.ershijin.model.PageResult;
import com.ershijin.model.entity.Menu;
import com.ershijin.model.entity.Role;
import com.ershijin.model.entity.User;
import com.ershijin.model.vo.UserChangePasswordVO;
import com.ershijin.model.vo.UserVO;
import com.ershijin.model.query.UserQuery;
import com.ershijin.model.dto.TreeNodeMenuDTO;
import com.ershijin.service.MenuService;
import com.ershijin.service.UserService;
import com.ershijin.util.UserUtils;
import com.ershijin.validation.groups.Save;
import com.ershijin.validation.groups.Update;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;

    /**
     * 获取当前用户信息
     *
     * @return
     * @todo 根据当前用户生成菜单，重新组装返回结果格式,删除返回内容中的roles
     */
    @GetMapping("/info")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    public Map<String, Object> info() {
        UserDetails userDetails = UserUtils.getCurrentUser();
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        // 获取当前用户拥有的菜单树
        List<TreeNodeMenuDTO> menus = menuService.getMenuTree(user.getRoles());
        // 获取当前用户拥有的所有权限
        List<Menu> allMenus = user.getMenus();
        Set<String> permissions = new HashSet<>();
        allMenus.forEach(i -> {
            if (!StringUtils.isEmpty(i.getPermission())) {
                permissions.add(i.getPermission());
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("name", user.getName());
        map.put("avatar", user.getAvatar());
        map.put("menus", menus);
        map.put("permissions", permissions);

        return map;
    }

    /**
     * 根据用户id查询用户角色id列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/roleIds")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    public Set<Long> listRoleIds(@PathVariable Long userId) {
        List<Role> roles = userService.listRolesById(userId);
        if (roles == null) {
            return null;
        }
        Set<Long> roleIds = new HashSet<>();
        roles.forEach(role -> {
            roleIds.add(role.getId());
        });
        return roleIds;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:list')")
    @Log("用户列表")
    public PageResult list(UserQuery query, Page page) {
        return userService.list(query, page);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:remove')")
    @Log("删除用户")
    public void remove(@PathVariable Long id) {
        userService.removeById(id);
    }

    /**
     * 添加用户
     *
     * @param userForm
     * @return void
     */
    @PostMapping
    @PreAuthorize("hasAuthority('users:save')")
    @Log("添加用户")
    public void save(@Validated({Save.class}) @RequestBody UserVO userForm) {
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        if (userForm.getRoleIds() != null) {
            user.setRoles(new ArrayList<>());
            userForm.getRoleIds().forEach(roleId -> {
                Role role = new Role();
                role.setId(roleId);
                user.getRoles().add(role);
            });
        }
        userService.save(user);
    }

    /**
     * 修改用户
     *
     * @param userForm
     */
    @PutMapping
    @PreAuthorize("hasAuthority('users:update')")
    @Log("修改用户")
    public void update(@Validated({Update.class}) @RequestBody UserVO userForm) {
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        List<Role> roles = new ArrayList<>();
        if (userForm.getRoleIds() != null && !userForm.getRoleIds().isEmpty()) {
            userForm.getRoleIds().forEach(roleId -> {
                Role role = new Role();
                role.setId(roleId);
                roles.add(role);
            });
        }
        user.setRoles(roles);
        userService.update(user);
    }

    /**
     * 启用用户
     */
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('users:enable')")
    @Log("启用用户")
    public void enable(@Validated @PathVariable Long id) {
        userService.enableById(id);
    }

    /**
     * 禁用用户
     */
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('users:disable')")
    @Log("禁用用户")
    public void disable(@Validated @PathVariable Long id) {
        userService.disableById(id);
    }

    /**
     * 修改密码
     *
     * @param userChangePasswordForm
     * @param request
     * @return
     */
    @PostMapping("updatePassword")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    @Log("修改密码")
    public void updatePassword(@Validated @RequestBody UserChangePasswordVO userChangePasswordForm,
                               HttpServletRequest request) throws ApiException {
        String oldPassword = userChangePasswordForm.getOldPassword();
        String newPassword = userChangePasswordForm.getNewPassword();

        UserDetails currentUser = UserUtils.getCurrentUser();
        User oldUserInfo = (User) userService.loadUserByUsername(currentUser.getUsername());
        if (oldUserInfo == null) {
            throw new NotFoundException("用户信息不存在");
        }

        User user = new User();
        // 校验原密码是否正确
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, oldUserInfo.getPassword())) {
            throw new ApiException("原密码不正确！", 10000);
        }
        // 更新用户名和密码
        user.setId(oldUserInfo.getId());
        user.setPassword(newPassword);
        user.setEnabled(oldUserInfo.isEnabled());
        userService.updatePassword(user);
    }

    @PostMapping("updateAvatar")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    @Log("修改头像")
    public Map updateAvatar(MultipartFile avatar) throws ApiException, IOException {
        UserDetails currentUser = UserUtils.getCurrentUser();
        User oldUserInfo = (User) userService.loadUserByUsername(currentUser.getUsername());
        if (oldUserInfo == null) {
            throw new NotFoundException("用户信息不存在");
        }

        Map<String, Object> map = new HashMap<>();
        if (avatar.isEmpty()) {
            throw new ApiException("文件内容为空");
        }

        map.put("avatar", userService.saveAvatar(avatar, oldUserInfo));

        return map;
    }
}
