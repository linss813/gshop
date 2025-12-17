<template>
  <div class="related-goods">
    <h3>相关推荐</h3>
    <div class="goods-list">
      <div
        class="goods-item"
        v-for="goods in goodsList"
        :key="goods.gid"
        @click="$emit('item-click', goods.gid)"
      >
        <div class="goods-image">
          <img :src="goods.image" @error="handleImageError">
        </div>
        <div class="goods-info">
          <p class="goods-name">{{ goods.title }}</p>
          <div class="price">￥{{ goods.price }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RelatedGoods",
  props: {
    goodsList: {
      type: Array,
      required: true
    }
  },
  methods: {
    handleImageError(event) {
      event.target.src = 'https://via.placeholder.com/1920x1080';
    }
  }
};
</script>

<style scoped>
/* 复制原组件相关样式（保持与原布局一致） */
.related-goods {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.05);
}

.goods-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.goods-item {
  cursor: pointer;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: transform 0.2s;
}

.goods-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.goods-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.goods-info {
  padding: 10px;
  text-align: left;
}

.goods-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 5px;
}

.price {
  color: #ff6700;
  font-size: 18px;
  font-weight: bold;
}
</style>
