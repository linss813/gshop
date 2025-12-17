// mixins/auth.js
export default {
  computed: {
    isAdmin() {
      return !!this.$store.getters.adminUser;
    },
    isUserLoggedIn() {
      return !!this.$store.getters.user;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      // 管理员权限检查
      if (to.meta.requiresAdmin && !vm.isAdmin) {
        next({ name: 'AdminLogin' });
      }
      // 普通用户登录检查
      else if (to.meta.requiresAuth && !vm.isUserLoggedIn) {
        next({ name: 'Login' });
      } else {
        next();
      }
    });
  }
};
