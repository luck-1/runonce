<template>
  <div class="login">
    <div class="content">
      <p style="font-size: 20px;text-align: center;margin-bottom: 20px;margin-top:-10px;font-weight: bold;">最多跑一次</p>
      <el-form :model="loginForm" status-icon :rules="rules2" ref="loginForm" label-width="0" class="login-form">
        <el-form-item prop="user">
          <el-input size="medium" placeholder="账号" auto-complete="off" prefix-icon="el-icon-mobile-phone" v-model="loginForm.user" @keyup.enter.native="next()"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input size="medium" type="password" placeholder="密码" auto-complete="off" prefix-icon="el-icon-goods" v-model="loginForm.password" @keyup.enter.native="submitForm('loginForm')" ref="password"></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="10">
            <el-form-item prop="code">
              <el-input size="medium" type="text" placeholder="验证码" auto-complete="off" prefix-icon="el-icon-goods" v-model="loginForm.code" @keyup.enter.native="submitForm('loginForm')" ref="code"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <div id="v_container" style="width: 90px;height: 37px;float:right" ref="containerCode"></div>
          </el-col>
        </el-row>

        <el-form-item class="remenber-pass">
          <el-checkbox v-model="single"><span>记住密码</span></el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="btn-login" @click="submitForm('loginForm')">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Verify from '@/utils/verify.js'
import { validatSpace } from '@/utils/validate'
import { LoginService } from '@/api/service'
import { common } from '@/utils/common'

export default {
  data () {
    const validateUser = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (validatSpace(value)) {
        callback(new Error('不能包含空字符'))
      }
      callback()
    }
    const validatePass = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (validatSpace(value)) {
        callback(new Error('不能包含空字符'))
      }
      callback()
    }
    return {
      loginForm: {
        user: '',
        password: ''
      },
      single: false,
      rules2: {
        user: [
          { required: true, validator: validateUser, trigger: 'blur' }
        ],
        password: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.verifyCode = new Verify('v_container')
    this.cookieOperate()
  },
  methods: {
    next () {
      this.$refs.password.focus()
    },
    // 记住密码
    cookieOperate () {
      if (localStorage) {
        if (localStorage.getItem('userName') != null && localStorage.getItem('pass') != null) {
          this.loginForm.user = localStorage.getItem('userName')
          this.loginForm.password = localStorage.getItem('pass')
          this.single = true
        } else {
          this.single = false
        }
      } else {
        this.single = false
      }
    },
    // 登录验证
    submitForm (name) {
      if (this.single) {
        localStorage.setItem('userName', this.loginForm.user)
        localStorage.setItem('pass', this.loginForm.password)
      } else {
        localStorage.removeItem('userName')
        localStorage.removeItem('pass')
      }
      // 验证用户名密码
      this.$refs[name].validate((valid) => {
        if (valid) {
          const res = this.verifyCode.validate(this.loginForm.code)
          if (res) {
            localStorage.setItem('userName', this.loginForm.user)
            this.$router.push({ path: '/' })
            // this.doLogin()
          } else {
            this.$message('验证码输入有误！')
          }
        } else {
          this.$message({
            message: '表单验证失败',
            type: 'error',
            duration: 5 * 1000
          })
          return false
        }
      })
    },
    async doLogin () {
      let res = await LoginService.login(this.loginForm)
      
      if (res.code === 200) {
        common.setToken(res.obj.token)
        common.setStore(res.obj)
        this.$router.push({ path: '/itemManagement' })
      } else {
        this.$message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        })
      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  width: 100vw;
  height: 100vh;
  background: url('../assets/background.svg');
}

.content {
  position: absolute;
  top: 50%;
  left: 50%;
  padding: 40px 30px;
  width: 400px;
  box-sizing: border-box;
  // border-radius: .4em;
  // box-shadow: 0 0 .25em rgba(0, 0, 0, .25);
  text-align: center;
  opacity: 8;
  transform: translate(-50%, -50%);
  background-color: rgba(255, 255, 255, .6);
  .login-form .el-form-item--small.el-form-item {
    margin-bottom: 22px
  }
  .btn-code {
    float: right;
    margin-top: 1px;
  }
  .remenber-pass {
    float: left;
  }
  .btn-login {
    width: 100%;
  }
}
</style>
