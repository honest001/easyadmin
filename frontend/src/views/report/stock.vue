<template>
  <div class="report-stock">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>库存数据报表</span>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择商品分类">
            <el-option label="全部" value="" />
            <el-option v-for="category in categoryList" :key="category.id" :label="category.categoryName" :value="category.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.goodsName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button type="info" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 库存概览 -->
      <div class="stock-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>总库存量</span>
                </div>
              </template>
              <div class="data-value">{{ totalStock }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>库存总价值</span>
                </div>
              </template>
              <div class="data-value">¥{{ totalStockValue }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>低库存商品数</span>
                </div>
              </template>
              <div class="data-value">{{ lowStockCount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>无库存商品数</span>
                </div>
              </template>
              <div class="data-value">{{ zeroStockCount }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 库存趋势图表 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>库存趋势</span>
            </div>
          </template>
          <div id="stockTrendChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
      
      <!-- 库存分类分布 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>库存分类分布</span>
            </div>
          </template>
          <div id="stockCategoryChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
      
      <!-- 库存列表 -->
      <div class="stock-list">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>库存明细</span>
            </div>
          </template>
          <el-table :data="stockList" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="goodsName" label="商品名称" />
            <el-table-column prop="categoryName" label="商品分类" width="120" />
            <el-table-column prop="stock" label="库存量" width="100">
              <template #default="scope">
                <span :class="{ 'low-stock': scope.row.stock < 10, 'zero-stock': scope.row.stock === 0 }">
                  {{ scope.row.stock }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="unitPrice" label="单价" width="100" />
            <el-table-column prop="totalValue" label="库存价值" width="120" />
            <el-table-column prop="lastInTime" label="最后入库时间" width="180" />
            <el-table-column prop="lastOutTime" label="最后出库时间" width="180" />
          </el-table>
          
          <!-- 分页 -->
          <div class="pagination">
            <el-pagination
              v-model:current-page="pageNum"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { getStockReport } from '../../api/report'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const searchForm = ref({
  categoryId: '',
  goodsName: ''
})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalStock = ref(1500)
const totalStockValue = ref(50000.00)
const lowStockCount = ref(2)
const zeroStockCount = ref(1)
const categoryList = ref([
  { id: 1, categoryName: '电子产品' },
  { id: 2, categoryName: '办公用品' },
  { id: 3, categoryName: '生活用品' }
])
const stockList = ref([])

let stockTrendChart = null
let stockCategoryChart = null

const loadStockList = async () => {
  try {
    const response = await getStockReport()
    stockList.value = response.data.stockList
    total.value = response.data.total
    totalStock.value = response.data.totalStock
    totalStockValue.value = response.data.totalStockValue
    lowStockCount.value = response.data.lowStockCount
    zeroStockCount.value = response.data.zeroStockCount
    // 更新图表数据
    updateCharts(response.data)
  } catch (error) {
    ElMessage.error('获取库存列表失败')
  }
}

const updateCharts = (data) => {
  // 更新库存趋势图表
  stockTrendChart.setOption({
    xAxis: {
      data: data.stockTrend.map(item => item.date)
    },
    series: [
      {
        data: data.stockTrend.map(item => item.stock)
      }
    ]
  })
  
  // 更新库存分类分布图表
  stockCategoryChart.setOption({
    series: [
      {
        data: data.categoryDistribution.map(item => ({
          value: item.stock,
          name: item.categoryName
        }))
      }
    ]
  })
}

const initCharts = () => {
  // 库存趋势图表
  stockTrendChart = echarts.init(document.getElementById('stockTrendChart'))
  stockTrendChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [1200, 1300, 1400, 1500, 1450, 1400, 1350, 1300, 1250, 1200, 1150, 1100],
        type: 'line',
        smooth: true
      }
    ]
  })
  
  // 库存分类分布图表
  stockCategoryChart = echarts.init(document.getElementById('stockCategoryChart'))
  stockCategoryChart.setOption({
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
          { value: 105, name: '电子产品' },
          { value: 0, name: '办公用品' },
          { value: 200, name: '生活用品' }
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
  stockTrendChart?.resize()
  stockCategoryChart?.resize()
}

const handleSearch = () => {
  pageNum.value = 1
  loadStockList()
}

const resetForm = () => {
  searchForm.value = {
    categoryId: '',
    goodsName: ''
  }
  pageNum.value = 1
  loadStockList()
}

const handleExport = () => {
  ElMessage.success('导出成功')
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadStockList()
}

const handleCurrentChange = (current) => {
  pageNum.value = current
  loadStockList()
}

onMounted(() => {
  loadStockList()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  stockTrendChart?.dispose()
  stockCategoryChart?.dispose()
})
</script>

<style scoped>
.report-stock {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.stock-overview {
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

.stock-list {
  margin-top: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.low-stock {
  color: #E6A23C;
  font-weight: bold;
}

.zero-stock {
  color: #F56C6C;
  font-weight: bold;
}
</style>