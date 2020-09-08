
<template>
  <div class="createPost-container">
    <div class="createPost-main-container">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="分类" prop="category_id">
          <el-select v-model="form.category_id" placeholder="请选择">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="封面">
          <el-upload
            class="avatar-uploader"
            :action="uploadAction"
            :headers="headers"
            :show-file-list="false"
            :file-list="form.pictures"
            :on-preview="handlePictureCardPreview"
            :on-success="handleSuccess"
            :on-remove="handleRemove"
          >
            <img v-if="cover_picture_preview" :src="cover_picture_preview" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt>
          </el-dialog>
          <input v-model="form.coverPicture" type="hidden">
        </el-form-item>

        <el-form-item label="链接" prop="link">
          <el-input v-model="form.link" />
        </el-form-item>

        <el-form-item prop="summary" style="margin-bottom: 30px;" label="摘要">
          <el-input v-model="form.summary" type="textarea" rows="5" />
        </el-form-item>

        <el-form-item prop="detail" style="margin-bottom: 30px;" label="详情">
          <Tinymce ref="editor" v-model="form.detail" :height="400" prop="detail" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">确定</el-button>
          <el-button @click="onCancel">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'

import { createArticle, getArticle, updateArticle } from '@/api/article.js'
import { removeFile } from '@/api/file.js'
import { getList as fetchCategories } from '@/api/articlecat.js'
import { getToken } from '@/utils/auth'

const defaultForm = {
  id: undefined,
  title: '',
  category_id: 1,
  link: '',
  cover_picture: '',
  // pictures: [{
  //   url: 'http://localhost:8080/bonusmall/upload/2019/05/19/22591082672.png',
  //   rela_url: '2019/05/19/22591082672.png'
  // }],
  summary: '',
  detail: 'asaasasas'
}

export default {
  name: 'ArticleDetail',
  components: { Tinymce },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      uploadAction: process.env.VUE_APP_BASE_API + '/upload',
      cover_picture_preview: '',
      categories: [{ id: 1, name: '11111' }],
      headers: {
        'X-Token': getToken()
      },
      form: {},
      rules: {
        category_id: [{
          type: 'number',
          required: true,
          message: '请选择分类',
          trigger: 'change'
        }],
        title: [{
          required: true,
          message: '请输入标题',
          trigger: 'blur'
        }]
      }
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      getArticle(id).then(response => {
        this.form = response.data
        this.cover_picture_preview = response.data.coverPicture ? process.env.UPLOAD_BASE_URL + response.data.coverPicture : ''
      })
    } else {
      this.form = Object.assign({}, defaultForm)
    }

    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)
    // 获取分类列表
    fetchCategories().then(response => {
      this.categories = response
    })
  },
  mounted() {
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            const id = this.$route.params && this.$route.params.id
            updateArticle(id, this.form).then(() => {
              this.$message({
                title: '成功',
                message: '更新成功！',
                type: 'success'
              })
            }).catch((response) => {
              this.$message({
                title: '提示',
                message: '更新失败！',
                type: 'error'
              })
            })
          } else {
            createArticle(this.form).then(() => {
              this.form = Object.assign({}, defaultForm)
              this.$refs.form.resetFields()
              this.cover_picture_preview = ''
              this.$message({
                title: '成功',
                message: '添加成功！',
                type: 'success'
              })
            })
          }
        }
      })
    },
    onCancel() {
      this.$router.go(-1) // 返回上一层
    },
    handleRemove(file, fileList) {
      removeFile(file.rela_url)
      this.form.pictures = fileList
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleSuccess(response, file, fileList) {
      console.log(response.data.rela_url)
      this.form.coverPicture = response.data.rela_url
      this.cover_picture_preview = process.env.UPLOAD_BASE_URL + response.data.rela_url
    }
  }
}
</script>

<style scoped>
.line {
  text-align: center;
}
</style>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";

.createPost-container {
  position: relative;

  .createPost-main-container {
    padding: 40px 45px 20px 50px;

    .postInfo-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;

      .postInfo-container-item {
        float: left;
      }
    }
  }

  .word-counter {
    width: 40px;
    position: absolute;
    right: 10px;
    top: 0px;
  }
}

.article-textarea ::v-article {
  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 320px;
  max-height: 152px;
  display: block;
}
</style>
