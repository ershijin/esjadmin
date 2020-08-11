<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <el-input
        clearable
        v-model="listQuery.keyword"
        placeholder="关键字"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />

      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>-->
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">添加</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleEdit"
      >编辑</el-button>
      <!-- <el-button
        v-waves
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-check"
        @click="handleEnable"
      >启用</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="warning"
        icon="el-icon-minus"
        @click="handleDisable"
      >禁用</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="handleDelete"
      >删除</el-button>-->
    </div>
    <div>
      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        style="width: 100%;margin-bottom: 20px;"
        row-key="id"
        border
        :default-expand-all="false"
        empty-text="暂无数据"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column type="selection" width="40" />
        <el-table-column prop="title" label="标题" />
        <!-- <el-table-column prop="id" label="ID" width="50"></el-table-column> -->
        <el-table-column prop="meta.icon" align="center" label="图标" width="60">
          <template slot-scope="scope">
            <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标志" width="80" />
        <el-table-column prop="path" label="path" width="140" />
        <el-table-column prop="component" label="component" width="130" />
        <el-table-column prop="priority" label="权重" width="50" />
        <el-table-column prop="hidden" label="隐藏" width="50">
          <template slot-scope="scope">{{ scope.row.hidden ? '是' : '否' }}</template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="操作"
          align="center"
          width="220"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="{row}">
            <el-tooltip content="添加子菜单" placement="top">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-plus"
                title="添加子菜单"
                circle
                @click="handleCreate(row)"
              />
            </el-tooltip>
            <el-tooltip content="编辑菜单" placement="top">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                title="编辑"
                circle
                @click="handleEdit(row)"
              />
            </el-tooltip>
            <el-tooltip v-if="!row.enabled" content="启用菜单" placement="top">
              <el-button
                type="success"
                size="mini"
                icon="el-icon-check"
                title="启用"
                circle
                @click="handleEnable(row.id)"
              />
            </el-tooltip>
            <el-tooltip v-if="row.enabled" content="禁用菜单" placement="top">
              <el-button
                type="warning"
                size="mini"
                icon="el-icon-minus"
                title="禁用"
                circle
                @click="handleDisable(row.id)"
              />
            </el-tooltip>
            <el-tooltip content="删除菜单" placement="top">
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                title="删除"
                circle
                @click="handleDelete(row.id)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增/编辑窗口 -->
      <el-dialog
        v-el-drag-dialog
        :title="textMap[dialogStatus]"
        :visible.sync="dialogFormVisible"
        :close-on-click-modal="false"
        top="1vh"
        width="800px"
      >
        <el-form
          ref="dataForm"
          :model="temp"
          label-position="right"
          label-width="100px"
          :rules="rules"
          size="small"
        >
          <el-form-item label="上级" prop="parent_id">
            <el-select
              v-for="(arrItem,key) in selectList"
              :key="key"
              v-model="selectedArray[key]"
              filterable
              placeholder="请选择"
              clearable
              value-key="id"
              style="margin-right: 10px"
              @change="selected"
              @focus="position=key"
              @clear="selectClear(key)"
            >
              <el-option v-for="item in arrItem" :key="item.id" :label="item.title" :value="item" />
            </el-select>
          </el-form-item>

          <!-- <el-row>
            parent_id: {{ temp.parent_id }}
            <br />
            selectedArray: {{ selectedArray }}
          </el-row>-->

          <el-form-item label="菜单类型" prop="type">
            <el-radio-group v-model="temp.type">
              <el-radio-button :label="0">目录</el-radio-button>
              <el-radio-button :label="1">菜单</el-radio-button>
              <el-radio-button :label="2">按钮</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-row>
            <el-col :span="12">
              <el-form-item v-if="temp.type.toString() !== '2'" label="菜单标题" prop="title">
                <el-input v-model="temp.title" />
              </el-form-item>
              <el-form-item v-if="temp.type.toString() === '2'" label="按钮名称" prop="title">
                <el-input v-model="temp.title" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-show="temp.type.toString() !== '2'" label="菜单图标" prop="icon">
                <el-popover
                  placement="bottom-start"
                  width="450"
                  trigger="click"
                  @show="$refs['iconSelect'].reset()"
                >
                  <IconSelect ref="iconSelect" @selected="selectedIcon" />
                  <el-input slot="reference" v-model="temp.icon" placeholder="点击选择图标" clearable>
                    <svg-icon
                      v-if="temp.icon"
                      slot="prefix"
                      :icon-class="temp.icon"
                      class="el-input__icon"
                      style="height: 32px;width: 16px;"
                    />
                    <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                  </el-input>
                </el-popover>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="temp.type.toString() !== '2'" label="path 路由地址" prop="path">
                <el-input v-model="temp.path" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-show="temp.type.toString() !== '2'" label="redirect" prop="redirect">
                <el-input v-model="temp.redirect" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item
                v-show="temp.type.toString() === '1'"
                label="component组件路径"
                prop="component"
              >
                <el-input v-model="temp.component" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item v-show="temp.type.toString() !== '2'" label="是否隐藏" prop="hidden">
                <el-radio-group v-model="temp.hidden">
                  <el-radio :label="false">否</el-radio>
                  <el-radio :label="true">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="enabled">
                <el-radio-group v-model="temp.enabled">
                  <el-radio :label="true">启用</el-radio>
                  <el-radio :label="false">禁用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item v-show="temp.type.toString() !== '0'" label="权限标志" prop="permission">
            <el-input v-model="temp.permission" />
          </el-form-item>
          <el-form-item label="权重" prop="priority">
            <el-input-number v-model="temp.priority" />(权重越大越靠前)
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="temp.remark" type="textarea" :rows="2" />
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">关闭</el-button>
          <el-button
            type="primary"
            @click="dialogStatus === 'create' ? createMenu() : saveMenu()"
          >确定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  getMenuTree,
  insertMenu,
  updateMenu,
  enableMenu,
  disableMenu,
  deleteMenu
} from '@/api/menu'
import IconSelect from '@/components/IconSelect'
import elDragDialog from '@/directive/el-drag-dialog'
import waves from '@/directive/waves' // waves directive

export default {
  directives: { waves, elDragDialog },
  components: { IconSelect },
  data() {
    return {
      tableData: [],

      list: [],
      listLoading: true,

      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        create: '添加',
        edit: '编辑'
      },
      temp: {
        id: null,
        type: 1,
        path: '',
        redirect: '',
        title: '',
        priority: 0,
        remark: '',
        parent_id: 0,
        hidden: false,
        enabled: true,
        component: '',
        icon: '',
        permission: ''
      },

      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        path: [{ required: true, message: '请输入地址', trigger: 'blur' }]
      },

      position: null,
      selectedArray: [], // 已经选过的select
      selectList: [] // select的数量
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getMenuTree().then(response => {
        this.list = response
        this.listLoading = false
      })
    },
    handleCreate(row) {
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      if (this.selectList.length < 1) {
        this.selectList = [this.list]
      }
      // this.temp.parent_id = 0
    },
    handleEdit(row) {
      this.createParentSelect(row)
      this.dialogStatus = 'edit'
      console.log(row)
      Object.assign(this.temp, row)
      this.dialogFormVisible = true
    },
    handleEnable(id) {
      this.$confirm('确定启用该菜单?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          enableMenu(id).then(() => {
            this.$message({
              title: '成功',
              message: '菜单启用成功！',
              type: 'success'
            })
            this.fetchData()
          })
        })
        .catch(() => {})
    },
    handleDisable(id) {
      this.$confirm('确定禁用该菜单?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          disableMenu(id).then(() => {
            this.$message({
              title: '成功',
              message: '菜单禁用成功！',
              type: 'success'
            })
            this.fetchData()
          })
        })
        .catch(() => {})
    },
    handleDelete(id) {
      this.$confirm(
        '确定删除该菜单?该操作也会删除当前菜单的子菜单，并且不能被还原！',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          deleteMenu(id).then(() => {
            this.$message({
              title: '成功',
              message: '菜单删除成功！',
              type: 'success'
            })
            this.fetchData()
          })
        })
        .catch(() => {})
    },
    // 新增菜单
    createMenu() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          insertMenu(this.temp).then(() => {
            this.$message({
              title: '成功',
              message: '菜单添加成功！',
              type: 'success'
            })
            this.fetchData()
            const tmpParentId = this.temp.parent_id
            this.$refs.dataForm.resetFields()
            this.temp.parent_id = tmpParentId
          })
        }
      })
    },
    // 更新菜单
    saveMenu() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          updateMenu(this.temp).then(() => {
            this.$message({
              title: '成功',
              message: '菜单更新成功！',
              type: 'success'
            })
            this.fetchData()
          })
        }
      })
    },

    // 清空当前选择内容
    selectClear(key) {
      this.selectList.splice(key + 1, this.selectList.length)
      this.selectedArray.splice(key + 1, this.selectedArray.length)
      if (key === 0) {
        this.temp.parent_id = 0
      } else {
        this.temp.parent_id = this.selectedArray[key - 1].id
      }
    },
    // 获取下级菜单
    selected(item) {
      // item为当前选中项的对象
      // 获取下级菜单
      // 清空选择项的相关处理由 selectClear 做
      if (item == null) {
        return
      }
      this.temp.parent_id = item.id
      // 清除当前下拉菜单后面的下拉菜单
      for (var i = 0; i < this.selectedArray.length; i++) {
        if (item.parent_id === this.selectedArray[i].id) {
          this.selectList.splice(i + 2, this.selectList.length - i - 2)
          break
        }
      }
      if (item.children && item.children.length > 0) {
        // 加入下级下拉菜单
        this.selectList.push(item.children)
      }
    },

    // 初始化上级下拉菜单
    createParentSelect(row) {
      // 清空已选择项目
      this.selectedArray = []
      this.selectList = []
      // 递归出上级菜单
      var parents = this.getParentsSelect(row, this.list, this.selectedArray)
      this.selectList = parents
    },
    // 获取级别高的菜单
    getParentsSelect(row, all, selectedArray) {
      // 顶级菜单直接返回
      if (row.parent_id === 0) {
        return [all]
      }
      let result = []
      for (const o in all) {
        // 命中，返回结果
        if (all[o].id === row.parent_id) {
          result.push(all)
          result.push(all[o].children)
          selectedArray.unshift(all[o])
          return result
        }
      }
      for (const o in all) {
        // 遍历下级
        if (all[o].children && all[o].children.length > 0) {
          const result2 = this.getParentsSelect(
            row,
            all[o].children,
            selectedArray
          )
          if (result2.length > 0) {
            result.push(all)
            result = result.concat(result2)
            selectedArray.unshift(all[o])
          }
        }
      }
      return result
    },

    // 选中图标
    selectedIcon(name) {
      this.temp.icon = name
    }
  }
}
</script>
