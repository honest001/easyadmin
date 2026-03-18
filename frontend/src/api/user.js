import request from '../utils/request'

// 部门管理
export function getDeptList() {
  return request({
    url: '/user/dept/list',
    method: 'get'
  })
}

export function saveDept(data) {
  return request({
    url: '/user/dept/save',
    method: 'post',
    data
  })
}

// 员工管理
export function getEmployeeList(params) {
  return request({
    url: '/user/employee/list',
    method: 'get',
    params
  })
}

export function saveEmployee(data) {
  return request({
    url: '/user/employee/save',
    method: 'post',
    data
  })
}

// 考勤管理
export function checkAttendance() {
  return request({
    url: '/user/attendance/check',
    method: 'post'
  })
}

export function getAttendanceStat(params) {
  return request({
    url: '/user/attendance/stat',
    method: 'get',
    params
  })
}

// 薪资管理
export function getSalaryStat(params) {
  return request({
    url: '/user/salary/stat',
    method: 'get',
    params
  })
}