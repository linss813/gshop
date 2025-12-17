<template>
  <div class="order-list-container">
    <!-- 订单状态统计卡片 -->
    <el-row :gutter="20" class="status-cards">
      <el-col :span="6" v-for="(count, status) in orderStatusCounts" :key="status">
        <el-card class="status-card">
          <div class="card-title">{{ statusLabels[status] }}</div>
          <div class="card-count">{{ count }} 单</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 订单表格 -->
    <el-table :data="orders" stripe style="width: 100%" row-key="orderId">
      <!-- 订单编号 -->
      <el-table-column prop="orderId" label="订单编号" />

      <!-- 总金额 -->
      <el-table-column prop="totalAmount" label="总金额">
        <template #default="{ row }">
          ￥{{ row.totalAmount || 0 }}
        </template>
      </el-table-column>

      <!-- 商品信息 -->
      <el-table-column label="商品信息">
        <template #default="{ row }">
          <div class="item-list">
            {{ formatOrderItems(row.items) }}
          </div>
        </template>
      </el-table-column>

      <!-- 支付状态 -->
      <el-table-column prop="paymentStatus" label="支付状态">
        <template #default="{ row }">
          {{ row.paymentStatus | paymentStatusFilter }}
        </template>
      </el-table-column>

      <!-- 订单状态 -->
      <el-table-column prop="orderStatus" label="订单状态">
        <template #default="{ row }">
          {{ row.orderStatus | orderStatusFilter }}
        </template>
      </el-table-column>

      <!-- 创建时间 -->
      <el-table-column prop="orderTime" label="创建时间">
        <template #default="{ row }">
          {{ row.orderTime | formatDate }}
        </template>
      </el-table-column>

      <!-- 操作 -->
      <el-table-column label="操作">
        <template #default="{ row }">
          <div class="actions">
            <el-button
              v-if="row.orderStatus !== 0 && row.paymentStatus === 0"
              type="primary"
              size="small"
              @click="payOrder(row.orderId)"
            >
              立即支付
            </el-button>
            <el-button
              v-if="row.orderStatus === 0"
              type="danger"
              size="small"
              @click="deleteOrder(row.orderId)"
            >
              删除订单
            </el-button>
            <el-button
              v-if="row.orderStatus === 2"
              type="success"
              size="small"
              @click="signOrder(row.orderId)"
            >
              确认签收
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      orders: [],
      statusLabels: ['待付款', '已付款', '已发货', '已完成']
    };
  },
  computed: {
    // 统计各订单状态数量
    orderStatusCounts() {
      const counts = [0, 0, 0, 0];
      this.orders.forEach(order => {
        if (order.orderStatus >= 0 && order.orderStatus <= 3) {
          counts[order.orderStatus]++;
        }
      });
      return {
        0: counts[0],
        1: counts[1],
        2: counts[2],
        3: counts[3]
      };
    }
  },
  created() {
    this.fetchOrders();
  },
  methods: {
    fetchOrders() {
      this.$axios.get(
        `/order/getOrderByUserId/${this.$store.getters.currentUser.uid}`
      ).then(res => {
        this.orders = res.data.data || [];
      }).catch(() => {
        this.$message.error("加载失败");
      });
    },

    formatOrderItems(items) {
      if (!items || items.length === 0) return '暂无商品';
      return items.map(item => `${item.title} × ${item.quantity}`).join('； ');
    },

    // 支付订单
    payOrder(orderId) {
      const order = this.orders.find(o => o.orderId === orderId);
      if (!order) {
        this.$message.error('订单不存在');
        return;
      }
      if (order.orderStatus === 0) {
        this.$message.warning('已取消的订单无法支付');
        return;
      }
      this.$confirm('确认支付该订单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post(`/order/payOrder/${orderId}`)
          .then(res => {
            this.$message.success('支付成功');
            this.fetchOrders(); // 刷新订单
          })
          .catch(err => {
            console.error('支付失败:', err);
            this.$message.error('支付失败，请重试');
          });
      }).catch(() => {
        this.$message.info('已取消支付');
      });
    },

    // 删除订单
    deleteOrder(orderId) {
      const order = this.orders.find(o => o.orderId === orderId);
      if (!order) {
        this.$message.error('订单不存在');
        return;
      }
      if (order.orderStatus === 0) {
        this.$confirm('该订单已取消，是否立即删除？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete(`/order/deleteOrder/${orderId}`)
            .then(res => {
              this.$message.success('订单已删除');
              this.fetchOrders(); // 刷新订单
            })
            .catch(err => {
              console.error('删除失败:', err);
              this.$message.error('删除订单失败，请重试');
            });
        }).catch(() => {
          this.$message.info('已取消操作');
        });
      } else {
        this.$confirm('确认删除该订单？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete(`/order/deleteOrder/${orderId}`)
            .then(res => {
              this.$message.success('订单已删除');
              this.fetchOrders(); // 刷新订单
            })
            .catch(err => {
              console.error('删除失败:', err);
              this.$message.error('删除订单失败，请重试');
            });
        }).catch(() => {
          this.$message.info('已取消操作');
        });
      }
    },

    // 确认签收订单
    signOrder(orderId) {
      const order = this.orders.find(o => o.orderId === orderId);
      if (!order) {
        this.$message.error('订单不存在');
        return;
      }
      if (order.orderStatus === 0) {
        this.$message.warning('已取消的订单无法签收');
        return;
      }
      this.$confirm('确认已收到货物？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 构造请求体，设置订单状态为 “已签收”
        const signOrderData = {
          orderId,
          orderStatus: 3 // 已签收
        };

        this.$axios.put(`/order/updateOrderStatus`, signOrderData)
            .then(res => {
              if (res.data.code === 200) {
                this.$message.success('签收成功');
                this.fetchOrders(); 
              } else {
                this.$message.error(res.data.message || '签收失败');
              }
            })
            .catch(err => {
              console.error('签收失败:', err);
              this.$message.error('网络异常，请稍后重试');
            });
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    }
  },
  filters: {
    paymentStatusFilter(status) {
      return ["未支付", "已支付", "支付失败"][status] || "未知状态";
    },
    orderStatusFilter(status) {
      return ["已取消", "待发货", "已发货", "已签收"][status] || "未知状态";
    },
    formatDate(time) {
      if (!time) return '';
      const date = new Date(time);
      return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
    }
  }
};
</script>

<style scoped>
.order-list-container {
  padding: 20px;
}

.status-cards {
  margin-bottom: 20px;
}

.status-card {
  text-align: center;
  font-size: 14px;
  color: #666;
}

.card-title {
  font-weight: bold;
  margin-bottom: 5px;
}

.card-count {
  font-size: 20px;
  color: #333;
  margin-top: 5px;
}

.actions {
  display: flex;
  gap: 8px;
}

.item-list {
  white-space: pre-wrap;
  font-size: 13px;
  color: #666;
}
</style>
