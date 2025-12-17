<template>
  <div>
    <el-container>
      <el-aside width="260px">
        <el-header class="aside-header">G-shop游戏商城后台管理系统</el-header>
        <el-menu :default-active="activeIndex" class="el-menu-vertical-demo" background-color="#304156"
                 text-color="#bfcbd9" active-text-color="#409EFF">
          <el-menu-item index="Home" @click="goToRoute('Index')">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <!-- 商品管理菜单仅对type为0和2的管理员显示 -->
          <el-menu-item v-if="isAdmin || isShopManager" index="GoodsManagement" @click="goToRoute('GoodsManagement')">
            <i class="el-icon-goods"></i>
            <span slot="title">商品管理</span>
          </el-menu-item>
          <!-- 订单管理菜单仅对type为0和2的管理员显示 -->
          <el-menu-item v-if="isAdmin || isShopManager" index="OrderManagement" @click="goToRoute('OrderManagement')">
            <i class="el-icon-document"></i>
            <span slot="title">订单管理</span>
          </el-menu-item>
          <!-- 广告管理菜单仅对type为0和2的管理员显示 -->
          <el-menu-item v-if="isAdmin || isShopManager" index="BannerManagement" @click="goToRoute('BannerManagement')">
            <i class="el-icon-document"></i>
            <span slot="title">广告管理</span>
          </el-menu-item>
          <!-- 评论管理菜单仅对type为0和1的管理员显示 -->
          <el-menu-item v-if="isAdmin || isInfoMaintainer" index="ReviewManagement" @click="goToRoute('ReviewManagement')">
            <i class="el-icon-document"></i>
            <span slot="title">评论管理</span>
          </el-menu-item>
          <!-- 数据统计菜单仅对type为0和2的管理员显示 -->
          <el-menu-item v-if="isAdmin || isShopManager" index="DataStatistics" @click="goToRoute('DataStatistics')">
            <i class="el-icon-data-analysis"></i>
            <span slot="title">数据统计</span>
          </el-menu-item>
          <!-- 商品分类菜单仅对type为0和2的管理员显示 -->
          <el-submenu v-if="isAdmin || isShopManager" index="ProductCategory">
            <template slot="title">
              <i class="el-icon-folder"></i>
              <span>商品分类</span>
            </template>
            <el-menu-item index="GoodsType" @click="goToRoute('GoodsType')">
              <span>一级分类</span>
            </el-menu-item>
            <el-menu-item index="Category" @click="goToRoute('Category')">
              <span>二级分类</span>
            </el-menu-item>
          </el-submenu>
          <!-- 用户管理菜单仅对type为0和1的管理员显示 -->
          <el-menu-item v-if="isAdmin || isInfoMaintainer" index="UserManagement" @click="goToRoute('UserManagement')">
            <i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
          <!-- 管理员注册菜单仅对type为0的管理员显示 -->
          <el-menu-item v-if="isAdmin" index="AdminAdd" @click="goToRoute('AdminAdd')">
            <i class="el-icon-user"></i>
            <span slot="title">管理员注册</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-main>
          <div class="custom-header">
            <div class="left-section">
              <el-button type="text" @click="refreshPage" class="refresh-button">
                <i class="el-icon-refresh"></i> 刷新
              </el-button>
              <span class="current-time">{{ currentTime }}</span>
            </div>
            <div class="right-section">
              <span class="username">你好, {{ adminUser?.user || '管理员' }}</span>
              <el-button type="text" @click="handleLogout" class="logout-button">
                <i class="el-icon-switch-button"></i> 退出
              </el-button>
            </div>
          </div>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>export default {
  name: "Home",
  components: {},
  data() {
    return {
      currentTime: new Date().toLocaleTimeString(),
    };
  },
  computed: {
    activeIndex() {
      return this.$route.name;
    },
    adminUser() {
      return this.$store.getters.adminUser;
    },
    isAdmin() {
      return this.adminUser?.type === 0;
    },
    isInfoMaintainer() {
      return this.adminUser?.type === 1;
    },
    isShopManager() {
      return this.adminUser?.type === 2;
    }
  },
  methods: {
    goToRoute(routeName) {
      if (this.$route.name !== routeName) {
        this.$router.push({ name: routeName });
      }
    },
    refreshPage() {
      location.reload();
    },
    updateTime() {
      this.currentTime = new Date().toLocaleTimeString();
    },
    handleLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('clearAdminUser');
        this.$router.push({ name: 'AdminLogin' });
      }).catch(() => {
        this.$message.info({
          message: '已取消退出操作',
          duration: 1500,
          type: 'info'
        });
      });
    },
  },
  created() {
    setInterval(this.updateTime, 1000);
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (!vm.$store.getters.isAdmin) {
        vm.$message.warning('请使用管理员账号登录')
        vm.$router.replace({ name: 'AdminLogin' })
      }
    })
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

.aside-header {
  background-color: #304156;
  color: #bfcbd9;
  text-align: center;
  line-height: 60px;
  border-bottom: 1px solid #40485b;
  font-family: 'Roboto', sans-serif;
  padding: 0;
  margin: 0;
}

.el-aside {
  background-color: #304156;
  color: #bfcbd9;
  text-align: left;
  padding: 0px 0;
  border-right: 1px solid #40485b;
  font-family: 'Roboto', sans-serif;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
}

.el-main {
  background-color: #f0f2f5;
  padding: 0px;
  font-family: 'Roboto', sans-serif;
  margin-left: 260px;
  height: 100vh;
  overflow-y: auto;
}

.el-container {
  height: 100vh;
}

.el-menu {
  border-right: none;
}

.el-menu-item,
.el-submenu__title {
  color: #bfcbd9;
}

.el-menu-item.is-active {
  background-color: #263445;
  color: #409eff;
}

.el-button--text {
  color: #409eff;
}

.el-button--text:hover {
  color: #66b1ff;
}

.custom-header {
  background-color: #3c0157f2;
  height: 60px;
  line-height: 60px;
  color: #bfcbd9;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #304156;
  margin-bottom: 20px;
}

.left-section {
  display: flex;
  align-items: center;
}

.refresh-button {
  margin-right: 20px;
}

.current-time {
  font-size: 14px;
}

.right-section {
  display: flex;
  align-items: center;
}

.username {
  font-size: 14px;
  margin-right: 10px;
}

.logout-button {
  margin-left: 10px;
}
</style>