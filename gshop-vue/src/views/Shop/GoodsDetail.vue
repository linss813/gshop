<template>
  <div class="goods-detail-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ name: 'Shop' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ goods.title }}</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 商品主图与操作栏 -->
    <div class="goods-main">
      <!-- 左侧图片 -->
      <div class="goods-gallery">
        <div class="aspect-ratio-container">
          <img :src="goods.image" class="gallery-image" @error="handleImageError">
        </div>
      </div>

      <!-- 右侧操作栏 -->
      <div class="goods-actions">
        <h2 class="goods-title">{{ goods.title }}</h2>
        <div class="price-section">
          <span class="price">￥{{ goods.price }}</span>
          <span v-if="goods.stock === 0" class="stock stock-out">已售空</span>
          <span v-else-if="goods.stock < 300" class="stock stock-low">即将售空</span>
        </div>

        <div class="button-group">
          <el-button type="primary" @click="addToCart" size="large" round>加入购物车</el-button>
          <el-button
              type="success"
              @click="buyNow"
              :disabled="goods.stock === 0"
              size="large"
              round
          >
            立即购买
          </el-button>
        </div>

        <div class="specs">
          <div class="spec-item">
            <span class="spec-label">分类：</span>
            <span class="spec-value">{{ goods.cname }}</span>
          </div>
          <div class="spec-item">
            <span class="spec-label">类型：</span>
            <span class="spec-value">{{ goods.tname }}</span>
          </div>
        </div>

      </div>

    </div>

    <!-- 商品详情与评价 -->
    <div class="goods-details">
      <div class="tab-container">
        <div class="tab" :class="{ active: currentTab === 'description' }" @click="currentTab = 'description'">
          商品详情
        </div>

        <div class="tab" :class="{ active: currentTab === 'review' }" @click="currentTab = 'review'">
          用户评价（{{ reviewCount }}）
        </div>
      </div>

      <div v-if="currentTab === 'description'">
        <div class="description-content" v-html="goods.details"></div>
      </div>
      <div v-if="currentTab === 'review'">
        <div class="comment-form">
          <h3>发表评论</h3>
          <el-rate
              v-model="currentRating"
              :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
          ></el-rate>
          <el-input
              type="textarea"
              :rows="4"
              placeholder="请输入您的评价内容"
              v-model="commentContent"
          ></el-input>
          <el-button
              type="primary"
              @click="submitReview"
              :disabled="!currentRating || !commentContent"
              class="submit-btn"
          >
            提交评价
          </el-button>
        </div>

        <review-section
            :reviews="reviews"
            :total-review-count="reviewCount"
            :average-rating="goods.rating || averageRating"
            :page-size="pageSize"
            @load-page="fetchReviews"
        />
      </div>
    </div>

    <!-- 相关商品推荐 -->
    <related-goods
        :goods-list="relatedGoods"
        @item-click="goToDetail"
    ></related-goods>
  </div>
</template>

<script>
import RelatedGoods from '@/views/Shop/RelatedGoods.vue';
import ReviewSection from '@/views/Shop/ReviewSection.vue';

export default {
  name: "GoodsDetail",
  components: {
    ReviewSection,
    RelatedGoods
  },
  data() {
    return {
      goods: {
        details: '',
        rating: 0
      },
      reviews: [],
      reviewCount: 0,
      averageRating: 0,
      currentTab: 'description',
      relatedGoods: [],
      cartItemCount: 0,
      currentRating: null,
      commentContent: '',
      pageSize: 7,
      currentPage: 1
    };
  },
  computed: {
    userId() {
      // 优先从普通用户中获取 id
      return this.$store.getters.currentUser?.uid
    }
  },
  mounted() {
    this.fetchCartCount();
  },
  created() {
    this.fetchGoodsDetail()
        .then(() => {
          this.fetchRelatedGoods();
          this.fetchReviews(1); // 初始化加载第一页
        })
        .catch(() => {
          this.$message.error('商品信息加载失败');
        });
  },
  watch: {
    '$route.params.gid': {
      immediate: true,
      handler(newGid) {
        if (newGid) {
          this.fetchGoodsDetail().then(() => {
            this.fetchRelatedGoods();
          });
        }
      }
    }
  },
  methods: {
    async fetchCartCount() {
      if (this.userId) {
        const res = await this.$axios.get(`/cart/getCartByUserId/${this.userId}`);
        this.cartItemCount = (res.data.cartItems || []).length;
      }
    },
    goToDetail(gid) {
      this.$router.push({name: 'GoodsDetail', params: {gid}});
    },
    async fetchGoodsDetail() {
      const gid = this.$route.params.gid;
      if (!gid) {
        this.$message.error('商品ID无效');
        return;
      }

      try {
        const res = await this.$axios.get(`/goods/getGoodsById/${gid}`);
        this.goods = res.data;
        this.goods.details = this.goods.details.replace(/\n/g, '<br>');
        return Promise.resolve();
      } catch (error) {
        console.error('获取商品详情失败:', error);
        this.$message.error('商品信息加载失败');
        this.$router.back();
        return Promise.reject(error);
      }
    },
    async fetchReviews(pageNum = 1) {
      const gid = this.goods.gid;
      if (!gid) {
        this.$message.error('商品ID无效');
        return;
      }

      try {
        const res = await this.$axios.get('/review/getReviewsByGoodsId', {
          params: {
            gid,
            pageNum,
            pageSize: this.pageSize
          }
        });

        const data = res.data.data;
        // 过滤状态为 false 的评论（假设字段名为 `status`）
        const validReviews = data.list
            .filter(item => item.status) // 根据实际字段名调整（如 `isApproved`）
            .map(item => ({
              ...item,
              rating: Number(item.rating)
            }));

        this.reviews = validReviews;
        this.reviewCount = validReviews.length; // 更新评论数为过滤后的数量

        // 更新平均评分
        if (validReviews.length > 0) {
          const total = validReviews.reduce((sum, review) => sum + review.rating, 0);
          this.averageRating = parseFloat((total / validReviews.length).toFixed(1));
        }
      } catch (error) {
        this.$message.error('评论加载失败');
      }
    },
    calculateAverageRating() {
      if (!this.reviews.length) return 0;
      const total = this.reviews.reduce((sum, review) => sum + review.rating, 0);
      return Number((total / this.reviews.length).toFixed(1));
    },
    async fetchRelatedGoods() {
      try {
        // 截取商品名称前5个字符用于模糊搜索
        const searchName = this.goods.title.slice(0, 5);

        // 1. 获取同名商品（模糊搜索前5个字符）
        const nameResponse = await this.$axios.get('/goods/getGoodsByName', {
          params: {
            name: searchName,
            pageNum: 1,
            pageSize: 5
          }
        });
        const nameResults = (nameResponse.data.list || []).slice(0, 5);


        // 同分类商品
        const cidResponse = await this.$axios.get('/goods/getGoodsByCid', {
          params: {
            cid: this.goods.cid,
            pageNum: 1,
            pageSize: 5
          }
        });
        const cidResults = cidResponse.data.list;

        // 合并并去重（排除当前商品）
        const combined = [...nameResults, ...cidResults];
        const filtered = combined
            .filter(g => g.gid !== this.goods.gid) // 排除当前商品
            .filter((g, index, self) =>
                index === self.findIndex(t => t.gid === g.gid) // 唯一性过滤
            );

        // 随机排序并取前5个
        const shuffled = this.shuffleArray(filtered);
        this.relatedGoods = shuffled.slice(0, 5);
      } catch (error) {
        this.$message.warning('推荐商品加载失败');
      }
    },
    shuffleArray(array) {
      return array.sort(() => Math.random() - 0.5);
    },
    handleImageError(event) {
      event.target.src = 'https://via.placeholder.com/1920x1080';
    },
    addToCart() {
      const goods = this.goods;
      const quantity = 1;

      this.$axios.get(`/cart/getCartByUserId/${this.userId}`)
          .then(response => {
            const userCart = response.data;
            const cartId = userCart ? userCart.cartId : null;

            if (!userCart) {
              // 创建新购物车并返回新 cartId
              return this.$axios.post(`/cart/addCartByUserId/${this.userId}`)
                  .then(createResp => createResp.data);
            } else {
              return Promise.resolve(userCart.cartId);
            }
          })
          .then(cartId => {
            // 调用 methods 中的 addCartItem 方法
            this.addCartItem(cartId, goods, quantity);
          })
          .catch(error => this.handleError(error));
    },

    addCartItem(cartId, goods, quantity) {
      const cartItem = {
        cartId: cartId,
        gid: goods.gid,
        quantity: quantity,
        price: goods.price,
        title: goods.title
      };

      this.$axios.post('/cart/addCartItem', cartItem)
          .then(() => {
            this.$message.success('已加入购物车');
          })
          .catch(error => this.handleError(error));
    },
    handleError(error) {
      console.error('购物车操作失败:', error);
      this.$message.error('加入购物车失败，请重试');
    },
    getUserId() {
      const storedUserId = localStorage.getItem('userId');
      if (storedUserId) return storedUserId;
      if (this.$store && this.$store.state.user && this.$store.state.user.id) {
        return this.$store.state.user.id;
      }
      return null;
    },
    buyNow() {
      if (this.goods.stock === 0) return;

      const userId = this.userId;
      if (!userId) {
        this.$message.error('请先登录');
        return;
      }

      this.$axios.post('/order/createOrder', {
        userId,
        items: [{
          gid: this.goods.gid,
          quantity: 1,
          price: this.goods.price
        }]
      }).then(res => {
        const orderId = res.data.data;
        // console.log(orderId)
        this.showConfirmDialog(orderId);

      }).catch(err => {
        this.$message.error('创建订单失败，请重试');
      });
    },
    showConfirmDialog(orderId) {
      this.$confirm('订单已生成，是否确认支付？', '订单确认', {
        confirmButtonText: '立即支付',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 2. 调用支付接口
        this.$axios.post(`/order/payOrder/${orderId}`)
            .then(res => {
              this.$message.success('支付成功');
              // 跳转到支付结果页或订单详情页
              // this.$router.push({name: 'OrderDetail', params: {orderId}});
            }).catch(err => {
          this.$message.error('支付失败，请重试');
        });
      }).catch(() => {
        this.$message.info('已取消支付');
      });
    },
    async submitReview() {
      if (typeof this.currentRating !== 'number') {
        this.$message.warning('请选择有效的评分');
        return;
      }

      const review = {
        uid: this.userId,
        gid: this.goods.gid,
        rating: this.currentRating,
        comment: this.commentContent,
      };

      try {
        // 提交评论
        await this.$axios.post('/review/insertReview', review);

        // 重置表单
        this.currentRating = null;
        this.commentContent = '';

        // 刷新商品详情和评论
        await this.fetchGoodsDetail();
        await this.fetchReviews(1);

        this.$message.success('评论提交成功');
      } catch (error) {
        this.$message.error('评论提交失败，请重试');
      }
    },
    // 分页加载方法
    handlePageChange(page) {
      this.currentPage = page;
      this.$emit('load-page', page);
    }
  }
};
</script>

<style scoped>
.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.breadcrumb .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #333;
  font-weight: bold;
}

.goods-detail-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.comment-form {
  margin-bottom: 30px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.comment-form h3 {
  margin-bottom: 15px;
}

.submit-btn {
  margin-top: 15px;
  width: 100%;
}

/* 主图与操作栏分栏布局 */
.goods-main {
  display: flex;
  gap: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

.goods-gallery {
  flex: 1;
  position: relative;
}

.aspect-ratio-container {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 比例 */
  height: 0;
  overflow: hidden;
}

.gallery-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.gallery-image:hover {
  transform: scale(1.05);
}

.goods-actions {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-left: 30px;
}

.goods-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 15px;
}

.price-section {
  display: flex;
  align-items: center;
}

.price {
  color: #ff6700;
  font-size: 28px;
  font-weight: bold;
  margin-right: 15px;
}

.stock {
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
  margin-top: 5px;
}

.stock-out {
  background: #ff494c;
  color: white;
}

.stock-low {
  background: #ff9900;
  color: white;
}

.button-group {
  display: flex;
  gap: 15px;
}

.specs {
  margin-top: 20px;
  background: #f5f7fa;
  padding: 15px;
  border-radius: 8px;
}

.spec-item {
  display: flex;
  align-items: center;
  margin: 10px 0;
}

.spec-label {
  color: #666;
  font-weight: bold;
  width: 120px;
}

.spec-value {
  color: #333;
}

/* 选项卡样式 */
.tab-container {
  display: flex;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.tab {
  padding: 12px 20px;
  cursor: pointer;
  font-weight: bold;
  transition: color 0.3s;
}

.tab.active {
  color: #409eff;
  border-bottom: 2px solid #409eff;
}

/* 商品详情内容 */
.description-content {
  line-height: 1.6;
  font-size: 14px;
  color: #666;
  padding: 20px;
}

.review-content {
  padding: 20px;
}

.goods-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}
</style>
