<template>
  <div class="home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用 EasyAdmin</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>中小微企业管理系统</h2>
        <p>一站式业务管理解决方案</p>
        <div class="stats">
          <el-statistic :value="stats.userCount" title="总用户数" />
          <el-statistic :value="stats.employeeCount" title="员工数" />
          <el-statistic :value="stats.orderCount" title="订单数" />
          <el-statistic :value="stats.customerCount" title="客户数" />
        </div>
      </div>
    </el-card>
    
    <div class="dashboard">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>销售趋势</span>
          </div>
        </template>
        <div class="chart-container">
          <div id="salesChart" style="width: 100%; height: 300px;"></div>
        </div>
      </el-card>
      
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>库存状态</span>
          </div>
        </template>
        <div class="chart-container">
          <div id="stockChart" style="width: 100%; height: 300px;"></div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import request from '../../utils/request'

const stats = ref({
  userCount: 0,
  employeeCount: 0,
  orderCount: 0,
  customerCount: 0
})

const salesChart = ref(null)
const stockChart = ref(null)

// 获取统计数据
const getStats = async () => {
  try {
    // 获取用户数量
    const userRes = await request.get('/sys/user/list', {
      params: { pageNum: 1, pageSize: 1 }
    })
    stats.value.userCount = userRes.data.total || 0
    
    // 获取员工数量
    const employeeRes = await request.get('/user/employee/list', {
      params: { pageNum: 1, pageSize: 1 }
    })
    stats.value.employeeCount = employeeRes.data.total || 0
    
    // 获取订单数量
    const orderRes = await request.get('/order/list', {
      params: { pageNum: 1, pageSize: 1 }
    })
    stats.value.orderCount = orderRes.data.total || 0
    
    // 获取客户数量
    const customerRes = await request.get('/customer/list', {
      params: { pageNum: 1, pageSize: 1 }
    })
    stats.value.customerCount = customerRes.data.total || 0
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取销售趋势数据
const getSalesData = async () => {
  try {
    const res = await request.get('/report/business', {
      params: {
        statType: 'month',
        statTime: new Date().getFullYear().toString()
      }
    })
    const data = res.data
    
    if (salesChart.value) {
      salesChart.value.setOption({
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.months || ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: data.sales || [120, 190, 300, 500, 200, 300],
          type: 'line',
          smooth: true
        }]
      })
    }
  } catch (error) {
    console.error('获取销售趋势数据失败:', error)
  }
}

// 获取库存状态数据
const getStockData = async () => {
  try {
    const res = await request.get('/report/stock')
    const data = res.data
    
    if (stockChart.value) {
      stockChart.value.setOption({
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [{
          name: '库存状态',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: data.status || [
            { value: 300, name: '正常库存' },
            { value: 50, name: '低库存' },
            { value: 20, name: '缺货' }
          ]
        }]
      })
    }
  } catch (error) {
    console.error('获取库存状态数据失败:', error)
  }
}

onMounted(async () => {
  // 初始化图表实例
  salesChart.value = echarts.init(document.getElementById('salesChart'))
  stockChart.value = echarts.init(document.getElementById('stockChart'))
  
  // 获取数据
  await getStats()
  await getSalesData()
  await getStockData()
  
  // 响应式处理
  window.addEventListener('resize', () => {
    salesChart.value?.resize()
    stockChart.value?.resize()
  })
})
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.welcome-content {
  text-align: center;
  padding: 20px 0;
}

.welcome-content h2 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #1890ff;
}

.welcome-content p {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.dashboard {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  height: calc(100% - 40px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

@media (max-width: 768px) {
  .dashboard {
    grid-template-columns: 1fr;
  }
  
  .stats {
    flex-direction: column;
    gap: 20px;
  }
}
</style>
