import request from '@/utils/request'

export function getConfigs() {
  return request({
    url: '/configs',
    method: 'get'
  })
}

