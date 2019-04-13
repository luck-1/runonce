<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChage">
      <i class="el-icon-menu"></i>
    </div>
    <div class="logo">最多跑一次事项梳理平台</div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 全屏显示 -->
        <div class="btn-fullscreen" @click="handleFullScreen">
          <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
            <i class="el-icon-rank"></i>
          </el-tooltip>
        </div>
        <!-- 消息中心 -->
        <!-- <div class="btn-bell">
          <el-tooltip effect="dark" :content="message?`有${message}条未读消息`:`消息中心`" placement="bottom">
            <router-link to="/problem">
              <i class="el-icon-bell"></i>
            </router-link>
          </el-tooltip>
          <span class="btn-bell-badge" v-if="message"></span>
        </div> -->
        <!-- 用户头像 -->
        <div class="user-avator"><img :src="avatar"></div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link"> {{userName}} <i class="el-icon-caret-bottom"></i></span>
          <el-dropdown-menu slot="dropdown">
            <!-- <a href="javascript:void(0);" target="_blank">
              <el-dropdown-item>我的消息</el-dropdown-item>
            </a> -->
            <!-- divided -->
            <el-dropdown-item command="edit">修改密码</el-dropdown-item>
            <el-dropdown-item command="loginout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 修改密码 -->
    <el-dialog title="修改密码" append-to-body :visible.sync="editVisible" width="500px"
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form ref="passwordForm" :rules="passwordValidate" :model="passwordForm" label-width="80px">
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" size="mini" v-model="passwordForm.oldPass" placeholder="请填写"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPass">
          <el-input type="password" size="mini" v-model="passwordForm.newPass" placeholder="请填写"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="passwdCheck">
          <el-input type="password" size="mini" v-model="passwordForm.passwdCheck" placeholder="请填写"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submit('passwordForm')">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import bus from '../common/bus'
import { aliConfig } from "@/utils/aliyunConfig"
import { userMangeService } from '@/api/service'

const OSS = require("ali-oss")
const client = new OSS(aliConfig)
export default {
  name: 'common-header',
  data () {
    const validatePass = (rule, value, callback) => {
      if (!value) {
        callback(new Error("密码不能为空"));
      } else if (value.length < 6) {
        callback(new Error("密码长度不得小于6位"));
      } else if(rule.field!=='oldPass') {
        if (this.passwordForm.passwdCheck) {
          // 对第二个密码框单独验证
          this.$refs.passwordForm.validateField("passwdCheck");
        }
      }
      callback();
    };
    const validateCheck = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.passwordForm.newPass) {
        callback(new Error("两个输入密码不匹配!"));
      } else {
        callback();
      }
    };
    return {
      userName: localStorage.userName,
      avatar: localStorage.avatar,
      collapse: false,
      fullscreen: false,
      message: 2,
      editVisible:false,
      passwordForm: {
        oldPass: '',
        newPass: '',
        passwdCheck: ''
      },
      passwordValidate: {
        oldPass: [
          { required: true, validator: validatePass, trigger: "blur" }
        ],
        newPass: [
          { required: true, validator: validatePass, trigger: "blur" }
        ],
        passwdCheck: [
          { required: true, validator: validateCheck, trigger: "blur" }
        ]
      }
    }
  },
  mounted () {
    this.avatarName()
    if (document.body.clientWidth < 1500) {
      this.collapseChage()
    }
  },
  methods: {
    avatarName() {
      if(this.avatar!=='https://s1.ax1x.com/2018/05/19/CcdVQP.png') {
        this.avatar = client.signatureUrl(this.avatar, { expires: 80000 })
      }
    },
    // 用户名下拉菜单选择事件
    handleCommand (command) {
      if(command === 'edit') {
        // 修改密码
        this.editVisible = true
      } else if (command === 'loginout') {
        this.$router.push('/login')
        localStorage.clear()
      }
    },
    cancel () {
      this.$refs.passwordForm.resetFields()
      this.editVisible = false
    },
    submit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if(this.passwordForm.oldPass === this.passwordForm.newPass){
            this.cancel()
            return
          }
          this.editPassword()
        } else {
          this.$message.error('必填项输入不能为空')
        }
      })
    },
    async editPassword() {
      let res = await userMangeService.editPassword(this.passwordForm)
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.cancel()
      }
    },
    // 侧边栏折叠
    collapseChage () {
      this.collapse = !this.collapse
      bus.$emit('collapse', this.collapse)
    },
    // 全屏事件
    handleFullScreen () {
      let element = document.documentElement
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      }
      this.fullscreen = !this.fullscreen
    }
  }
}
</script>
<style lang="scss" scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 50px;
  font-size: 22px;
  color: #fff;
}
.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 50px;
}
.header .logo {
  float: left;
  width: 250px;
  line-height: 50px;
}
.header-right {
  float: right;
  padding-right: 50px;
}
.header-user-con {
  display: flex;
  height: 50px;
  align-items: center;
}
.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
}
.btn-bell-badge {
  position: absolute;
  right: 0;
  top: -2px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}
.btn-bell .el-icon-bell {
  color: #fff;
}
.user-name {
  margin-left: 10px;
}
.user-avator {
  margin-left: 20px;
}
.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}
.el-dropdown-menu__item {
  text-align: center;
}
</style>
