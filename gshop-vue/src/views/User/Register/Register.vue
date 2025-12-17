<template>
  <div class="login-register-container">
    <el-card class="form-card">
      <h2 class="form-title">注册</h2>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
          <el-button type="success" size="small" @click="checkUsernameUniqueness" :disabled="!registerForm.username"
            icon="el-icon-check">检测唯一性</el-button>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
          <el-button type="success" size="small" @click="checkEmailUniqueness" :disabled="!registerForm.email"
            icon="el-icon-check">检测唯一性</el-button>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode">
          <el-input v-model="registerForm.verificationCode" placeholder="请输入验证码"></el-input>
          <el-button type="success" size="small" @click="sendVerificationCode" :disabled="!registerForm.email"
            icon="el-icon-message">发送验证码</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button @click="switchToLogin">去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Register",
  data() {
    return {
      registerForm: {
        username: "",
        email: "",
        phone: "",
        password: "",
        verificationCode: "",
      },
      registerRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 10, message: "用户名长度为 3 到 10 个字符", trigger: "blur" },
          { validator: this.validateUsername, trigger: "blur" }
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { validator: this.validateEmail, trigger: "blur" },
          { validator: this.validateEmailUniqueness, trigger: "blur" }
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { validator: this.validatePhone, trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 16, message: "密码长度为 6 到 16 个字符", trigger: "blur" },
        ],
        verificationCode: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "验证码长度为 5 个字符", trigger: "blur" }, // 修改验证码长度
        ],
      },
      usernameUnique: true,
      emailUnique: true
    };
  },
  methods: {
    switchToLogin() {
      this.$router.push({ name: 'Login' });
    },
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8088/user/register', this.registerForm)
            .then(response => {
              this.$message.success("注册成功");
              this.$refs.registerForm.resetFields(); // 清除表单信息
            })
            .catch(error => {
              this.$message.error("注册失败，请检查表单输入");
              console.error(error);
            });
        } else {
          this.$message.error("请检查表单输入");
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
    validatePhone(rule, value, callback) {
      const phoneRegex = /^1[3-9]\d{9}$/; // 假设手机号为11位数字且以1开头
      if (!phoneRegex.test(value)) {
        callback(new Error("请输入有效的手机号"));
      } else {
        callback();
      }
    },
    validateUsername(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入用户名'));
      } else {
        axios.get(`http://localhost:8088/user/checkUsername/${value}`)
          .then(response => {
            if (response.data.exists) {
              this.usernameUnique = false;
              callback(new Error('用户名已存在'));
            } else {
              this.usernameUnique = true;
              callback();
            }
          })
          .catch(error => {
            console.error('检查用户名失败:', error);
            callback(new Error('检查用户名失败，请稍后再试'));
          });
      }
    },
    validateEmailUniqueness(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入邮箱'));
      } else {
        axios.get(`http://localhost:8088/user/checkEmail/${value}`)
          .then(response => {
            if (response.data.exists) {
              this.emailUnique = false;
              callback(new Error('邮箱已存在'));
            } else {
              this.emailUnique = true;
              callback();
            }
          })
          .catch(error => {
            console.error('检查邮箱失败:', error);
            callback(new Error('检查邮箱失败，请稍后再试'));
          });
      }
    },
    checkUsernameUniqueness() {
      this.$refs.registerForm.validateField('username', (error) => {
        if (!error) {
          this.$message.success('用户名可用');
        }
      });
    },
    checkEmailUniqueness() {
      this.$refs.registerForm.validateField('email', (error) => {
        if (!error) {
          this.$message.success('邮箱可用');
        }
      });
    },
    sendVerificationCode() {
      if (!this.registerForm.email) {
        this.$message.error('请输入邮箱');
        return;
      }
      axios.post(`http://localhost:8088/user/sendVerificationCode/${this.registerForm.email}`)
        .then(response => {
          this.$message.success(response.data);
        })
        .catch(error => {
          this.$message.error("发送验证码失败: " + error.response.data);
          console.error(error);
        });
    }
    

  },
};
</script>

<style scoped>
.login-register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.form-card {
  width: 400px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.form-title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.el-form-item__content .el-button {
  margin-left: 10px;
}

</style>
