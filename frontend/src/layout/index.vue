<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="layout-aside">
      <div class="logo">
        <h1>EasyAdmin</h1>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="layout-menu"
        router
        :unique-opened="true"
      >
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <!-- 系统管理 -->
        <el-sub-menu index="sys">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/sys/user">用户管理</el-menu-item>
          <el-menu-item index="/sys/role">角色管理</el-menu-item>
          <el-menu-item index="/sys/config">系统配置</el-menu-item>
          <el-menu-item index="/sys/log">操作日志</el-menu-item>
        </el-sub-menu>
        
        <!-- 人员管理 -->
        <el-sub-menu index="user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>人员管理</span>
          </template>
          <el-menu-item index="/user/employee">员工管理</el-menu-item>
          <el-menu-item index="/user/dept">部门管理</el-menu-item>
          <el-menu-item index="/user/attendance">考勤管理</el-menu-item>
          <el-menu-item index="/user/salary">薪资管理</el-menu-item>
        </el-sub-menu>
        
        <!-- 审批模块 -->
        <el-sub-menu index="approve">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>审批模块</span>
          </template>
          <el-menu-item index="/approve/apply">发起审批</el-menu-item>
          <el-menu-item index="/approve/wait">待审批</el-menu-item>
          <el-menu-item index="/approve/my">我的审批</el-menu-item>
        </el-sub-menu>
        
        <!-- 货品库存 -->
        <el-sub-menu index="goods">
          <template #title>
            <el-icon><Goods /></el-icon>
            <span>货品库存</span>
          </template>
          <el-menu-item index="/goods/category">货品分类</el-menu-item>
          <el-menu-item index="/goods/supplier">供应商管理</el-menu-item>
          <el-menu-item index="/goods/list">货品管理</el-menu-item>
          <el-menu-item index="/goods/in">入库管理</el-menu-item>
          <el-menu-item index="/goods/out">出库管理</el-menu-item>
        </el-sub-menu>
        
        <!-- 订单管理 -->
        <el-menu-item index="/order/list">
          <el-icon><Tickets /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        
        <!-- 客户管理 -->
        <el-menu-item index="/customer/list">
          <el-icon><Avatar /></el-icon>
          <span>客户管理</span>
        </el-menu-item>
        
        <!-- 数据报表 -->
        <el-sub-menu index="report">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>数据报表</span>
          </template>
          <el-menu-item index="/report/business">经营报表</el-menu-item>
          <el-menu-item index="/report/stock">库存报表</el-menu-item>
          <el-menu-item index="/report/attendance">考勤报表</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container class="layout-main">
      <!-- 顶部导航 -->
      <el-header height="60px" class="layout-header">
        <div class="header-left">
          <el-icon @click="toggleCollapse" class="collapse-btn"><Menu /></el-icon>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar size="small">{{ userName }}</el-avatar>
              <span>{{ userName }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 内容区域 -->
      <el-main class="layout-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  House, Setting, User, Document, Goods, Tickets, Avatar, DataAnalysis, Menu 
} from '@element-plus/icons-vue'

const router = useRouter()
const isCollapse = ref(false)
const userName = ref('管理员')

const activeMenu = computed(() => {
  return router.currentRoute.value.path
})

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(() => {
  // 获取用户信息
  const user = localStorage.getItem('user')
  if (user) {
    userName.value = JSON.parse(user).realName || '管理员'
  }
})
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.layout-aside {
  background-color: #001529;
  color: #fff;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #1890ff;
}

.logo h1 {
  font-size: 18px;
  font-weight: bold;
  color: #1890ff;
}

.layout-menu {
  height: calc(100vh - 60px);
  border-right: none;
}

.layout-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.layout-header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.layout-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f0f2f5;
}
</style>
