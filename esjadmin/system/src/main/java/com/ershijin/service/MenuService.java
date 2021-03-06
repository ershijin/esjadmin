package com.ershijin.service;

import com.ershijin.constant.MenuType;
import com.ershijin.dao.MenuMapper;
import com.ershijin.model.entity.Menu;
import com.ershijin.model.entity.Role;
import com.ershijin.model.dto.TreeNodeMenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    public List<Menu> listAll() {
        return menuMapper.listAll();
    }
    public List<Menu> getByParentId(Integer parentId) {
        return menuMapper.listByParentId(parentId);
    }

    /**
     * 新增菜单
     * @param menu 菜单实体
     */
    public void save(Menu menu) {
        menuMapper.insert(menu);
    }

    /**
     * 获取全部菜单树
     * @return
     */
    public List<TreeNodeMenuDTO> getMenuTree() {
        List<TreeNodeMenuDTO> menuTree = new ArrayList<>();
        // 获取所有菜单
        List<TreeNodeMenuDTO> menus = menuMapper.listTreeNodeMenu();

        // 顶级菜单
        for (Iterator<TreeNodeMenuDTO> iterator = menus.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            if (menu.getParentId() == 0) {
                menuTree.add(menu);
                iterator.remove();
            }
        }
        // 获取子菜单
        for (Iterator<TreeNodeMenuDTO> iterator = menuTree.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            menu.setChildren(getMenuChildren(menu.getId(), menus, true));
        }
        return menuTree;
    }

    /**
     * 获取子菜单
     * @param parentId 父菜单id
     * @param menus 原始数据
     * @param all 是否返回全部可用菜单/按钮
     * @return
     */
    private List<TreeNodeMenuDTO> getMenuChildren(Long parentId, List<TreeNodeMenuDTO> menus, boolean all) {
        List<TreeNodeMenuDTO> children = new ArrayList<>();
        for (Iterator<TreeNodeMenuDTO> iterator = menus.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            if (menu.getParentId().equals(parentId)) {
                children.add(menu);
                iterator.remove();
            }
        }
        // 获取孙菜单
        for (Iterator<TreeNodeMenuDTO> iterator = children.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            menu.setChildren(getMenuChildren(menu.getId(), menus, true));
        }

        return children;
    }

    /**
     * 获取角色对应菜单树
     * @return
     */
    public List<TreeNodeMenuDTO> getMenuTree(List<Role> roles) {
        List<TreeNodeMenuDTO> menuTree = new ArrayList<>();
        // 获取所有菜单
        List<TreeNodeMenuDTO> menus;
//        if (roles == null) {
//            menus = menuMapper.listTreeNodeMenu();
//        } else {
//            if (roles.isEmpty()) return null;
//            menus= menuMapper.listTreeNodeMenuByRoles(roles);
//        }
        if (roles.isEmpty()) return null;
        menus= menuMapper.listTreeNodeMenuByRoles(roles);

        // 顶级菜单
        for (Iterator<TreeNodeMenuDTO> iterator = menus.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            if (menu.getParentId() == 0) {
                if (menu.getType() != MenuType.BUTTON) {
                    menuTree.add(menu);
                }
                iterator.remove();
            }
        }
        // 获取子菜单
        for (Iterator<TreeNodeMenuDTO> iterator = menuTree.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            menu.setChildren(getMenuChildren(menu.getId(), menus));
        }
        return menuTree;
    }

    private List<TreeNodeMenuDTO> getMenuChildren(Long parentId, List<TreeNodeMenuDTO> menus) {
        List<TreeNodeMenuDTO> children = new ArrayList<>();
        for (Iterator<TreeNodeMenuDTO> iterator = menus.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            if (menu.getParentId().equals(parentId)) {
                if (menu.getType() != MenuType.BUTTON) {
                    children.add(menu);
                }
                iterator.remove();
            }
        }
        // 获取孙菜单
        for (Iterator<TreeNodeMenuDTO> iterator = children.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            menu.setChildren(getMenuChildren(menu.getId(), menus));
        }

        return children;
    }

    public void update(Menu menu) {
        menuMapper.updateById(menu);
    }

    public void enableById(Long id) {
        Menu menu = new Menu();
        menu.setId(id);
        menuMapper.enable(menu);
    }

    public void disableById(Long id) {
        Menu menu = new Menu();
        menu.setId(id);
        menuMapper.disable(menu);
    }

    public void removeById(Long id) {
        // 查询出所有的子菜单id
        // 获取所有菜单
        List<TreeNodeMenuDTO> menus = menuMapper.listTreeNodeMenu();
        Set<Long> ids = new HashSet<>();
        ids.add(id);
        ids.addAll(getTreeNodeMenuIds(getMenuChildren(id, menus)));
        menuMapper.removeByIds(ids);

    }
    /**
     * 获取菜单所有的id
     */
    private Set<Long> getTreeNodeMenuIds(List<TreeNodeMenuDTO> menus) {
        Set<Long> ids = new HashSet<>();
        for (Iterator<TreeNodeMenuDTO> iterator = menus.iterator(); iterator.hasNext();) {
            TreeNodeMenuDTO menu = iterator.next();
            ids.add(menu.getId());
            if (!menu.getChildren().isEmpty()) {
                ids.addAll(getTreeNodeMenuIds(menu.getChildren()));
            }
        }
        return ids;
    }
}
