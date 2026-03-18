<template>
  <div class="approve-wait">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>待审批列表</span>
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
          <el-form-item label="申请人">
            <el-input v-model="searchForm.applyEmpName" placeholder="请输入申请人姓名" />
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
        <el-table-column prop="applyEmpName" label="申请人" width="120" />
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleApprove(scope.row, 1)">通过</el-button>
            <el-button type="danger" size="small" @click="handleApprove(scope.row, 2)">驳回</el-button>
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
    
    <!-- 审批对话框 -->
    <el-dialog
      v-model="approveDialogVisible"
      :title="approveDialogTitle"
      width="500px"
    >
      <el-form
        ref="approveForm"
        :model="approveForm"
        :rules="approveRules"
        label-width="100px"
      >
        <el-form-item label="审批意见" prop="approveOpinion">
          <el-input
            v-model="approveForm.approveOpinion"
            type="textarea"
            rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="approveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleApproveSubmit">提交</el-button>
        </span>
      </template>
    </el-dialog>
    
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
        <el-form-item label="申请人">
          <span>{{ currentApprove.applyEmpName }}</span>
        </el-form-item>
        <el-form-item label="申请时间">
          <span>{{ currentApprove.applyTime }}</span>
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
import { getWaitApproveList, operateApprove } from '../../api/approve'

const approveList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  approveType: '',
  applyEmpName: ''
})
const approveDialogVisible = ref(false)
const approveDialogTitle = ref('审批操作')
const approveForm = ref({
  approveOpinion: ''
})
const viewDialogVisible = ref(false)
const currentApprove = ref({})
const currentApproveId = ref(null)
const currentApproveStatus = ref(null)

const approveRules = {
  approveOpinion: [
    { required: true, message: '请输入审批意见', trigger: 'blur' }
  ]
}

const loadApproveList = async () => {
  try {
    const response = await getWaitApproveList({
      ...searchForm.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    approveList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取待审批列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadApproveList()
}

const resetForm = () => {
  searchForm.value = {
    approveType: '',
    applyEmpName: ''
  }
  handleSearch()
}

const handleApprove = (row, status) => {
  currentApproveId.value = row.id
  currentApproveStatus.value = status
  approveDialogTitle.value = status === 1 ? '审批通过' : '审批驳回'
  approveForm.value = {
    approveOpinion: ''
  }
  approveDialogVisible.value = true
}

const handleApproveSubmit = async () => {
  try {
    await operateApprove({
      id: currentApproveId.value,
      approveStatus: currentApproveStatus.value,
      approveOpinion: approveForm.value.approveOpinion
    })
    ElMessage.success('审批操作成功')
    approveDialogVisible.value = false
    loadApproveList()
  } catch (error) {
    ElMessage.error('审批操作失败')
  }
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
.approve-wait {
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