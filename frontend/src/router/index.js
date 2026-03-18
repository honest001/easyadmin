import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('../layout/index.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/home/index.vue'),
        meta: { title: '首页' }
      },
      // 系统管理
      {
        path: 'sys/user',
        name: 'SysUser',
        component: () => import('../views/sys/user.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'sys/role',
        name: 'SysRole',
        component: () => import('../views/sys/role.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'sys/config',
        name: 'SysConfig',
        component: () => import('../views/sys/config.vue'),
        meta: { title: '系统配置' }
      },
      {
        path: 'sys/log',
        name: 'SysLog',
        component: () => import('../views/sys/log.vue'),
        meta: { title: '操作日志' }
      },
      // 人员管理
      {
        path: 'user/employee',
        name: 'Employee',
        component: () => import('../views/user/employee.vue'),
        meta: { title: '员工管理' }
      },
      {
        path: 'user/dept',
        name: 'Dept',
        component: () => import('../views/user/dept.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'user/attendance',
        name: 'Attendance',
        component: () => import('../views/user/attendance.vue'),
        meta: { title: '考勤管理' }
      },
      {
        path: 'user/salary',
        name: 'Salary',
        component: () => import('../views/user/salary.vue'),
        meta: { title: '薪资管理' }
      },
      // 审批模块
      {
        path: 'approve/apply',
        name: 'ApproveApply',
        component: () => import('../views/approve/apply.vue'),
        meta: { title: '发起审批' }
      },
      {
        path: 'approve/wait',
        name: 'ApproveWait',
        component: () => import('../views/approve/wait.vue'),
        meta: { title: '待审批' }
      },
      {
        path: 'approve/my',
        name: 'ApproveMy',
        component: () => import('../views/approve/my.vue'),
        meta: { title: '我的审批' }
      },
      {
        path: 'approve/finish',
        name: 'ApproveFinish',
        component: () => import('../views/approve/finish.vue'),
        meta: { title: '已完结审批' }
      },
      // 货品库存
      {
        path: 'goods/category',
        name: 'GoodsCategory',
        component: () => import('../views/goods/category.vue'),
        meta: { title: '货品分类' }
      },
      {
        path: 'goods/supplier',
        name: 'GoodsSupplier',
        component: () => import('../views/goods/supplier.vue'),
        meta: { title: '供应商管理' }
      },
      {
        path: 'goods/index',
        name: 'GoodsIndex',
        component: () => import('../views/goods/index.vue'),
        meta: { title: '货品管理' }
      },
      {
        path: 'goods/in',
        name: 'GoodsIn',
        component: () => import('../views/goods/in.vue'),
        meta: { title: '入库管理' }
      },
      {
        path: 'goods/out',
        name: 'GoodsOut',
        component: () => import('../views/goods/out.vue'),
        meta: { title: '出库管理' }
      },
      {
        path: 'goods/stock',
        name: 'GoodsStock',
        component: () => import('../views/goods/stock.vue'),
        meta: { title: '库存查询' }
      },
      {
        path: 'goods/record',
        name: 'GoodsRecord',
        component: () => import('../views/goods/record.vue'),
        meta: { title: '出入库明细' }
      },
      // 订单管理
      {
        path: 'order/index',
        name: 'OrderIndex',
        component: () => import('../views/order/index.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'order/pay',
        name: 'OrderPay',
        component: () => import('../views/order/pay.vue'),
        meta: { title: '订单收款' }
      },
      // 客户管理
      {
        path: 'customer/index',
        name: 'CustomerIndex',
        component: () => import('../views/customer/index.vue'),
        meta: { title: '客户管理' }
      },
      {
        path: 'customer/record',
        name: 'CustomerRecord',
        component: () => import('../views/customer/record.vue'),
        meta: { title: '客户往来记录' }
      },
      // 数据报表
      {
        path: 'report/business',
        name: 'ReportBusiness',
        component: () => import('../views/report/business.vue'),
        meta: { title: '经营报表' }
      },
      {
        path: 'report/stock',
        name: 'ReportStock',
        component: () => import('../views/report/stock.vue'),
        meta: { title: '库存报表' }
      },
      {
        path: 'report/attendance',
        name: 'ReportAttendance',
        component: () => import('../views/report/attendance.vue'),
        meta: { title: '考勤报表' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - EasyAdmin` : 'EasyAdmin'
  
  // 登录验证
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
