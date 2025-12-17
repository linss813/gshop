<template>
  <div class="add-admin-container">
    <el-form ref="adminForm" :model="adminForm" :rules="adminRules" label-width="100px" class="add-admin-form">
      <h2 class="add-admin-title">添加管理员</h2>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="adminForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="adminForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="使用人" prop="user">
        <el-input v-model="adminForm.user" placeholder="请输入使用人"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="adminForm.type" placeholder="请选择类型">
          <el-option label="系统管理员" value="0"></el-option>
          <el-option label="信息管理员" value="1"></el-option>
          <el-option label="商城管理员" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleAddAdmin">添加管理员</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'AdminAdd',
  data() {
    return {
      adminForm: {
        username: '',
        password: '',
        user: '',
        type: ''
      },
      adminRules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        user: [{ required: true, message: '使用人不能为空', trigger: 'blur' }],
        type: [{ required: true, message: '类型不能为空', trigger: 'change' }]
      }
    };
  },
  methods: {
    handleAddAdmin() {
      this.$refs.adminForm.validate(valid => {
        if (valid) {
          // 调用后端注册接口
          this.$axios.post('http://localhost:8088/admin/register', this.adminForm)
            .then(response => {
              if (response.data && response.data.id) {
                this.$message.success('管理员添加成功');
                this.$router.push('/admin/home/adminadd'); // 修改跳转路径
              } else {
                this.$message.error('管理员添加失败');
              }
            })
            .catch(error => {
              this.$message.error('添加管理员失败');
              console.error(error);
            });
        } else {
          this.$message.error('请填写所有字段');
        }
      });
    }
  }
};
</script>

<style scoped>
.add-admin-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.add-admin-form {
  width: 300px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.add-admin-title {
  text-align: center;
  margin-bottom: 20px;
}
</style>
