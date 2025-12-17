<!-- Carousel.vue -->
<template>
    <el-carousel 
      :interval="5000"
      indicator-position="outside"
      class="banner-container"
    >
      <el-carousel-item v-for="banner in banners" :key="banner.bid">
        <a 
          v-if="banner.jump" 
          :href="banner.jump" 
          target="_blank"
          class="carousel-link"
        >
          <img 
            :src="banner.image" 
            @error="handleImageError" 
            class="carousel-image"
          />
        </a>
        <img 
          v-else 
          :src="banner.image" 
          @error="handleImageError" 
          class="carousel-image"
        >
      </el-carousel-item>
    </el-carousel>
  </template>

<script>
export default {
    name: 'Carousel',
    props: {
        banners: {
            type: Array,
            required: true
        }
    },
    methods: {
        handleImageError(event) {
            event.target.src = 'https://via.placeholder.com/1200x400';
        }
    }
}
</script>

<style scoped>
.carousel-image {
    width: 100%;
    height: 100%;
    display: block;
    object-fit: cover;
}

.carousel-link {
    display: block;
    width: 100%;
    height: 100%;
}

.image-error-slot {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    background: #f5f7fa;
    color: #999;
}

.error-icon {
    font-size: 60px;
    margin-bottom: 10px;
}

.error-text {
    margin: 0;
    font-size: 14px;
}
</style>