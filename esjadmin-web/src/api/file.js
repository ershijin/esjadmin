import request from '@/utils/request'

export function uploadImage(data) {
  return request({
    url: '/files/images',
    method: 'post',
    data
  })
}
