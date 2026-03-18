<template>
  <div class="goods-stock">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>库存查询</span>
          <el-button type="primary" @click="handleExport">导出</el-button>
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
          <el-form-item label="库存状态">
            <el-select v-model="searchForm.stockStatus" placeholder="请选择库存状态">
              <el-option label="全部" value="" />
              <el-option label="正常" value="normal" />
              <el-option label="低库存" value="low" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="stockList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="goodsName" label="货品名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="goodsSpec" label="规格" />
        <el-table-column prop="price" label="售价" width="100" />
        <el-table-column prop="stock" label="当前库存" width="120">
          <template #default="scope">
            <span :class="{ 'low-stock': scope.row.stock <= scope.row.warnStock }">
              {{ scope.row.stock }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="warnStock" label="预警值" width="100" />
        <el-table-column label="库存状态" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.stock > scope.row.warnStock" type="success">正常</el-tag>
            <el-tag v-else type="danger">低库存</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const stockList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  goodsName: '',
  categoryId: '',
  stockStatus: ''
})

const categoryList = ref([])

const loadCategoryList = async () => {
  try {
    const response = await request({
      url: '/goods/category/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      categoryList.value = response.data.records
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

const loadStockList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      goodsName: searchForm.value.goodsName,
      categoryId: searchForm.value.categoryId,
      stockStatus: searchForm.value.stockStatus
    }
    
    const response = await request({
      url: '/goods/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      stockList.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error('获取库存列表失败')
    }
  } catch (error) {
    ElMessage.error('获取库存列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadStockList()
}

const resetForm = () => {
  searchForm.value = {
    goodsName: '',
    categoryId: '',
    stockStatus: ''
  }
  handleSearch()
}

const handleExport = () => {
  try {
    // 模拟导出
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadStockList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadStockList()
}

onMounted(async () => {
  await loadCategoryList()
  await loadStockList()
})
</script>

<style scoped>
.goods-stock {
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

.low-stock {
  color: #f56c6c;
  font-weight: bold;
}
</style>