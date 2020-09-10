import request from '@/utils/request'
import Vue from 'vue'

export function uploadImage(data) {
  // 暂存 axios baseURL地址
  const baseURL = request.defaults.baseURL
  request.defaults.baseURL = Vue.prototype.$config.upload_api

  const result = request({
    url: '/images',
    method: 'post',
    data
  })
  // axios baseURL地址 改回去
  request.defaults.baseURL = baseURL
  return result
}
