<template>
  <div class="order-pay">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单收款</span>
        </div>
      </template>
      
      <!-- 订单选择 -->
      <div class="order-select">
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
          <el-form-item>
            <el-button type="primary" @click="handleSearchOrder">搜索</el-button>
            <el-button @click="resetOrderForm">重置</el-button>
          </el-form-item>
        </el-form>
        
        <!-- 订单列表 -->
        <el-table :data="orderList" style="width: 100%" @row-click="selectOrder">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="orderNo" label="订单号" />
          <el-table-column prop="customerName" label="客户" />
          <el-table-column prop="totalAmount" label="总金额" width="120" />
          <el-table-column prop="paidAmount" label="已收金额" width="120" />
          <el-table-column prop="unpaidAmount" label="未收金额" width="120" />
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
        </el-table>
      </div>
      
      <!-- 收款表单 -->
      <div class="pay-form" v-if="selectedOrder.id">
        <el-divider>收款信息</el-divider>
        <el-form
          ref="payForm"
          :model="payForm"
          :rules="payRules"
          label-width="120px"
        >
          <el-form-item label="订单号">
            <el-input v-model="selectedOrder.orderNo" disabled />
          </el-form-item>
          <el-form-item label="客户">
            <el-input v-model="selectedOrder.customerName" disabled />
          </el-form-item>
          <el-form-item label="总金额">
            <el-input v-model="selectedOrder.totalAmount" disabled />
          </el-form-item>
          <el-form-item label="已收金额">
            <el-input v-model="selectedOrder.paidAmount" disabled />
          </el-form-item>
          <el-form-item label="未收金额">
            <el-input v-model="selectedOrder.unpaidAmount" disabled />
          </el-form-item>
          <el-form-item label="收款金额" prop="payAmount">
            <el-input-number
              v-model="payForm.payAmount"
              :min="0.01"
              :max="selectedOrder.unpaidAmount"
              :precision="2"
            />
          </el-form-item>
          <el-form-item label="收款方式" prop="payType">
            <el-select v-model="payForm.payType" placeholder="请选择收款方式">
              <el-option label="现金" value="1" />
              <el-option label="微信" value="2" />
              <el-option label="支付宝" value="3" />
              <el-option label="转账" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="收款时间" prop="payTime">
            <el-date-picker v-model="payForm.payTime" type="datetime" placeholder="请选择收款时间" />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="payForm.remark"
              type="textarea"
              rows="2"
              placeholder="请输入备注"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handlePay">确认收款</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 收款记录 -->
      <div class="pay-records" v-if="selectedOrder.id">
        <el-divider>收款记录</el-divider>
        <el-table :data="selectedOrder.payRecords" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="payAmount" label="收款金额" />
          <el-table-column prop="payTime" label="收款时间" width="180" />
          <el-table-column prop="payType" label="收款方式">
            <template #default="scope">
              <span v-if="scope.row.payType === 1">现金</span>
              <span v-else-if="scope.row.payType === 2">微信</span>
              <span v-else-if="scope.row.payType === 3">支付宝</span>
              <span v-else-if="scope.row.payType === 4">转账</span>
            </template>
          </el-table-column>
          <el-table-column prop="operatorName" label="操作人" />
          <el-table-column prop="remark" label="备注" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const orderList = ref([])
const searchForm = ref({
  orderNo: '',
  customerId: ''
})
const selectedOrder = ref({
  id: '',
  orderNo: '',
  customerName: '',
  totalAmount: 0,
  paidAmount: 0,
  unpaidAmount: 0,
  orderStatus: 0,
  payStatus: 0,
  payRecords: []
})

const payForm = ref({
  payAmount: 0,
  payType: '',
  payTime: new Date(),
  remark: ''
})

const customerList = ref([])

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

const payRules = {
  payAmount: [
    { required: true, message: '请输入收款金额', trigger: 'blur' }
  ],
  payType: [
    { required: true, message: '请选择收款方式', trigger: 'change' }
  ],
  payTime: [
    { required: true, message: '请选择收款时间', trigger: 'change' }
  ]
}

const loadOrderList = async () => {
  try {
    const params = {
      orderNo: searchForm.value.orderNo,
      customerId: searchForm.value.customerId
    }
    
    const response = await request({
      url: '/order/list',
      method: 'get',
      params
    })
    
    if (response.code === 200) {
      orderList.value = response.data.records
    } else {
      ElMessage.error('获取订单列表失败')
    }
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  }
}

const handleSearchOrder = () => {
  loadOrderList()
}

const resetOrderForm = () => {
  searchForm.value = {
    orderNo: '',
    customerId: ''
  }
  loadOrderList()
}

const selectOrder = async (row) => {
  try {
    const response = await request({
      url: '/order/detail',
      method: 'get',
      params: { id: row.id }
    })
    
    if (response.code === 200) {
      selectedOrder.value = response.data
      // 重置收款表单
      payForm.value = {
        payAmount: response.data.unpaidAmount || 0,
        payType: '',
        payTime: new Date(),
        remark: ''
      }
    } else {
      ElMessage.error('获取订单详情失败')
    }
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  }
}

const handlePay = async () => {
  try {
    const payRequest = {
      orderId: selectedOrder.value.id,
      payAmount: payForm.value.payAmount,
      payType: payForm.value.payType,
      payTime: payForm.value.payTime,
      remark: payForm.value.remark
    }
    
    const response = await request({
      url: '/order/pay',
      method: 'post',
      data: payRequest
    })
    
    if (response.code === 200) {
      ElMessage.success('收款成功')
      // 重新加载订单列表
      loadOrderList()
      // 清空选择
      selectedOrder.value = {
        id: '',
        orderNo: '',
        customerName: '',
        totalAmount: 0,
        paidAmount: 0,
        unpaidAmount: 0,
        orderStatus: 0,
        payStatus: 0,
        payRecords: []
      }
    } else {
      ElMessage.error('收款失败')
    }
  } catch (error) {
    ElMessage.error('收款失败')
  }
}

onMounted(async () => {
  await loadCustomerList()
  await loadOrderList()
})
</script>

<style scoped>
.order-pay {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-select {
  margin-bottom: 30px;
}

.pay-form {
  margin-bottom: 30px;
}

.pay-records {
  margin-top: 20px;
}
</style>