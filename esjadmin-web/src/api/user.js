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

export function updatePassword(data) {
  return request({
    url: '/users/updatePassword',
    method: 'post',
    data
  })
}

export function listRoleIds(params) {
  return request({
    url: '/users/' + params.user_id + '/roleIds',
    method: 'get'
  })
}
export function list(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

export function save(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/users',
    method: 'put',
    data
  })
}

export function enable(data) {
  return request({
    url: '/users/' + data.id + '/enable',
    method: 'post'
  })
}
export function disable(data) {
  return request({
    url: '/users/' + data.id + '/disable',
    method: 'post'
  })
}
export function remove(data) {
  return request({
    url: '/users/' + data.id,
    method: 'delete'
  })
}
