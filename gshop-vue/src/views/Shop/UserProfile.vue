<template>
  <div class="profile-container">
    <h3>个人资料</h3>
    <el-form :model="userForm" label-width="120px" :rules="rules" ref="profileForm">
      <!-- 头像 -->
      <el-form-item label="用户头像">
        <el-upload
          :action="uploadUrl"
          :headers="headers"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img :src="userAvatar" class="avatar" />
        </el-upload>
      </el-form-item>

      <!-- 昵称 -->
      <el-form-item label="昵称" prop="username">
        <el-input v-model="userForm.username" placeholder="请输入昵称"></el-input>
      </el-form-item>

      <!-- 邮箱 -->
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userForm.email" placeholder="请输入邮箱" :disabled="true"></el-input>
      </el-form-item>

      <!-- 手机号 -->
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="userForm.phone" placeholder="请输入手机号"></el-input>
      </el-form-item>

      <!-- 收货地址 -->
      <el-form-item label="收货地址" prop="address">
        <el-input v-model="userForm.address" placeholder="请输入收货地址"></el-input>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userForm: {
        uid: null,
        username: '',
        avatar: '',
        email: '',
        phone: '',
        address: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 16, message: '长度在2到16个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入收货地址', trigger: 'blur' }
        ]
      }
    };
  },
  computed: {
    user() {
      return this.$store.getters.currentUser || {};
    },
    userAvatar() {
      return this.userForm.avatar || '默认头像URL';
    },
    uploadUrl() {
      return `/user/uploadAvatar/${this.user.uid}`;
    },
    headers() {
      return { Authorization: `Bearer ${this.$store.getters.token}` };
    }
  },
  watch: {
    user: {
      deep: true,
      handler(newVal) {
        this.userForm = {
          uid: newVal.uid,
          username: newVal.username,
          avatar: newVal.avatar,
          email: newVal.email,
          phone: newVal.phone,
          address: newVal.address
        };
      }
    }
  },
  created() {
    this.userForm = {
      uid: this.user.uid,
      username: this.user.username,
      avatar: this.user.avatar,
      email: this.user.email,
      phone: this.user.phone,
      address: this.user.address
    };
  },
  methods: {
    beforeAvatarUpload(file) {
      const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!isJPGOrPNG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPGOrPNG && isLt2M;
    },
    saveProfile() {
      this.$refs.profileForm.validate(valid => {
        if (valid) {
          this.$axios
            .put(`/user/updateUser`, this.userForm)
            .then(() => {
              this.$store.dispatch('setUser', this.userForm);
              this.$message.success('资料更新成功');
            })
            .catch(error => {
              this.$message.error(`保存失败：${error.response?.data?.message || '未知错误'}`);
            });
        } else {
          this.$message.warning('请检查表单内容');
        }
      });
    },
    handleAvatarSuccess(res) {
      this.userForm.avatar = res.data;
      this.$store.dispatch('updateUser', { avatar: res.data });
    }
  }
};
</script>

<style scoped>
.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  cursor: pointer;
}
.profile-container {
  padding: 20px;
}
</style>
