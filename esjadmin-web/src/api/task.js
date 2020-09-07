import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/taskJobs',
    method: 'get',
    params
  })
}

export function execute(id) {
  return request({
    url: '/taskJobs/' + id + '/execute',
    method: 'post'
  })
}

export function save(data) {
  return request({
    url: '/taskJobs',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/taskJobs',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/taskJobs',
    method: 'delete',
    data
  })
}
export function enable(id) {
  return request({
    url: '/taskJobs/' + id + '/enable',
    method: 'post'
  })
}
export function disable(id) {
  return request({
    url: '/taskJobs/' + id + '/disable',
    method: 'post'
  })
}

export function listLog(params) {
  return request({
    url: '/taskJobs/logs',
    method: 'get',
    params
  })
}

export default { list, execute, save, update, remove, enable, disable, listLog }
