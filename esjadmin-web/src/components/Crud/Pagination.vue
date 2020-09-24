<!--分页-->
<template>
  <div class="pagination-container" v-show="page.total>0">
    <el-pagination
      :background="true"
      :page-size.sync="page.size"
      :total="page.total"
      :current-page.sync="page.page"
      :page-sizes="[5, 10, 20, 30, 50, 100]"
      style="margin-top: 8px;"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="crud.sizeChangeHandler($event)"
      @current-change="crud.pageChangeHandler"
    />
  </div>
</template>
<script>
import { pagination } from '@crud/crud'
export default {
  mixins: [pagination()]
}
</script>

<style scoped>
.pagination-container {
  background: #fff;
  /* padding: 32px 16px; */
  padding: 0px 16px;
}
</style>
