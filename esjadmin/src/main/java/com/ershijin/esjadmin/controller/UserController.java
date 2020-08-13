package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.model.Result;
import com.ershijin.esjadmin.model.entity.Menu;
import com.ershijin.esjadmin.model.entity.Role;
import com.ershijin.esjadmin.model.entity.User;
import com.ershijin.esjadmin.model.form.IdForm;
import com.ershijin.esjadmin.model.form.UserForm;
import com.ershijin.esjadmin.model.query.UserQuery;
import com.ershijin.esjadmin.model.vo.TreeNodeMenu;
import com.ershijin.esjadmin.service.MenuService;
import com.ershijin.esjadmin.service.UserService;
import com.ershijin.esjadmin.util.UserUtils;
import com.ershijin.esjadmin.validation.groups.Save;
import com.ershijin.esjadmin.validation.groups.Update;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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
     * @return
     * @todo 根据当前用户生成菜单，重新组装返回结果格式,删除返回内容中的roles
     */
    @GetMapping("info")
    public Result info() {
        UserDetails userDetails = UserUtils.getCurrentUser();
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        // 获取当前用户拥有的菜单树
        List<TreeNodeMenu> menus = menuService.getMenuTree(user.getRoles());
        // 获取当前用户拥有的所有权限
        List<Menu> allMenus = user.getMenus();
        Set<String> permissions = new HashSet<>();
        allMenus.forEach(i -> {
            if (!StringUtils.isEmpty(i.getPermission())) {
                permissions.add(i.getPermission());
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("avatar", user.getAvatar());
        map.put("menus", menus);
        map.put("permissions", permissions);

        return Result.success(map);
    }

    /**
     * 查询单个用户
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("get")
    public Result get(@Validated @NotNull Long id) {
        return Result.success(userService.get(id));
    }

    /**
     * 根据用户id查询用户角色id列表
     * @param userId
     * @return
     */
    @GetMapping("list_role_ids")
    public Result listRoleIds(@RequestParam("user_id") Long userId) {
        List<Role> roles = userService.listRolesById(userId);
        if (roles == null) {
            return null;
        }
        Set<Long> roleIds = new HashSet<>();
        roles.forEach(role -> {
            roleIds.add(role.getId());
        });
        return Result.success(roleIds);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('users:list')")
    public Result list(int page, int pageSize, UserQuery query) {
        return Result.success(userService.list(page, pageSize, query));
    }

    /**
     * 删除用户
     * @param idDTO
     * @throws Exception
     */
    @PostMapping("remove")
    @PreAuthorize("hasAuthority('users:remove')")
    public Result remove(@Validated @RequestBody IdForm idDTO) {
        userService.removeById(idDTO.getId());
        return Result.success();
    }

    /**
     * 添加用户
     * @param userForm
     * @return void
     */
    @PostMapping("save")
    @PreAuthorize("hasAuthority('users:save')")
    public Result save(@Validated({Save.class}) @RequestBody UserForm userForm) {
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        if (userForm.getRoleIds() != null) {
            user.setRoles(new ArrayList<>());
            userForm.getRoleIds().forEach(roleId -> {
                Role role = new Role();
                role.setId(roleId);
                role.setCreateTime(new Date());
                user.getRoles().add(role);
            });
        }
        userService.save(user);
        return Result.success();
    }

    /**
     * 修改用户
     * @param userForm
     */
    @PostMapping("update")
    @PreAuthorize("hasAuthority('users:update')")
    public Result update(@Validated({Update.class}) @RequestBody UserForm userForm) {
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
        return Result.success();
    }

    /**
     * 启用用户
     * @param idDTO
     */
    @PostMapping("enable")
    @PreAuthorize("hasAuthority('users:enable')")
    public Result enable(@Validated @RequestBody IdForm idDTO) {
        userService.enableById(idDTO.getId());
        return Result.success();
    }

    /**
     * 禁用用户
     * @param idDTO
     */
    @PostMapping("disable")
    @PreAuthorize("hasAuthority('users:disable')")
    public Result disable(@Validated @RequestBody IdForm idDTO) {
        userService.disableById(idDTO.getId());
        return Result.success();
    }

}
