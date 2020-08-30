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
            <el-form-item label="请求方法:">
              <span>{{ props.row.method }}</span>
            </el-form-item>
            <el-form-item label="请求参数:">
              <span>{{ props.row.params }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column prop="id" label="ID" align="left" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="ip" label="IP" />
      <el-table-column prop="address" label="来源" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="browser" label="浏览器" />
      <el-table-column label="请求耗时">
        <template slot-scope="{row}">{{ row.time }}ms</template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="异常详情" width="100px">
        <template slot-scope="scope">
          <el-button type="text" @click="info(scope.row.id)">查看详情</el-button>
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

    <el-dialog :visible.sync="dialogVisible" title="异常详情" append-to-body top="30px" width="85%">
      <pre>{{ errorInfo }}</pre>
    </el-dialog>
  </div>

</template>

<script>
import {
  errorList as listLogs, getErrorDetail
} from '@/api/logs'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ErrorLog',
  components: { Pagination },
  directives: { waves },

  data() {
    return {
      dialogVisible: false,
      errorInfo: null,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        pageSize: 10,
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
      listLogs(this.listQuery).then(response => {
        this.list = response.rows
        this.total = response.total
        this.listLoading = false
      })
    },

    handleFilter() {
      this.listQuery.page = 1
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

    info(id) {
      this.dialogVisible = true
      getErrorDetail(id).then(res => {
        console.log(res)
        this.errorInfo = res.exceptionDetail
      })
    }

  }
}
</script>
