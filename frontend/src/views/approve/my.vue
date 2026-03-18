<template>
  <div class="approve-my">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我发起的审批</span>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="审批类型">
            <el-select v-model="searchForm.approveType" placeholder="请选择审批类型">
              <el-option label="全部" value="" />
              <el-option label="请假" value="1" />
              <el-option label="报销" value="2" />
              <el-option label="采购" value="3" />
              <el-option label="通用" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="审批状态">
            <el-select v-model="searchForm.approveStatus" placeholder="请选择审批状态">
              <el-option label="全部" value="" />
              <el-option label="待审核" value="0" />
              <el-option label="已通过" value="1" />
              <el-option label="已驳回" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="approveList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="approveNo" label="审批单号" />
        <el-table-column prop="approveType" label="审批类型" width="100">
          <template #default="scope">
            <span v-if="scope.row.approveType === 1">请假</span>
            <span v-else-if="scope.row.approveType === 2">报销</span>
            <span v-else-if="scope.row.approveType === 3">采购</span>
            <span v-else-if="scope.row.approveType === 4">通用</span>
          </template>
        </el-table-column>
        <el-table-column prop="approveTitle" label="审批标题" />
        <el-table-column prop="approveStatus" label="审批状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.approveStatus === 0" type="info">待审核</el-tag>
            <el-tag v-else-if="scope.row.approveStatus === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="scope.row.approveStatus === 2" type="danger">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approveUser" label="审批人" width="120" />
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column prop="approveTime" label="审批时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
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
    
    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="审批详情"
      width="600px"
    >
      <el-form :model="currentApprove" label-width="100px">
        <el-form-item label="审批单号">
          <span>{{ currentApprove.approveNo }}</span>
        </el-form-item>
        <el-form-item label="审批类型">
          <span v-if="currentApprove.approveType === 1">请假</span>
          <span v-else-if="currentApprove.approveType === 2">报销</span>
          <span v-else-if="currentApprove.approveType === 3">采购</span>
          <span v-else-if="currentApprove.approveType === 4">通用</span>
        </el-form-item>
        <el-form-item label="审批标题">
          <span>{{ currentApprove.approveTitle }}</span>
        </el-form-item>
        <el-form-item label="审批内容">
          <span>{{ currentApprove.approveContent }}</span>
        </el-form-item>
        <el-form-item label="审批人">
          <span>{{ currentApprove.approveUser }}</span>
        </el-form-item>
        <el-form-item label="审批状态">
          <el-tag v-if="currentApprove.approveStatus === 0" type="info">待审核</el-tag>
          <el-tag v-else-if="currentApprove.approveStatus === 1" type="success">已通过</el-tag>
          <el-tag v-else-if="currentApprove.approveStatus === 2" type="danger">已驳回</el-tag>
        </el-form-item>
        <el-form-item label="审批意见">
          <span>{{ currentApprove.approveOpinion || '无' }}</span>
        </el-form-item>
        <el-form-item label="申请时间">
          <span>{{ currentApprove.applyTime }}</span>
        </el-form-item>
        <el-form-item label="审批时间">
          <span>{{ currentApprove.approveTime || '未审批' }}</span>
        </el-form-item>
        <el-form-item label="附件">
          <el-button v-if="currentApprove.filePath" type="link">查看附件</el-button>
          <span v-else>无</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyApproveList } from '../../api/approve'

const approveList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  approveType: '',
  approveStatus: ''
})
const viewDialogVisible = ref(false)
const currentApprove = ref({})

const loadApproveList = async () => {
  try {
    const response = await getMyApproveList({
      ...searchForm.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    approveList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取我发起的审批列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadApproveList()
}

const resetForm = () => {
  searchForm.value = {
    approveType: '',
    approveStatus: ''
  }
  handleSearch()
}

const handleView = (row) => {
  currentApprove.value = { ...row }
  viewDialogVisible.value = true
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadApproveList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadApproveList()
}

onMounted(() => {
  loadApproveList()
})
</script>

<style scoped>
.approve-my {
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

.dialog-footer {
  text-align: right;
}
</style>