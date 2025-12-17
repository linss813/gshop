<template>
  <div class="shop-container">
    <el-container>
      <el-header class="header">
        <div class="logo">G-shop游戏商城</div>
        <div class="nav">
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect"
            background-color="#ffffff" active-text-color="#409eff">
            <el-menu-item index="home">首页</el-menu-item>
            <el-submenu index="category">
              <template slot="title">{{ submenuTitle }}</template>
              <el-menu-item v-for="category in categories" :key="category.cid" :index="`cat-${category.cid}`"
                @click="handleCategorySelect(category)">
                {{ category.cname }}
              </el-menu-item>
            </el-submenu>
            <el-menu-item index="gameZone">游戏专区</el-menu-item>
            <el-menu-item index="peripheralZone">周边专区</el-menu-item>
          </el-menu>

          <el-input v-model="searchQuery" placeholder="请输入商品名称" class="search-input" @keyup.enter.native="handleSearch">
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
        </div>
        <div class="user-info">
          <el-dropdown v-if="user" trigger="click">
            <span class="el-dropdown-link">
              <img :src="user.avatar || 'https://a.520gexing.com/uploads/allimg/2021042109/uqaqhuvavt0.jpg'" alt="用户头像"
                class="user-avatar" />
              <span class="username">{{ user.username }}</span>
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="goToProfile">个人主页</el-dropdown-item>
              <el-dropdown-item @click.native="goToCart">购物车</el-dropdown-item>
              <el-dropdown-item @click.native="goToOrders">我的订单</el-dropdown-item>
              <el-dropdown-item @click.native="handleLogout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button v-else type="text" @click="goToLogin">点击登录</el-button>
        </div>
      </el-header>
      <el-main>
        <carousel :banners="banners" />
        <div class="goods-list">
          <el-card v-for="goods in goodsList" :key="goods.gid" class="goods-card" >

            <div class="goods-image" >
              <img :src="goods.image" alt="商品图片" @click="goToDetail(goods)" />
            </div>
            <div class="goods-info">
              <h3 class="goods-title" @click="goToDetail(goods)">{{ goods.title }}</h3>
              <p class="goods-details" @click="goToDetail(goods)">{{ goods.details }}</p>
              <div class="price-container">
                <div class="goods-price">
                  <span class="price-label">价格:</span>
                  <span class="price-value">{{ goods.price }} 元</span>
                </div>
                <div v-if="goods.stock === 0" class="stock-out">已售空</div>
                <div v-else-if="goods.stock < 300" class="stock-low">即将售空</div>
              </div>
<!--              <div class="goods-actions">-->
<!--                <el-button type="primary" @click="addToCart(goods)">加入购物车</el-button>-->
<!--                <el-button type="success" :disabled="goods.stock === 0" @click="buyNow(goods)">-->
<!--                  立即购买-->
<!--                </el-button>-->
<!--              </div>-->
            </div>
          </el-card>
        </div>
        <div class="pagination">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              :current-page="pageNum"
              @current-change="handlePageChange"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Carousel from '@/components/Carousel.vue';

export default {
  name: "Shop",
  components: {
    Carousel
  },
  data() {
    return {
      banners: [],
      goodsList: [],
      total: 0,
      pageNum: 1,
      pageSize: 12,
      searchQuery: '',
      activeIndex: 'home',
      categories: [],
      selectedCid: null,
      selectedTids: null,
      categoryMenuIndex: '2', // 单独维护分类菜单index
      submenuTitle: '商品分类', // 分类菜单标题
      selectedCategory: null,// 当前选中分类
    };
  },
  created() {
    this.fetchBanners();
    this.fetchGoodsList();
    this.fetchCategories();
  },
  computed: {

    user() {
      return this.$store.getters.currentUser; // 获取当前用户
    },
  },
  methods: {
    beforeRouteEnter(to, from, next) {
      next(vm => {
        if (!vm.user) {
          vm.$message.warning('检测到未登录状态');
          vm.$router.replace({ name: 'Login', query: { redirect: to.fullPath } });
        }
      });
    },
    goToDetail(goods) {
      this.$router.push({
        name: 'GoodsDetail',
        params: { gid: goods.gid.toString() } // 强制转为字符串
      });
    },
    fetchBanners() {
      this.$axios.get('/banner/getAllBanner')
        .then(res => {
          this.banners = res.data.filter(b => b.status);
        })
        .catch(err => {
          console.error('获取轮播图失败:', err);
          this.$message.error('轮播图加载失败');
        });
    },
    fetchGoodsByTids(tids) {
      this.selectedCid = null; // 清除分类筛选
      this.searchQuery = ''; // 清除搜索

      this.$axios.get('/goods/getByTid', {
        params: {
          tids: tids,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        },
        headers: {
          Authorization: `Bearer ${this.$store.getters.token}`,
        }
      }).then(response => {
        const filteredData = response.data.list.filter(goods => goods.isShelved);
        this.goodsList = this.shuffleArray(filteredData);
        this.total = filteredData.length;
      }).catch(error => {
        console.error('获取商品失败:', error);
        this.$message.error('获取商品失败');
      });
    },
    // 随机排序函数
    shuffleArray(array) {
      return array.sort(() => Math.random() - 0.5);
    },
    fetchGoodsList() {
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        search: this.searchQuery // 保留搜索参数
      };

      let apiPath = '/goods/getAllGoods';
     
      if (this.selectedCid !== null) {
        params.cid = this.selectedCid;
        apiPath = '/goods/getGoodsByCid';
      } else if (this.searchQuery) {
        apiPath = '/goods/getGoodsByName';
        params.name = this.searchQuery;
      }

      this.$axios.get(`http://localhost:8088${apiPath}`, {
        params: params,
        headers: {
          Authorization: `Bearer ${this.$store.getters.token}`,
        },
      })
          .then(response => {
            const pageInfo = response.data; // 假设后端返回的是 PageInfo 对象
            const filteredData = pageInfo.list.filter(goods => goods.isShelved);
            this.goodsList = this.shuffleArray(filteredData);
            // 使用后端返回的总记录数，而非当前页过滤后的数量
            this.total = pageInfo.total;
            // console.log('成功获取商品列表:', filteredData);
          })
          .catch(error => {
            console.error('获取商品列表失败', error);
            this.$message.error('获取商品列表失败');
          });
    },
    handlePageChange(newPage) {
      this.pageNum = newPage;
      this.fetchGoodsList();
    },
    handleSearch() {
      this.pageNum = 1;
      this.selectedCid = null; // 清除分类选择
      this.submenuTitle = '商品分类'; // 恢复默认标题
      this.fetchGoodsList();
    },
    addToCart(goods) {
      // 实现加入购物车逻辑
      this.$message.success(`${goods.title} 已加入购物车`);
    },
    buyNow(goods) {
      // 实现立即购买逻辑
      this.$message.success(`立即购买 ${goods.title}`);
    },
    fetchCategories() {
      this.$axios.get('http://localhost:8088/category/getAllCategory')
        .then(response => {
          this.categories = response.data;
        })
        .catch(error => {
          console.error('获取分类列表失败', error);
          this.$message.error('获取分类列表失败');
        });
    },
    handleCategorySelect(category) {
      this.submenuTitle = category.cname;
      this.selectedCategory = category;
      this.activeIndex = ''; // 清除激活状态
      this.selectedCid = category.cid; // 设置 selectedCid
      this.fetchGoodsList();
    },
    handleSelect(key) {
      this.pageNum = 1;
      this.searchQuery = '';
      this.selectedCid = null;
      this.submenuTitle = '商品分类';
      this.selectedCategory = null;

      switch (key) {
        case 'home':
          this.fetchGoodsList();
          this.$router.push({ name: 'Shop' }).catch(() => {
          });
          break;
        case 'gameZone':
          // 获取tid为1和3的商品
          this.fetchGoodsByTids(1, 3);
          break;
        case 'peripheralZone':
          // 获取tid为2的商品
          this.fetchGoodsByTids(2);
          break;
        default:
          if (key.startsWith('cat-')) {
            const categoryCid = parseInt(key.replace('cat-', ''), 10);
            this.handleCategorySelect(this.categories.find(cat => cat.cid === categoryCid));
          }
          break;
      }

      // 仅设置可激活菜单项的index
      if (['home', 'gameZone', 'peripheralZone'].includes(key)) {
        this.activeIndex = key;
      }
    },
    fetchGoodsListByCategory(categoryCid) {
      this.selectedCid = categoryCid; // 设置 selectedCid
      this.fetchGoodsList();
    },
    handleLogout() {
      this.$store.dispatch('clearUser'); // 清除普通用户信息
      this.$store.dispatch('clearToken'); // 清除 Token
      this.$router.push({ name: 'Shop' });
    },
    goToLogin() {
      this.$router.push({ name: 'Login' }); // 重定向到登录页面
    },
    goToShop() {
      this.$router.push({ name: 'Shop' }).catch(() => {
      });
      this.submenuTitle = '商品分类';
      this.selectedCategory = null;
    },
    goToProfile() {
      this.$router.push({ name: 'UserProfile' });
    },
    goToCart() {
      this.$router.push({ name: 'Cart' });
    },
    goToOrders() {
      this.$router.push({ name: 'OrderList' });
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

.banner-container {
  margin: 20px 0;
}

.price-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.stock-out {
  color: red;
  font-weight: bold;
  margin-top: 10px;
  margin-left: 15px;
  /* 与价格部分间隔 */
  white-space: nowrap;
  /* 防止文字换行 */
}

.stock-low {
  color: orange;
  font-weight: bold;
  margin-top: 10px;
  margin-left: 15px;
  /* 与价格部分间隔 */
  white-space: nowrap;
  /* 防止文字换行 */
}

.shop-container {
  padding: 20px;
  background-color: #f5f7fa;
}

/* 添加分类选中状态 */
.el-submenu__title {
  transition: color 0.3s;
}

.el-submenu.is-selected .el-submenu__title {
  color: #409eff !important;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  color: #333333;
  padding: 0 20px;
  height: 60px;
  line-height: 60px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.el-main {
  margin-top: 60px;
  /* 与header高度一致 */
  min-height: calc(100vh - 60px);
  /* 确保内容高度 */
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #ff6700;
}

.nav {
  display: flex;
  align-items: center;
}

.el-menu-demo {
  margin-right: 20px;
}

.search-input {
  width: 300px;
}

.goods-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  /* 自适应列宽 */
  gap: 20px;
  padding: 20px;
}

.goods-card {
  width: 100%;
  /* 自适应列宽 */
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s;
  pointer-events: auto !important;
  cursor: pointer;
}

.goods-card:hover {
  transform: translateY(-5px);
}

/* 优化激活状态样式 */
.el-menu--horizontal {
  /* 移除默认下划线 */
  border-bottom: none !important;

  /* 自定义激活状态 */

  .el-menu-item {
    height: 60px;
    line-height: 60px;
    border-bottom: 2px solid transparent;
    transition: border-color 0.3s;

    &.is-active {
      border-bottom-color: #409eff;
      background: none !important;
    }

    &:hover {
      background-color: #f5f7fa !important;
    }
  }

  /* 分类菜单样式 */

  .el-submenu {
    &__title {
      height: 60px;
      line-height: 60px;
      border-bottom: 2px solid transparent;
    }

    /* 选中状态指示 */

    &.is-selected .el-submenu__title {
      color: #409eff;
    }
  }
}

.el-menu--horizontal>.el-submenu.is-active .el-submenu__title {
  border-bottom: none !important;
}

.goods-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.goods-info {
  padding: 15px;
  text-align: left;
}

.goods-title {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333333;
}

.goods-details {
  font-size: 14px;
  color: #666666;
  margin-bottom: 10px;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.goods-price {
  flex: 1;
  /* 占据左侧剩余空间 */
  margin-bottom: 10px;
}

.price-label {
  font-weight: bold;
}

.price-value {
  color: #ff6700;
}

.goods-stock {
  margin-bottom: 10px;
}

.stock-label {
  font-weight: bold;
}

.stock-value {
  color: #333333;
}

.goods-actions {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 10px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* 修改对应的CSS样式 */
.user-info {
  display: flex;
  align-items: center;
  /* 新增垂直居中 */
  height: 100%;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 2px solid #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  object-fit: cover;
}

.username {
  font-size: 14px;
  line-height: 1;
  /* 确保文字行高统一 */
}

.el-icon-arrow-down {
  margin-left: 4px;
  /* 微调箭头位置 */
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  /* 新增垂直居中 */
  gap: 8px;
  /* 元素间距 */
}

.el-icon-arrow-down {
  font-size: 12px;
}

.el-button--text {
  color: #409eff;
}

.el-button--text:hover {
  color: #66b1ff;
}

.el-card__body {
  padding: 15px;
}

.el-pagination {
  margin-top: 20px;
}

.banner-container {
  margin: 20px 0;
}

.carousel-image {
  object-fit: contain;
  max-height: 100%;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.image-error-slot {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f5f7fa;
  color: #999;
}

.error-icon {
  font-size: 60px;
  margin-bottom: 10px;
}

.error-text {
  margin: 0;
  font-size: 14px;
}

.banner-section {
  margin: 20px 0;
  position: relative;
}

.carousel-item {
  position: relative;
  height: 100%;
}
</style>
