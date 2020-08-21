<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <span>个人信息</span>
    </div>

    <div class="user-profile">
      <div class="box-center">
        <el-avatar :size="100" :src="user.avatar ? user.avatar : defaultAvatar" />
      </div>
    </div>

    <ul class="user-info">
      <li>
        <div style="height: 100%">
          <svg-icon icon-class="user" />登录账号
          <div class="user-right">{{ user.username }}</div>
        </div>
      </li>
      <li>
        <svg-icon icon-class="people" />用户姓名
        <div class="user-right">{{ user.name }}</div>
      </li>
      <li>
        <svg-icon icon-class="phone" />手机号码
        <div class="user-right">{{ user.phone }}</div>
      </li>
    </ul>
  </el-card>
</template>

<script>
import defaultAvatar from '@/assets/images/avatar.png'
export default {
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
      defaultAvatar: defaultAvatar
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
