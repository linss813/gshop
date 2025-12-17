<template>
    <div>
        <div class="toolbar">
            <el-button type="primary" @click="openAddDialog = true">添加分类</el-button>
        </div>
        <div class="table">
            <el-table ref="multipleTable" :data="filteredTableData" tooltip-effect="dark" style="width: 100%"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="tid" label="tid" width="100"></el-table-column>
                <el-table-column prop="tname" label="一级分类" width="200"></el-table-column>
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
                <el-form-item label="分类名">
                    <el-input v-model="addForm.tname" placeholder="请输入分类名"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="openAddDialog = false">取消</el-button>
                <el-button type="primary" @click="addGoodsType">确定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑分类对话框 -->
        <el-dialog title="编辑分类" :visible.sync="editDialog" width="30%">
            <el-form :model="editForm" label-width="100px">
                <el-form-item label="分类名">
                    <el-input v-model="editForm.tname" placeholder="请输入分类名"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editDialog = false">取消</el-button>
                <el-button type="primary" @click="editGoodsType">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'GoodsType',
    components: {},
    data() {
        return {
            filteredTableData: [],
            openAddDialog: false,
            editDialog: false,
            addForm: {
                tname: ''
            },
            editForm: {
                tid: '',
                tname: ''
            }
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
            this.editForm.tname = row.tname;
            this.editDialog = true;
        },
        async handleDelete(index, row) {
            console.log('delete', index, row);
            this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    const response = await axios.delete('/goodstype/deleteGoodsType', {
                        params: { tid: row.tid }
                    });
                    if (response.status === 200) {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.filteredTableData.splice(index, 1);
                    }
                } catch (error) {
                    console.error('Error:', error);
                    this.$message({
                        type: 'error',
                        message: '删除失败!'
                    });
                }
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        async fetchData() {
            try {
                const response = await axios.get('/goodstype/getAllGoodsType');
                this.filteredTableData = response.data;
            } catch (error) {
                console.error('Error:', error);
            }
        },
        async addGoodsType() {
            try {
                const response = await axios.post('/goodstype/addGoodsType', {
                    tname: this.addForm.tname
                });
                if (response.status === 200) {
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    this.addForm.tname = '';
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
        async editGoodsType() {
            try {
                const response = await axios.put('/goodstype/updateGoodsType', {
                    tid: this.editForm.tid,
                    tname: this.editForm.tname
                });
                if (response.status === 200) {
                    this.$message({
                        type: 'success',
                        message: '编辑成功!'
                    });
                    this.editForm.tid = '';
                    this.editForm.tname = '';
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