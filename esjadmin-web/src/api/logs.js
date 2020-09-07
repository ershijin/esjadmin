import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/logs',
    method: 'get',
    params
  })
}

export function errorList(params) {
  return request({
    url: '/logs/error',
    method: 'get',
    params
  })
}

export function getErrorDetail(id) {
  return request({
    url: '/logs/error/' + id,
    method: 'get'
  })
}

export function getUserLogs(params) {
  return request({
    url: '/logs/user',
    method: 'get',
    params
  })
}
