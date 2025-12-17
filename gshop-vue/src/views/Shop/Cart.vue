<template>
  <div class="cart-container">
    <!-- 购物车为空提示 -->
    <div v-if="cartItems.length === 0" class="empty-tip">
      购物车为空，快去 <router-link to="/shop">选购商品</router-link> 吧！
    </div>

    <!-- 购物车表格 -->
    <el-table
      :data="cartItems"
      ref="cartTable"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <!-- 多选列 -->
      <el-table-column type="selection" width="55" align="center"></el-table-column>

      <!-- 商品信息列 -->
      <el-table-column label="商品信息" width="400">
        <template slot-scope="scope">
          <div class="product-details">
            <div class="title" @click="goToDetail(scope.row.gid)">
              {{ scope.row.title }}
            </div>
            <div class="price">￥{{ scope.row.price || 0 }}</div>
          </div>
        </template>
      </el-table-column>

      <!-- 数量列 -->
      <el-table-column prop="quantity" label="数量" width="240">
        <template slot-scope="scope">
          <el-input-number
            v-model="scope.row.quantity"
            :min="1"
            :max="scope.row.stock || 100"
            @change="updateQuantity(scope.row)"
          ></el-input-number>
        </template>
      </el-table-column>

      <!-- 小计列 -->
      <el-table-column label="小计">
        <template slot-scope="scope">
          ￥{{ (scope.row.quantity || 1) * (scope.row.price || 0) }}
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" width="80">
        <template slot-scope="scope">
          <el-button
            type="text"
            icon="el-icon-delete"
            @click="removeItem(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部操作栏 -->
    <div class="cart-footer" v-if="cartItems.length > 0">
      <div class="left">
        <el-checkbox @change="toggleSelectAll">全选</el-checkbox>
      </div>
      <div class="right">
        <span style="margin-right: 10px">合计：￥{{ total }}</span>
        <el-button @click="deleteSelected" type="danger" class="mr-10">批量删除</el-button>
        <el-button type="primary" @click="checkout">去结算</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cartItems: [],
      selectedItems: []
    };
  },
  computed: {
    total() {
      return this.selectedItems.reduce((sum, item) => sum + item.quantity * item.price, 0);
    }
  },
  created() {
    this.fetchCart();
  },
  methods: {
    async fetchCart() {
      try {
        const res = await this.$axios.get(
          `/cart/getCartByUserId/${this.$store.getters.currentUser.uid}`
        );
        this.cartItems = (res.data.cartItems || []).filter(item =>
          item.gid !== null &&
          item.title !== null &&
          item.price !== null &&
          item.quantity !== null
        );

        await Promise.all(
          this.cartItems.map(async item => {
            if (!item.title || !item.price) {
              const goodsRes = await this.$axios.get(
                `/goods/getGoodsById/${item.gid}`
              );
              item.title = goodsRes.data.title;
              item.price = goodsRes.data.price;
            }
          })
        );
      } catch (error) {
        if (
          error.response?.status === 404 &&
          error.config.url.includes('getCartByUserId')
        ) {
          // 自动创建购物车
          await this.$axios.post(
            `/cart/addCartByUserId/${this.$store.getters.currentUser.uid}`
          );
          await this.fetchCart();
        } else {
          this.$message.error('获取购物车数据失败');
        }
      }
    },

    updateQuantity(item) {
      this.$axios.put('/cart/updateCartItem', item).then(() => {
        this.fetchCart();
      });
    },

    async removeItem(item) {
      try {
        await this.$axios.delete(
          `/cart/deleteCartItem/${item.itemId}`
        );
        this.$message.success('删除成功');
        await this.fetchCart();
      } catch (error) {
        this.$message.error('删除失败，请重试');
      }
    },

    async deleteSelected() {
      if (!this.selectedItems.length) {
        return this.$message.warning('请先选择要删除的商品');
      }

      try {
        await Promise.all(
          this.selectedItems.map(item =>
            this.$axios.delete(
              `/cart/deleteCartItem/${item.itemId}`
            )
          )
        );
        this.$message.success('删除成功');
        this.fetchCart();
      } catch (error) {
        this.$message.error('删除失败，请重试');
      }
    },

    // 去结算按钮逻辑
    async checkout() {
      if (!this.selectedItems.length) {
        return this.$message.error('请选择商品');
      }

      try {
        const res = await this.$axios.post(
          '/order/createOrder',
          {
            userId: this.$store.getters.currentUser.uid,
            items: this.selectedItems.map(item => ({
              gid: item.gid,
              quantity: item.quantity,
              price: item.price,
              title: item.title
            }))
          }
        );

        const orderId = res.data.data; // 获取生成的订单ID
        console.log('订单已创建，订单ID：', orderId)
        this.showPayConfirmDialog(orderId); // 弹出是否支付对话框
      } catch (err) {
        this.$message.error('订单创建失败');
      }
    },

    // 显示是否支付的确认框
    showPayConfirmDialog(orderId) {
      this.$confirm('订单已创建，是否立即支付？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.payOrder(orderId); // 用户确认支付
      }).catch(() => {
        this.$message.info('已取消支付');
      });
    },

    // 支付订单方法
    async payOrder(orderId) {
      try {
        await this.$axios.post(`/order/payOrder/${orderId}`);
        this.$message.success('支付成功');
        // 删除购物车中已支付的商品条目
        await Promise.all(
            this.selectedItems.map(item =>
                this.$axios.delete(`/cart/deleteCartItem/${item.itemId}`)
            )
        );
        await this.fetchCart(); // 刷新购物车
      } catch (err) {
        console.error('支付失败:', err);
        this.$message.error('支付失败，请重试');
      }
    },

    handleSelectionChange(selection) {
      this.selectedItems = selection;
    },
    toggleSelectAll(val) {
      this.$refs.cartTable.toggleAllSelection();
    }
  },
  filters: {
    orderStatusFilter(status) {
      return ['待付款', '已付款', '已发货', '已完成'][status] || '未知';
    },
    formatDate(time) {
      if (!time) return '';
      const date = new Date(time);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
    }
  }
};
</script>

<style scoped>
.cart-container {
  padding: 20px;
}
.product-details {
  flex: 1;
}
.title {
  cursor: pointer;
  color: #409EFF;
}
.cart-footer {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  background: #f5f7fa;
  margin-top: 20px;
}
</style>
