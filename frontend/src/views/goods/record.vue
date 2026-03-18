<template>
  <div class="goods-record">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>出入库明细</span>
          <el-button type="primary" @click="handleExport">导出</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="记录类型">
            <el-select v-model="searchForm.recordType" placeholder="请选择记录类型">
              <el-option label="全部" value="" />
              <el-option label="入库" value="in" />
              <el-option label="出库" value="out" />
            </el-select>
          </el-form-item>
          <el-form-item label="货品名称">
            <el-input v-model="searchForm.goodsName" placeholder="请输入货品名称" />
          </el-form-item>
          <el-form-item label="操作时间">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="recordList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="recordType" label="记录类型" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.recordType === 'in'" type="success">入库</el-tag>
            <el-tag v-else-if="scope.row.recordType === 'out'" type="danger">出库</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="单据号" />
        <el-table-column prop="goodsName" label="货品名称" />
        <el-table-column prop="goodsSpec" label="规格" />
        <el-table-column prop="goodsNum" label="数量" width="100" />
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column prop="operateTime" label="操作时间" width="180" />
        <el-table-column prop="remark" label="备注" />
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

const recordList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  recordType: '',
  goodsName: ''
})
const dateRange = ref([])

const loadRecordList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      recordType: searchForm.value.recordType,
      goodsName: searchForm.value.goodsName
    }
    
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    
    const response = await request({
      url: '/goods/record/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      recordList.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error('获取出入库记录失败')
    }
  } catch (error) {
    ElMessage.error('获取出入库记录失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadRecordList()
}

const resetForm = () => {
  searchForm.value = {
    recordType: '',
    goodsName: ''
  }
  dateRange.value = []
  handleSearch()
}

const handleExport = () => {
  try {
    // 模拟导出
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadRecordList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadRecordList()
}

onMounted(() => {
  loadRecordList()
})
</script>

<style scoped>
.goods-record {
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