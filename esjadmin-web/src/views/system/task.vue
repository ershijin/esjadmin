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

      <el-date-picker
        v-model="time"
        type="datetimerange"
        value-format="yyyy-MM-dd HH:mm:ss"
        :picker-options="pickerOptions"
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        class="filter-item"
        @change="changeTime"
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
        style="margin-left: 0px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >添加</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      fit
      stripe
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left">
            <el-form-item label="描述:">
              <span>{{ props.row.description }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column prop="id" label="ID" align="left" />
      <el-table-column prop="jobName" label="任务名称" />
      <el-table-column prop="beanName" label="Bean名称" />
      <el-table-column prop="methodName" label="执行方法" />
      <el-table-column prop="params" label="参数" />
      <el-table-column prop="cronExpression" label="cron表达式" />
      <el-table-column prop="pause" label="状态">
        <template slot-scope="scope">
          <font v-if="scope.row.pause" color="#F56C6C">暂停</font>
          <font v-else color="#67C23A">启用</font>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="description" label="描述" /> -->
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" align="center" width="200" class-name="small-padding">
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
            type="primary"
            size="mini"
            icon="el-icon-caret-right"
            title="执行一次"
            circle
            @click="handleExecute(row)"
          />
          <el-button
            v-if="row.pause"
            type="success"
            size="mini"
            icon="el-icon-check"
            title="启用"
            circle
            @click="handleEnable(row.id)"
          />
          <el-button
            v-if="!row.pause"
            type="warning"
            size="mini"
            icon="el-icon-minus"
            title="停用"
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
      :page.sync="listQuery.current"
      :limit.sync="listQuery.size"
      @pagination="getList"
    />

    <!-- 新增/编辑窗口 -->
    <el-dialog
      v-el-drag-dialog
      :title="dialogType === 'edit' ? '编辑任务' : '添加任务'"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="730px"
    >
      <el-form
        ref="dataForm"
        :model="temp"
        label-position="left"
        :inline="true"
        label-width="100px"
        size="small"
      >
        <el-form-item label="任务名称" prop="jobName" :rules="{ required: true, message: '请输入任务名称', trigger: 'blur' }">
          <el-input v-model="temp.jobName" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="任务描述" prop="description" :rules="{ required: true, message: '请输入任务描述', trigger: 'blur' }">
          <el-input v-model="temp.description" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="Bean名称" prop="beanName" :rules="{ required: true, message: '请输入Bean名称', trigger: 'blur' }">
          <el-input v-model="temp.beanName" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="执行方法" prop="methodName" :rules="{ required: true, message: '请输入方法名称', trigger: 'blur' }">
          <el-input v-model="temp.methodName" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="参数内容">
          <el-input v-model="temp.params" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="Cron表达式" prop="cronExpression" :rules="{ required: true, message: '请输入Cron表达式', trigger: 'blur' }">
          <el-input v-model="temp.cronExpression" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="失败后暂停">
          <el-radio-group v-model="temp.pauseAfterFailure" style="width: 220px">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-radio-group v-model="temp.pause" style="width: 220px">
            <el-radio :label="false">启用</el-radio>
            <el-radio :label="true">暂停</el-radio>
          </el-radio-group>
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
import taskJob from '@/api/task'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import elDragDialog from '@/directive/el-drag-dialog'

const defaultData = {
  id: null,
  jobName: '',
  description: '',
  beanName: '',
  methodName: '',
  params: '',
  cronExpression: '',
  pauseAfterFailure: true,
  pause: false
}

export default {
  name: 'Task',
  components: { Pagination },
  directives: { waves, elDragDialog },

  data() {
    return {
      dialogFormVisible: false,
      dialogType: '',
      temp: defaultData,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        current: 1,
        size: 10,
        keyword: undefined,
        startTime: null,
        endTime: null
      },
      time: '',
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      taskJob.list(this.listQuery).then(response => {
        this.list = response.rows
        this.total = response.total
        this.listLoading = false
      })
    },

    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },

    changeTime() {
      if (this.time === null) {
        this.listQuery.startTime = null
        this.listQuery.endTime = null
      } else {
        this.listQuery.startTime = this.time[0]
        this.listQuery.endTime = this.time[1]
      }
    },

    handleExecute(row) {
      this.$confirm('确定执行一次任务 ' + row.jobName + ' ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          taskJob.execute(row.id).then(() => {
            this.$message({
              title: '成功',
              message: '任务 ' + row.jobName + ' 执行成功！',
              type: 'success'
            })
            this.getList()
          })
        })
    },
    // 创建
    handleCreate() {
      this.dialogType = 'create'
      this.dialogFormVisible = true
      this.temp = Object.assign({}, defaultData)
    },
    // 修改
    handleEdit(row) {
      this.dialogType = 'edit'
      this.temp = Object.assign({}, row)
      this.dialogFormVisible = true
    },
    // 启用
    handleEnable(id) {
      this.$confirm('确定启用该任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          taskJob.enable(id).then(() => {
            this.$message({
              title: '成功',
              message: '任务启用成功！',
              type: 'success'
            })
            this.getList()
          })
        })
        .catch(() => {})
    },
    // 禁用
    handleDisable(id) {
      this.$confirm('确定禁用该任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          taskJob.disable(id).then(() => {
            this.$message({
              title: '成功',
              message: '任务禁用成功！',
              type: 'success'
            })
            this.getList()
          })
        })
        .catch(() => {})
    },
    // 删除
    handleRemove(id) {
      this.$confirm('确定删除该任务?该操作不能被还原！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      })
        .then(() => {
          const ids = [id]
          taskJob.remove(ids).then(() => {
            this.$message({
              title: '成功',
              message: '任务删除成功！',
              type: 'success'
            })
            this.getList()
          })
        })
        .catch(() => {})
    },
    save() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          if (this.dialogType === 'edit') {
            taskJob.update(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '任务修改成功！',
                type: 'success'
              })
              this.getList()
            })
          } else {
            taskJob.save(this.temp).then(() => {
              this.$message({
                title: '成功',
                message: '任务添加成功！',
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
