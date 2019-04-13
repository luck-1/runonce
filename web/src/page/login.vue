<template>
  <Row type="flex" justify="center" align="middle" class="login" @keydown.enter.native="submitLogin">
    <i-col :xs="{span:22}" class="login-content">
      <Row class="login-form">
        <p class="login-title">最多跑一次事项梳理平台</p>
          <Form ref="usernameLoginForm" :model="form" :rules="rules" class="form">
            <FormItem prop="username">
              <Input v-model="form.username" prefix="ios-contact" size="large" clearable placeholder="请输入用户名" autocomplete="off" />
            </FormItem>
            <FormItem prop="password">
              <Input type="password" v-model="form.password" prefix="ios-lock" size="large" clearable placeholder="请输入密码" autocomplete="off" />
            </FormItem>
            <Row>
              <i-col span="10">
                <FormItem prop="code">
                  <i-input type="text" size="large" placeholder="验证码" auto-complete="off" prefix="md-alert" v-model="form.code" @keyup.enter.native="submitLogin" ref="code"></i-input>
                </FormItem>
              </i-col>
              <i-col span="14">
                <div id="v_container" style="width: 90px;height: 37px;float:right" ref="containerCode"></div>
              </i-col>
            </Row>
          </Form>
        <Row>
          <Button class="login-btn" type="primary" size="large" :loading="loading" @click="submitLogin" long>
            <span v-if="!loading">登录</span>
            <span v-else>登录中...</span>
          </Button>
        </Row>
      </Row>
    </i-col>
  </Row>
</template>
<script>
import Verify from '@/utils/verify.js'
import Cookies from "js-cookie";
import { loginService, menu } from "@/api/service";
export default {
  data() {
    const validateMobile = (rule, value, callback) => {
      var reg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (!reg.test(value)) {
        callback(new Error("手机号格式错误"));
      } else {
        callback();
      }
    };
    return {
      error: false,
      errorMsg: "",
      loading: false,
      sending: false,
      sended: false,
      count: 60,
      countButton: "60 s",
      errorCode: "",
      form: {
        username: "",
        password: "",
        code: ""
      },
      rules: {
        username: [
          { required: true, message: "账号不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "验证码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  mounted() {
    this.verifyCode = new Verify('v_container')
  },
  methods: {
    submitLogin() {
      this.$refs.usernameLoginForm.validate(valid => {
        if (valid) {
          this.loading = true
          let str="userName="+this.form.username+"&code="+this.form.password;
          const code = this.verifyCode.validate(this.form.code)
          if (code) {
            loginService.login(str).then(res => {
              if (res.code === 0) {
                localStorage.setItem("accessToken", res.obj?res.obj.webToken:'')
                // 获取用户信息
                this.getUserInfo()
                this.getMenuList()
              } else {
                this.$message.error(res.msg)
                this.verifyCode = new Verify('v_container')
                setTimeout(() => { this.loading = false }, 1500)
              }
            })
          } else {
            setTimeout(() => { this.loading = false }, 1500)
            this.$message.error('验证码输入有误！')
          }
        }
      })
    },
    getMenuList () {
      menu.getMenuList().then(res=>{
        if(res.code === 0){
          this.$store.state.system.userMenu = res.obj
          if(res.obj.length>0) {
            setTimeout(()=>{
              this.$router.push(res.obj[0].path)
            },300)
          } else {
            setTimeout(() => { this.loading = false }, 1500)
            this.$message.error('该角色没有配置菜单！')
          }
        }
      })
    },
    getUserInfo () {
      loginService.userInfo().then(res => {
        if (res.code === 0) {
          localStorage.setItem("userName", res.obj.userName)
          localStorage.setItem("avatar", res.obj.avatar)
          localStorage.setItem("userInfo", JSON.stringify(res.obj))
          localStorage.setItem('uesrRole',res.obj.roles[0].dataType)
          this.$store.state.system.avatorImgPath = res.obj.avatar
        } else {
          setTimeout(() => { this.loading = false }, 1500)
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
  .login {
    width: 100vw;
    height: 100vh;
    background: url('../assets/background.jpg')
  }

  .login-title {
    margin-bottom: 20px;
    font-size: 20px;
    text-align: center;
    font-weight: bold;
  }

  .login-content {
    position: absolute;
    top: 50%;
    left: 50%;
    padding: 40px 30px;
    width: 400px;
    box-sizing: border-box;
    border-radius: .4em;
    // box-shadow: 0 0 .25em rgba(0, 0, 0, .25);
    text-align: center;
    opacity: 8;
    transform: translate(-50%, -50%);
    background-color: rgba(255, 255, 255, .6);
  }
</style>
