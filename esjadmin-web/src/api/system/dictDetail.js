import request from '@/utils/request'

export function get(dictName) {
  const params = {
    dictName,
    page: 0,
    size: 9999
  }
  return request({
    url: 'dictDetail',
    method: 'get',
    params
  })
}

export function getDictMap(dictName) {
  const params = {
    dictName,
    page: 0,
    size: 9999
  }
  return request({
    url: 'dictDetail/map',
    method: 'get',
    params
  })
}

export function save(data) {
  return request({
    url: 'dictDetail',
    method: 'post',
    data
  })
}

export function remove(id) {
  return request({
    url: 'dictDetail/' + id,
    method: 'delete'
  })
}

export function update(data) {
  return request({
    url: 'dictDetail',
    method: 'put',
    data
  })
}

export default { save, remove, update }
