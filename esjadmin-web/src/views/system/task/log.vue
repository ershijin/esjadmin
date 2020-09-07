<template>
  <el-dialog v-el-drag-dialog :visible.sync="dialogVisible" append-to-body title="执行日志" top="1vh" width="88%">
    <!-- 搜索 -->
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
    </div>
    <!--表格渲染-->
    <el-table v-loading="listLoading" :data="list" style="width: 100%;margin-top: -10px;">
      <el-table-column :show-overflow-tooltip="true" prop="jobName" label="任务名称" />
      <el-table-column :show-overflow-tooltip="true" prop="beanName" label="Bean名称" />
      <el-table-column :show-overflow-tooltip="true" prop="methodName" label="执行方法" />
      <el-table-column :show-overflow-tooltip="true" prop="params" width="120px" label="参数" />
      <el-table-column :show-overflow-tooltip="true" prop="cronExpression" label="cron表达式" />
      <el-table-column prop="createTime" label="异常详情" width="110px">
        <template slot-scope="scope">
          <el-button v-show="scope.row.exceptionDetail" size="mini" type="text" @click="info(scope.row.exceptionDetail)">查看详情</el-button>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" align="center" prop="time" width="100px" label="耗时(毫秒)" />
      <el-table-column align="center" prop="isSuccess" width="80px" label="状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isSuccess ? 'success' : 'danger'">{{ scope.row.isSuccess ? '成功' : '失败' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" width="160" />
    </el-table>
    <el-dialog :visible.sync="errorDialog" append-to-body title="异常详情" width="85%">
      <pre>{{ errorInfo }}</pre>
    </el-dialog>
    <!--分页组件-->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.current"
      :limit.sync="listQuery.size"
      @pagination="getList"
    />
  </el-dialog>
</template>

<script>
import taskJob from '@/api/task'

import elDragDialog from '@/directive/el-drag-dialog'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'TaskLog',
  components: { Pagination },
  directives: { waves, elDragDialog },
  data() {
    return {
      dialogVisible: false,
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
      },

      title: '任务日志',
      errorInfo: '',
      errorDialog: false,
      enabledTypeOptions: [
        { key: 'true', display_name: '成功' },
        { key: 'false', display_name: '失败' }
      ]
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      taskJob.listLog(this.listQuery).then(response => {
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

    // 异常详情
    info(errorInfo) {
      this.errorInfo = errorInfo
      this.errorDialog = true
    }
  }
}
</script>

<style scoped>
  .java.hljs{
    color: #444;
    background: #ffffff !important;
  }
 ::v-deep .el-dialog__body{
    padding: 0 20px 10px 20px !important;
  }
</style>
