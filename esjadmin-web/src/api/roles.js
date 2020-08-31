import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/roles',
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
    url: '/roles',
    method: 'post',
    data
  })
}

/**
 * 获取角色权限菜单的id合集
 */
export function listMenuIds(id) {
  return request({
    url: '/roles/' + id + '/menuIds',
    method: 'get'
  })
}

/**
 * 更新角色
 * @param {*} data
 */
export function update(data) {
  return request({
    url: '/roles',
    method: 'put',
    data
  })
}

/**
 * 删除角色
 */
export function remove(params) {
  return request({
    url: '/roles',
    method: 'delete',
    params
  })
}
