<!-- Login.vue -->
<template>
  <div class="login-register-container">
    <el-card class="form-card">
      <h2 class="form-title">登录</h2>
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="密码登录" name="password">
          <el-form :model="loginForm" :rules="loginRules" ref="loginForm">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="loginForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin">登录</el-button>
              <el-button @click="switchToRegister">去注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="验证码登录" name="verification">
          <el-form :model="verificationForm" :rules="verificationRules" ref="verificationForm">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="verificationForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="verificationCode">
              <el-input v-model="verificationForm.verificationCode" placeholder="请输入验证码"></el-input>
              <el-button type="success" size="small" @click="sendVerificationCode" :disabled="!verificationForm.email"
                         icon="el-icon-message">发送验证码
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleVerificationLogin">登录</el-button>
              <el-button @click="switchToRegister">去注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Login",

  data() {
    return {
      activeTab: 'password',
      loginForm: {
        email: "lin813ss@163.com",
        password: "123456",
      },
      verificationForm: {
        email: "",
        verificationCode: "",
      },
      loginRules: {
        email: [
          {required: true, message: "请输入邮箱", trigger: "blur"},
          {validator: this.validateEmail, trigger: "blur"},
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
          {min: 6, max: 16, message: "密码长度为 6 到 16 个字符", trigger: "blur"},
        ],
      },
      verificationRules: {
        email: [
          {required: true, message: "请输入邮箱", trigger: "blur"},
          {validator: this.validateEmail, trigger: "blur"},
        ],
        verificationCode: [
          {required: true, message: "请输入验证码", trigger: "blur"},
          {min: 5, max: 5, message: "验证码长度为 5 个字符", trigger: "blur"},
        ],
      },
    };
  },
  methods: {
    switchToRegister() {
      this.$router.push({name: 'Register'});
    },
    handleLoginSuccess(response) {
      const resData = response.data?.data || {}; // 调整数据层级
      const token = resData.token;
      const user = resData.user; // 直接使用返回的用户对象

      if (!token || !user) {
        console.error('缺失必要字段:', { token, user });
        this.$message.error('登录凭证获取失败');
        return;
      }

      // 存储用户信息到 store
      this.$store.dispatch('setUserToken', token);
      this.$store.dispatch('setUser', user); // 直接传递用户对象
      this.$message.success('用户登录成功');
      this.$router.push({ name: 'Shop' });
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8088/user/login', this.loginForm)
              .then(response => this.handleLoginSuccess(response))
              .catch(error => {
                this.$message.error("登录失败，请检查邮箱和密码");
                console.error(error);
              });
        }
      });
    },
    handleVerificationLogin() {
      this.$refs.verificationForm.validate((valid) => {
        if (valid) {
          axios.post(`http://localhost:8088/user/loginWithVerificationCode/${this.verificationForm.email}`, {
            verificationCode: this.verificationForm.verificationCode
          })
              .then(response => this.handleLoginSuccess(response))
              .catch(error => {
                this.$message.error("登录失败，请检查邮箱和验证码");
                console.error(error);
              });
        }
      });
    },
    validateEmail(rule, value, callback) {
      const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
      if (!emailRegex.test(value)) {
        callback(new Error("请输入有效的邮箱地址"));
      } else {
        callback();
      }
    },
    sendVerificationCode() {
      if (!this.verificationForm.email) {
        this.$message.error('请输入邮箱');
        return;
      }
      axios.post(`http://localhost:8088/user/sendVerificationCode/${this.verificationForm.email}`)
          .then(response => {
            this.$message.success(response.data);
          })
          .catch(error => {
            this.$message.error("发送验证码失败: " + error.response.data);
            console.error(error);
          });
    },
  },
};
</script>

<style scoped>
.login-register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('@/assets/bj1.jpg'); 
  background-size: cover; 
  background-position: center; 
  background-repeat: no-repeat;
}
.form-card {
  width: 400px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.929); 
}

.form-title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.el-form-item__content {
  display: flex;
  align-items: center;
}

.el-form-item__content .el-button {
  margin-left: 10px;
}

.el-button {
  margin-top: 10px;
}

.el-input {
  width: 100%;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-tabs {
  width: 100%;
}

.el-tab-pane {
  padding: 0 20px;
}
</style>
