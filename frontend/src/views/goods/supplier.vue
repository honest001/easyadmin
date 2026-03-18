<template>
  <div class="goods-supplier">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>供应商管理</span>
          <el-button type="primary" @click="handleAdd">新增供应商</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="供应商名称">
            <el-input v-model="searchForm.supplierName" placeholder="请输入供应商名称" />
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="searchForm.contactUser" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态">
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
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="supplierList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="supplierName" label="供应商名称" />
        <el-table-column prop="supplierPhone" label="联系电话" />
        <el-table-column prop="supplierAddress" label="地址" />
        <el-table-column prop="contactUser" label="联系人" />
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
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
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
      width="600px"
    >
      <el-form
        ref="supplierForm"
        :model="supplierForm"
        :rules="supplierRules"
        label-width="120px"
      >
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="supplierForm.supplierName" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="supplierPhone">
          <el-input v-model="supplierForm.supplierPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="地址" prop="supplierAddress">
          <el-input
            v-model="supplierForm.supplierAddress"
            type="textarea"
            rows="2"
            placeholder="请输入地址"
          />
        </el-form-item>
        <el-form-item label="联系人" prop="contactUser">
          <el-input v-model="supplierForm.contactUser" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="supplierForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="supplierForm.remark"
            type="textarea"
            rows="3"
            placeholder="请输入备注"
          />
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

const supplierList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  supplierName: '',
  contactUser: '',
  status: ''
})
const dialogVisible = ref(false)
const dialogTitle = ref('新增供应商')
const supplierForm = ref({
  id: null,
  supplierName: '',
  supplierPhone: '',
  supplierAddress: '',
  contactUser: '',
  status: 1,
  remark: ''
})

const supplierRules = {
  supplierName: [
    { required: true, message: '请输入供应商名称', trigger: 'blur' }
  ],
  supplierPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' }
  ],
  contactUser: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ]
}

const loadSupplierList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      supplierName: searchForm.value.supplierName,
      contactUser: searchForm.value.contactUser,
      status: searchForm.value.status
    }
    
    const response = await request({
      url: '/goods/supplier/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      supplierList.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error('获取供应商列表失败')
    }
  } catch (error) {
    ElMessage.error('获取供应商列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadSupplierList()
}

const resetForm = () => {
  searchForm.value = {
    supplierName: '',
    contactUser: '',
    status: ''
  }
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增供应商'
  supplierForm.value = {
    id: null,
    supplierName: '',
    supplierPhone: '',
    supplierAddress: '',
    contactUser: '',
    status: 1,
    remark: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑供应商'
  supplierForm.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const response = await request({
      url: '/goods/supplier/save',
      method: 'post',
      data: supplierForm.value
    })
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadSupplierList()
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该供应商吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await request({
      url: '/goods/supplier/delete',
      method: 'post',
      data: { id }
    })
    
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadSupplierList()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    // 用户取消删除
  }
}

const handleStatusChange = async (row) => {
  try {
    const response = await request({
      url: '/goods/supplier/save',
      method: 'post',
      data: row
    })
    
    if (response.code === 200) {
      ElMessage.success('状态更新成功')
    } else {
      ElMessage.error('状态更新失败')
      // 恢复原状态
      loadSupplierList()
    }
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 恢复原状态
    loadSupplierList()
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadSupplierList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadSupplierList()
}

onMounted(() => {
  loadSupplierList()
})
</script>

<style scoped>
.goods-supplier {
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