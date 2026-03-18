<template>
  <div class="customer-record">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户往来记录</span>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="客户名称">
          <el-select v-model="searchForm.customerId" placeholder="请选择客户">
            <el-option label="全部" value="" />
            <el-option v-for="customer in customerList" :key="customer.id" :label="customer.customerName" :value="customer.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录类型">
          <el-select v-model="searchForm.recordType" placeholder="请选择记录类型">
            <el-option label="全部" value="" />
            <el-option label="订单" value="1" />
            <el-option label="收款" value="2" />
            <el-option label="退款" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button type="info" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 往来记录列表 -->
      <el-table :data="recordList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="recordType" label="记录类型" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.recordType === 1" type="primary">订单</el-tag>
            <el-tag v-else-if="scope.row.recordType === 2" type="success">收款</el-tag>
            <el-tag v-else-if="scope.row.recordType === 3" type="danger">退款</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" />
        <el-table-column prop="recordNo" label="单据号" />
        <el-table-column prop="recordTime" label="记录时间" width="180" />
        <el-table-column prop="operatorName" label="操作人" width="120" />
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCustomerRecordList } from '../../api/customer'
import { getCustomerList } from '../../api/customer'
import { ElMessage } from 'element-plus'

const recordList = ref([])
const searchForm = ref({
  customerId: '',
  recordType: '',
  dateRange: []
})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const customerList = ref([])

const loadRecordList = async () => {
  try {
    const response = await getCustomerRecordList({
      ...searchForm.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    recordList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取往来记录失败')
  }
}

const loadCustomerList = async () => {
  try {
    const response = await getCustomerList()
    customerList.value = response.data.records
  } catch (error) {
    ElMessage.error('获取客户列表失败')
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadRecordList()
}

const resetForm = () => {
  searchForm.value = {
    customerId: '',
    recordType: '',
    dateRange: []
  }
  pageNum.value = 1
  loadRecordList()
}

const handleExport = () => {
  ElMessage.success('导出成功')
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadRecordList()
}

const handleCurrentChange = (current) => {
  pageNum.value = current
  loadRecordList()
}

onMounted(() => {
  loadCustomerList()
  loadRecordList()
})
</script>

<style scoped>
.customer-record {
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