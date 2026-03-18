<template>
  <div class="log-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="操作人">
            <el-input v-model="searchForm.operUser" placeholder="请输入操作人" />
          </el-form-item>
          <el-form-item label="操作模块">
            <el-input v-model="searchForm.operModule" placeholder="请输入操作模块" />
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker v-model="searchForm.startTime" type="date" placeholder="选择开始日期" />
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker v-model="searchForm.endTime" type="date" placeholder="选择结束日期" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="logList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operUser" label="操作人" />
        <el-table-column prop="operModule" label="操作模块" />
        <el-table-column prop="operType" label="操作类型" />
        <el-table-column prop="operContent" label="操作内容" />
        <el-table-column prop="operTime" label="操作时间" width="180" />
        <el-table-column prop="operIp" label="操作IP" width="150" />
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const logList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  operUser: '',
  operModule: '',
  startTime: null,
  endTime: null
})

const loadLogList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      operUser: searchForm.value.operUser,
      operModule: searchForm.value.operModule,
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime
    }
    
    const response = await request({
      url: '/sys/log/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      logList.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error('获取日志列表失败')
    }
  } catch (error) {
    ElMessage.error('获取日志列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadLogList()
}

const resetForm = () => {
  searchForm.value = {
    operUser: '',
    operModule: '',
    startTime: null,
    endTime: null
  }
  handleSearch()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadLogList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadLogList()
}

onMounted(() => {
  loadLogList()
})
</script>

<style scoped>
.log-management {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
