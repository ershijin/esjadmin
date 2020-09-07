<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">
        <el-col :span="6" :xs="24">
          <user-card :user="user" />
        </el-col>

        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab" @tab-click="handleClick">
              <el-tab-pane label="修改密码" name="account">
                <account :user="user" />
              </el-tab-pane>
              <el-tab-pane label="操作日志" name="operation-log">
                <el-table
                  v-loading="listLoading"
                  :data="list"
                  element-loading-text="Loading"
                  fit
                  stripe
                  highlight-current-row
                  style="width: 100%;"
                >
                  <el-table-column prop="id" label="ID" align="left" />
                  <el-table-column prop="ip" label="IP" />
                  <el-table-column prop="address" label="来源" />
                  <el-table-column prop="description" label="描述" />
                  <el-table-column prop="browser" label="浏览器" />
                  <el-table-column label="请求耗时">
                    <template slot-scope="{row}">{{ row.time }}ms</template>
                  </el-table-column>
                  <el-table-column align="right">
                    <template slot="header">
                      <div style="display:inline-block;float: right;cursor: pointer" @click="getList">创建时间<i class="el-icon-refresh" style="margin-left: 40px" /></div>
                    </template>
                    <template slot-scope="scope">
                      <span>{{ scope.row.createTime }}</span>
                    </template>
                  </el-table-column>
                </el-table>
                <!--分页组件-->
                <pagination
                  v-show="total>0"
                  :total="total"
                  :page.sync="listQuery.current"
                  :limit.sync="listQuery.size"
                  @pagination="getList"
                />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UserCard from './components/UserCard'
import Account from './components/Account'
import Pagination from '@/components/Pagination'

import { getUserLogs } from '@/api/logs'

export default {
  name: 'Profile',
  components: { UserCard, Account, Pagination },
  data() {
    return {
      user: {},
      activeTab: 'account',
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        current: 1,
        size: 10
      }
    }
  },
  computed: {
    ...mapGetters(['username', 'name', 'avatar', 'roles'])
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      this.user = {
        username: this.username,
        name: this.name,
        role: this.roles.join(' | '),
        email: 'admin@test.com',
        avatar: this.avatar
      }
    },
    getList() {
      this.listLoading = true
      getUserLogs(this.listQuery).then(response => {
        this.list = response.rows
        this.total = response.total
        this.listLoading = false
      })
    },
    handleClick(tab, event) {
      if (tab.name === 'operation-log') {
        this.getList()
      }
    }
  }
}
</script>
