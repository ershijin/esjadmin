import request from '@/utils/request'

export function listArticle(params) {
  return request({
    url: '/articles',
    method: 'get',
    params
  })
}

export function getArticle(id) {
  return request({
    url: '/articles/' + id,
    method: 'get'
  })
}

export function createArticle(data) {
  return request({
    url: '/articles',
    method: 'post',
    data
  })
}

export function updateArticle(id, data) {
  return request({
    url: '/articles/' + id,
    method: 'put',
    data
  })
}

export function removeArticle(id) {
  return request({
    url: '/articles/' + id,
    method: 'delete'
  })
}
