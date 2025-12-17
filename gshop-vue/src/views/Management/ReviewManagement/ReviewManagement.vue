<template>
  <div class="review-management">
    <!-- 搜索栏 -->
    <el-form :inline="true" class="demo-form-inline mb-20">
<!--      <el-form-item label="商品ID">-->
<!--        <el-input v-model="search.gid" placeholder="请输入商品ID"/>-->
<!--      </el-form-item>-->
      <el-form-item label="评论内容">
        <el-input v-model="search.content" placeholder="模糊搜索"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
      <el-button type="success" :disabled="!selectedIds.length" @click="batchApprove"
                 style="float:right;margin-left:20px">
        批量审核
      </el-button>
      <el-button type="warning" :disabled="!selectedIds.length" @click="batchReject" style="float:right">
        批量撤销
      </el-button>
    </el-form>

    <!-- 表格 -->
    <el-table
        :data="reviewList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="reviewId" label="评论ID" width="100"/>
      <el-table-column prop="username" label="用户" width="120"/>
      <el-table-column prop="title" label="商品名称" show-overflow-tooltip/>
      <el-table-column prop="rating" label="评分" width="150">
        <template #default="scope">
          <el-rate
              :value="Number(scope.row.rating)"
          disabled
          show-score
          text-color="#ff9900"
          score-template="{score}"
          />
        </template>
      </el-table-column>
      <el-table-column prop="comment" label="评论内容" min-width="200">
        <template #default="scope">
          <span class="text-ellipsis" :title="scope.row.comment">{{ scope.row.comment }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status ? 'success' : 'danger'">
            {{ scope.row.status ? '已显示' : '未审核' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="160">
        <template #default="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button
              v-if="!scope.row.status"
              type="success"
              size="mini"
              @click="handleApprove(scope.row)"
          >
            审核通过
          </el-button>
          <el-button
              v-if="scope.row.status"
              type="warning"
              size="mini"
              @click="handleReject(scope.row)"
          >
            撤销审核
          </el-button>
          <el-button
              type="danger"
              size="mini"
              @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        class="mt-20"
        @current-change="handlePageChange"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
    />
  </div>
</template>

<script>
import moment from 'moment';

export default {
  data() {
    return {
      pageNum: 1,
      pageSize: 20,
      total: 0,
      reviewList: [],
      search: {
        gid: null,
        content: ''
      },
      selectedIds: []
    };
  },

  methods: {
    async fetchData() {
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        };
        const res = await this.$axios.get('/review/getAllReviews', {params});
        this.processData(res);
        console.log(res);
      } catch (error) {
        this.$message.error('数据加载失败');
      }
    },

    async fetchDataWithSearch() {
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          content: this.search.content // 移除 gid 参数
        };
        const res = await this.$axios.get(
            '/review/searchReviewsByContent',
            { params }
        );
        this.processData(res);
      } catch (error) {
        this.$message.error('搜索失败');
      }
    },

    // 公共数据处理函数
    processData(res) {
      const backendData = res.data.data;
      // 确保后端返回的 list 和 total 存在
      if (backendData.list && backendData.total) {
        this.reviewList = backendData.list.map(item => ({
          ...item,
          rating: Number(item.rating), // 强制转换为数字
          createdAt: new Date(item.createdAt) // 确保日期格式正确
        }));
        this.total = Number(backendData.total); // 确保 total 是数字
      }
    },

    handleSearch() {
      this.pageNum = 1;
      if (this.search.content) { // 仅检查 content 是否存在
        this.fetchDataWithSearch();
      } else {
        this.fetchData();
      }
    },

    resetSearch() {
      this.search = {
        gid: null,
        content: ''
      };
      this.handleSearch();
    },

    handleSelectionChange(val) {
      this.selectedIds = val.map(item => item.reviewId);
    },

    async handleApprove(row) {
      await this.$confirm('确认审核通过该评论？', '提示', {type: 'success'});
      await this.$axios.put('/review/updateReview', {
        reviewId: row.reviewId,
        status: true
      });
      this.$message.success('审核成功');
      this.fetchData();
    },

    async handleReject(row) {
      await this.$confirm('确认撤销审核？', '提示', {type: 'warning'});
      await this.$axios.put('/review/updateReview', {
        reviewId: row.reviewId,
        status: false
      });
      this.$message.success('撤销成功');
      this.fetchData();
    },

    async handleDelete(row) {
      await this.$confirm('确定删除该评论？', '提示', {type: 'warning'});
      await this.$axios.delete('/review/deleteReview', {params: {reviewId: row.reviewId}});
      this.$message.success('删除成功');
      this.fetchData();
    },

    async batchApprove() {
      await this.$confirm('确认批量审核？', '提示', {type: 'success'});
      await this.$axios.post('/review/batchApprove', this.selectedIds);
      this.$message.success('批量审核成功');
      this.fetchData();
    },

    async batchReject() {
      await this.$confirm('确认批量撤销？', '提示', {type: 'warning'});
      await this.$axios.post('/review/batchReject', this.selectedIds);
      this.$message.success('批量撤销成功');
      this.fetchData();
    },

    handlePageChange(page) {
      this.pageNum = page;
      this.fetchData();
    },

    formatDate(date) {
      if (date) {
        return moment(date).format('YYYY-MM-DD HH:mm');
      }
      return '未知';
    }
  },

  created() {
    this.fetchData();
  }
};
</script>

<style scoped>
.mb-20 {
  margin-left: 20px;
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
