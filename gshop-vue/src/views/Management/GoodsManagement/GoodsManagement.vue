<template>
  <div>
    <div class="toolbar">
      <!-- 快捷操作 -->
      <el-button type="primary" @click="openAddDialog">添加商品</el-button>
      <el-button type="danger" @click="batchDelete">批量删除</el-button>
      <el-button type="success" @click="batchShelfUp">批量上架</el-button>
      <el-button type="warning" @click="batchShelfDown">批量下架</el-button>

      <!-- 搜索 -->
      <el-input v-model="searchValue" placeholder="输入商品名称搜索" style="width: 200px; margin-left: 20px;"
                @input="search"></el-input>
    </div>

    <div class="table">
      <el-table ref="multipleTable" :data="filteredTableData" tooltip-effect="dark" style="width: 100%"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="图片"  width="120" hight="">
          <template slot-scope="scope">
            <el-image
                :src="scope.row.image"
                style="width: 100px; height: 100px"
                fit="cover"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column label="商品名称" width="240">
          <template slot-scope="scope">{{ scope.row.title }}</template>
        </el-table-column>
        <el-table-column label="一级分类" width="100">
          <template slot-scope="scope">{{ scope.row.tname }}</template>
        </el-table-column>
        <el-table-column label="二级分类" width="100">
          <template slot-scope="scope">{{ scope.row.cname }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存数量" width="100"></el-table-column>
        <el-table-column prop="price" label="价格" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column prop="status" label="状态" width="80"></el-table-column>
        <el-table-column prop="rating" label="评分" width="50"></el-table-column>
        <el-table-column label="操作" fixed="right" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="openViewDialog(scope.row)">查看</el-button>
            <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.isShelved" size="mini" type="danger"
                       @click="handleShelfDown(scope.$index, scope.row)">下架
            </el-button>
            <el-button v-else size="mini" type="success" @click="handleShelfUp(scope.$index, scope.row)">上架
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination" style="margin-top: 20px;">
      <el-pagination :disabled="loading" @size-change="handleSizeChange" @current-change="handlePageChange"
                     :current-page="currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 商品新增编辑对话框 -->
    <GoodsForm ref="productForm" :mode="formMode" :initialGoods="selectedGoods" @add-goods="addGoodsHandler"
               @update-goods="updateGoodsHandler"/>

    <!-- 商品预览对话框 -->
    <GoodsView ref="productView" :initialGoods="selectedGoods"/>
  </div>
</template>

<script>
import axios from 'axios';
import {MessageBox, Message, Image} from 'element-ui'; // 引入 el-image
import GoodsForm from './GoodsForm.vue';
import GoodsView from './GoodsView.vue';

export default {
  name: "GoodsManagement",
  components: {
    GoodsForm,
    GoodsView,
    'el-image': Image // 注册 el-image 组件
  },
  data() {
    return {
      loading: false,
      tableData: [],
      searchValue: "",
      multipleSelection: [],
      selectedGoods: {},
      formMode: 'add', // 'add' 或 'edit'
      currentPage: 1, // 当前页码
      pageSize: 10,   // 每页条数
      total: 0        // 总数据量
    };
  },
  computed: {
    filteredTableData() {
      return this.tableData.filter(item => {
        if (this.searchValue && !item.title.includes(this.searchValue)) {
          return false;
        }
        return true;
      });
    }
  },
  methods: {
    getSortedImages(images) {
      return images || [];
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.fetchGoods(); // 切换页码时重新加载数据
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.currentPage = 1; // 改变每页条数时重置到第一页
      this.fetchGoods();
    },
    openAddDialog() {
      this.selectedGoods = {}; // 清空选中的商品信息
      this.formMode = 'add'; // 设置为添加模式
      this.$refs.productForm.openDialog();
    },
    openEditDialog(row) {
      const goodsData = {...row};
      // 确保 images 是数组
      if (!Array.isArray(goodsData.images)) {
        goodsData.images = [];
      }
      this.selectedGoods = goodsData;
      this.formMode = 'edit'; // 设置为编辑模式
      this.$refs.productForm.openDialog();
    },
    openViewDialog(row) {
      this.selectedGoods = {...row}; // 设置选中的商品信息
      this.$refs.productView.openDialog();
    },
    addGoodsHandler(goods) {
      this.fetchGoods(); // 触发重新加载数据
    },

    updateGoodsHandler(updatedGoods) {
      this.fetchGoods(); // 触发重新加载数据
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleView(index, row) {
      console.log("查看", index, row);
      this.openViewDialog(row);
    },
    handleEdit(index, row) {
      console.log("编辑", index, row);
      this.openEditDialog(row);
    },
    handleShelfDown(index, row) {
      const updatedGoods = {...row, isShelved: false};
      console.log("下架商品参数:", updatedGoods);
      this.updateGoods(updatedGoods)
          .then(() => {
            this.fetchGoods();
            Message.success('商品已下架');
          })
          .catch(error => {
            console.error('下架商品失败:', error);
            Message.error('下架商品失败');
          });
    },
    handleShelfUp(index, row) {
      const updatedGoods = {...row, isShelved: true};
      this.updateGoods(updatedGoods)
          .then(() => {
            this.fetchGoods();
            Message.success('商品已上架');
          })
          .catch(error => {
            console.error('上架商品失败:', error);
            Message.error('上架商品失败');
          });
    },
    handleDelete(index, row) {
      MessageBox.confirm('此操作将永久删除该商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteGoods(row.gid)
            .then(() => {
              this.fetchGoods();
              Message.success('商品已删除');
            })
            .catch(error => {
              console.error('删除商品失败:', error);
              Message.error('删除商品失败');
            });
      }).catch(() => {
        Message.info('已取消删除');
      });
    },
    search() {
      // 触发计算属性重新计算
      this.$refs.multipleTable.clearFilter();
    },
    goToRoute(routeName) {
      this.$router.push({name: routeName});
    },
    async fetchGoods() {
      this.loading = true;
      try {
        axios.get('http://localhost:8088/goods/getAllGoods', {
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize
          }
        })
            .then(response => {
              const {list, total} = response.data; // 解析分页数据
              this.tableData = list.map(item => ({
                ...item,
                title: item.title,
                image: item.image || (item.images && item.images.length > 0 ? item.images[0].imgUrl : ''),
                createTime: item.createdAt,
                status: item.isShelved ? '上架' : '下架',
                goodstype: item.tid.tname,
                category: item.cid.cname,
                images: (item.images || []).map(img => ({
                  imgid: img.imgid || Date.now(),
                  imgUrl: img.imgUrl || ''
                })),
                details: item.details,
              }));
              this.total = total; // 更新总条数
            })
            .catch(error => {
              console.error('获取商品信息失败:', error);
            });
      } finally {
        this.loading = false;
      }
    },
    getGoodsById(gid) {
      return axios.get(`http://localhost:8088/goods/getGoodsById/${gid}`);
    },
    addGoods(goods) {
      return axios.post('http://localhost:8088/goods/addGoods', goods);
    },
    updateGoods(goods) {
      return axios.put('http://localhost:8088/goods/updateGoods', goods, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
    },
    deleteGoods(gid) {
      return axios.delete(`http://localhost:8088/goods/deleteGoods/${gid}`);
    },
    batchDelete() {
      if (this.multipleSelection.length === 0) {
        Message.warning('请选择要删除的商品');
        return;
      }
      MessageBox.confirm('此操作将永久删除选中的商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.gid);
        Promise.all(ids.map(id => this.deleteGoods(id)))
            .then(() => {
              this.fetchGoods();
              Message.success('商品已批量删除');
            })
            .catch(error => {
              console.error('批量删除商品失败:', error);
              Message.error('批量删除商品失败');
            });
      }).catch(() => {
        Message.info('已取消批量删除');
      });
    },
    batchShelfUp() {
      if (this.multipleSelection.length === 0) {
        Message.warning('请选择要上架的商品');
        return;
      }
      const updatedGoodsList = this.multipleSelection.map(item => ({...item, isShelved: true}));
      Promise.all(updatedGoodsList.map(goods => this.updateGoods(goods)))
          .then(() => {
            this.fetchGoods();
            Message.success('商品已批量上架');
          })
          .catch(error => {
            console.error('批量上架商品失败:', error);
            Message.error('批量上架商品失败');
          });
    },
    batchShelfDown() {
      if (this.multipleSelection.length === 0) {
        Message.warning('请选择要下架的商品');
        return;
      }
      const updatedGoodsList = this.multipleSelection.map(item => ({...item, isShelved: false}));
      Promise.all(updatedGoodsList.map(goods => this.updateGoods(goods)))
          .then(() => {
            this.fetchGoods();
            Message.success('商品已批量下架');
          })
          .catch(error => {
            console.error('批量下架商品失败:', error);
            Message.error('批量下架商品失败');
          });
    },
    getMainImageUrl(row) {
      return row.image || (row.images && row.images.length > 0 ? row.images[0].imgUrl : '');
    }
  },
  created() {
    this.fetchGoods();
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
</style>
