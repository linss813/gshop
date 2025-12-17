<template>
  <div class="password-container">
    <el-form :model="form" label-width="120px" :rules="rules" ref="passwordForm">
      <el-radio-group v-model="form.type" style="margin-bottom: 20px;">
        <el-radio label="oldPassword" aria-hidden="false">通过旧密码修改</el-radio>
        <el-radio label="emailCode" aria-hidden="false">通过邮箱验证码修改</el-radio>
      </el-radio-group>

      <!-- 旧密码修改方式 -->
      <div v-if="form.type === 'oldPassword'">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="form.oldPassword" type="password" autocomplete="off"></el-input>
        </el-form-item>
      </div>

      <!-- 邮箱验证码修改方式 -->
      <div v-if="form.type === 'emailCode'">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" disabled></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode">
          <el-input v-model="form.verificationCode" autocomplete="off">
            <template #append>
              <el-button @click="sendVerificationCode" :disabled="countdown > 0">
                {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
      </div>

      <!-- 公共字段 -->
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">修改密码</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.form.newPassword) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    
    return {
      countdown: 0,
      form: {
        type: 'oldPassword',
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
        email: this.$store.state.user?.email || '',
        verificationCode: ''
      },
      rules: {
        oldPassword: [
          { required: false, message: '请输入旧密码', trigger: 'blur' }
        ],
        email: [
          { required: false, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
        ],
        verificationCode: [
          { required: false, message: '请输入验证码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度至少6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    sendVerificationCode() {
      if (!this.form.email) {
        return this.$message.error('邮箱未获取到，请刷新页面');
      }
      
      if (this.countdown > 0) {
        return this.$message.info(`请${this.countdown}s后再次发送`);
      }
      
      this.countdown = 60;
      const timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
        } else {
          clearInterval(timer);
        }
      }, 1000);
      
      this.$axios.post(`http://localhost:8088/user/sendVerificationCode/${this.form.email}`)
        .then(() => this.$message.success('验证码已发送'))
        .catch(() => this.$message.error('发送失败'));
    },
    submitForm() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          const uid = this.$store.state.user?.uid || null;
          if (!uid) {
            this.$message.error('用户未登录，请先登录');
            return;
          }
          
          let params = {};
          let url = '';
          if (this.form.type === 'oldPassword') {
            params = {
              uid,
              oldPassword: this.form.oldPassword,
              newPassword: this.form.newPassword
            };
            url = 'http://localhost:8088/user/updatePasswordWithOld';
          } else {
            params = {
              uid,
              email: this.form.email,
              verificationCode: this.form.verificationCode,
              newPassword: this.form.newPassword
            };
            url = 'http://localhost:8088/user/updatePasswordWithEmail';
          }
          
          // 添加调试日志
          console.log('发送请求:', { url, params });
          
          this.$axios.put(url, params)
            .then(response => {
              console.log('响应成功:', response);
              if (response.data.success) {
                this.$message.success('密码修改成功');
                this.$store.dispatch('logout');
                this.$router.push('/login');
              } else {
                this.$message.error(response.data.message || '修改失败');
              }
            })
            .catch(error => {
              console.error('请求失败:', {
                status: error.response?.status,
                data: error.response?.data,
                message: error.message,
                params
              });
              this.$message.error(error.response?.data?.msg || '修改失败');
            });
        }
      });
    }
  }
};
</script>