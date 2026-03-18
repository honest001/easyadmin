<template>
  <div class="report-business">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>经营数据报表</span>
        </div>
      </template>
      
      <!-- 日期选择 -->
      <div class="date-select">
        <el-form :inline="true" :model="dateForm">
          <el-form-item label="统计周期">
            <el-select v-model="dateForm.period" placeholder="请选择统计周期">
              <el-option label="日" value="day" />
              <el-option label="周" value="week" />
              <el-option label="月" value="month" />
              <el-option label="年" value="year" />
            </el-select>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="dateForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button type="info" @click="handleExport">导出</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据概览 -->
      <div class="data-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>总销售额</span>
                </div>
              </template>
              <div class="data-value">¥{{ totalSales }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>总订单数</span>
                </div>
              </template>
              <div class="data-value">{{ totalOrders }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>平均客单价</span>
                </div>
              </template>
              <div class="data-value">¥{{ avgOrderAmount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>总收款额</span>
                </div>
              </template>
              <div class="data-value">¥{{ totalPayments }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 销售趋势图表 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
            </div>
          </template>
          <div id="salesChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
      
      <!-- 客户销售排行 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>客户销售排行</span>
            </div>
          </template>
          <div id="customerChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
      
      <!-- 商品销售排行 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>商品销售排行</span>
            </div>
          </template>
          <div id="productChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { getBusinessReport } from '../../api/report'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const dateForm = ref({
  period: 'month',
  dateRange: []
})
const totalSales = ref(15308.00)
const totalOrders = ref(3)
const avgOrderAmount = ref(5102.67)
const totalPayments = ref(9001.00)

let salesChart = null
let customerChart = null
let productChart = null

const initCharts = () => {
  // 销售趋势图表
  salesChart = echarts.init(document.getElementById('salesChart'))
  salesChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [
      {
        data: [12000, 19000, 30000, 25000, 36000, 45000, 42000, 50000, 55000, 60000, 65000, 70000],
        type: 'line',
        smooth: true,
        areaStyle: {}
      }
    ]
  })
  
  // 客户销售排行图表
  customerChart = echarts.init(document.getElementById('customerChart'))
  customerChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    yAxis: {
      type: 'category',
      data: ['客户C', '客户B', '客户A', '客户D']
    },
    series: [
      {
        data: [7998, 1309, 6001, 0],
        type: 'bar'
      }
    ]
  })
  
  // 商品销售排行图表
  productChart = echarts.init(document.getElementById('productChart'))
  productChart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: [
          { value: 6001, name: '商品A' },
          { value: 1309, name: '商品B' },
          { value: 7998, name: '商品C' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })
}

const handleResize = () => {
  salesChart?.resize()
  customerChart?.resize()
  productChart?.resize()
}

const handleQuery = async () => {
  try {
    const response = await getBusinessReport({
      statType: dateForm.value.period,
      statTime: dateForm.value.dateRange ? dateForm.value.dateRange.join(',') : ''
    })
    // 更新数据
    totalSales.value = response.data.totalSales
    totalOrders.value = response.data.totalOrders
    avgOrderAmount.value = response.data.avgOrderAmount
    totalPayments.value = response.data.totalPayments
    // 更新图表数据
    updateCharts(response.data)
    ElMessage.success('查询成功')
  } catch (error) {
    ElMessage.error('查询失败')
  }
}

const updateCharts = (data) => {
  // 更新销售趋势图表
  salesChart.setOption({
    xAxis: {
      data: data.salesTrend.map(item => item.date)
    },
    series: [
      {
        data: data.salesTrend.map(item => item.amount)
      }
    ]
  })
  
  // 更新客户销售排行图表
  customerChart.setOption({
    yAxis: {
      data: data.customerRanking.map(item => item.customerName)
    },
    series: [
      {
        data: data.customerRanking.map(item => item.amount)
      }
    ]
  })
  
  // 更新商品销售排行图表
  productChart.setOption({
    series: [
      {
        data: data.productRanking.map(item => ({
          value: item.amount,
          name: item.productName
        }))
      }
    ]
  })
}

const handleExport = () => {
  ElMessage.success('导出成功')
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  salesChart?.dispose()
  customerChart?.dispose()
  productChart?.dispose()
})
</script>

<style scoped>
.report-business {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.date-select {
  margin-bottom: 20px;
}

.data-overview {
  margin-bottom: 20px;
}

.data-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  margin-top: 10px;
}

.chart-container {
  margin-bottom: 20px;
}
</style>