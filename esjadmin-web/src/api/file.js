import request from '@/utils/request'

export function removeFile(data) {
  return request({
    url: '/removefile',
    method: 'delete',
    data
  })
}
