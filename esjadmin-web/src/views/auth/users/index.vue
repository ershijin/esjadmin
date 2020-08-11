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
      />

      <el-select v-model="listQuery.role_id" placeholder="角色" clearable class="filter-item" style="width:160px">
        <el-option v-for="item in roles" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>

      <el-select v-model="listQuery.enabled" placeholder="激活" clearable class="filter-item" style="width:80px">
        <el-option label="是" :value="true" />
        <el-option label="否" :value="false" />
      </el-select>

      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>

      <el-button
        class="filter-item"
        style="margin-left: 0px;"
        type="primary"
        icon="el-icon-edit"
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
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="name" label="名字" />
      <el-table-column label="激活" align="center" width="50">
        <template slot-scope="scope">
          <font v-if="scope.row.enabled" color="#67C23A">是</font>
          <font v-else color="#F56C6C">否</font>
        </template>
      </el-table-column>
      <el-table-column label="头像" width="80" align="center">
        <template slot-scope="scope">
          <el-image style="width: 32px; height: 32px; " :src="scope.row.avatar" fit="scale-down" lazy />
        </template>
      </el-table-column>
      <el-table-column prop="create_time" label="创建时间" width="160" />
      <el-table-column prop="update_time" label="更新时间" width="160" />
      <el-table-column prop="remark" label="备注" />

      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            title="编辑"
            circle
            @click="handleEdit(row)"
          />
          <el-button
            v-if="!row.enabled"
            type="success"
            size="mini"
            icon="el-icon-check"
            title="启用"
            circle
            @click="handleEnable(row.id)"
          />
          <el-button
            v-if="row.enabled"
            type="warning"
            size="mini"
            icon="el-icon-minus"
            title="禁用"
            circle
            @click="handleDisable(row.id)"
          />
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            title="删除"
            circle
            @click="handleRemove(row.id)"
          />
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑窗口 -->
    <el-dialog
      v-el-drag-dialog
      :title="dialogType === 'edit' ? '编辑用户' : '添加用户'"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      top="1vh"
      width="500px"
    >
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="50px">
        <el-form-item
          label="账号"
          :rules="{
            required: true,
            message: '请输入账号',
            trigger: 'blur'
          }"
        >
          <el-input v-model="temp.username" :readonly="dialogType === 'edit' ? true : false" />
        </el-form-item>
        <el-form-item
          label="密码"
          :rules="dialogType == 'edit' ? null : {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }"
        >
          <el-input v-model="temp.password" :placeholder="dialogType === 'edit' ? '留空不修改' : ''" />
        </el-form-item>
        <el-form-item
          label="名字"
          prop="name"
          :rules="{
            required: true,
            message: '请输入名字',
            trigger: 'blur'
          }"
        >
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="temp.avatar" />
        </el-form-item>
        <el-form-item
          label="激活"
          prop="enabled"
          :rules="{
            required: true,
            message: '请选择激活状态',
            trigger: 'change'
          }"
        >
          <el-radio-group v-model="temp.enabled">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="temp.role_ids" multiple placeholder="请选择" style="width: 100%">
            <el-option v-for="item in roles" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="temp.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关闭</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  list,
  save,
  update,
  listRoleIds,
  enable,
  disable,
  remove
} from '@/api/user'
import { list as listRoles } from '@/api/roles'

import elDragDialog from '@/directive/el-drag-dialog'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const defaultData = {
  id: null,
  username: '',
  password: '',
  name: '',
  avatar: '',
  enabled: true,
  role_ids: [],
  remark: ''
}
export default {
  components: { Pagination },
  directives: { waves, elDragDialog },

  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        pageSize: 10,
        keyword: undefined,
        role_id: undefined,
        enabled: undefined
      },
      dialogFormVisible: false,
      dialogType: '',
      temp: defaultData,
      roles: null
    }
  },
  created() {
    this.getList()
    listRoles({ page: 0, pageSize: 9999 }).then(response => {
      this.roles = response.rows
    })
  },
  methods: {
    getList() {
      this.listLoading = true
      list(this.listQuery).then(response => {
        this.list = response.rows
        this.total = response.total
        this.listLoading = false
      })
    },

    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    // 创建用户
    handleCreate() {
      this.dialogType = 'create'
      this.dialogFormVisible = true
      this.temp = Object.assign({}, defaultData)
    },
    // 修改用户
    handleEdit(row) {
      this.dialogType = 'edit'
      listRoleIds({ user_id: row.id }).then(response => {
        this.temp = Object.assign({}, row, { role_ids: response })
        this.dialogFormVisible = true
      })
    },
    handleEnable(id) {
      this.$confirm('确定启用该账号?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          enable({ id: id }).then(() => {
            this.$message({
              title: '成功',
              message: '账号启用成功！',
              type: 'success'
            })
            this.getList()
          })
        })
        .catch(() => {})
    },
    handleDisable(id) {
      this.$confirm('确定禁用该账号?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          disable({ id: id }).then(() => {
            this.$message({
              title: '成功',
              message: '账号禁用成功！',
              type: 'success'
            })
            this.getList()
          })
        })
        .catch(() => {})
    },
    handleRemove(id) {
      this.$confirm('确定删除该用户?该操作不能被还原！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      })
        .then(() => {
          remove({ id: id }).then(() => {
            this.$message({
              title: '成功',
              message: '用户删除成功！',
              type: 'success'
            })
            this.getList()
          })
        })
        .catch(() => {})
    },
    saveUser() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          if (this.dialogType === 'edit') {
            update(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '用户修改成功！',
                type: 'success'
              })
              this.getList()
            })
          } else {
            save(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '用户添加成功！',
                type: 'success'
              })
              this.temp = Object.assign({}, defaultData)
              this.getList()
            })
          }
        }
      })
    }
  }
}
</script>
