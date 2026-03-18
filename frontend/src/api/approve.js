import request from '../utils/request'

// 审批管理
export function applyApprove(data) {
  return request({
    url: '/approve/apply',
    method: 'post',
    data
  })
}

export function getWaitApproveList() {
  return request({
    url: '/approve/wait/list',
    method: 'get'
  })
}

export function operateApprove(data) {
  return request({
    url: '/approve/operate',
    method: 'post',
    data
  })
}

export function getMyApproveList() {
  return request({
    url: '/approve/my/list',
    method: 'get'
  })
}

export function getFinishApproveList() {
  return request({
    url: '/approve/finish/list',
    method: 'get'
  })
}