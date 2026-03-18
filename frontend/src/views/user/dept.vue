<template>
  <div class="dept-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" @click="handleAdd">新增部门</el-button>
        </div>
      </template>
      
      <!-- 数据表格 -->
      <el-table :data="deptList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="deptName" label="部门名称" />
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch 
              v-model="scope.row.status" 
              active-value="1" 
              inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="400px"
    >
      <el-form
        ref="deptForm"
        :model="deptForm"
        :rules="deptRules"
        label-width="100px"
      >
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="deptForm.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="deptForm.sort" :min="0" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="deptForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const deptList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const deptForm = ref({
  id: null,
  deptName: '',
  sort: 0,
  status: 1
})

const deptRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ]
}

const loadDeptList = async () => {
  try {
    const response = await request({
      url: '/user/dept/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      deptList.value = response.data.records
    } else {
      ElMessage.error('获取部门列表失败')
    }
  } catch (error) {
    ElMessage.error('获取部门列表失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增部门'
  deptForm.value = {
    id: null,
    deptName: '',
    sort: 0,
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑部门'
  deptForm.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const response = await request({
      url: '/user/dept/save',
      method: 'post',
      data: deptForm.value
    })
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadDeptList()
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这个部门吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await request({
        url: '/user/dept/delete',
        method: 'post',
        data: { id: row.id }
      })
      
      if (response.code === 200) {
        ElMessage.success('删除成功')
        loadDeptList()
      } else {
        ElMessage.error('删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleStatusChange = async (row) => {
  try {
    const response = await request({
      url: '/user/dept/save',
      method: 'post',
      data: row
    })
    
    if (response.code === 200) {
      ElMessage.success('状态更新成功')
    } else {
      ElMessage.error('状态更新失败')
      // 恢复原状态
      loadDeptList()
    }
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 恢复原状态
    loadDeptList()
  }
}

onMounted(() => {
  loadDeptList()
})
</script>

<style scoped>
.dept-management {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  text-align: right;
}
</style>
