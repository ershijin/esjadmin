import request from '@/utils/request'

export function getConfigs() {
  return request({
    url: '/configs/kv',
    method: 'get'
  })
}

export function getList(params) {
  return request({
    url: '/configs',
    method: 'get',
    params
  })
}

export function save(data) {
  return request({
    url: '/configs',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/configs',
    method: 'put',
    data
  })
}

export function remove(id) {
  return request({
    url: '/configs/' + id,
    method: 'delete'
  })
}

export default { getConfigs, getList, save, update, remove }
