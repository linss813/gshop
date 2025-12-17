<template>
  <el-container class="user-index-container">
    <!-- ... existing code ... -->
    <el-container>
      <el-main style="height: calc(100vh - 120px); overflow-y: auto;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'UserIndex',
  data() {
    return {
      defaultAvatar: 'https://a.520gexing.com/uploads/allimg/2021042109/uqaqhuvavt0.jpg'
    };
  },
  computed: {
    currentPath() {
      return this.$route.path;
    },
    user() {
      return this.$store.getters.currentUser; // 确保 Vuex 中有 currentUser
    }
  },
  methods: {
  }
};
</script>

<style scoped>
.user-index-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  padding: 0 20px;
  box-sizing: border-box;
}

.user-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.header-left,
.header-center,
.header-right {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.username {
  font-size: 14px;
  line-height: 1;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-aside {
  background-color: #f5f5f5;
  border-right: 1px solid #e6e6e6;
  width: 240px;
  height: calc(100vh - 60px);
}

.user-menu {
  border: none;
  width: 100%;
}

.el-menu-item i {
  margin-right: 10px;
}

.el-menu-item.is-active {
  background-color: #f0f9eb !important;
}
</style>