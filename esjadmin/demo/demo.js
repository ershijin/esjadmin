import request from '@/utils/request'

export function save(data) {
  return request({
    url: 'demo',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: 'demo',
    method: 'put',
    data
  })
}

export function remove(ids) {
  return request({
    url: 'demo/',
    method: 'delete',
    data: ids
  })
}

export default { save, update, remove }
