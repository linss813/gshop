<template>
  <div class="user-layout">
    <!-- 顶部Header -->
    <div class="header">
      <div class="back-btn" @click="$router.push({ name: 'Shop' })">
        <i class="el-icon-back"></i> 返回商城
      </div>
      <h2 class="title">{{$route.meta.title}}</h2>
    </div>

    <!-- 左侧导航栏 -->
    <div class="aside">
      <el-menu
        :default-active="$route.path"
        router
        class="user-menu"
      >
        <el-menu-item index="/user/profile">
          <i class="el-icon-user"></i> 个人主页
        </el-menu-item>
        <el-menu-item index="/user/orders">
          <i class="el-icon-tickets"></i> 我的订单
        </el-menu-item>
        <el-menu-item index="/user/cart">
          <i class="el-icon-shopping-cart-2"></i> 购物车
        </el-menu-item>
        <el-menu-item index="/user/password">
          <i class="el-icon-lock"></i> 修改密码
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区域 -->
    <div class="main">
      <router-view></router-view>
    </div>
  </div>
</template>

<style>
.user-layout {
  display: flex;
  height: 100vh;
  padding-top: 60px; /* 适配顶部Header */
}

.header {
  position: fixed;
  top: 0;
  width: 100%;
  background: #fff;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  margin: 0 auto;
  font-size: 20px;
  color: #333;
}

.aside {
  width: 240px;
  height: 100%;
  background: #f5f7fa;
  padding: 20px;
}

.user-menu {
  border: none;
}

.main {
  flex: 1;
  padding: 20px;
  margin-left: 240px;
}
</style>
