<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="goToRoute('UserAdd')">添加用户</el-button>
      <el-input v-model="searchValue" placeholder="输入用户名搜索" style="width: 200px; margin-left: 20px;" @input="search"></el-input>
    </div>

    <div class="table">
      <el-table ref="multipleTable" :data="filteredTableData" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="uid" label="UID" width="100"></el-table-column>
        <el-table-column prop="username" label="用户名" width="100"></el-table-column>
        <el-table-column label="头像" width="120">
          <template slot-scope="scope">
            <el-image :src="scope.row.avatar || defaultAvatar" style="width: 80px; height: 80px" :preview-src-list="[scope.row.avatar || defaultAvatar]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
        <el-table-column prop="phone" label="电话" width="150"></el-table-column>
        <el-table-column prop="address" label="收货地址" width="150"></el-table-column>
        <el-table-column prop="created" label="创建时间" width="180"></el-table-column>
        <el-table-column prop="last" label="最后登录时间" width="180"></el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username"></el-input>
          <el-button type="success" size="small" @click="checkUsernameUniqueness" :disabled="!form.username" icon="el-icon-check">检测唯一性</el-button>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.uid">
          <el-input type="password" v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
          <el-button type="success" size="small" @click="checkEmailUniqueness" :disabled="!form.email" icon="el-icon-check">检测唯一性</el-button>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              v-if="form.uid"
              class="avatar-uploader"
              :action="`${baseURL}/user/uploadAvatar/${form.uid}`"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleAvatarChange"
              :before-upload="beforeAvatarUpload"
              name="file"
          >
            <el-image v-if="avatarPreview" :src="avatarPreview" class="avatar" style="width: 178px; height: 178px"></el-image>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <div v-else>请先保存用户信息以启用头像上传功能。</div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import { MessageBox } from 'element-ui';
import { validateEmail, validatePhone } from '@/utils/utils.js';

export default {
  name: "UserManagement",
  data() {
    return {
      baseURL: 'http://localhost:8088',
      defaultAvatar: 'https://a.520gexing.com/uploads/allimg/2021042109/uqaqhuvavt0.jpg',
      tableData: [],
      searchValue: "",
      multipleSelection: [],
      dialogVisible: false,
      form: {
        uid: '',
        username: '',
        password: '',
        email: '',
        phone: '',
        avatar: '',
        address: ''
      },
      dialogTitle: '',
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '用户名长度为 3 到 10 个字符', trigger: 'blur' },
          { validator: this.validateUsername, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 16, message: '密码长度为 6 到 16 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' },
          { validator: this.validateEmail, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' }
        ]
      },
      tempAvatarFile: null,
      avatarPreview: '',
      usernameUnique: true,
      emailUnique: true,
      originalForm: {}
    };
  },
  computed: {
    filteredTableData() {
      return this.tableData.filter(item => {
        if (this.searchValue && !item.username?.includes(this.searchValue)) {
          return false;
        }
        return true;
      });
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val; // 保存多选数据
    },
    search() {
      this.filteredTableData = this.tableData.filter(item =>
          item.username?.includes(this.searchValue)
      );
    },
    async fetchUsers() {
      try {
        const response = await axios.get(`${this.baseURL}/user/getAllUser`);
        const result = response.data;

        if (result.success && Array.isArray(result.data)) {
          this.tableData = result.data.map(user => ({
            ...user,
            avatar: user.avatar || this.defaultAvatar,
            created: user.created || '-', 
          }));
        } else {
          this.tableData = [];
          console.error('后端返回数据格式错误:', result);
          this.$message.error('获取用户列表失败，请重试');
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
        this.tableData = [];
        this.$message.error('网络错误或服务器异常');
      }
    },

    handleAvatarChange(file) {
      this.avatarPreview = URL.createObjectURL(file.raw);
      this.tempAvatarFile = file.raw;
    },

    handleDelete(index, row) {
      MessageBox.confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`${this.baseURL}/user/deleteUser/${row.uid}`)
          .then(response => {
            if (response.data.success) {
              this.fetchUsers();
              this.$message.success('用户删除成功');
            } else {
              this.$message.error(response.data.message);
            }
          })
          .catch(() => {
            this.$message.error('删除失败，请重试');
          });
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    goToRoute() {
      this.dialogTitle = '添加用户';
      this.form = {
        uid: '',
        username: '',
        password: '',
        email: '',
        phone: '',
        avatar: ''
      };
      this.dialogVisible = true;
    },

    async submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            // 先上传头像（如果存在）
            if (this.tempAvatarFile) {
              const formData = new FormData();
              formData.append('file', this.tempAvatarFile);

              const avatarRes = await axios.post(
                `${this.baseURL}/user/uploadAvatar/${this.form.uid}`,
                formData,
                { headers: { 'Content-Type': 'multipart/form-data' } }
              );

              if (avatarRes.data.success) {
                this.form.avatar = avatarRes.data.data;
              } else {
                throw new Error(avatarRes.data.message);
              }
            }

            // 提交用户数据（JSON格式）
            const userPayload = JSON.parse(JSON.stringify(this.form));
            const method = this.form.uid ? 'put' : 'post';
            const url = this.form.uid ? `${this.baseURL}/user/updateUser` : `${this.baseURL}/user/insertUser`;

            const userRes = await axios({
              method,
              url,
              data: userPayload,
              headers: { 'Content-Type': 'application/json' }
            });

            if (userRes.data.success) {
              this.fetchUsers();
              this.$message.success('操作成功');
              this.dialogVisible = false;
              this.tempAvatarFile = null;
              this.avatarPreview = '';
            } else {
              throw new Error(userRes.data.message);
            }
          } catch (error) {
            this.$message.error(error.message || '操作失败，请重试');
          }
        }
      });
    },

    handleEdit(index, row) {
      this.dialogTitle = '编辑用户';
      this.originalForm = { ...row };
      this.form = { ...row };
      this.dialogVisible = true;
    },

    validateUsername(rule, value, callback) {
      if (value === this.originalForm.username) {
        callback();
        return;
      }
      if (!value) {
        callback(new Error('请输入用户名'));
      } else {
        axios.get(`${this.baseURL}/user/checkUsername/${value}`, {
          params: { uid: this.form.uid }
        })
          .then(response => {
            if (response.data.success && response.data.data.exists) {
              this.usernameUnique = false;
              callback(new Error('用户名已存在'));
            } else {
              this.usernameUnique = true;
              callback();
            }
          })
          .catch(() => {
            callback(new Error('检查失败，请重试'));
          });
      }
    },

    validateEmail(rule, value, callback) {
      if (value === this.originalForm.email) {
        callback();
        return;
      }
      if (!value) {
        callback(new Error('请输入邮箱'));
      } else {
        axios.get(`${this.baseURL}/user/checkEmail/${value}`, {
          params: { uid: this.form.uid }
        })
          .then(response => {
            if (response.data.success && response.data.data.exists) {
              this.emailUnique = false;
              callback(new Error('邮箱已存在'));
            } else {
              this.emailUnique = true;
              callback();
            }
          })
          .catch(() => {
            callback(new Error('检查失败，请重试'));
          });
      }
    },

    checkUsernameUniqueness() {
      if (!this.form.username) return;
      this.$refs.form.validateField('username', (error) => {
        if (!error) this.$message.success('用户名可用');
      });
    },

    checkEmailUniqueness() {
      if (!this.form.email) return;
      this.$refs.form.validateField('email', (error) => {
        if (!error) this.$message.success('邮箱可用');
      });
    },

    beforeAvatarUpload(file) {
      const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type);
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isImage) {
        this.$message.error('只能上传 JPG/PNG/GIF 格式!');
      }
      if (!isLt10M) {
        this.$message.error('头像大小不能超过 10MB!');
      }

      return isImage && isLt10M;
    }
  },
  created() {
    this.fetchUsers();
  }
};
</script>


<style scoped>
.toolbar {
  margin-left: 20px;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.toolbar .el-button {
  margin-right: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

/* 添加自定义样式 */
.el-form-item__content {
  display: flex;
  align-items: center;
}

.el-form-item__content .el-button {
  margin-left: 10px;
}
</style>