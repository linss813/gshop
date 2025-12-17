<template>
  <el-main>
    <!-- 轮播图 -->
    <carousel :banners="banners" />

    <!-- 商品列表 -->
    <div class="goods-list">
      <el-card
        v-for="goods in goodsList"
        :key="goods.gid"
        class="goods-card"
        @click="goToDetail(goods)"
      >
        <div class="goods-image">
          <img :src="goods.image" alt="商品图片" />
        </div>
        <div class="goods-info">
          <h3 class="goods-title">{{ goods.title }}</h3>
          <p class="goods-details">{{ goods.details }}</p>
          <div class="price-container">
            <div class="goods-price">
              <span class="price-label">价格:</span>
              <span class="price-value">{{ goods.price }} 元</span>
            </div>
            <div v-if="goods.stock === 0" class="stock-out">已售空</div>
            <div v-else-if="goods.stock < 300" class="stock-low">即将售空</div>
          </div>
          <div class="goods-actions">
            <el-button
              type="primary"
              @click.stop="addToCart(goods)"
            >
              加入购物车
            </el-button>
            <el-button
              type="success"
              :disabled="goods.stock === 0"
              @click.stop="buyNow(goods)"
            >
              立即购买
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 分页 -->
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
</template>

<script>
export default {
  inject: [
    'banners',        // 轮播图数据
    'goodsList',      // 商品列表数据
    'total',          // 总商品数
    'pageNum',        // 当前页码
    'pageSize',       // 每页数量
    'fetchGoodsList', // 加载商品列表的方法
    'handlePageChange',// 分页页码变化处理
    'goToDetail',     // 跳转商品详情的方法
    'addToCart',      // 加入购物车的方法
    'buyNow'          // 立即购买的方法
  ],
  created() {
    // 如果需要初始化数据（如轮播图或商品列表），可以调用父组件的方法
    // this.fetchBanners();
    // this.fetchGoodsList();
  },
  methods: {
    // 如果需要本地方法，可在此定义
  }
};
</script>

<style scoped>
/* 如果需要覆盖样式，可在此添加 */
.goods-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.goods-card {
  width: 100%;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s;
  cursor: pointer;
}

.goods-card:hover {
  transform: translateY(-5px);
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
  color: #333;
}

.goods-details {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
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
  margin-left: 15px;
}

.stock-low {
  color: orange;
  font-weight: bold;
  margin-left: 15px;
}

.goods-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>
