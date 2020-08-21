package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.model.entity.Menu;
import com.ershijin.esjadmin.model.vo.TreeNodeMenu;
import com.ershijin.esjadmin.service.MenuService;
import com.ershijin.esjadmin.validation.groups.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("list")
    @PreAuthorize("hasAuthority('menus:list')")
    List<Menu> listMenu() {
        return menuService.listAll();
    }

    @GetMapping("tree")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    List<TreeNodeMenu> menuTree() {
        return menuService.getMenuTree();
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('menus:save')")
    void save(@RequestBody Menu menu) {
        menuService.save(menu);
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('menus:update')")
    void update(@Validated({Update.class}) @RequestBody Menu menu) {
        menuService.update(menu);
    }

    /**
     * 启用菜单
     * @param id
     */
    @PostMapping("enable")
    @PreAuthorize("hasAuthority('menus:enable')")
    void enable(Long id) {
        menuService.enableById(id);
    }

    /**
     * 禁用菜单
     * @param id
     */
    @PostMapping("disable")
    @PreAuthorize("hasAuthority('menus:disable')")
    void disable(Long id) {
        menuService.disableById(id);
    }

    /**
     * 删除菜单
     * @param id
     */
    @PostMapping("remove")
    @PreAuthorize("hasAuthority('menus:remove')")
    void remove(Long id) {
        menuService.removeById(id);
    }
}
