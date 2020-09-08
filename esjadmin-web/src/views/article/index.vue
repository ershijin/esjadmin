<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" clearable placeholder="关键词" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-select v-model="listQuery.categoryId" clearable placeholder="分类" class="filter-item">
        <el-option
          v-for="item in categories"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
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
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column label="封面" width="350" align="center">
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.coverPicture"
            style="width: 320px; max-height: 152px"
            :src="UPLOAD_BASE + scope.row.coverPicture"
            fit="scale-down"
          />
        </template>
      </el-table-column>
      <el-table-column label="标题">
        <template slot-scope="scope">{{ scope.row.title }}</template>
      </el-table-column>
      <el-table-column label="链接" width="100">
        <template slot-scope="scope">{{ scope.row.link }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">编辑</el-button>
          <el-button
            v-if="row.status!='deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.pageSize" @pagination="getList" />

  </div>
</template>

<script>
import { listArticle as fetchList, removeArticle } from '@/api/article'
import { getList as fetchCategories } from '@/api/articlecat.js'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      UPLOAD_BASE: process.env.UPLOAD_BASE_URL,
      list: [],
      total: 0,
      listLoading: true,
      categories: null,
      listQuery: {
        page: 1,
        pageSize: 10,
        keyword: undefined,
        categoryId: undefined
      }
    }
  },
  created() {
    this.getList()
    // 获取分类列表
    fetchCategories().then(response => {
      this.categories = response
    })
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.list
        this.total = response.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    handleCreate() {
      this.$router.push({
        path: 'create'
      })
    },
    handleUpdate(row) {
      this.$router.push({
        path: 'edit/' + row.id
      })
    },
    handleDelete(row) {
      this.$confirm('此操作不能被还原, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeArticle(row.id).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getList()
        })
      }).catch(() => {})
    }
  }
}
</script>
