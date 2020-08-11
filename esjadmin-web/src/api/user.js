import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/users/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

export function listRoleIds(params) {
  return request({
    url: '/users/list_role_ids',
    method: 'get',
    params
  })
}
export function list(params) {
  return request({
    url: '/users/list',
    method: 'get',
    params
  })
}

export function save(data) {
  return request({
    url: '/users/save',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/users/update',
    method: 'post',
    data
  })
}

export function enable(data) {
  return request({
    url: '/users/enable',
    method: 'post',
    data
  })
}
export function disable(data) {
  return request({
    url: '/users/disable',
    method: 'post',
    data
  })
}
export function remove(data) {
  return request({
    url: '/users/remove',
    method: 'post',
    data
  })
}
