<template>
  <div class="attendance-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考勤管理</span>
          <el-button type="primary" @click="handleCheckIn">打卡</el-button>
        </div>
      </template>
      
      <!-- 月度考勤统计 -->
      <div class="stat-section">
        <el-form :inline="true" :model="statForm">
          <el-form-item label="月份">
            <el-date-picker v-model="statForm.salaryMonth" type="month" placeholder="选择月份" style="width: 200px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleStat">统计</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 考勤统计结果 -->
      <div v-if="attendanceStat" class="stat-result">
        <el-card>
          <template #header>
            <span>{{ statForm.salaryMonth }} 考勤统计</span>
          </template>
          <div class="stat-info">
            <div class="stat-item">
              <span class="stat-label">出勤天数：</span>
              <span class="stat-value">{{ attendanceStat.workDays }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">正常次数：</span>
              <span class="stat-value">{{ attendanceStat.normalCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">迟到次数：</span>
              <span class="stat-value">{{ attendanceStat.lateCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">早退次数：</span>
              <span class="stat-value">{{ attendanceStat.earlyCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">缺卡次数：</span>
              <span class="stat-value">{{ attendanceStat.absentCount }}</span>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 打卡记录 -->
      <div class="record-section">
        <h3>打卡记录</h3>
        <el-table :data="attendanceList" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="checkDate" label="打卡日期" width="120" />
          <el-table-column prop="checkTime" label="打卡时间" width="150" />
          <el-table-column prop="checkType" label="打卡状态" width="100">
            <template #default="scope">
              <span v-if="scope.row.checkType === 1" class="status-normal">正常</span>
              <span v-else-if="scope.row.checkType === 2" class="status-late">迟到</span>
              <span v-else-if="scope.row.checkType === 3" class="status-early">早退</span>
              <span v-else class="status-absent">缺卡</span>
            </template>
          </el-table-column>
          <el-table-column prop="checkIp" label="打卡IP" width="150" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const statForm = ref({
  salaryMonth: new Date()
})
const attendanceStat = ref(null)
const attendanceList = ref([])

const handleCheckIn = async () => {
  try {
    const response = await request({
      url: '/user/attendance/checkIn',
      method: 'post'
    })
    
    if (response.code === 200) {
      ElMessage.success('打卡成功')
      loadAttendanceList()
    } else {
      ElMessage.error('打卡失败')
    }
  } catch (error) {
    ElMessage.error('打卡失败')
  }
}

const handleStat = async () => {
  try {
    const salaryMonth = statForm.value.salaryMonth
    const formattedMonth = salaryMonth.getFullYear() + '-' + String(salaryMonth.getMonth() + 1).padStart(2, '0')
    
    const response = await request({
      url: '/user/attendance/stat',
      method: 'get',
      params: { salaryMonth: formattedMonth }
    })
    
    if (response.code === 200) {
      attendanceStat.value = response.data
    } else {
      ElMessage.error('统计失败')
    }
  } catch (error) {
    ElMessage.error('统计失败')
  }
}

const loadAttendanceList = async () => {
  try {
    const response = await request({
      url: '/user/attendance/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      attendanceList.value = response.data.records
    } else {
      ElMessage.error('获取打卡记录失败')
    }
  } catch (error) {
    ElMessage.error('获取打卡记录失败')
  }
}

onMounted(() => {
  loadAttendanceList()
  handleStat()
})
</script>

<style scoped>
.attendance-management {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-section {
  margin-bottom: 20px;
}

.stat-result {
  margin-bottom: 20px;
}

.stat-info {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.stat-label {
  font-weight: bold;
}

.stat-value {
  font-size: 18px;
  color: #1890ff;
}

.record-section {
  margin-top: 20px;
}

.record-section h3 {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: bold;
}

.status-normal {
  color: #67c23a;
}

.status-late {
  color: #e6a23c;
}

.status-early {
  color: #409eff;
}

.status-absent {
  color: #f56c6c;
}
</style>
