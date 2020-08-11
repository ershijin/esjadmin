<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="分类名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">添加</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { add } from '@/api/articlecat.js'

export default {
  data() {
    return {
      form: {
        name: ''
      }
    }
  },
  methods: {
    onSubmit() {
      add(this.form)
      this.$message('submit!')
    },
    onCancel() {
      this.$router.go(-1)// 返回上一层
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

