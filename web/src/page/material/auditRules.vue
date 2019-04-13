<template>
  <Drawer v-model="auditModel" width="950" :title="title" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('auditForm')">
    <el-row :gutter="20">
      <el-col :span="imgs.length?8:0">
        <!-- <viewer :images="imgs"> -->
          <div class="img-div" v-for="(item, index) in imgs" :key="index">
            <!-- v-drag="dragImg" -->
            <img class="audit-img" :src="item.url" :alt="item.name.split('/')[1]" @click="watchImg(index)">
          </div>
        <!-- </viewer> -->

      </el-col>
      <el-col :span="imgs.length?16:24">
        <el-form class="audit-rule" :model="auditForm" ref="auditForm">
          <p>材料审查细则(受理环节)</p>
          <div class="audit">
            <el-row v-for="(audit, index) in auditForm.acceptance" :key="-index-1">
              <el-col :span='watchPic?24:20'>
                <el-form-item :prop="'acceptance.' + index + '.content'" class="no-bottom">
                  <!-- :rules="{
                    required: true, message: '不能为空', trigger: 'blur'
                  }" -->
                  <el-input v-model="audit.content" :readonly="watchPic" @dblclick.native="judge('材料审查细则(受理环节)')"><template slot="prepend">{{index+1}}</template></el-input>
                </el-form-item>
              </el-col>
              <el-col  :span='4' v-if="!watchPic">
                <div style="float: right">
                  <el-button @click="addAudit" type="primary" icon="el-icon-plus" style="margin-right: 10px;" circle></el-button>
                  <el-button @click.prevent="handleDelete(index, 'removeAudit')" type="danger" icon="el-icon-delete" circle :disabled="auditForm.acceptance.length===1"></el-button>
                </div>
              </el-col>
            </el-row>
          </div>
          <p>材料审查细则(审批环节)</p>
          <div class="approver">
            <el-row v-for="(approver, idx) in auditForm.approval" :key="idx">
              <el-col :span='watchPic?24:20'>
                <el-form-item :prop="'approval.' + idx + '.content'" class="no-bottom">
                  <el-input v-model="approver.content" :readonly="watchPic" @dblclick.native="judge('材料审查细则(审批环节)')"><template slot="prepend">{{idx+1}}</template></el-input>
                </el-form-item>
              </el-col>
              <el-col :span='4' v-if="!watchPic">
                <div style="float: right">
                  <el-button @click="addApprover" type="primary" icon="el-icon-plus" style="margin-right: 10px;" circle></el-button>
                  <el-button @click.prevent="handleDelete(idx, 'removeApprover')" type="danger" icon="el-icon-delete" circle :disabled="auditForm.approval.length===1"></el-button>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-form>
      </el-col>
    </el-row>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('auditForm')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('auditForm')" v-if="!watchPic">确定</Button>
    </div>

    <ul ref="imgView" style="display:none">
      <li v-for="(item, index) in imgs" :key="index"><img :src="item.url" :alt="item.name"></li>
    </ul>

    <!-- 删除 -->
    <DeleteDialog class="audit-delete" :visible="showDel" @cancel="cancelDel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="材料分组评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </Drawer>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import Viewer from 'viewerjs'
import Sortable from 'sortablejs'

import DeleteDialog from '@/components/dialog.vue'
import JudgeDialog from '@/components/judgeDialog'

import { department } from '@/api/service'

import { validatSpace } from '@/utils/validate'

let scale = 1
export default {
  components: { DeleteDialog, JudgeDialog },
  props: {
    title: {
      type: String,
    },
    // 当前点击行
    currentRow: {
      type: Object
    },
    auditForm: {
      type: Object
    },
    auditModel: {
      type: Boolean,
      default: false
    },
    imgs: {
      type: Array,
      default: []
    },
    watchPic: {
      type: Boolean,
      default: false
    },
    itemSee: {
      type: Boolean,
      default: false
    },
    editState: {
      type: Boolean,
      default: true // true 是梳理员和部门管理员，false是评审员
    }
  },
  computed: {
    ...mapState([
      'itemDetail'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      return itemDetail
    }
  },
  data () {
    return {
      userRole: localStorage.uesrRole,
      // 评审
      judgeShow: false,
      judgeForm: {},
      loading: false,
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },
      // 删除
      showDel: false,
      currentIndex: 0,
      deleObj: null,
      currentForm: {}
    }
  },
  watch: {
    loading (val) {
      if (!val) scale = 1
    },
    auditForm (val) {
      this.currentForm = JSON.parse(JSON.stringify(val))
    }
  },
  mounted () {},
  methods: {
    // 滚轮事件
    // 缩放
    mousewheel (e, index) {
      let img = document.querySelectorAll('.audit-img')[index]
      if(e.deltaY>0) {
        // 缩小
        scale = scale > 0.5 ? scale - 0.1 : 0.5
      } else {
        // 放大
        scale = scale < 5 ? scale + 0.1 : 5
      }
      img.style.transform = `scale(${scale})`
    },
    mouseDown (e, index) {
      let img = document.querySelectorAll('.audit-img')[index]
      let parent = img.parentNode
      let limit = [0, 289, 0, 260]
      /**
       * e.clientX,e.clientY 鼠标距离浏览器左上角距离
       * img.offsetLeft，img.offsetTop 图片相对于父元素的left和top
       */
      let disX = e.clientX - img.offsetLeft
      let disY = e.clientY - img.offsetTop

      e.preventDefault()
      if (img.setCapture) {
        /**
         * 该函数在属于当前线程的指定窗口里设置鼠标捕获。
         * 一旦窗口捕获了鼠标，所有鼠标输入都针对该窗口，无论光标是否在窗口的边界内。
         * 同一时刻只能有一个窗口捕获鼠标。
         * 如果鼠标光标在另一个线程创建的窗口上，只有当鼠标键按下时系统才将鼠标输入指向指定的窗口。
         */
        img.setCapture()
      }
      document.onmousemove = function (ev) {
        let left = ev.clientX - disX
        let top = ev.clientY - disY
        let width = img.offsetWidth * scale
        let height = img.offsetHeight * scale
        left = left <= limit[0] ? limit[0] : ((left >= limit[1] - width) ? limit[1] - width : left)
        top = top <= limit[2] ? limit[2] : ((top >= limit[3] - height) ? limit[3] - height : top)

        img.style.left = left + 'px'
        img.style.top = top + 'px'
        ev.preventDefault()
      }
      document.onmouseup = function (ev) {
        document.onmousemove = null
        document.onmouseup = null
        ev.preventDefault()
      }
    },
    // 评审
    judge (name) {
      let endTime = this.itemInfo.examineEndTime
      if(this.userRole!=='2'&&endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          id: this.itemInfo.id,
          sheetNum: 2,
          lineNumber: Number(this.currentRow.orderNum),
          propertyName: `${this.currentRow.name}-->${name}`,
          property: `${this.currentRow.name}-->${name}`,
          questionContent: ''
        }
      }
    },
    judgeCancel () {
      this.judgeShow = false
    },
    dragImg(val) {},
    watchImg (index) {
      global.thisView && global.thisView.destroy()
      global.thisView = new Viewer(this.$refs.imgView)
      global.thisView.view(index)
    },
    // 添加审核人受理要点
    addAudit () {
      this.$refs.auditForm.model.acceptance.push({ content: '', type: 1})
    },
    // 移除
    removeAudit (index) {
      if (index !== -1) {
        this.$refs.auditForm.model.acceptance.splice(index, 1)
        this.showDel = false
      }
    },
    // 添加审批人受理要点
    addApprover () {
      this.$refs.auditForm.model.approval.push({ content: '', type: 2 })
    },
    // 移除
    removeApprover (index) {
      if (index !== -1) {
        this.$refs.auditForm.model.approval.splice(index, 1)
        this.showDel = false
      }
    },
    // 删除
    handleDelete (index, type) {
      this.showDel = true
      this.currentIndex = index
      this.deleObj = type
    },
    cancelDel () {
      this.showDel = false
    },
    del () {
      if (this.deleObj==='removeAudit') {
        this.removeAudit(this.currentIndex)
      } else {
        this.removeApprover(this.currentIndex)
      }
    },
    // 取消
    cancel (name) {
      if(this.watchPic) {
        this.$emit('cancel')
        return
      }
      this.$emit('cancel', this.currentForm)
      this.loading = false
      // this.$refs[name].resetFields();
    },
    // 提交
    submitForm (name) {
      this.loading = true
      if(this.watchPic) {
        this.cancel('auditForm')
        return
      }
      this.$refs[name].validate((valid) => {
        if (valid) {
          let acceptance = this.auditForm.acceptance.map((x, index) => {
            return {...x,number: index+1}
          })
          let approval = this.auditForm.approval.map((x, index) => {
            return {...x,number: index+1}
          })
          this.$emit('cancel', {acceptance, approval})
          this.loading = false
        } else {
          this.$message.error('表单验证失败')
          setTimeout(() => {this.loading = false},1500)
          return false;
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.img-div {
  position: relative;
  width: 100%;
  height: 260px;
  overflow: hidden;
  box-shadow: 2px 2px 10px rgba(0,0,0,.5);
  &:not(:last-child) {
    margin-bottom: 10px;
  }
  .audit-img {
    position: absolute;
    width: 100%;
    transform: scale(1);
    transition: all ease 0.5s;
    cursor: pointer;
  }
}
.audit-rule {
  p{
    border-left: 3px solid #2088fc;
    padding: 5px 10px;
    background-color: #eee;
  }
  .audit,.approver {
    .el-row:nth-child(1) {
      margin-top: 1rem;
    }
    .no-bottom {
      & /deep/ .el-input-group__prepend {
        width: 60px;
      }
    }
    .el-row:not(:last-child) .el-col .no-bottom {
      margin-bottom: 0;
    }
  }
}
.drawer-footer{
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
