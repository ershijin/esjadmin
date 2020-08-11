import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/articlecategories',
    method: 'get',
    params
  })
}

export function create(data) {
  return request({
    url: '/articlecategories',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/articlecategories/' + data.id,
    method: 'put',
    data
  })
}

export function remove(id) {
  return request({
    url: '/articlecategories/' + id,
    method: 'delete'
  })
}
