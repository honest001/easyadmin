import request from '../utils/request'

// 客户管理
export function getCustomerList(params) {
  return request({
    url: '/customer/list',
    method: 'get',
    params
  })
}

export function saveCustomer(data) {
  return request({
    url: '/customer/save',
    method: 'post',
    data
  })
}

// 客户往来记录
export function saveCustomerRecord(data) {
  return request({
    url: '/customer/record/save',
    method: 'post',
    data
  })
}

export function getCustomerRecordList(params) {
  return request({
    url: '/customer/record/list',
    method: 'get',
    params
  })
}