<template>
  <div class="goods-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>货品管理</span>
          <el-button type="primary" @click="handleAdd">新增货品</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="货品名称">
            <el-input v-model="searchForm.goodsName" placeholder="请输入货品名称" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="searchForm.categoryId" placeholder="请选择分类">
              <el-option label="全部" value="" />
              <el-option v-for="category in categoryList" :key="category.id" :label="category.categoryName" :value="category.id" />
            </el-select>
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
      <el-table :data="goodsList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="goodsName" label="货品名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="goodsSpec" label="规格" />
        <el-table-column prop="price" label="售价" width="100" />
        <el-table-column prop="costPrice" label="成本价" width="100" />
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="warnStock" label="预警值" width="100" />
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
        ref="goodsForm"
        :model="goodsForm"
        :rules="goodsRules"
        label-width="120px"
      >
        <el-form-item label="货品名称" prop="goodsName">
          <el-input v-model="goodsForm.goodsName" placeholder="请输入货品名称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="goodsForm.categoryId" placeholder="请选择分类">
            <el-option v-for="category in categoryList" :key="category.id" :label="category.categoryName" :value="category.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格" prop="goodsSpec">
          <el-input v-model="goodsForm.goodsSpec" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="售价" prop="price">
          <el-input-number v-model="goodsForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="成本价" prop="costPrice">
          <el-input-number v-model="goodsForm.costPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="预警值" prop="warnStock">
          <el-input-number v-model="goodsForm.warnStock" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="goodsForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="goodsForm.remark"
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getGoodsList, saveGoods } from '../../api/goods'
import { getCategoryList } from '../../api/goods'

const goodsList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  goodsName: '',
  categoryId: '',
  status: ''
})
const dialogVisible = ref(false)
const dialogTitle = ref('新增货品')
const goodsForm = ref({
  id: null,
  goodsName: '',
  categoryId: '',
  goodsSpec: '',
  price: 0,
  costPrice: 0,
  stock: 0,
  warnStock: 5,
  status: 1,
  remark: ''
})
const categoryList = ref([])

const goodsRules = {
  goodsName: [
    { required: true, message: '请输入货品名称', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入售价', trigger: 'blur' }
  ],
  costPrice: [
    { required: true, message: '请输入成本价', trigger: 'blur' }
  ]
}

const loadGoodsList = async () => {
  try {
    const response = await getGoodsList({
      ...searchForm.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    goodsList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取货品列表失败')
  }
}

const loadCategoryList = async () => {
  try {
    const response = await getCategoryList()
    categoryList.value = response.data
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadGoodsList()
}

const resetForm = () => {
  searchForm.value = {
    goodsName: '',
    categoryId: '',
    status: ''
  }
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增货品'
  goodsForm.value = {
    id: null,
    goodsName: '',
    categoryId: '',
    goodsSpec: '',
    price: 0,
    costPrice: 0,
    stock: 0,
    warnStock: 5,
    status: 1,
    remark: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑货品'
  goodsForm.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await saveGoods(goodsForm.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadGoodsList()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该货品吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await fetch('/api/goods/delete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({ id })
    })
    ElMessage.success('删除成功')
    loadGoodsList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleStatusChange = async (row) => {
  try {
    await saveGoods(row)
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadGoodsList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadGoodsList()
}

onMounted(() => {
  loadCategoryList()
  loadGoodsList()
})
</script>

<style scoped>
.goods-management {
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