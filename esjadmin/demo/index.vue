<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">名称</label>
        <el-input v-model="query.title" clearable placeholder="名称" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">分类id</label>
        <el-input v-model="query.categoryId" clearable placeholder="分类id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">链接地址</label>
        <el-input v-model="query.link" clearable placeholder="链接地址" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="名称" prop="title">
            <el-input v-model="form.title" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分类id" prop="categoryId">
            <el-select v-model="form.categoryId" filterable placeholder="请选择">
              <el-option
                v-for="item in dict.user_status"
                :key="item.id"
                :label="item.label"
                :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="链接地址" prop="link">
            <el-input v-model="form.link" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态：-1,删除；0，待审核；1，正常" prop="status">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="createTime" prop="createTime">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="updateTime">
            <el-input v-model="form.updateTime" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="id" />
        <el-table-column prop="title" label="名称" />
        <el-table-column prop="categoryId" label="分类id">
          <template slot-scope="scope">
            {{ dict.label.user_status[scope.row.categoryId] }}
          </template>
        </el-table-column>
        <el-table-column prop="link" label="链接地址" />
        <el-table-column prop="status" label="状态：-1,删除；0，待审核；1，正常" />
        <el-table-column prop="createTime" label="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="updateTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','demo:edit','demo:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudDemo from '@/api/demo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, title: null, categoryId: null, link: null, status: null, createTime: null, updateTime: null }
export default {
  name: 'Demo',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['user_status'],
  cruds() {
    return CRUD({ title: 'demo', url: 'demo', sort: 'id,desc', crudMethod: { ...crudDemo }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'demo:add'],
        edit: ['admin', 'demo:edit'],
        del: ['admin', 'demo:del']
      },
      rules: {
        title: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '分类id不能为空', trigger: 'blur' }
        ],
        link: [
          { required: true, message: '链接地址不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态：-1,删除；0，待审核；1，正常不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'title', display_name: '名称' },
        { key: 'categoryId', display_name: '分类id' },
        { key: 'link', display_name: '链接地址' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
