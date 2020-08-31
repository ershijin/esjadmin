/* eslint-disable no-trailing-spaces */
import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/menus',
    method: 'get',
    params
  })
}

/**
 * 创建新菜单
 * @param {*} data
 */
export function insertMenu(data) {
  return request({
    url: '/menus',
    method: 'post',
    data
  })
}

/**
 * 更新菜单
 * @param {*} data
 */
export function updateMenu(data) {
  return request({
    url: '/menus',
    method: 'put',
    data
  })
}

/**
 * 启用菜单
 * @param {*} id
 */
export function enableMenu(id) {
  return request({
    url: '/menus/' + id + '/enable',
    method: 'post'
  })
}

/**
 * 禁用菜单
 * @param int id 
 */
export function disableMenu(id) {
  return request({
    url: '/menus/' + id + '/disable',
    method: 'post'
  })
}

/**
 * 删除菜单
 * @param int id 
 */
export function deleteMenu(id) {
  return request({
    url: '/menus/' + id,
    method: 'delete'
  })
}

/**
 * 获取菜单树
 * @param {*} params
 */
export function getMenuTree(params) {
  return request({
    url: '/menus/tree',
    method: 'get',
    params
  })
}
