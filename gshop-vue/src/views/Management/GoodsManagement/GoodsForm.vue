<template>
  <el-dialog :title="formTitle" :visible.sync="dialogVisible" width="50%" @close="resetForm">
    <el-form :model="goodsForm" label-width="120px" ref="goodsForm" :rules="rules">
      <!-- 商品名称 -->
      <el-form-item label="商品名称" prop="title">
        <el-input v-model="goodsForm.title"></el-input>
      </el-form-item>

      <!-- 一级分类 -->
      <el-form-item label="一级分类" prop="tid">
        <el-select v-model="goodsForm.tid" placeholder="请选择一级分类" @change="handleTidChange">
          <el-option
              v-for="type in goodsTypes"
              :key="type.tid"
              :label="type.tname"
              :value="type.tid"
          ></el-option>
        </el-select>
      </el-form-item>

      <!-- 二级分类 -->
      <el-form-item label="二级分类" prop="cid">
        <el-select v-model="goodsForm.cid" placeholder="请选择二级分类">
          <el-option
              v-for="category in filteredCategories"
              :key="category.cid"
              :label="category.cname"
              :value="category.cid"
          ></el-option>
        </el-select>
      </el-form-item>

      <!-- 商品价格 -->
      <el-form-item label="商品价格" prop="price">
        <el-input-number v-model="goodsForm.price" :min="0"></el-input-number>
      </el-form-item>

      <!-- 商品库存 -->
      <el-form-item label="商品库存" prop="stock">
        <el-input-number v-model="goodsForm.stock" :min="0"></el-input-number>
      </el-form-item>

      <!-- 是否上架 -->
      <el-form-item label="是否上架">
        <el-switch v-model="goodsForm.isShelved"></el-switch>
      </el-form-item>

      <!-- 商品详情 -->
      <el-form-item label="商品详情">
        <el-input type="textarea" v-model="goodsForm.details"></el-input>
      </el-form-item>

      <!-- 商品评分 -->
      <!-- <el-form-item label="商品评分">
        <el-rate v-model="goodsForm.rating"></el-rate>
      </el-form-item> -->

      <!-- 商品图片上传 -->
      <el-form-item label="商品图片">
        <el-upload
            action="http://localhost:8088/file/upload"
            :on-success="handleImageUploadSuccess"
            :on-error="handleImageUploadError"
            :before-upload="beforeImageUpload"
            :limit="1"
        >
          <el-button type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button :disabled="isSubmitting" type="primary" @click="submitForm">提交</el-button>
    </span>
  </el-dialog>
</template>

<script>
import axios from 'axios';
import {Message} from 'element-ui';

export default {
  props: {
    mode: {
      type: String,
      default: 'add' // 'add' 或 'edit'
    },
    initialGoods: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      isSubmitting: false, // 提交状态标记
      dialogVisible: false, // 对话框显示状态
      goodsForm: {
        title: '',
        tid: null,
        cid: null,
        price: 0,
        stock: 0,
        isShelved: false,
        details: '',
        // rating: 0,
        image: ''
      },
      goodsTypes: [], // 一级分类列表
      categories: [], // 所有二级分类列表
      filteredCategories: [], // 根据一级分类筛选后的二级分类列表
      rules: {
        title: [{required: true, message: '请输入商品名称', trigger: 'blur'}],
        tid: [{required: true, message: '请选择一级分类', trigger: 'change'}],
        cid: [{required: true, message: '请选择二级分类', trigger: 'change'}],
        price: [{required: true, message: '请输入商品价格', trigger: 'blur'}],
        stock: [{required: true, message: '请输入商品库存', trigger: 'blur'}]
      }
    };
  },
  computed: {
    formTitle() {
      return this.mode === 'add' ? '添加商品' : '编辑商品';
    }
  },
  watch: {
    initialGoods: {
      handler(newVal) {
        this.goodsForm = {...newVal};
      },
      immediate: true
    }
  },
  created() {
    this.fetchGoodsTypes();
    this.fetchCategories();
  },
  methods: {

    // 打开对话框
    openDialog() {
      this.dialogVisible = true;
    },
    // 获取一级分类
    async fetchGoodsTypes() {
      try {
        const response = await axios.get('/goodstype/getAllGoodsType');
        this.goodsTypes = response.data;
      } catch (error) {
        Message.error('获取一级分类失败');
      }
    },
    // 获取所有二级分类
    async fetchCategories() {
      try {
        const response = await axios.get('/category/getAllCategory');
        this.categories = response.data;
      } catch (error) {
        Message.error('获取二级分类失败');
      }
    },
    // 一级分类选择变化时，筛选二级分类
    handleTidChange(tid) {
      this.filteredCategories = this.categories.filter(category => category.tid === tid);
    },
    // 图片上传成功
    handleImageUploadSuccess(response) {
      this.goodsForm.image = response.url; // 后端返回的完整URL
      Message.success('图片上传成功');
    },
    // 图片上传失败
    handleImageUploadError(err, file) {
      Message.error('图片上传失败');
    },
    // 图片上传前的校验
    beforeImageUpload(file) {
      const isJPGPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPGPNG) {
        Message.error('上传图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        Message.error('上传图片大小不能超过 2MB!');
      }
      return isJPGPNG && isLt2M;
    },
    // 提交表单
    async submitForm() {
      // 防止重复提交
      if (this.isSubmitting) return;

      this.isSubmitting = true;
      this.loading = true;

      try {
        await this.$refs.goodsForm.validate();
        if (this.mode === 'add') {
          await this.addGoods();
        } else {
          await this.updateGoods();
        }
      } catch (error) {
        // 处理验证错误
      } finally {
        this.isSubmitting = false;
        this.loading = false;
      }
    },
    // 添加商品
    async addGoods() {
      try {
        const response = await axios.post(
            'http://localhost:8088/goods/addGoods',
            this.goodsForm,
            {
              headers: {
                'Content-Type': 'application/json'
              }
            }
        );
        Message.success('添加成功');
        this.$emit('add-goods', response.data); // 传递完整数据
        this.dialogVisible = false;
      } catch (error) {
        Message.error('添加失败: ' + error.response?.data?.message || error.message);
      }
    },
    // 更新商品
    async updateGoods() {
      try {
        const response = await axios.put(
            'http://localhost:8088/goods/updateGoods',
            this.goodsForm,
            {
              headers: {
                'Content-Type': 'application/json'
              },
            }
        );
        Message.success('更新成功');
        this.$emit('update-goods', response.data); // 传递完整数据
        this.dialogVisible = false;
      } catch (error) {
        Message.error('更新失败: ' + error.response?.data?.message || error.message);
      }
    },
    // 重置表单
    resetForm() {
      this.$refs.goodsForm.resetFields();
    }
  }
};
</script>

<style scoped>
/* 样式可以根据需要自行调整 */
</style>
