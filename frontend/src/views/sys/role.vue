<template>
  <div class="role-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>
      
      <!-- 数据表格 -->
      <el-table :data="roleList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="permission" label="权限" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch 
              v-model="scope.row.status" 
              :active-value="1" 
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        ref="roleForm"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="权限" prop="permission">
          <el-select v-model="roleForm.permission" multiple placeholder="请选择权限">
            <el-option label="系统管理" value="sys" />
            <el-option label="人员管理" value="user" />
            <el-option label="审批模块" value="approve" />
            <el-option label="货品库存" value="goods" />
            <el-option label="订单管理" value="order" />
            <el-option label="客户管理" value="customer" />
            <el-option label="数据报表" value="report" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="roleForm.status" :active-value="1" :inactive-value="0" />
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
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const roleList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  permission: [],
  status: 1
})

const roleRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ]
}

const loadRoleList = async () => {
  try {
    const response = await request({
      url: '/sys/role/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      // 处理角色数据，将权限字符串转换为数组
      roleList.value = response.data.map(role => ({
        ...role,
        permission: role.permission ? role.permission.split(',') : [],
        status: Number(role.status)
      }))
    } else {
      ElMessage.error('获取角色列表失败')
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  // 重置表单
  Object.assign(roleForm, {
    id: null,
    roleName: '',
    roleCode: '',
    permission: [],
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  // 复制数据到表单
  Object.assign(roleForm, {
    id: row.id,
    roleName: row.roleName,
    roleCode: row.roleCode,
    permission: Array.isArray(row.permission) ? [...row.permission] : (row.permission ? row.permission.split(',') : []),
    status: Number(row.status)
  })
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    // 准备提交数据
    const formData = {
      id: roleForm.id,
      roleName: roleForm.roleName,
      roleCode: roleForm.roleCode,
      permission: Array.isArray(roleForm.permission) ? roleForm.permission.join(',') : roleForm.permission,
      status: Number(roleForm.status)
    }
    
    console.log('Submitting role data:', formData)
    
    const response = await request({
      url: '/sys/role/save',
      method: 'post',
      data: formData
    })
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadRoleList()
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    console.error('Error saving role:', error)
    ElMessage.error('保存失败')
  }
}

const handleStatusChange = async (row) => {
  try {
    const roleData = {
      ...row,
      status: Number(row.status),
      permission: Array.isArray(row.permission) ? row.permission.join(',') : row.permission
    }
    
    const response = await request({
      url: '/sys/role/save',
      method: 'post',
      data: roleData
    })
    
    if (response.code === 200) {
      ElMessage.success('状态更新成功')
    } else {
      ElMessage.error('状态更新失败')
      loadRoleList()
    }
  } catch (error) {
    ElMessage.error('状态更新失败')
    loadRoleList()
  }
}

onMounted(() => {
  loadRoleList()
})
</script>

<style scoped>
.role-management {
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
