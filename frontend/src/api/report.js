import request from '../utils/request'

// 数据报表
export function getBusinessReport(params) {
  return request({
    url: '/report/business',
    method: 'get',
    params
  })
}

export function getStockReport() {
  return request({
    url: '/report/stock',
    method: 'get'
  })
}

export function getAttendanceReport(params) {
  return request({
    url: '/report/attendance',
    method: 'get',
    params
  })
}

export function exportReport(params) {
  return request({
    url: '/report/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}