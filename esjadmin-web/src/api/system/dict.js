import request from '@/utils/request'

export function getDicts() {
  return request({
    url: 'dict/all',
    method: 'get'
  })
}

export function save(data) {
  return request({
    url: 'dict',
    method: 'post',
    data
  })
}

export function remove(ids) {
  return request({
    url: 'dict/',
    method: 'delete',
    data: ids
  })
}

export function update(data) {
  return request({
    url: 'dict',
    method: 'put',
    data
  })
}

export default { save, update, remove }
