<template>
  <div class="config-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统配置</span>
        </div>
      </template>
      
      <el-form
        ref="configForm"
        :model="configForm"
        :rules="configRules"
        label-width="150px"
      >
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="configForm.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="企业LOGO" prop="companyLogo">
          <el-upload
            class="upload-demo"
            action="#"
            :on-change="handleLogoUpload"
            :auto-upload="false"
            :show-file-list="false"
          >
            <el-button type="primary">选择文件</el-button>
          </el-upload>
          <div v-if="configForm.companyLogo" class="logo-preview">
            <img :src="configForm.companyLogo" alt="企业LOGO" style="width: 100px; height: 100px;" />
          </div>
        </el-form-item>
        <el-form-item label="自动备份时间" prop="backupTime">
          <el-time-picker
            v-model="configForm.backupTime"
            format="HH:mm"
            placeholder="选择时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="默认库存预警值" prop="stockWarnNum">
          <el-input-number v-model="configForm.stockWarnNum" :min="1" :max="100" placeholder="请输入预警值" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const configForm = ref({
  companyName: '中小微企业管理系统',
  companyLogo: '',
  backupTime: new Date('2026-01-01 02:00:00'),
  stockWarnNum: 5
})

const configRules = {
  companyName: [
    { required: true, message: '请输入企业名称', trigger: 'blur' }
  ],
  backupTime: [
    { required: true, message: '请选择自动备份时间', trigger: 'change' }
  ],
  stockWarnNum: [
    { required: true, message: '请输入默认库存预警值', trigger: 'blur' }
  ]
}

const handleLogoUpload = (file) => {
  // 模拟上传
  configForm.value.companyLogo = URL.createObjectURL(file.raw)
  ElMessage.success('LOGO上传成功')
}

const handleSave = async () => {
  try {
    // 构建配置数据
    const configs = [
      { configKey: 'companyName', configValue: configForm.value.companyName },
      { configKey: 'companyLogo', configValue: configForm.value.companyLogo },
      { configKey: 'backupTime', configValue: configForm.value.backupTime.toTimeString().substring(0, 5) },
      { configKey: 'stockWarnNum', configValue: configForm.value.stockWarnNum.toString() }
    ]
    
    const response = await request({
      url: '/sys/config/save',
      method: 'post',
      data: configs
    })
    
    if (response.code === 200) {
      ElMessage.success('配置保存成功')
    } else {
      ElMessage.error('配置保存失败')
    }
  } catch (error) {
    ElMessage.error('配置保存失败')
  }
}

onMounted(() => {
  // 加载配置
  loadConfig()
})

const loadConfig = async () => {
  try {
    const response = await request({
      url: '/sys/config/list',
      method: 'get'
    })
    
    if (response.code === 200) {
      // 将配置数据转换为表单格式
      const configs = response.data.records
      configs.forEach(config => {
        if (config.configKey === 'companyName') {
          configForm.value.companyName = config.configValue
        } else if (config.configKey === 'companyLogo') {
          configForm.value.companyLogo = config.configValue
        } else if (config.configKey === 'backupTime') {
          configForm.value.backupTime = new Date('2026-01-01 ' + config.configValue)
        } else if (config.configKey === 'stockWarnNum') {
          configForm.value.stockWarnNum = parseInt(config.configValue)
        }
      })
    } else {
      ElMessage.error('获取配置失败')
    }
  } catch (error) {
    ElMessage.error('获取配置失败')
  }
}
</script>

<style scoped>
.config-management {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-preview {
  margin-top: 10px;
}

.upload-demo {
  margin-bottom: 10px;
}
</style>
