import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/roles/list',
    method: 'get',
    params
  })
}

/**
 * 创建新角色
 * @param {*} data
 */
export function insert(data) {
  return request({
    url: '/roles/save',
    method: 'post',
    data
  })
}

/**
 * 获取角色权限菜单的id合集
 */
export function listMenuIds(params) {
  return request({
    url: '/roles/list_menu_ids',
    method: 'get',
    params
  })
}

/**
 * 更新角色
 * @param {*} data
 */
export function update(data) {
  return request({
    url: '/roles/update',
    method: 'post',
    data
  })
}

/**
 * 删除角色
 */
export function remove(params) {
  return request({
    url: '/roles/remove',
    method: 'post',
    params
  })
}
