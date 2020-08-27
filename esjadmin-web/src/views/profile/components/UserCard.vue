<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <span>个人信息</span>
    </div>

    <div class="user-profile">
      <div class="box-center">
        <el-avatar
          style="cursor: pointer"
          alt="点击上传头像"
          title="点击上传头像"
          :size="100"
          :src="avatar ? $config.upload_base_url + avatar : defaultAvatar"
          @click.native="imagecropperShow=true"
        />
        <image-cropper
          v-show="imagecropperShow"
          :key="imagecropperKey"
          :width="200"
          :height="200"
          :url="$config.avatar_upload_api"
          @close="close"
          @crop-upload-success="cropSuccess"
        />
      </div>
    </div>

    <ul class="user-info">
      <li>
        <div style="height: 100%">
          <svg-icon icon-class="user" />登录账号
          <div class="user-right">{{ userinfo.username }}</div>
        </div>
      </li>
      <li>
        <svg-icon icon-class="people" />用户姓名
        <div class="user-right">{{ userinfo.name }}</div>
      </li>
      <li>
        <svg-icon icon-class="phone" />手机号码
        <div class="user-right">{{ userinfo.phone }}</div>
      </li>
    </ul>
  </el-card>
</template>

<script>
import ImageCropper from '@/components/ImageCropper'
import defaultAvatar from '@/assets/images/avatar.png'
import store from '@/store'
export default {
  components: { ImageCropper },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          avatar: '',
          role: ''
        }
      }
    }
  },
  data() {
    return {
      imagecropperShow: false,
      imagecropperKey: 0,
      defaultAvatar: defaultAvatar,
      userinfo: this.user,
      avatar: this.user.avatar
    }
  },
  methods: {
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.avatar = resData.avatar
      store.dispatch('user/getInfo')
    },
    close() {
      this.imagecropperShow = false
    }
  }
}

</script>

<style lang="scss" scoped>
.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
}

.user-info {
  padding-left: 0;
  list-style: none;
  li {
    border-bottom: 1px solid #f0f3f4;
    padding: 11px 0;
    font-size: 13px;
  }
  .user-right {
    float: right;
    a {
      color: #317ef3;
    }
  }
}
</style>
