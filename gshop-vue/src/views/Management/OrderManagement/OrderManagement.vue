<template>
  <div class="order-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleBatchUpdate" :disabled="!selectedOrders.length">批量修改订单状态</el-button>
      <el-input v-model="searchValue" placeholder="输入订单ID或用户ID搜索" style="width: 250px; margin-left: 20px" @input="filterOrders"></el-input>
    </div>

    <!-- 订单表格 -->
    <div class="table">
      <el-table ref="multipleTable" :data="filteredTableData" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>

        <el-table-column prop="orderId" label="订单" width="60"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="80"></el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="80">
          <template slot-scope="scope">{{ scope.row.totalAmount }} 元</template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="支付方式" width="100"></el-table-column>
        <el-table-column prop="paymentStatusText" label="支付状态" width="100"></el-table-column>
        <el-table-column prop="orderStatusText" label="订单状态" width="140">
          <template slot-scope="scope">
            <el-select
                v-model="scope.row.orderStatus"
                @change="handleSingleUpdate(scope.row)"      style="width: 100%"
                :disabled="scope.row.orderStatus === 0"
            >
            <el-option
                v-for="status in orderStatusOptions"
                :key="status.value"
                :label="status.label"
                :value="status.value"
            ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="payNum" label="支付流水号" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payTime" label="支付时间" width="180"></el-table-column>
        <el-table-column prop="orderTime" label="下单时间" width="180"></el-table-column>
        <el-table-column prop="expireTime" label="过期时间" width="180"></el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleShow(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        @current-change="handlePageChange"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
        style="text-align: right; margin-top: 20px"
      ></el-pagination>
    </div>

    <!-- 订单详情弹窗 -->
    <el-dialog :visible.sync="dialogVisible" title="订单详情" width="42%">
      <div class="order-detail">
        <!-- 订单基本信息 -->
        <div class="order-info">
          <p>订单ID: {{ detailData.orderId }}</p>
          <p>用户ID: {{ detailData.userId }}</p>
          <p>用户名: {{ detailData.username }}</p>
          <p>订单状态: {{ detailData.orderStatusText }}</p>
          <p>支付方式: {{ detailData.paymentMethod }}</p>
          <p>下单时间: {{ detailData.orderTime }}</p>
        </div>

        <!-- 商品列表 -->
        <el-table :data="detailData.items" border style="width: 100%; margin-top: 20px">
          <el-table-column prop="gid" label="商品ID" width="100"></el-table-column>
          <el-table-column prop="title" label="商品名称" width="190" show-overflow-tooltip></el-table-column>
          <el-table-column prop="price" label="单价" width="140">
            <template slot-scope="scope">{{ scope.row.price }} 元</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
          <el-table-column prop="total" label="小计" width="120">
            <template slot-scope="scope">{{ (scope.row.price * scope.row.quantity).toFixed(2) }} 元</template>
          </el-table-column>
        </el-table>

        <!-- 总价 -->
        <div class="total" style="margin-top: 20px; text-align: right">
          总价：{{ detailData.totalAmount }} 元
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'OrderManagement',
  data() {
    return {
      tableData: [],
      filteredTableData: [],
      selectedOrders: [],
      pageNum: 1,
      pageSize: 20,
      dialogVisible: false,
      detailData: {},
      total: 0,
      searchValue: '',
      orderStatusOptions: [
        { label: '已取消', value: 0 },
        { label: '待发货', value: 1 },
        { label: '已发货', value: 2 },
        { label: '已签收', value: 3 }
      ]
    }
  },
  created() {
    this.fetchOrders()
  },
  methods: {
    async handleShow(row) {
      try {
        // 获取订单基本信息
        const orderRes = await axios.get(`/order/getOrderById/${row.orderId}`);
        const order = orderRes.data.data[0]; // 假设返回数据是数组

        // 获取订单项列表
        const itemsRes = await axios.get(`/order/getOrderItemsByOrderId/${row.orderId}`);
        const items = itemsRes.data.data;

        // 合并数据并补充状态文本
        this.detailData = {
          ...order,
          items: items,
          orderStatusText: this.getOrderStatusText(order.orderStatus), // ✅ 新增状态转换
          totalAmount: items.reduce((sum, item) => sum + item.price * item.quantity, 0)
        };

        // 获取用户名
        const userRes = await axios.get(`/user/getUserById/${order.userId}`);
        this.detailData.username = userRes.data.data.username;

        this.dialogVisible = true;
      } catch (error) {
        console.error('获取订单详情失败:', error);
        this.$message.error('获取订单详情失败');
      }
    },
    async fetchOrders() {
      try {
        const res = await axios.get('/order/getAllOrder', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize
          }
        })
        this.tableData = res.data.data.list.map(item => ({
          ...item,
          paymentStatusText: this.getPaymentStatusText(item.paymentStatus),
          orderStatusText: this.getOrderStatusText(item.orderStatus)
        }))
        this.filteredTableData = [...this.tableData]
        this.total = res.data.data.total
      } catch (error) {
        console.error('获取订单失败:', error)
      }
    },
    handleSelectionChange(val) {
      this.selectedOrders = val
    },
    handleBatchUpdate() {
      const newStatus = prompt('请输入要设置的订单状态（0-取消，1-待发货，2-已发货，3-已签收）')
      if (newStatus === null) return
      const status = parseInt(newStatus)
      if (this.selectedOrders.some(order => order.orderStatus === 0)) {
        this.$message.warning('已取消的订单不可批量修改状态')
        return
      }
      if (![0,1,2,3].includes(status)) {
        this.$message.error('状态值不合法')
        return
      }
      this.selectedOrders.forEach(order => {
        this.updateOrderStatus(order.orderId, status)
      })
    },
    handleSingleUpdate(row) {
      this.updateOrderStatus(row.orderId, row.orderStatus)
    },
    async updateOrderStatus(orderId, status) {
      try {
        // 封装 Order 对象
        const orderData = {
          orderId: orderId,
          orderStatus: status
        };

        // 发送 PUT 请求
        await axios.put(`/order/updateOrderStatus`, orderData);

        this.$message.success('状态更新成功');
        await this.fetchOrders();
      } catch (error) {
        this.$message.error('更新失败，请重试');
      }
    }
    ,
    handlePageChange(newPage) {
      this.pageNum = newPage
      this.fetchOrders()
    },
    filterOrders() {
      this.filteredTableData = this.tableData.filter(item => {
        return (
          item.orderId.toString().includes(this.searchValue) ||
          item.userId.toString().includes(this.searchValue)
        )
      })
    },
    getPaymentStatusText(code) {
      const statusMap = {
        0: '未支付',
        1: '已支付',
        2: '支付失败'
      }
      return statusMap[code] || '未知'
    },
    getOrderStatusText(code) {
      const statusMap = {
        0: '已取消',
        1: '待发货',
        2: '已发货',
        3: '已签收'
      }
      return statusMap[code] || '未知'
    }
  }
}
</script>

<style scoped>
.order-detail .order-info {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 20px;
}

.order-detail .total {
  font-size: 1.2em;
  color: #e6a23c;
  font-weight: bold;
}

.order-detail .el-table {
  border: 1px solid #ebeef5;
}

.order-detail {
  border-radius: 4px;
}

.toolbar {
  margin: 20px 20px;
  display: flex;
  align-items: center;
}

.toolbar .el-button {
  margin-right: 10px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 表格列样式优化 */
 .el-table .cell {
  white-space: nowrap;
}

.el-table .el-select {
  width: 100%;
}

/* 表格行悬停效果 */
.el-table--enable-row-hover .el-table__body-tr:hover > td {
  background-color: #f5f7fa;
}
</style>
