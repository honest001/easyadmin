<template>
  <div class="employee-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工管理</span>
          <el-button type="primary" @click="handleAdd">新增员工</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="姓名">
            <el-input v-model="searchForm.empName" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="部门">
            <el-select v-model="searchForm.deptId" placeholder="请选择部门">
              <el-option v-for="dept in deptList" :key="dept.id" :label="dept.deptName" :value="dept.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态">
              <el-option label="在职" value="1" />
              <el-option label="离职" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="employeeList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="empName" label="姓名" />
        <el-table-column prop="empPhone" label="电话" />
        <el-table-column prop="deptName" label="部门" />
        <el-table-column prop="post" label="岗位" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column prop="baseSalary" label="基础薪资" width="100" />
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
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
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
        ref="employeeForm"
        :model="employeeForm"
        :rules="employeeRules"
        label-width="100px"
      >
        <el-form-item label="姓名" prop="empName">
          <el-input v-model="employeeForm.empName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="empPhone">
          <el-input v-model="employeeForm.empPhone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <el-select v-model="employeeForm.deptId" placeholder="请选择部门">
            <el-option v-for="dept in deptList" :key="dept.id" :label="dept.deptName" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位" prop="post">
          <el-input v-model="employeeForm.post" placeholder="请输入岗位" />
        </el-form-item>
        <el-form-item label="入职日期" prop="entryDate">
          <el-date-picker v-model="employeeForm.entryDate" type="date" placeholder="选择入职日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="基础薪资" prop="baseSalary">
          <el-input v-model="employeeForm.baseSalary" type="number" placeholder="请输入基础薪资" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="employeeForm.status" active-value="1" inactive-value="0" />
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

const employeeList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  empName: '',
  deptId: '',
  status: ''
})
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const employeeForm = ref({
  id: null,
  empName: '',
  empPhone: '',
  deptId: '',
  post: '',
  entryDate: null,
  baseSalary: 0,
  status: 1
})
const deptList = ref([])

const loadDeptList = async () => {
  try {
    const response = await request({
      url: '/user/dept/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      deptList.value = response.data.records
    }
  } catch (error) {
    ElMessage.error('获取部门列表失败')
  }
}

const employeeRules = {
  empName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  empPhone: [
    { required: true, message: '请输入电话', trigger: 'blur' }
  ],
  deptId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  entryDate: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ],
  baseSalary: [
    { required: true, message: '请输入基础薪资', trigger: 'blur' }
  ]
}

const loadEmployeeList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      empName: searchForm.value.empName,
      deptId: searchForm.value.deptId,
      status: searchForm.value.status
    }
    
    const response = await request({
      url: '/user/employee/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      employeeList.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error('获取员工列表失败')
    }
  } catch (error) {
    ElMessage.error('获取员工列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadEmployeeList()
}

const resetForm = () => {
  searchForm.value = {
    empName: '',
    deptId: '',
    status: ''
  }
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增员工'
  employeeForm.value = {
    id: null,
    empName: '',
    empPhone: '',
    deptId: '',
    post: '',
    entryDate: null,
    baseSalary: 0,
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑员工'
  employeeForm.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const response = await request({
      url: '/user/employee/save',
      method: 'post',
      data: employeeForm.value
    })
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadEmployeeList()
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleStatusChange = async (row) => {
  try {
    const response = await request({
      url: '/user/employee/save',
      method: 'post',
      data: row
    })
    
    if (response.code === 200) {
      ElMessage.success('状态更新成功')
    } else {
      ElMessage.error('状态更新失败')
      // 恢复原状态
      loadEmployeeList()
    }
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 恢复原状态
    loadEmployeeList()
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadEmployeeList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadEmployeeList()
}

onMounted(async () => {
  await loadDeptList()
  await loadEmployeeList()
})
</script>

<style scoped>
.employee-management {
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
