import request from '@/utils/request'

export function getAllTable() {
  return request({
    url: 'generator/tables/all',
    method: 'get'
  })
}

export function generate(tableName) {
  return request({
    url: 'generator/' + tableName + '/generate',
    method: 'post'
  })
}
export function preview(tableName) {
  return request({
    url: 'generator/' + tableName + '/preview',
    method: 'post'
  })
}
export function download(tableName) {
  return request({
    url: 'generator/' + tableName + '/download',
    method: 'post',
    responseType: 'blob'
  })
}

export function save(data) {
  return request({
    url: 'generator',
    data,
    method: 'put'
  })
}

export function sync(tables) {
  return request({
    url: 'generator/sync',
    method: 'post',
    data: tables
  })
}

