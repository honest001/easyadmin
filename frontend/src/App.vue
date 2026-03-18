<template>
  <div class="app">
    <router-view />
  </div>
</template>

<script setup>
// 路由守卫已经处理了登录验证，不需要在这里重复检查
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}
</style>
