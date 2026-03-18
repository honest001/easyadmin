import request from '../utils/request'

// 系统配置
export function getSysConfig() {
  return request({
    url: '/sys/config/info',
    method: 'get'
  })
}

export function updateSysConfig(data) {
  return request({
    url: '/sys/config/update',
    method: 'post',
    data
  })
}

// 系统用户
export function getUserList(params) {
  return request({
    url: '/sys/user/list',
    method: 'get',
    params
  })
}

export function saveUser(data) {
  return request({
    url: '/sys/user/save',
    method: 'post',
    data
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: '/sys/user/updateStatus',
    method: 'post',
    params: { id, status }
  })
}

// 角色管理
export function getRoleList() {
  return request({
    url: '/sys/role/list',
    method: 'get'
  })
}

// 操作日志
export function getLogList(params) {
  return request({
    url: '/sys/log/list',
    method: 'get',
    params
  })
}

// 数据备份
export function backupNow() {
  return request({
    url: '/sys/backup/now',
    method: 'get'
  })
}

// 修改密码
export function updatePwd(data) {
  return request({
    url: '/sys/user/updatePwd',
    method: 'post',
    data
  })
}