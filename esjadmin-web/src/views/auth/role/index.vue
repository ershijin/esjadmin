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
      <el-table-column prop="name" label="角色名" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column prop="updateTime" label="修改时间" />

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

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.current"
      :limit.sync="listQuery.size"
      @pagination="getList"
    />

    <!-- 新增/编辑窗口 -->
    <el-dialog
      v-el-drag-dialog
      :title="dialogType==='edit'?'编辑角色':'添加角色'"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      top="1vh"
    >
      <el-form ref="dataForm" :model="temp" label-position="right" label-width="80px">
        <el-form-item label="角色名">
          <el-input v-model="temp.role.name" placeholder="角色名" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="temp.role.remark"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="角色描述"
          />
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
            ref="menuTree"
            :check-strictly="checkStrictly"
            :data="menusData"
            :props="defaultMenuProps"
            show-checkbox
            node-key="id"
            class="permission-tree"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关闭</el-button>
        <el-button type="primary" @click="saveRole">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import {
  list as listRoles,
  insert as insertRole,
  update as updateRole,
  remove as removeRole,
  listMenuIds
} from '@/api/roles'
import { getMenuTree } from '@/api/menu'
import elDragDialog from '@/directive/el-drag-dialog'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const defaultRoleMenuIds = {
  role: {
    id: null,
    name: '',
    remark: ''
  },
  menuIds: []
}
export default {
  name: 'Role',
  components: { Pagination },
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
      temp: deepClone(defaultRoleMenuIds),
      dialogFormVisible: false,
      dialogType: '',
      checkStrictly: false,
      menusData: null,
      defaultMenuProps: {
        children: 'children',
        label: 'title'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listRoles(this.listQuery).then(response => {
        this.list = response.rows
        this.total = response.total
        this.listLoading = false
      })
    },

    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },

    async handleCreate() {
      this.dialogType = 'create'
      this.dialogFormVisible = true
      this.temp = deepClone(defaultRoleMenuIds)
      if (this.menusData === null) {
        this.listLoading = true
        await getMenuTree().then(response => {
          this.menusData = response
          this.listLoading = false
        })
      }
      await this.$refs.menuTree.setCheckedKeys([])
    },
    saveRole() {
      const checkedTree = this.generateCheckedTree(
        deepClone(this.menusData),
        this.$refs['menuTree'].getCheckedKeys()
      )
      this.temp.menuIds = this.getTreeKeys(checkedTree)
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          if (this.dialogType === 'edit') {
            updateRole(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '角色更新成功！',
                type: 'success'
              })
            })
          } else {
            insertRole(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '角色添加成功！',
                type: 'success'
              })
              this.$refs.dataForm.resetFields()
            })
          }
          this.getList()
        }
      })
    },
    getTreeKeys(tree) {
      let keys = []
      for (const node of tree) {
        keys.push(node.id)
        if (node.children && node.children.length > 0) {
          keys = keys.concat(this.getTreeKeys(node.children))
        }
      }
      return keys
    },
    generateCheckedTree(menuTree, checkedKeys) {
      const res = []
      for (const node of menuTree) {
        if (node.children) {
          node.children = this.generateCheckedTree(node.children, checkedKeys)
        }
        if (
          checkedKeys.includes(node.id) ||
          (node.children && node.children.length > 0)
        ) {
          res.push(node)
        }
      }
      return res
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.dialogFormVisible = true
      Object.assign(this.temp.role, {
        id: row.id,
        name: row.name,
        remark: row.remark
      })
      if (this.menusData === null) {
        this.listLoading = true
        getMenuTree().then(response => {
          this.menusData = response
          this.listLoading = false
          this.fetchRoleMenus(row)
        })
      } else {
        this.fetchRoleMenus(row)
      }
    },
    // 获取角色对应的权限菜单
    fetchRoleMenus(row) {
      this.listLoading = true
      listMenuIds(row.id).then(response => {
        this.checkStrictly = true
        this.listLoading = false
        this.$nextTick(() => {
          this.$refs.menuTree.setCheckedKeys(response)
          this.checkStrictly = false
        })
      })
    },

    handleDelete(row) {
      this.$confirm('此操作不能被还原, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          removeRole({ id: row.id }).then(() => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getList()
          })
        })
        .catch(() => {})
    }
  }
}
</script>
