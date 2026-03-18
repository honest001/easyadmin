<template>
  <div class="goods-out">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>出库管理</span>
          <el-button type="primary" @click="handleAdd">新增出库单</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="出库单号">
            <el-input v-model="searchForm.outNo" placeholder="请输入出库单号" />
          </el-form-item>
          <el-form-item label="出库类型">
            <el-select v-model="searchForm.outType" placeholder="请选择出库类型">
              <el-option label="全部" value="" />
              <el-option label="销售" value="1" />
              <el-option label="损耗" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="出库时间">
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
      <el-table :data="outList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="outNo" label="出库单号" />
        <el-table-column prop="outType" label="出库类型" width="100">
          <template #default="scope">
            <span v-if="scope.row.outType === 1">销售</span>
            <span v-else-if="scope.row.outType === 2">损耗</span>
          </template>
        </el-table-column>
        <el-table-column prop="outTime" label="出库时间" width="180" />
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
    
    <!-- 新增/编辑出库单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
    >
      <el-form
        ref="outForm"
        :model="outForm"
        :rules="outRules"
        label-width="120px"
      >
        <el-form-item label="出库单号" prop="outNo">
          <el-input v-model="outForm.outNo" placeholder="系统自动生成" disabled />
        </el-form-item>
        <el-form-item label="出库类型" prop="outType">
          <el-select v-model="outForm.outType" placeholder="请选择出库类型">
            <el-option label="销售" value="1" />
            <el-option label="损耗" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="出库时间" prop="outTime">
          <el-date-picker v-model="outForm.outTime" type="datetime" placeholder="请选择出库时间" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="outForm.remark"
            type="textarea"
            rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
        
        <!-- 出库明细 -->
        <el-form-item label="出库明细">
          <el-table :data="outItems" style="width: 100%">
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
            <el-table-column label="库存" width="100">
              <template #default="scope">
                <span>{{ getGoodsStock(scope.row.goodsId) }}</span>
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
    
    <!-- 查看出库单详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="出库单详情"
      width="800px"
    >
      <el-form :model="currentOut" label-width="120px">
        <el-form-item label="出库单号">
          <span>{{ currentOut.outNo }}</span>
        </el-form-item>
        <el-form-item label="出库类型">
          <span v-if="currentOut.outType === 1">销售</span>
          <span v-else-if="currentOut.outType === 2">损耗</span>
        </el-form-item>
        <el-form-item label="出库时间">
          <span>{{ currentOut.outTime }}</span>
        </el-form-item>
        <el-form-item label="总数量">
          <span>{{ currentOut.totalNum }}</span>
        </el-form-item>
        <el-form-item label="操作人">
          <span>{{ currentOut.operatorName }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <span>{{ currentOut.remark || '无' }}</span>
        </el-form-item>
        <el-form-item label="出库明细">
          <el-table :data="currentOut.items" style="width: 100%">
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
import { getGoodsOutList, saveGoodsOut } from '../../api/goods'
import { getGoodsList } from '../../api/goods'

const outList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  outNo: '',
  outType: ''
})
const dateRange = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增出库单')
const viewDialogVisible = ref(false)
const currentOut = ref({})

const outForm = ref({
  id: null,
  outNo: '',
  outType: '',
  outTime: new Date(),
  remark: ''
})

const outItems = ref([])

const goodsList = ref([])

const outRules = {
  outType: [
    { required: true, message: '请选择出库类型', trigger: 'change' }
  ],
  outTime: [
    { required: true, message: '请选择出库时间', trigger: 'change' }
  ]
}

const getGoodsStock = (goodsId) => {
  const goods = goodsList.value.find(item => item.id === goodsId)
  return goods ? goods.stock : 0
}

const loadOutList = async () => {
  try {
    const response = await getGoodsOutList({
      ...searchForm.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    outList.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取出库记录失败')
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
  loadOutList()
}

const resetForm = () => {
  searchForm.value = {
    outNo: '',
    outType: ''
  }
  dateRange.value = []
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增出库单'
  outForm.value = {
    id: null,
    outNo: 'OUT' + new Date().getTime(),
    outType: '',
    outTime: new Date(),
    remark: ''
  }
  outItems.value = [{ goodsId: '', goodsNum: 1 }]
  dialogVisible.value = true
}

const addItem = () => {
  outItems.value.push({ goodsId: '', goodsNum: 1 })
}

const removeItem = (index) => {
  outItems.value.splice(index, 1)
}

const handleSave = async () => {
  try {
    await saveGoodsOut({
      goodsOut: outForm.value,
      items: outItems.value
    })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadOutList()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleView = (row) => {
  currentOut.value = {
    ...row,
    items: []
  }
  viewDialogVisible.value = true
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadOutList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadOutList()
}

onMounted(() => {
  loadGoodsList()
  loadOutList()
})
</script>

<style scoped>
.goods-out {
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