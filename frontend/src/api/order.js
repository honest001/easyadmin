import request from '../utils/request'

// 订单管理
export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

export function saveOrder(data) {
  return request({
    url: '/order/save',
    method: 'post',
    data
  })
}

export function payOrder(data) {
  return request({
    url: '/order/pay',
    method: 'post',
    data
  })
}

export function cancelOrder(id) {
  return request({
    url: '/order/cancel',
    method: 'post',
    params: { id }
  })
}

export function getPayList(params) {
  return request({
    url: '/order/pay/list',
    method: 'get',
    params
  })
}