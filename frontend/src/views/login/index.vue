<template>
  <div class="login-container">
    <div class="login-form">
      <h1>EasyAdmin</h1>
      <h2>中小微企业管理系统</h2>
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="loginRules"
        label-position="top"
      >
        <el-form-item label="账号" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-tips">
        <p>默认账号：admin</p>
        <p>默认密码：123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const loginForm = reactive({
  username: 'admin',
  password: '123456'
})
const loading = ref(false)

const loginRules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  try {
    loading.value = true
    console.log('登录请求发送前:', loginForm)
    // 调用后端登录接口
    const res = await request.post('/login', loginForm)
    console.log('登录请求响应:', res)
    
    // 检查响应状态
    if (res.code === 200) {
      // 登录成功，保存token和用户信息
      localStorage.setItem('token', res.data.token || 'mock-token')
      localStorage.setItem('user', JSON.stringify(res.data.user))
      
      ElMessage.success('登录成功')
      router.push('/home')
    } else {
      // 登录失败，显示错误信息
      ElMessage.error(res.message || '登录失败，请检查账号密码')
    }
  } catch (error) {
    console.log('登录请求错误:', error)
    ElMessage.error('登录失败，请检查账号密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-form {
  width: 400px;
  padding: 40px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.login-form h1 {
  color: #1890ff;
  font-size: 24px;
  margin-bottom: 10px;
}

.login-form h2 {
  color: #666;
  font-size: 16px;
  margin-bottom: 30px;
  font-weight: normal;
}

.login-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.login-tips {
  margin-top: 20px;
  font-size: 14px;
  color: #999;
}

.login-tips p {
  margin: 5px 0;
}
</style>
