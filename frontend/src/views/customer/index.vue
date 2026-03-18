<template>
  <div class="customer-index">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户管理</span>
          <el-button type="primary" @click="handleAdd">新增客户</el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="searchForm.contactPhone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="客户状态">
          <el-select v-model="searchForm.status" placeholder="请选择客户状态">
            <el-option label="全部" value="" />
            <el-option label="正常" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 客户列表 -->
      <el-table :data="customerList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="contactPhone" label="联系方式" width="150" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
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
      
      <!-- 新增/编辑对话框 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="form.customerName" placeholder="请输入客户名称" />
          </el-form-item>
          <el-form-item label="联系人" prop="contactPerson">
            <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="联系方式" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入联系方式" />
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input
              v-model="form.address"
              type="textarea"
              rows="2"
              placeholder="请输入地址"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-switch v-model="form.status" active-value="1" inactive-value="0" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCustomerList, saveCustomer } from '../../api/customer'
import { ElMessage, ElMessageBox } from 'element-plus'

const customerList = ref([])
const searchForm = ref({
  customerName: '',
  contactPhone: '',
  status: ''
})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const form = ref({
  id: '',
  customerName: '',
  contactPerson: '',
  contactPhone: '',
  address: '',
  status: 1
})
const rules = {
  customerName: [
    { required: true, message: '请输入客户名称', trigger: 'blur' }
  ],
  contactPerson: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系方式', trigger: 'blur' }
  ]
}

const loadCustomerList = async () => {
  try {
    const response = await getCustomerList({
      ...searchForm.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    customerList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取客户列表失败')
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadCustomerList()
}

const resetForm = () => {
  searchForm.value = {
    customerName: '',
    contactPhone: '',
    status: ''
  }
  pageNum.value = 1
  loadCustomerList()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadCustomerList()
}

const handleCurrentChange = (current) => {
  pageNum.value = current
  loadCustomerList()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  form.value = {
    id: '',
    customerName: '',
    contactPerson: '',
    contactPhone: '',
    address: '',
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑客户'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await saveCustomer(form.value)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadCustomerList()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该客户吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await fetch('/api/customer/delete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({ id })
    })
    ElMessage.success('删除成功')
    loadCustomerList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadCustomerList()
})
</script>

<style scoped>
.customer-index {
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