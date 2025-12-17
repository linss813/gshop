<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="openAddDialog">添加轮播</el-button>
    </div>

    <div class="table">
      <el-table :data="filteredTableData" style="width: 100%">
        <el-table-column label="轮播图" width="200">
          <template slot-scope="scope">
            <el-image
                :src="scope.row.image"
                style="width: 150px; height: 80px"
                fit="cover"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="jump" label="跳转链接" show-overflow-tooltip></el-table-column>
        <el-table-column label="排序" width="300">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.sort" :min="1" @change="updateSort(scope.row)"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="toggleStatus(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination" style="margin-top: 20px;">
      <el-pagination
          :current-page="currentPage"
          :page-sizes="[10, 20, 30]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
      >
      </el-pagination>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="formTitle" :visible.sync="dialogVisible" width="40%">
      <el-form :model="bannerForm" label-width="100px" ref="bannerForm" :rules="rules">
        <el-form-item label="轮播图片" prop="image">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8088/file/upload"
              :show-file-list="false"
              :on-success="handleImageSuccess"
              :before-upload="beforeImageUpload"
          >
            <el-image v-if="bannerForm.image" :src="bannerForm.image" class="avatar"></el-image>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="跳转链接" prop="jump">
          <el-input v-model="bannerForm.jump"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="bannerForm.sort" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="bannerForm.status"></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">{{ formMode === 'add' ? '添加' : '保存' }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import { MessageBox, Message } from 'element-ui';

export default {
  data() {
    return {
      banners: [], // 全部数据
      searchValue: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      formMode: 'add', // 'add' 或 'edit'
      bannerForm: {
        bid: null,
        image: '',
        jump: '',
        sort: 1,
        status: true,
      },
      rules: {
        image: [{ required: true, message: '请上传轮播图', trigger: 'change' }],
        jump: [{ required: true, message: '请输入跳转链接', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序值', trigger: 'blur' }]
      }
    };
  },
  computed: {
    filteredTableData() {
      return this.banners.filter(item => {
        return !this.searchValue || item.jump.includes(this.searchValue);
      });
    },
    formTitle() {
      return this.formMode === 'add' ? '新增轮播' : '编辑轮播';
    }
  },
  methods: {
    // 获取轮播列表
    async fetchBanners() {
      try {
        const response = await axios.get('http://localhost:8088/banner/getAllBanner');
        this.banners = response.data;
        this.total = this.banners.length;
      } catch (error) {
        Message.error('获取轮播列表失败');
      }
    },
    // 图片上传成功
    handleImageSuccess(response) {
      this.bannerForm.image = response.url;
    },
    // 图片上传前校验
    beforeImageUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        Message.error('上传图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    // 打开新增对话框
    openAddDialog() {
      this.formMode = 'add';
      this.resetForm();
      this.dialogVisible = true;
    },
    // 打开编辑对话框
    openEditDialog(row) {
      this.formMode = 'edit';
      this.bannerForm = { ...row };
      this.dialogVisible = true;
    },
    // 提交表单
    submitForm() {
      this.$refs.bannerForm.validate(valid => {
        if (valid) {
          if (this.formMode === 'add') {
            this.addBanner();
          } else {
            this.updateBanner();
          }
        }
      });
    },
    // 新增轮播
    async addBanner() {
      try {
        await axios.post('http://localhost:8088/banner/addBanner', this.bannerForm);
        this.fetchBanners();
        Message.success('新增成功');
        this.dialogVisible = false;
      } catch (error) {
        Message.error('新增失败');
      }
    },
    // 更新轮播
    async updateBanner() {
      try {
        await axios.put('http://localhost:8088/banner/updateBanner', this.bannerForm);
        this.fetchBanners();
        Message.success('保存成功');
        this.dialogVisible = false;
      } catch (error) {
        Message.error('保存失败');
      }
    },
    // 删除轮播
    handleDelete(row) {
      MessageBox.confirm('此操作将删除该轮播, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`http://localhost:8088/banner/deleteBanner/${row.bid}`)
            .then(() => {
              this.fetchBanners();
              Message.success('删除成功');
            })
            .catch(() => {
              Message.error('删除失败');
            });
      });
    },
    // 切换状态
    async toggleStatus(row) {
      try {
        await axios.put('http://localhost:8088/banner/updateBanner', { ...row });
        Message.success('状态更新成功');
      } catch (error) {
        row.status = !row.status; // 恢复原始状态
        Message.error('状态更新失败');
      }
    },
    // 更新排序
    async updateSort(row) {
      try {
        await axios.put('http://localhost:8088/banner/updateBanner', { ...row });
        Message.success('排序更新成功');
      } catch (error) {
        this.fetchBanners(); // 恢复原始排序
        Message.error('排序更新失败');
      }
    },
    // 重置表单
    resetForm() {
      this.bannerForm = {
        bid: null,
        image: '',
        jump: '',
        sort: 1,
        status: true
      };
    },
    // 搜索
    search() {
      this.currentPage = 1;
    },
    // 分页相关
    handleSizeChange(size) {
      this.pageSize = size;
      this.fetchBanners();
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchBanners();
    }
  },
  created() {
    this.fetchBanners();
  }
};
</script>

<style scoped>
.toolbar {
  margin: 20px 20px;
  display: flex;
  align-items: center;
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
  width: 150px;
  height: 80px;
  line-height: 80px;
  text-align: center;
}
.avatar {
  width: 150px;
  height: 80px;
  display: block;
}
</style>
