<template>
  <div class="goods-in">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>入库管理</span>
          <el-button type="primary" @click="handleAdd">新增入库单</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="入库单号">
            <el-input v-model="searchForm.inNo" placeholder="请输入入库单号" />
          </el-form-item>
          <el-form-item label="供应商">
            <el-select v-model="searchForm.supplierId" placeholder="请选择供应商">
              <el-option label="全部" value="" />
              <el-option v-for="supplier in supplierList" :key="supplier.id" :label="supplier.supplierName" :value="supplier.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="入库时间">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="inList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="inNo" label="入库单号" />
        <el-table-column prop="supplierName" label="供应商" />
        <el-table-column prop="inTime" label="入库时间" width="180" />
        <el-table-column prop="totalNum" label="总数量" width="100" />
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
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
    
    <!-- 新增/编辑入库单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
    >
      <el-form
        ref="inForm"
        :model="inForm"
        :rules="inRules"
        label-width="120px"
      >
        <el-form-item label="入库单号" prop="inNo">
          <el-input v-model="inForm.inNo" placeholder="系统自动生成" disabled />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="inForm.supplierId" placeholder="请选择供应商">
            <el-option v-for="supplier in supplierList" :key="supplier.id" :label="supplier.supplierName" :value="supplier.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="入库时间" prop="inTime">
          <el-date-picker v-model="inForm.inTime" type="datetime" placeholder="请选择入库时间" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="inForm.remark"
            type="textarea"
            rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
        
        <!-- 入库明细 -->
        <el-form-item label="入库明细">
          <el-table :data="inItems" style="width: 100%">
            <el-table-column prop="goodsId" label="货品" width="200">
              <template #default="scope">
                <el-select v-model="scope.row.goodsId" placeholder="请选择货品">
                  <el-option v-for="goods in goodsList" :key="goods.id" :label="goods.goodsName + '(' + goods.goodsSpec + ')'" :value="goods.id" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="goodsNum" label="数量" width="100">
              <template #default="scope">
                <el-input-number v-model="scope.row.goodsNum" :min="1" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button type="danger" size="small" @click="removeItem(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button type="primary" size="small" @click="addItem">添加货品</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 查看入库单详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="入库单详情"
      width="800px"
    >
      <el-form :model="currentIn" label-width="120px">
        <el-form-item label="入库单号">
          <span>{{ currentIn.inNo }}</span>
        </el-form-item>
        <el-form-item label="供应商">
          <span>{{ currentIn.supplierName }}</span>
        </el-form-item>
        <el-form-item label="入库时间">
          <span>{{ currentIn.inTime }}</span>
        </el-form-item>
        <el-form-item label="总数量">
          <span>{{ currentIn.totalNum }}</span>
        </el-form-item>
        <el-form-item label="操作人">
          <span>{{ currentIn.operatorName }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <span>{{ currentIn.remark || '无' }}</span>
        </el-form-item>
        <el-form-item label="入库明细">
          <el-table :data="currentIn.items" style="width: 100%">
            <el-table-column prop="goodsName" label="货品名称" />
            <el-table-column prop="goodsSpec" label="规格" />
            <el-table-column prop="goodsNum" label="数量" />
          </el-table>
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
import { getGoodsInList, saveGoodsIn } from '../../api/goods'
import { getSupplierList } from '../../api/goods'
import { getGoodsList } from '../../api/goods'

const inList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  inNo: '',
  supplierId: ''
})
const dateRange = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增入库单')
const viewDialogVisible = ref(false)
const currentIn = ref({})

const inForm = ref({
  id: null,
  inNo: '',
  supplierId: '',
  inTime: new Date(),
  remark: ''
})

const inItems = ref([])

const supplierList = ref([])
const goodsList = ref([])

const inRules = {
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  inTime: [
    { required: true, message: '请选择入库时间', trigger: 'change' }
  ]
}

const loadInList = async () => {
  try {
    const response = await getGoodsInList({
      ...searchForm.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    inList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取入库记录失败')
  }
}

const loadSupplierList = async () => {
  try {
    const response = await getSupplierList()
    supplierList.value = response.data
  } catch (error) {
    ElMessage.error('获取供应商列表失败')
  }
}

const loadGoodsList = async () => {
  try {
    const response = await getGoodsList()
    goodsList.value = response.data.records
  } catch (error) {
    ElMessage.error('获取货品列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadInList()
}

const resetForm = () => {
  searchForm.value = {
    inNo: '',
    supplierId: ''
  }
  dateRange.value = []
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增入库单'
  inForm.value = {
    id: null,
    inNo: 'IN' + new Date().getTime(),
    supplierId: '',
    inTime: new Date(),
    remark: ''
  }
  inItems.value = [{ goodsId: '', goodsNum: 1 }]
  dialogVisible.value = true
}

const addItem = () => {
  inItems.value.push({ goodsId: '', goodsNum: 1 })
}

const removeItem = (index) => {
  inItems.value.splice(index, 1)
}

const handleSave = async () => {
  try {
    await saveGoodsIn({
      goodsIn: inForm.value,
      items: inItems.value
    })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadInList()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleView = (row) => {
  currentIn.value = {
    ...row,
    items: []
  }
  viewDialogVisible.value = true
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadInList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadInList()
}

onMounted(() => {
  loadSupplierList()
  loadGoodsList()
  loadInList()
})
</script>

<style scoped>
.goods-in {
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