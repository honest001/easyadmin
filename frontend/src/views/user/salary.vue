<template>
  <div class="salary-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>薪资管理</span>
        </div>
      </template>
      
      <!-- 月度薪资核算 -->
      <div class="stat-section">
        <el-form :inline="true" :model="statForm">
          <el-form-item label="月份">
            <el-date-picker v-model="statForm.salaryMonth" type="month" placeholder="选择月份" style="width: 200px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleStat">核算</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 薪资核算结果 -->
      <div v-if="salaryList.length > 0" class="salary-result">
        <el-table :data="salaryList" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="empName" label="员工姓名" />
          <el-table-column prop="baseSalary" label="基础薪资" width="120" />
          <el-table-column prop="workDays" label="出勤天数" width="100" />
          <el-table-column prop="realSalary" label="实发薪资" width="120" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <span v-if="scope.row.payStatus === 0" class="status-unpaid">未发放</span>
              <span v-else class="status-paid">已发放</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button v-if="scope.row.payStatus === 0" type="primary" size="small" @click="handlePay(scope.row)">发放</el-button>
            </template>
          </el-table-column>
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
const salaryList = ref([])

const handleStat = async () => {
  try {
    const salaryMonth = statForm.value.salaryMonth
    const formattedMonth = salaryMonth.getFullYear() + '-' + String(salaryMonth.getMonth() + 1).padStart(2, '0')
    
    const response = await request({
      url: '/user/salary/stat',
      method: 'get',
      params: { salaryMonth: formattedMonth }
    })
    
    if (response.code === 200) {
      salaryList.value = response.data
    } else {
      ElMessage.error('核算失败')
    }
  } catch (error) {
    ElMessage.error('核算失败')
  }
}

const handlePay = async (row) => {
  try {
    const response = await request({
      url: '/user/salary/pay',
      method: 'post',
      data: { id: row.id }
    })
    
    if (response.code === 200) {
      ElMessage.success('薪资发放成功')
      row.payStatus = 1
    } else {
      ElMessage.error('发放失败')
    }
  } catch (error) {
    ElMessage.error('发放失败')
  }
}

onMounted(() => {
  handleStat()
})
</script>

<style scoped>
.salary-management {
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

.status-unpaid {
  color: #f56c6c;
}

.status-paid {
  color: #67c23a;
}
</style>
