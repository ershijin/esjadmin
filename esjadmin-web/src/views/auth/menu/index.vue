<template>
  <div class="app-container">
    <div class="head-container">
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
          width="165"
          class-name="small-padding"
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
          <el-form-item label="上级" prop="parentId">
            <treeselect
              v-model="temp.parentId"
              :multiple="false"
              no-results-text="未匹配到数据"
              placeholder="输入关键词搜索..."
              :options="options"
            />
          </el-form-item>

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

            <el-col :span="12">
              <el-form-item v-if="temp.type.toString() !== '2'" label="路由地址" prop="path">
                <el-input v-model="temp.path" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-show="temp.type.toString() !== '0'" label="权限标志" prop="permission">
                <el-input v-model="temp.permission" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item v-if="temp.type.toString() === '1'" label="组件名称" prop="component">
                <el-input v-model="temp.name" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item v-show="temp.type.toString() === '1'" label="组件路径" prop="component">
                <el-input v-model="temp.component" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item v-show="temp.type.toString() === '1'" label="菜单缓存" prop="hidden">
                <el-radio-group v-model="temp.noCache">
                  <el-radio :label="false">是</el-radio>
                  <el-radio :label="true">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
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
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from '@/components/IconSelect'
import elDragDialog from '@/directive/el-drag-dialog'
import waves from '@/directive/waves' // waves directive

function transformOptions(menus) {
  const menusMap = []
  menus.map(v => {
    const { id, title, children } = v
    // 重新构建路由对象
    const item = {
      id,
      label: title
    }
    if (children.length > 0) {
      item.children = transformOptions(children)
    }
    menusMap.push(item)
  })
  return menusMap
}

export default {
  name: 'Menu',
  directives: { waves, elDragDialog },
  components: { IconSelect, Treeselect },
  data() {
    return {
      tableData: [],

      list: [],
      options: [],
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
        title: '',
        priority: 0,
        remark: '',
        parentId: 0,
        hidden: false,
        enabled: true,
        name: '',
        noCache: false,
        component: '',
        icon: '',
        permission: ''
      },

      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        path: [{ required: true, message: '请输入地址', trigger: 'blur' }]
      }

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
        let options = []
        options.push({
          'id': 0,
          'label': '顶级菜单'
        })
        options = options.concat(transformOptions(response))
        this.options = options
      })
    },
    handleCreate(row) {
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    handleEdit(row) {
      this.dialogStatus = 'edit'
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
            const tmpParentId = this.temp.parentId
            this.$refs.dataForm.resetFields()
            this.temp.parentId = tmpParentId
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

    // 选中图标
    selectedIcon(icon) {
      this.temp.icon = icon
    }
  }
}
</script>
