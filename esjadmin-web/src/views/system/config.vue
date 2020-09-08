<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        clearable
        placeholder="关键词"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
        @clear="handleFilter"
      />

      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >添加</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column prop="id" label="ID" align="center" width="80" />
      <el-table-column prop="name" label="配置名称" />
      <el-table-column prop="code" label="参数名" />
      <el-table-column prop="value" label="参数值" />
      <el-table-column prop="description" label="配置说明" />

      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(row)">编辑</el-button>
          <el-button
            v-if="row.status!='deleted'"
            size="mini"
            icon="el-icon-delete"
            type="danger"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑窗口 -->
    <el-dialog
      v-el-drag-dialog
      :title="dialogType==='edit'?'编辑配置项':'添加配置项'"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
    >
      <el-form ref="dataForm" :model="temp" label-position="right" label-width="80px">
        <el-form-item label="配置名称" prop="name" :rules="{ required: true, message: '配置名称不能为空', trigger: 'blur' }">
          <el-input v-model="temp.name" placeholder="配置名称" />
        </el-form-item>
        <el-form-item label="参数名" prop="code" :rules="{ required: true, message: '参数名不能为空', trigger: 'blur' }">
          <el-input v-model="temp.code" placeholder="参数名" :readonly="dialogType === 'edit' ? true : false" />
        </el-form-item>
        <el-form-item label="参数值" prop="value" :rules="{ required: true, message: '参数值不能为空', trigger: 'blur' }">
          <el-input v-model="temp.value" placeholder="参数值" />
        </el-form-item>
        <el-form-item label="配置说明" prop="description">
          <el-input
            v-model="temp.description"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="配置说明"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关闭</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import Config from '@/api/config'
import elDragDialog from '@/directive/el-drag-dialog'
import waves from '@/directive/waves' // waves directive

const defaultData = {
  id: null,
  name: '',
  code: '',
  value: '',
  description: ''
}
export default {
  name: 'Config',
  directives: { waves, elDragDialog },

  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        current: 1,
        size: 10,
        keyword: undefined
      },
      temp: deepClone(defaultData),
      dialogFormVisible: false,
      dialogType: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      Config.getList(this.listQuery).then(data => {
        this.list = data
        this.listLoading = false
      })
    },

    handleFilter() {
      this.listQuery.current = 1
      this.fetchData()
    },

    async handleCreate() {
      this.dialogType = 'create'
      this.dialogFormVisible = true
      this.temp = deepClone(defaultData)
    },
    save() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          if (this.dialogType === 'edit') {
            Config.update(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '配置项更新成功！',
                type: 'success'
              })
              this.fetchData()
            })
          } else {
            Config.save(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '配置项添加成功！',
                type: 'success'
              })
              this.$refs.dataForm.resetFields()
              this.fetchData()
            })
          }
        }
      })
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.temp = deepClone(defaultData)
      for (const key in this.temp) {
        this.temp[key] = row[key]
      }
      this.dialogFormVisible = true
    },

    handleDelete(row) {
      this.$confirm('此操作不能被还原, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          Config.remove(row.id).then(() => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.fetchData()
          })
        })
        .catch(() => {})
    }
  }
}
</script>
