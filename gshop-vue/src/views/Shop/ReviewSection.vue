<template>
  <div class="review-content">
    <el-rate
        :value="averageRating"
        disabled
        show-score
        text-color="#ff9900"
        :max="5"
    ></el-rate>

    <div v-if="reviews.length">
      <div class="review-item" v-for="review in reviews" :key="review.id">
        <div class="review-user">
          <span class="user-name">{{ review.username }}</span>
        </div>
        <div class="review-body">
          <p class="review-text">{{ review.comment }}</p>
          <div class="review-time">
            {{ formatDate(review.createdAt) }}
          </div>
        </div>
      </div>
    </div>
    <div v-else class="no-reviews">
      暂无评价，快来成为第一个评价者！
    </div>

    <el-pagination
        layout="prev, pager, next"
        :total="totalReviewCount"
        :page-size="pageSize"
        @current-change="$emit('load-page', $event)"
        style="text-align: center; margin-top: 20px;">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "ReviewSection",
  props: {
    reviews: {
      type: Array,
      required: true
    },
    totalReviewCount: {
      type: Number,
      required: true
    },
    averageRating: {
      type: Number,
      required: true
    },
    pageSize: {
      type: Number,
      default: 5
    }
  },
  methods: {
    formatDate(timestamp) {
      const date = new Date(timestamp);
      return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
    }
  }
};
</script>

<style scoped>
.review-content {
  padding: 20px;
}

.review-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  gap: 15px;
}

.review-user {
  display: flex;
  align-items: center;
  gap: 10px;
}
.review-body {
  flex: 1;
}

.review-time {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
}

.no-reviews {
  color: #999;
  text-align: center;
  padding: 20px;
}
</style>