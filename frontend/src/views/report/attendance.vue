<template>
  <div class="report-attendance">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考勤数据报表</span>
        </div>
      </template>
      
      <!-- 日期选择 -->
      <div class="date-select">
        <el-form :inline="true" :model="dateForm">
          <el-form-item label="部门">
            <el-select v-model="dateForm.deptId" placeholder="请选择部门">
              <el-option label="全部" value="" />
              <el-option v-for="dept in deptList" :key="dept.id" :label="dept.deptName" :value="dept.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="月份">
            <el-date-picker
              v-model="dateForm.month"
              type="month"
              placeholder="选择月份"
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button type="info" @click="handleExport">导出</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 考勤概览 -->
      <div class="attendance-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>总出勤天数</span>
                </div>
              </template>
              <div class="data-value">{{ totalWorkDays }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>迟到次数</span>
                </div>
              </template>
              <div class="data-value">{{ lateCount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>早退次数</span>
                </div>
              </template>
              <div class="data-value">{{ earlyLeaveCount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>缺勤次数</span>
                </div>
              </template>
              <div class="data-value">{{ absentCount }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 考勤趋势图表 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>考勤趋势</span>
            </div>
          </template>
          <div id="attendanceChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
      
      <!-- 部门考勤对比 -->
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>部门考勤对比</span>
            </div>
          </template>
          <div id="deptAttendanceChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </div>
      
      <!-- 考勤明细 -->
      <div class="attendance-list">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>考勤明细</span>
            </div>
          </template>
          <el-table :data="attendanceList" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="employeeName" label="员工姓名" />
            <el-table-column prop="deptName" label="部门" width="120" />
            <el-table-column prop="attendanceDate" label="日期" width="120" />
            <el-table-column prop="checkInTime" label="上班时间" width="150" />
            <el-table-column prop="checkOutTime" label="下班时间" width="150" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
                <el-tag v-else-if="scope.row.status === 2" type="warning">迟到</el-tag>
                <el-tag v-else-if="scope.row.status === 3" type="warning">早退</el-tag>
                <el-tag v-else-if="scope.row.status === 4" type="danger">缺勤</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
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
import { getAttendanceReport } from '../../api/report'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const dateForm = ref({
  deptId: '',
  month: new Date()
})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalWorkDays = ref(220)
const lateCount = ref(15)
const earlyLeaveCount = ref(8)
const absentCount = ref(5)
const deptList = ref([
  { id: 1, deptName: '技术部' },
  { id: 2, deptName: '销售部' },
  { id: 3, deptName: '财务部' },
  { id: 4, deptName: '行政部' }
])
const attendanceList = ref([])

let attendanceChart = null
let deptAttendanceChart = null

const loadAttendanceList = async () => {
  try {
    const response = await getAttendanceReport({
      salaryMonth: dateForm.value.month ? dateForm.value.month.toISOString().substring(0, 7) : '',
      deptId: dateForm.value.deptId
    })
    attendanceList.value = response.data.attendanceList
    total.value = response.data.total
    totalWorkDays.value = response.data.totalWorkDays
    lateCount.value = response.data.lateCount
    earlyLeaveCount.value = response.data.earlyLeaveCount
    absentCount.value = response.data.absentCount
    // 更新图表数据
    updateCharts(response.data)
  } catch (error) {
    ElMessage.error('获取考勤列表失败')
  }
}

const updateCharts = (data) => {
  // 更新考勤趋势图表
  attendanceChart.setOption({
    xAxis: {
      data: data.attendanceTrend.map(item => item.date)
    },
    series: [
      {
        name: '正常',
        data: data.attendanceTrend.map(item => item.normal)
      },
      {
        name: '迟到',
        data: data.attendanceTrend.map(item => item.late)
      },
      {
        name: '早退',
        data: data.attendanceTrend.map(item => item.earlyLeave)
      },
      {
        name: '缺勤',
        data: data.attendanceTrend.map(item => item.absent)
      }
    ]
  })
  
  // 更新部门考勤对比图表
  deptAttendanceChart.setOption({
    yAxis: {
      data: data.deptAttendance.map(item => item.deptName)
    },
    series: [
      {
        name: '正常',
        data: data.deptAttendance.map(item => item.normal)
      },
      {
        name: '迟到',
        data: data.deptAttendance.map(item => item.late)
      },
      {
        name: '早退',
        data: data.deptAttendance.map(item => item.earlyLeave)
      },
      {
        name: '缺勤',
        data: data.deptAttendance.map(item => item.absent)
      }
    ]
  })
}

const initCharts = () => {
  // 考勤趋势图表
  attendanceChart = echarts.init(document.getElementById('attendanceChart'))
  attendanceChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['1日', '2日', '3日', '4日', '5日', '6日', '7日', '8日', '9日', '10日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '正常',
        data: [10, 12, 11, 10, 12, 11, 10, 12, 11, 10],
        type: 'bar',
        itemStyle: {
          color: '#67C23A'
        }
      },
      {
        name: '迟到',
        data: [2, 1, 2, 1, 2, 1, 2, 1, 2, 1],
        type: 'bar',
        itemStyle: {
          color: '#E6A23C'
        }
      },
      {
        name: '早退',
        data: [1, 1, 0, 1, 1, 0, 1, 1, 0, 1],
        type: 'bar',
        itemStyle: {
          color: '#F56C6C'
        }
      },
      {
        name: '缺勤',
        data: [0, 0, 1, 0, 0, 1, 0, 0, 1, 0],
        type: 'bar',
        itemStyle: {
          color: '#909399'
        }
      }
    ]
  })
  
  // 部门考勤对比图表
  deptAttendanceChart = echarts.init(document.getElementById('deptAttendanceChart'))
  deptAttendanceChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: ['技术部', '销售部', '财务部', '行政部']
    },
    series: [
      {
        name: '正常',
        type: 'bar',
        stack: 'total',
        emphasis: {
          focus: 'series'
        },
        data: [45, 40, 35, 30],
        itemStyle: {
          color: '#67C23A'
        }
      },
      {
        name: '迟到',
        type: 'bar',
        stack: 'total',
        emphasis: {
          focus: 'series'
        },
        data: [5, 8, 3, 2],
        itemStyle: {
          color: '#E6A23C'
        }
      },
      {
        name: '早退',
        type: 'bar',
        stack: 'total',
        emphasis: {
          focus: 'series'
        },
        data: [2, 4, 1, 1],
        itemStyle: {
          color: '#F56C6C'
        }
      },
      {
        name: '缺勤',
        type: 'bar',
        stack: 'total',
        emphasis: {
          focus: 'series'
        },
        data: [1, 3, 1, 0],
        itemStyle: {
          color: '#909399'
        }
      }
    ]
  })
}

const handleResize = () => {
  attendanceChart?.resize()
  deptAttendanceChart?.resize()
}

const handleQuery = () => {
  ElMessage.success('查询成功')
  loadAttendanceList()
}

const handleExport = () => {
  ElMessage.success('导出成功')
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadAttendanceList()
}

const handleCurrentChange = (current) => {
  pageNum.value = current
  loadAttendanceList()
}

onMounted(() => {
  loadAttendanceList()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  attendanceChart?.dispose()
  deptAttendanceChart?.dispose()
})
</script>

<style scoped>
.report-attendance {
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

.attendance-overview {
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

.attendance-list {
  margin-top: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>