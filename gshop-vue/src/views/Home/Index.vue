<template>
  <div class="home-container">
    <!-- 快捷操作卡片 -->
    <div class="action-grid">
      <div
        v-for="action in quickActions"
        :key="action.name"
        class="action-btn"
        @click="handleQuickAction(action)"
      >
        <div class="action-icon">
          <i :class="action.icon" class="icon"></i>
        </div>
        <div class="action-text">{{ action.text }}</div>
      </div>
    </div>


    <!-- 统计卡片 -->
    <div class="stat-grid">
      <div
        v-for="(stat, index) in staticStats"
        :key="index"
        class="stat-card"
        :class="stat.styleClass"
      >
        <div class="stat-value">{{ stat.value }}</div>
        <div class="stat-label">{{ stat.label }}</div>
      </div>
    </div>

    <!-- 预警列表 -->
    <div class="alert-card">
      <h3>商品预警列表</h3>
      <div class="alert-table">
        <table>
          <thead>
            <tr>
              <th>商品名称</th>
              <th>当前库存</th>
              <th>预警阈值</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="item in formattedWarningGoods"
              :key="item.goodsName"
              :class="item.status"
            >
              <td>{{ item.goodsName }}</td>
              <td>{{ item.currentStock }}</td>
              <td>{{ item.alertThreshold }}</td>
              <td :class="item.status">{{ item.status }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      quickActions: [
        { name: 'goods', text: '添加商品', icon: 'el-icon-plus', path: 'GoodsManagement' },
        { name: 'order', text: '订单管理', icon: 'el-icon-document', path: 'OrderManagement' },
        { name: 'user', text: '用户管理', icon: 'el-icon-user', path: 'UserManagement' },
        { name: 'setting', text: '商品管理', icon: 'el-icon-setting', path: 'GoodsManagement' }
      ],
      // 统计数据
      staticStats: [
        { label: '总用户数', value: 0, styleClass: 'primary' },
        { label: '总商品数', value: 0, styleClass: 'secondary' },
        { label: '总销售额', value: 0, styleClass: 'success' },
        { label: '预警商品', value: 0, styleClass: 'danger' }
      ],
      warningGoods: [],
      // 销售趋势数据
      salesData: [],
      // 商品类型数据
      categoryData: []
    };
  },
  computed: {
    formattedWarningGoods() {
      return this.warningGoods.map(item => ({
        ...item,
        status: item.currentStock <= 300 ? 'danger' : 'normal'
      }));
    }
  },
  methods: {
    handleQuickAction(action) {
      this.$router.push({ name: action.path });
    },
    // 生成日期字符串
    getDateString(daysAgo) {
      const date = new Date();
      date.setDate(date.getDate() - daysAgo);
      return `${date.getMonth()+1}-${date.getDate()}`;
    },
    // 初始化销售趋势图
    initSalesChart() {
      const chart = echarts.init(this.$refs.salesChart);
      chart.setOption({
        xAxis: { data: this.salesData.map(item => item.date) },
        yAxis: { type: 'value' },
        series: [{
          type: 'line',
          data: this.salesData.map(item => item.amount),
          itemStyle: { color: '#409EFF' }
        }]
      });
    },
    // 初始化类型占比图
    initCategoryChart() {
      const chart = echarts.init(this.$refs.categoryChart);
      chart.setOption({
        series: [{
          type: 'pie',
          radius: '60%',
          data: this.categoryData.map(item => ({
            name: item.name,
            value: item.count
          })),
          itemStyle: { color: '#67C23A' }
        }]
      });
    }
  },
  async mounted() {
    try {
      // 获取统计数值数据
      const [userRes, productRes, salesRes, alertRes] = await Promise.all([
        axios.get('/user/countUsers'),
        axios.get('/goods/getAllGoodsCount'),
        axios.get('/order/countSalesAmount'),
        axios.get('/goods/getWarningGoods')
      ]);

      console.log('用户统计:', userRes);
      console.log('商品统计:', productRes);
      console.log('销售金额:', salesRes);
      console.log('预警商品:', alertRes);

      // 更新统计数值（带容错）
      this.staticStats[0].value = typeof userRes.data === 'number' ? userRes.data : 0;
      this.staticStats[1].value = typeof productRes.data === 'number' ? productRes.data : 0;
      this.staticStats[2].value = salesRes.data && typeof salesRes.data.data === 'number' ? salesRes.data.data : 0;
      this.staticStats[3].value = Array.isArray(alertRes.data) ? alertRes.data.length : 0;

      // 更新预警商品列表
      this.warningGoods = Array.isArray(alertRes.data)
        ? alertRes.data.map(good => ({
            goodsName: good.title,
            currentStock: good.stock,
            alertThreshold: 300
          }))
        : [];

      // 获取商品类型数据
      const categoryRes = await axios.get('/category/getCategoryCounts');
      this.categoryData = categoryRes.data && Array.isArray(categoryRes.data)
        ? categoryRes.data.map(item => ({
            name: item.typeName,
            count: item.totalCount
          }))
        : [];

      // 初始化图表
      this.initSalesChart();
      this.initCategoryChart();
    } catch (error) {
      console.error('获取统计数据失败:', error);
    }
  }
};
</script>



<style scoped>
.home-container {
  padding: 20px;
  margin-left: 0; /* 移除侧边栏留白 */
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 操作按钮 */
.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.action-btn {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.action-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #e6f7ff;
  margin: 0 auto 15px;
}

/* 数据看板 */
.dashboard {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.chart-card h3 {
  margin: 0 0 15px;
  color: #303133;
}

/* 统计卡片 */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  background: #f0f9eb;
  padding: 25px;
  border-radius: 8px;
  text-align: center;
}

.stat-card.primary { background: #e6f7ff; }
.stat-card.secondary { background: #fff5f0; }
.stat-card.danger { background: #fef0f0; }

.stat-value {
  font-size: 28px;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  color: #606266;
  font-size: 14px;
}

/* 预警列表 */
.alert-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.alert-table table {
  width: 100%;
  border-collapse: collapse;
}

.alert-table th {
  background: #f5f7fa;
  padding: 12px;
  text-align: left;
}

.alert-table td {
  padding: 12px;
  border-bottom: 1px solid #eee;
}

.alert-table tr.danger td {
  background: #fff0f0;
}

.alert-table .danger {
  color: #f56c6c;
}

.alert-table .normal {
  color: #67c23a;
}

.sales-trend-card {
  margin-top: 20px;
}
</style>