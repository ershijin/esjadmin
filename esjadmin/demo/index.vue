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
    </div>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" border style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="id" />
        <el-table-column prop="title" label="名称" />
        <el-table-column prop="categoryId" label="分类id" />
        <el-table-column prop="link" label="链接地址" />
        <el-table-column prop="status" label="状态：-1,删除；0，待审核；1，正常">
          <template slot-scope="scope">
            {{ dict.label.GeneralStatus[scope.row.status] }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="createTime" />
        <el-table-column prop="uni" label="uni" />
        <el-table-column v-if="checkPermission(['demo:update','demo:remove'])" label="操作"
        width="150px"
        align="center">
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

      <!--表单组件-->
      <el-dialog v-el-drag-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0"
      :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="名称" prop="title">
            <el-input v-model="form.title" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分类id">
            <el-input v-model="form.categoryId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="链接地址" prop="link">
            <el-input v-model="form.link" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态：-1,删除；0，待审核；1，正常">
            <el-select v-model="form.status" filterable placeholder="请选择">
              <el-option
                v-for="item in dict.GeneralStatus"
                :key="item.id"
                :label="item.label"
                :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="createTime">
            <el-date-picker v-model="form.createTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>

  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import crudDemo from '@/api/demo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import checkPermission from '@/utils/permission'

const defaultForm = { id: null, title: null, categoryId: null, link: null, status: null, createTime: null, updateTime: null, uni: null }
export default {
  name: 'Demo',
  components: { pagination, crudOperation, rrOperation, udOperation },
  directives: { elDragDialog },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['GeneralStatus'],
  cruds() {
    return CRUD({ title: 'demo', url: 'demo', sort: '', crudMethod: { ...crudDemo }})
  },
  data() {
    return {
      permission: {
        add: ['demo:save'],
        edit: ['demo:update'],
        del: ['demo:remove']
      },
      rules: {
        title: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        link: [
          { required: true, message: '链接地址不能为空', trigger: 'blur' }
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
    },
    checkPermission
  }
}
</script>

<style scoped>

</style>
