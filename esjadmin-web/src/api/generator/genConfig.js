import request from '@/utils/request'

export function get(tableName) {
  return request({
    url: 'genConfig/' + tableName,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'genConfig',
    data,
    method: 'put'
  })
}
