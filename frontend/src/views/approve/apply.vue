<template>
  <div class="approve-apply">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>发起审批</span>
        </div>
      </template>
      
      <el-form
        ref="approveForm"
        :model="approveForm"
        :rules="approveRules"
        label-width="100px"
      >
        <el-form-item label="审批类型" prop="approveType">
          <el-select v-model="approveForm.approveType" placeholder="请选择审批类型">
            <el-option label="请假" value="1" />
            <el-option label="报销" value="2" />
            <el-option label="采购" value="3" />
            <el-option label="通用" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批标题" prop="approveTitle">
          <el-input v-model="approveForm.approveTitle" placeholder="请输入审批标题" />
        </el-form-item>
        <el-form-item label="审批内容" prop="approveContent">
          <el-input
            v-model="approveForm.approveContent"
            type="textarea"
            rows="4"
            placeholder="请输入审批内容"
          />
        </el-form-item>
        <el-form-item label="审批人" prop="approveUser">
          <el-select v-model="approveForm.approveUser" placeholder="请选择审批人">
            <el-option v-for="user in userList" :key="user.id" :label="user.realName" :value="user.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            class="upload-demo"
            action="#"
            :on-change="handleFileUpload"
            :auto-upload="false"
            multiple
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持上传多个文件
              </div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交审批</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { applyApprove } from '../../api/approve'
import { getUserList } from '../../api/sys'

const approveForm = ref({
  approveType: '',
  approveTitle: '',
  approveContent: '',
  approveUser: '',
  filePath: ''
})
const userList = ref([])

const approveRules = {
  approveType: [
    { required: true, message: '请选择审批类型', trigger: 'change' }
  ],
  approveTitle: [
    { required: true, message: '请输入审批标题', trigger: 'blur' }
  ],
  approveContent: [
    { required: true, message: '请输入审批内容', trigger: 'blur' }
  ],
  approveUser: [
    { required: true, message: '请选择审批人', trigger: 'change' }
  ]
}

const handleFileUpload = (file) => {
  // 模拟文件上传
  ElMessage.success('文件上传成功')
}

const handleSubmit = async () => {
  try {
    await applyApprove(approveForm.value)
    ElMessage.success('审批提交成功')
    // 重置表单
    approveForm.value = {
      approveType: '',
      approveTitle: '',
      approveContent: '',
      approveUser: '',
      filePath: ''
    }
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

onMounted(() => {
  // 加载用户列表
  loadUserList()
})

const loadUserList = async () => {
  try {
    const response = await getUserList()
    userList.value = response.data
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  }
}
</script>

<style scoped>
.approve-apply {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-demo {
  margin-bottom: 20px;
}
</style>
