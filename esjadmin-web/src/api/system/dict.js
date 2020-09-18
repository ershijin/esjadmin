import request from '@/utils/request'

export function getDicts() {
  return request({
    url: 'dict/all',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'dict',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'dict/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'dict',
    method: 'put',
    data
  })
}

export default { add, edit, del }
