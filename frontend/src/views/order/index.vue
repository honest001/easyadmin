<template>
  <div class="order-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="primary" @click="handleAdd">新增订单</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="订单号">
            <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" />
          </el-form-item>
          <el-form-item label="客户">
            <el-select v-model="searchForm.customerId" placeholder="请选择客户">
              <el-option label="全部" value="" />
              <el-option v-for="customer in customerList" :key="customer.id" :label="customer.customerName" :value="customer.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="searchForm.orderStatus" placeholder="请选择订单状态">
              <el-option label="全部" value="" />
              <el-option label="待处理" value="0" />
              <el-option label="已完成" value="1" />
              <el-option label="已取消" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="收款状态">
            <el-select v-model="searchForm.payStatus" placeholder="请选择收款状态">
              <el-option label="全部" value="" />
              <el-option label="未收款" value="0" />
              <el-option label="部分收款" value="1" />
              <el-option label="已收款" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="下单时间">
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
      <el-table :data="orderList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="customerName" label="客户" />
        <el-table-column prop="orderTime" label="下单时间" width="180" />
        <el-table-column prop="totalAmount" label="总金额" width="120" />
        <el-table-column prop="orderStatus" label="订单状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.orderStatus === 0" type="info">待处理</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 1" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 2" type="danger">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payStatus" label="收款状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.payStatus === 0" type="danger">未收款</el-tag>
            <el-tag v-else-if="scope.row.payStatus === 1" type="warning">部分收款</el-tag>
            <el-tag v-else-if="scope.row.payStatus === 2" type="success">已收款</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button v-if="scope.row.payStatus !== 2" type="primary" size="small" @click="handlePay(scope.row)">收款</el-button>
            <el-button v-if="scope.row.orderStatus === 0" type="warning" size="small" @click="handleCancel(scope.row.id)">取消</el-button>
            <el-button type="info" size="small" @click="handleView(scope.row)">查看</el-button>
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
    
    <!-- 新增/编辑订单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
    >
      <el-form
        ref="orderForm"
        :model="orderForm"
        :rules="orderRules"
        label-width="120px"
      >
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="orderForm.orderNo" placeholder="系统自动生成" disabled />
        </el-form-item>
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="orderForm.customerId" placeholder="请选择客户">
            <el-option v-for="customer in customerList" :key="customer.id" :label="customer.customerName" :value="customer.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单时间" prop="orderTime">
          <el-date-picker v-model="orderForm.orderTime" type="datetime" placeholder="请选择下单时间" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="orderForm.remark"
            type="textarea"
            rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
        
        <!-- 订单明细 -->
        <el-form-item label="订单明细">
          <el-table :data="orderItems" style="width: 100%">
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
            <el-table-column prop="goodsPrice" label="单价" width="100">
              <template #default="scope">
                <span>{{ getGoodsPrice(scope.row.goodsId) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="itemAmount" label="金额" width="120">
              <template #default="scope">
                <span>{{ scope.row.goodsNum * getGoodsPrice(scope.row.goodsId) }}</span>
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
        <el-form-item label="总金额">
          <el-input v-model="orderForm.totalAmount" placeholder="自动计算" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="订单详情"
      width="800px"
    >
      <el-form :model="currentOrder" label-width="120px">
        <el-form-item label="订单号">
          <span>{{ currentOrder.orderNo }}</span>
        </el-form-item>
        <el-form-item label="客户">
          <span>{{ currentOrder.customerName }}</span>
        </el-form-item>
        <el-form-item label="下单时间">
          <span>{{ currentOrder.orderTime }}</span>
        </el-form-item>
        <el-form-item label="总金额">
          <span>{{ currentOrder.totalAmount }}</span>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-tag v-if="currentOrder.orderStatus === 0" type="info">待处理</el-tag>
          <el-tag v-else-if="currentOrder.orderStatus === 1" type="success">已完成</el-tag>
          <el-tag v-else-if="currentOrder.orderStatus === 2" type="danger">已取消</el-tag>
        </el-form-item>
        <el-form-item label="收款状态">
          <el-tag v-if="currentOrder.payStatus === 0" type="danger">未收款</el-tag>
          <el-tag v-else-if="currentOrder.payStatus === 1" type="warning">部分收款</el-tag>
          <el-tag v-else-if="currentOrder.payStatus === 2" type="success">已收款</el-tag>
        </el-form-item>
        <el-form-item label="操作人">
          <span>{{ currentOrder.operatorName }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <span>{{ currentOrder.remark || '无' }}</span>
        </el-form-item>
        <el-form-item label="订单明细">
          <el-table :data="currentOrder.items" style="width: 100%">
            <el-table-column prop="goodsName" label="货品名称" />
            <el-table-column prop="goodsSpec" label="规格" />
            <el-table-column prop="goodsNum" label="数量" />
            <el-table-column prop="goodsPrice" label="单价" />
            <el-table-column prop="itemAmount" label="金额" />
          </el-table>
        </el-form-item>
        <el-form-item label="收款记录" v-if="currentOrder.payRecords && currentOrder.payRecords.length > 0">
          <el-table :data="currentOrder.payRecords" style="width: 100%">
            <el-table-column prop="payAmount" label="收款金额" />
            <el-table-column prop="payTime" label="收款时间" />
            <el-table-column prop="payType" label="收款方式">
              <template #default="scope">
                <span v-if="scope.row.payType === 1">现金</span>
                <span v-else-if="scope.row.payType === 2">微信</span>
                <span v-else-if="scope.row.payType === 3">支付宝</span>
                <span v-else-if="scope.row.payType === 4">转账</span>
              </template>
            </el-table-column>
            <el-table-column prop="operatorName" label="操作人" />
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
import { ref, onMounted, computed } from 'vue'
import request from '../../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const orderList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  orderNo: '',
  customerId: '',
  orderStatus: '',
  payStatus: ''
})
const dateRange = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增订单')
const viewDialogVisible = ref(false)
const currentOrder = ref({})

const orderForm = ref({
  id: null,
  orderNo: '',
  customerId: '',
  orderTime: new Date(),
  totalAmount: 0,
  remark: ''
})

const orderItems = ref([])

const customerList = ref([])
const goodsList = ref([])

const loadCustomerList = async () => {
  try {
    const response = await request({
      url: '/customer/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      customerList.value = response.data.records
    }
  } catch (error) {
    ElMessage.error('获取客户列表失败')
  }
}

const loadGoodsList = async () => {
  try {
    const response = await request({
      url: '/goods/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      goodsList.value = response.data.records
    }
  } catch (error) {
    ElMessage.error('获取货品列表失败')
  }
}

const orderRules = {
  customerId: [
    { required: true, message: '请选择客户', trigger: 'change' }
  ],
  orderTime: [
    { required: true, message: '请选择下单时间', trigger: 'change' }
  ]
}

const getGoodsPrice = (goodsId) => {
  const goods = goodsList.value.find(item => item.id === goodsId)
  return goods ? goods.price : 0
}

const loadOrderList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      orderNo: searchForm.value.orderNo,
      customerId: searchForm.value.customerId,
      orderStatus: searchForm.value.orderStatus,
      payStatus: searchForm.value.payStatus
    }
    
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    
    const response = await request({
      url: '/order/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      orderList.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error('获取订单列表失败')
    }
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadOrderList()
}

const resetForm = () => {
  searchForm.value = {
    orderNo: '',
    customerId: '',
    orderStatus: '',
    payStatus: ''
  }
  dateRange.value = []
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增订单'
  orderForm.value = {
    id: null,
    orderNo: 'ORD' + new Date().getTime(),
    customerId: '',
    orderTime: new Date(),
    totalAmount: 0,
    remark: ''
  }
  orderItems.value = [{ goodsId: '', goodsNum: 1 }]
  dialogVisible.value = true
}

const addItem = () => {
  orderItems.value.push({ goodsId: '', goodsNum: 1 })
}

const removeItem = (index) => {
  orderItems.value.splice(index, 1)
}

const handleSave = async () => {
  try {
    const orderRequest = {
      order: orderForm.value,
      items: orderItems.value
    }
    
    const response = await request({
      url: '/order/save',
      method: 'post',
      data: orderRequest
    })
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadOrderList()
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handlePay = (row) => {
  // 跳转到收款页面
  console.log('跳转到收款页面', row.id)
}

const handleCancel = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await request({
      url: '/order/cancel',
      method: 'post',
      data: { id }
    })
    
    if (response.code === 200) {
      ElMessage.success('订单已取消')
      loadOrderList()
    } else {
      ElMessage.error('取消订单失败')
    }
  } catch (error) {
    // 用户取消操作
  }
}

const handleView = async (row) => {
  try {
    const response = await request({
      url: '/order/detail',
      method: 'get',
      params: { id: row.id }
    })
    
    if (response.code === 200) {
      currentOrder.value = response.data
      viewDialogVisible.value = true
    } else {
      ElMessage.error('获取订单详情失败')
    }
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadOrderList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadOrderList()
}

onMounted(async () => {
  await loadCustomerList()
  await loadGoodsList()
  await loadOrderList()
})
</script>

<style scoped>
.order-management {
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