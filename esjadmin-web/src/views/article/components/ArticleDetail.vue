
<template>
  <div class="createPost-container">
    <div class="createPost-main-container">
      <el-form ref="form" :model="form" :rules="rules" label-width="60px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择">
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
            class="image-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeUpload"
          >
            <div v-if="coverPicturePreview">
              <el-image
                class="image"
                :src="coverPicturePreview"
                fit="scale-down">
              </el-image>
              <i class="el-icon-delete image-tools" @click.stop="removeCoverPicture" title="点击删除" />
            </div>

            <i v-else class="el-icon-plus image-uploader-icon" />
          </el-upload>
          <input v-model="form.coverPicture" type="hidden">
        </el-form-item>

        <el-form-item label="图片墙">
          <el-upload
            action="#"
            list-type="picture-card"
            multiple
            :limit="5"
            :file-list="form.pictures"
            :before-upload="beforePictureUpload"
            :on-preview="handlePictureCardPreview"
            :on-remove="handlePictureCardRemove"
            :on-exceed="handleExceed">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="picturePreviewDialogVisible">
            <img width="100%" :src="picturePreviewDialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>

        <el-form-item label="链接" prop="link">
          <el-input v-model="form.link" />
        </el-form-item>

        <el-form-item prop="summary" style="margin-bottom: 30px;" label="摘要">
          <el-input v-model="form.summary" type="textarea" rows="5" />
        </el-form-item>

        <el-form-item prop="detail" style="margin-bottom: 30px;" label="详情">
          <!-- <Tinymce ref="editor" v-model="form.detail" :height="400" prop="detail" /> -->
          <Tinymce
            ref="detail"
            v-model="form.detail"
            :disabled="false"
            :images-upload-url="upload_api"
          />
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
import { getList as fetchCategories } from '@/api/articlecat.js'
import { uploadImage } from '@/api/file'
import { MessageBox, Notification } from 'element-ui'

const defaultForm = {
  id: undefined,
  title: '',
  categoryId: 1,
  link: '',
  coverPicture: '',
  pictures: [],
  summary: '',
  detail: ''
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
      upload_api: this.$config.upload_api,
      coverPicturePreview: '',
      categories: [],
      form: {},
      rules: {
        categoryId: [{
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
      },
      picturePreviewDialogVisible: false,
      picturePreviewDialogImageUrl: ''
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      getArticle(id).then(data => {
        this.form = data
        this.coverPicturePreview = data.coverPicture
      })
    } else {
      this.form = Object.assign({}, defaultForm)
    }

    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)
    // 获取分类列表
    fetchCategories().then(data => {
      this.categories = data
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
              this.coverPicturePreview = ''
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

    beforeUpload(file) {
      const formData = new FormData()
      formData.append('file', file)
      uploadImage(formData).then((data) => {
        this.coverPicturePreview = data.location
        this.form.coverPicture = data.location
      })
      return false
    },
    removeCoverPicture() {
      this.coverPicturePreview = ''
      this.form.coverPicture = ''
    },

    beforePictureUpload(file) {
      const formData = new FormData()
      formData.append('file', file)
      uploadImage(formData).then((data) => {
        if (!this.form.pictures) {
          this.form.pictures = []
        }
        this.form.pictures.push({ url: data.location })
      })
      return false
    },
    handlePictureCardPreview(file) {
      this.picturePreviewDialogImageUrl = file.url
      this.picturePreviewDialogVisible = true
    },
    handlePictureCardRemove(file, fileList) {
      this.form.pictures = fileList
    },
    handleExceed(files, fileList) {
      MessageBox.alert('最多只能上传5长图片！', '', {
        type: 'warning'
      })
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
    padding: 20px;

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
.image-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.image-uploader .el-upload:hover {
  border-color: #409eff;
}
.image-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 146px;
  height: 146px;
  line-height: 146px;
  text-align: center;
}
.image-tools {
  color: #8c939d;
  width: 28px;
  font-size: 28px;
  display: block;
  position: absolute;
  right: 0px;
  top: 0px;
}
.image-tools:hover {
  color: #409eff;
}
.image {
  width: 148px;
  height: 148px;
  margin-bottom: -10px;
}
</style>
