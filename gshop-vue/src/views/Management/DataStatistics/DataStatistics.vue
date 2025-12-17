<template>
  <div class="statistics-container">
    <!-- 统计卡片 -->
    <div class="card-group">
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="title">用户总数</div>
          <div class="value">{{ userCount }}</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="title">订单总数</div>
          <div class="value">{{ totalOrders }}</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="title">总销售额</div>
          <div class="value">¥{{ totalSales }}</div>
        </div>
      </el-card>
    </div>

    <!-- 销售TOP10商品 -->
    <div class="section">
      <h2>销售TOP10商品</h2>
      <el-table :data="topProducts" border style="width: 100%">
        <el-table-column prop="productName" label="商品名称"></el-table-column>
        <el-table-column prop="totalSales" label="销量">
          <template slot-scope="scope">{{ scope.row.totalSales }} 件</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="销售额">
          <template slot-scope="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 商品类型占比饼图 -->
    <div class="section">
      <h2>商品类型销售占比</h2>
      <div id="categoryChart" style="width: 100%; height: 400px;"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'; // 引入ECharts
import axios from 'axios';

export default {
  name: 'DataStatistics',
  data() {
    return {
      userCount: 0,
      totalOrders: 0,
      totalSales: 0,
      topProducts: [],
      categoryData: []
    };
  },
  created() {
    this.fetchStatistics();
    this.fetchTopProducts();
    this.fetchCategoryRatio();
  },
  methods: {
    async fetchStatistics() {
      try {
        // 获取用户总数
        const userRes = await axios.get('/user/countUsers');
        this.userCount = userRes.data;

        // 获取订单总数和总销售额
        const salesRes = await axios.get('/order/getSalesSummary');
        this.totalOrders = salesRes.data.data.totalOrders;
        this.totalSales = salesRes.data.data.totalSales;
      } catch (error) {
        console.error('获取统计信息失败:', error);
      }
    },
    async fetchTopProducts() {
      try {
        const res = await axios.get('/order/getTopSellingProducts/10');
        this.topProducts = res.data.data.map(product => ({
          productName: product.title,
          totalSales: product.total_sales,
          totalAmount: product.total_amount
        }));
      } catch (error) {
        console.error('获取TOP10商品失败:', error);
      }
    },
    async fetchCategoryRatio() {
      try {
        const res = await axios.get('/order/getCategorySalesRatio');
        this.categoryData = res.data.data;
        this.initCategoryChart();
      } catch (error) {
        console.error('获取分类占比失败:', error);
      }
    },
    initCategoryChart() {
      const chart = echarts.init(document.getElementById('categoryChart'));
      chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '商品类型占比',
          type: 'pie',
          radius: '60%',
          data: this.categoryData.map(item => ({
            name: item.categoryName,
            value: item.salesAmount
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      });
    }
  }
};
</script>

<style scoped>
.statistics-container {
  padding: 20px;
}

.card-group {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  width: 30%;
}

.stat-item {
  padding: 20px;
  text-align: center;
}

.title {
  font-size: 16px;
  color: #666;
}

.value {
  font-size: 24px;
  color: #ff6700;
  font-weight: bold;
  margin-top: 10px;
}

.section {
  margin-bottom: 30px;
}

.section h2 {
  margin-bottom: 15px;
}

.el-table {
  width: 100%;
  margin-top: 15px;
}
</style>
