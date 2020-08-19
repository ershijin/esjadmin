<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="120px">
    <el-form-item label="登录名">
      <el-input v-model="username" readonly />
    </el-form-item>
    <el-form-item label="原密码" prop="oldPassword">
      <el-input v-model="form.oldPassword" type="password" />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input v-model="form.newPassword" type="password" />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirm_password">
      <el-input v-model="form.confirm_password" type="password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">确认</el-button>
      <el-button @click="onCancel">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapGetters } from 'vuex'
import { updatePassword } from '@/api/user.js'

export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('确认密码不能少于6位'))
      } else {
        if (value !== this.form.newPassword) {
          callback(new Error('确认密码与新密码不同'))
        }
        callback()
      }
    }

    return {
      form: {
        oldPassword: '',
        newPassword: '',
        confirm_password: ''
      },
      rules: {
        oldPassword: [{ required: true, trigger: 'blur', validator: validatePassword }],
        newPassword: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirm_password: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]

      }
    }
  },
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          updatePassword(this.form).then(() => {
            this.$message({
              title: '成功',
              message: '密码修改成功！',
              type: 'success'
            })
          })
        } else {
          return false
        }
      })
    },
    onCancel() {
      this.$router.go(-1) // 返回上一层
    }
  }
}
</script>
