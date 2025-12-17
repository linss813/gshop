// AdminLogin.vue
<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="80px" class="login-form">
      <h2 class="login-title">管理员登录</h2>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        username: 'admin123',
        password: '123456'
      },
      loginRules: {
        username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空', trigger: 'blur'}]
      }
    };
  },
  methods: {

    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$axios.post('http://localhost:8088/admin/login', this.loginForm)
              .then(async response => {
                const token = response.data;
                await this.$store.dispatch('setAdminToken', token); // 存储 admin token
                // 获取管理员信息并存储
                const userRes = await this.$axios.get('http://localhost:8088/admin/current', {
                  headers: { Authorization: `Bearer ${token}` }
                });
                await this.$store.dispatch('setAdminUser', userRes.data); // 存储 adminUser

                const redirect = this.$route.query.redirect || '/admin/home/index';
                this.$router.replace(redirect).catch(() => {});
              })
              .catch(error => {
                this.$message.error(error.response?.data?.message || '登录失败');
              });
        }
      });
    }


  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-form {
  width: 300px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
}
</style>
