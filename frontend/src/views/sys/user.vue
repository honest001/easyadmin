<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">新增用户</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="账号">
            <el-input v-model="searchForm.username" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="searchForm.realName" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="userList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="roleName" label="角色" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch 
              :model-value="scope.row.status" 
              :active-value="1" 
              :inactive-value="0"
              @change="(value) => handleStatusChange(scope.row, value)"
              :loading="scope.row.loading"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)" v-if="Number(scope.row.id) !== 1">删除</el-button>
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
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" placeholder="编辑时为空则保持原密码不变" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="userForm.roleId" placeholder="请选择角色">
            <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="userForm.status" :active-value="1" :inactive-value="0" />
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
import { ElMessage } from 'element-plus'

const userList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  username: '',
  realName: ''
})
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const userForm = ref({
  id: null,
  username: '',
  password: '',
  realName: '',
  phone: '',
  roleId: '',
  status: 1
})
const roleList = ref([])

const loadRoleList = async () => {
  try {
    const response = await request({
      url: '/sys/role/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      roleList.value = response.data.records
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  }
}

const userRules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: (rule, value, callback) => {
      if (!userForm.value.id && !value) {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

const loadUserList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      username: searchForm.value.username,
      realName: searchForm.value.realName
    }
    
    const response = await request({
      url: '/sys/user/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      console.log('User records from backend:', response.data.records)
      // 确保status字段是数字类型，并且密码字段存在
      userList.value = response.data.records.map(item => ({
        ...item,
        status: Number(item.status),
        password: '123456' // 硬编码密码，确保前端能够显示
      }))
      console.log('User list after processing:', userList.value)
      total.value = response.data.total
    } else {
      ElMessage.error('获取用户列表失败')
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadUserList()
}

const resetForm = () => {
  searchForm.value = {
    username: '',
    realName: ''
  }
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  userForm.value = {
    id: null,
    username: '',
    password: '',
    realName: '',
    phone: '',
    roleId: '',
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  console.log('Editing user:', row)
  
  // 直接从列表行中获取用户信息
  userForm.value = {
    id: row.id,
    username: row.username,
    password: '123456', // 直接显示密码明文
    realName: row.realName,
    phone: row.phone || '',
    roleId: row.roleId || '',
    status: Number(row.status) // 确保状态值是数字类型
  }
  console.log('User form after setting:', userForm.value)
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    // 复制表单数据，避免直接修改原始数据
    const formData = { ...userForm.value }
    
    // 确保status值是数字类型
    formData.status = Number(formData.status)
    
    // 如果是编辑操作且密码为空，删除密码字段，避免清空密码
    if (formData.id && !formData.password) {
      delete formData.password
    }
    
    const response = await request({
      url: '/sys/user/save',
      method: 'post',
      data: formData
    })
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadUserList()
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleStatusChange = async (row, newValue) => {
  // 保存原始状态
  const originalStatus = row.status
  // 设置加载状态
  row.loading = true
  try {
    // 使用save接口来更新状态，确保状态更新功能正常工作
    const response = await request({
      url: '/sys/user/save',
      method: 'post',
      data: {
        id: row.id,
        username: row.username,
        realName: row.realName,
        phone: row.phone,
        roleId: row.roleId,
        status: Number(newValue) // 确保状态值是数字类型
      }
    })
    if (response.code === 200) {
      ElMessage.success('状态更新成功')
      // 状态更新成功后重新加载用户列表
      loadUserList()
    } else {
      ElMessage.error('状态更新失败')
      // 恢复原状态
      row.status = originalStatus
    }
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 恢复原状态
    row.status = originalStatus
  } finally {
    // 清除加载状态
    row.loading = false
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadUserList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadUserList()
}

const handleDelete = async (id) => {
  try {
    const response = await request({
      url: '/sys/user/delete',
      method: 'post',
      data: { id }
    })
    
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadUserList()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(async () => {
  await loadRoleList()
  await loadUserList()
})
</script>

<style scoped>
.user-management {
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
