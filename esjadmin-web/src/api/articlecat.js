import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/articleCategories',
    method: 'get',
    params
  })
}

export function create(data) {
  return request({
    url: '/articleCategories',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/articleCategories/' + data.id,
    method: 'put',
    data
  })
}

export function remove(id) {
  return request({
    url: '/articleCategories/' + id,
    method: 'delete'
  })
}
