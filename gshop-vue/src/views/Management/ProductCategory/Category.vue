<template>
    <div>
        <div class="toolbar">
            <el-button type="primary" @click="openAddDialog = true">添加分类</el-button>
        </div>
        <div class="table">
            <el-table ref="multipleTable" :data="filteredTableData" tooltip-effect="dark" style="width: 100%"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="cid" label="cid" width="100"></el-table-column>
                <el-table-column prop="tname" label="一级分类" width="200"></el-table-column>
                <el-table-column prop="cname" label="二级分类" width="200"></el-table-column>
                <el-table-column label="操作" fixed="right" width="200">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 添加分类对话框 -->
        <el-dialog title="添加分类" :visible.sync="openAddDialog" width="30%">
            <el-form :model="addForm" label-width="100px">
                <el-form-item label="二级分类名">
                    <el-input v-model="addForm.cname" placeholder="请输入二级分类名"></el-input>
                </el-form-item>
                <el-form-item label="一级分类">
                    <el-select v-model="addForm.tid" placeholder="请选择一级分类">
                        <el-option v-for="goodsType in goodsTypes" :key="goodsType.tid" :label="goodsType.tname" :value="goodsType.tid"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="openAddDialog = false">取消</el-button>
                <el-button type="primary" @click="addCategory">确定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑分类对话框 -->
        <el-dialog title="编辑分类" :visible.sync="editDialog" width="30%">
            <el-form :model="editForm" label-width="100px">
                <el-form-item label="二级分类名">
                    <el-input v-model="editForm.cname" placeholder="请输入二级分类名"></el-input>
                </el-form-item>
                <el-form-item label="一级分类">
                    <el-select v-model="editForm.tid" placeholder="请选择一级分类">
                        <el-option v-for="goodsType in goodsTypes" :key="goodsType.tid" :label="goodsType.tname" :value="goodsType.tid"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editDialog = false">取消</el-button>
                <el-button type="primary" @click="editCategory">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'Category',
    components: {},
    data() {
        return {
            filteredTableData: [],
            openAddDialog: false,
            editDialog: false,
            addForm: {
                tid: '',
                cname: ''
            },
            editForm: {
                tid: '',
                cname: ''
            },
            goodsTypes: []
        };
    },
    props: {},
    computed: {},
    methods: {
        handleClick(row) {
            console.log(row);
        },
        handleSelectionChange(val) {
            console.log(val);
        },
        handleEdit(index, row) {
            this.editForm.tid = row.tid;
            this.editForm.cname = row.cname;
            this.editDialog = true;
        },
      async handleDelete(index, row) {
        this.$confirm(`此操作将永久删除分类【${row.cname}】，是否继续？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          try {
            const response = await axios.delete(
                `/category/deleteCategory/${row.cid}`
            );

            // 处理业务逻辑错误（如存在商品）
            if (response.data === "该类别下有商品，请先删除商品") {
              this.$message({
                type: 'warning',
                message: response.data
              });
              await this.fetchData(); // 保持数据一致性
              return;
            }

            // 正常删除成功
            if (response.status === 200) {
              this.$message({ type: 'success', message: '删除成功!' });
              await this.fetchData(); // 刷新数据
            }
          } catch (error) {
            console.error('Error:', error);
            const errorMsg = error.response?.data?.message || '删除失败!';
            this.$message({ type: 'error', message: errorMsg });
          }
        }).catch(() => {
          this.$message({ type: 'info', message: '已取消删除' });
        });
      },
        async fetchData() {
            try {
                const response = await axios.get('/category/getAllCategory');
                this.filteredTableData = response.data;
            } catch (error) {
                console.error('Error:', error);
            }
        },
        async fetchGoodsTypes() {
            try {
                const response = await axios.get('/goodstype/getAllGoodsType');
                this.goodsTypes = response.data;
            } catch (error) {
                console.error('Error:', error);
            }
        },
        async addCategory() {
            try {
                const response = await axios.post('/category/addCategory', {
                    tid: this.addForm.tid,
                    cname: this.addForm.cname
                });
                if (response.status === 200) {
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    this.addForm.tid = '';
                    this.addForm.cname = '';
                    this.openAddDialog = false;
                    this.fetchData();
                }
            } catch (error) {
                console.error('Error:', error);
                this.$message({
                    type: 'error',
                    message: '添加失败!'
                });
            }
        },
        async editCategory() {
            try {
                const response = await axios.put(`/category/updateCategory/${this.editForm.tid}`, {
                    cname: this.editForm.cname
                });
                if (response.status === 200) {
                    this.$message({
                        type: 'success',
                        message: '编辑成功!'
                    });
                    this.editForm.tid = '';
                    this.editForm.cname = '';
                    this.editDialog = false;
                    this.fetchData();
                }
            } catch (error) {
                console.error('Error:', error);
                this.$message({
                    type: 'error',
                    message: '编辑失败!'
                });
            }
        }
    },
    created() {
        this.fetchData();
        this.fetchGoodsTypes();
    }
}
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
