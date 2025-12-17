<template>
  <el-dialog :visible.sync="dialogVisible" title="商品详情" width="80%">
    <el-row :gutter="20">
      <!-- 左侧：商品图片 -->
      <el-col :span="12">
        <div class="image-container">
          <el-image :src="firstImage" :fit="fit" class="main-image"></el-image>
          <div class="thumbnail-gallery">
            <img
                v-for="(img, index) in sortedImages"
                :key="index"
                :src="img.imgUrl"
                alt="商品缩略图"
                class="thumbnail"
                @click="setMainImage(img.imgUrl)"
            />
          </div>
        </div>
      </el-col>

      <!-- 右侧：商品信息 -->
      <el-col :span="12">
        <el-card class="info-card">
          <h2 class="product-title">{{ initialGoods.title }}</h2>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="分类">
              {{ initialGoods.tid ? initialGoods.tname : '无' }} > {{
                initialGoods.cid ? initialGoods.cname : '无'
              }}
            </el-descriptions-item>
            <el-descriptions-item label="价格">￥{{ initialGoods.price }}</el-descriptions-item>
            <el-descriptions-item label="库存">{{ initialGoods.stock }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ initialGoods.isShelved ? '上架' : '下架' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ initialGoods.createTime }}</el-descriptions-item>
          </el-descriptions>
          <el-descriptions title="商品介绍" :column="1" border>
            <el-descriptions-item>
              <p class="product-details">{{ productDetails }}</p>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "GoodsView",
  props: {
    initialGoods: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      dialogVisible: false,
      fit: 'contain',
      mainImage: '' // 当前主图
    };
  },
  computed: {
    sortedImages() {
      return this.initialGoods?.images || [];
    },
    firstImage() {
      return this.mainImage || this.initialGoods?.image || this.sortedImages[0]?.imgUrl || '';
    },
    productDetails() {
      return this.initialGoods?.details || this.initialGoods?.description || '暂无商品详情';
    }
  },
  methods: {
    openDialog() {
      this.dialogVisible = true;
      this.mainImage = this.initialGoods?.image || this.sortedImages[0]?.imgUrl || '';
    },
    setMainImage(imgUrl) {
      this.mainImage = imgUrl;
    }
  }
};
</script>

<style scoped>
/* 图片容器 */
.image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

/* 主图样式 */
.main-image {
  width: 100%;
  height: 400px;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 缩略图画廊 */
.thumbnail-gallery {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding: 8px;
}

/* 缩略图样式 */
.thumbnail {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.thumbnail:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 商品信息卡片 */
.info-card {
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* 商品标题 */
.product-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
}

/* 商品详情文字 */
.product-details {
  font-size: 15px;
  line-height: 1.6;
  color: #555;
  margin: 0;
}
</style>
