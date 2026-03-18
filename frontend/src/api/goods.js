import request from '../utils/request'

// 货品分类
export function getCategoryList() {
  return request({
    url: '/goods/category/list',
    method: 'get'
  })
}

export function saveCategory(data) {
  return request({
    url: '/goods/category/save',
    method: 'post',
    data
  })
}

// 供应商管理
export function getSupplierList() {
  return request({
    url: '/goods/supplier/list',
    method: 'get'
  })
}

export function saveSupplier(data) {
  return request({
    url: '/goods/supplier/save',
    method: 'post',
    data
  })
}

// 货品管理
export function getGoodsList(params) {
  return request({
    url: '/goods/list',
    method: 'get',
    params
  })
}

export function saveGoods(data) {
  return request({
    url: '/goods/save',
    method: 'post',
    data
  })
}

// 入库管理
export function saveGoodsIn(data) {
  return request({
    url: '/goods/in/save',
    method: 'post',
    data
  })
}

export function getGoodsInList(params) {
  return request({
    url: '/goods/in/list',
    method: 'get',
    params
  })
}

// 出库管理
export function saveGoodsOut(data) {
  return request({
    url: '/goods/out/save',
    method: 'post',
    data
  })
}

export function getGoodsOutList(params) {
  return request({
    url: '/goods/out/list',
    method: 'get',
    params
  })
}

// 库存查询
export function getStockList() {
  return request({
    url: '/goods/stock/list',
    method: 'get'
  })
}

// 出入库明细
export function getRecordList(params) {
  return request({
    url: '/goods/record/list',
    method: 'get',
    params
  })
}