<template>
  <Drawer v-model="modifyModel" width="750" title="变更履历" :styles="styles" :closable="false" :mask-closable="false">
    <div>
    <!-- 变更状态  1、已发布，2、变更申请中，3、变更中，4、驳回 -->
      <TimeLine :class="[contentClass(item.state)]" :time="item.dateOfChange" :user="item.who" v-for="(item, index) in historyLog" :key="index">
        <span slot="version">V{{item.versionNumber}}</span>
        <div class="discuss-content contentbg" @click="modifyHistory(item)" slot="content">
          <i>{{item.state===1?'已发布':item.state===2?'变更申请中':item.state===3?'变更中':'驳回'}}</i>
          <p v-html="item.changeContent"></p>
        </div>
      </TimeLine>
    </div>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel()">取消</Button>
      <Button style="margin-right: 8px" v-if="revisionShow" type="primary" @click="applyChange()">申请变更</Button>
      <Button style="margin-right: 8px" v-if="modifyShow" type="primary" @click="allowChange()">允许变更</Button>
      <Button  @click="notAllowChange()" v-if="modifyShow">不允许变更</Button>
    </div>
    <!-- 申请变更dialog -->
    <el-dialog title="变更申请" append-to-body width="600px"
      :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" :visible="eventVisible">
      <el-form :model="applyForm" :rules="applyRules" ref="applyForm" class="search-form">
        <el-form-item label="事项名称" prop="eventName" label-width="95px">
          <el-input v-model="applyForm.eventName" readonly></el-input>
        </el-form-item>

        <el-form-item label="变更事由" prop="cause" label-width="95px">
          <el-input v-model="applyForm.cause" placeholder="请输入变更事由"  type="textarea" resize="none" :autosize="{ minRows: 2, maxRows: 6}"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelModifyEvent()">取 消</el-button>
        <el-button :loading="applyloading" type="primary" @click="submitModify">提交</el-button>
      </span>
    </el-dialog>
    <!-- 驳回dialog -->
    <el-dialog title="确定不允许事项修改吗？确定驳回申请填写驳回理由吗？" append-to-body width="600px"
      :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" :visible="rejectVisible">
      <el-form :model="rejectForm" :rules="rejectRules" ref="rejectForm" class="search-form">
        <el-form-item label="驳回理由" prop="cause" label-width="95px">
          <el-input v-model="rejectForm.cause" placeholder="请输入变更事由"  type="textarea" resize="none" :autosize="{ minRows: 2, maxRows: 6}"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelReject()">取 消</el-button>
        <el-button :loading="rejectloading" type="primary" @click="submitReject()">确定</el-button>
      </span>
    </el-dialog>
  </Drawer>
</template>

<script>
import { department } from '@/api/service'
import TimeLine from '@/components/timeLine'
import {revisionhistory } from '@/api/service'
import { validatSpace } from '@/utils/validate'

export default {
  components: { TimeLine},
  props: {
    modifyModel:{
      type: Boolean,
      default: false
    },
    revisionEvent:{
      type: Object,
      default: {}
    },
    historyLog:{
      type: Array,
      default: []
    },
    revisionShow:{
      type: Boolean,
      default: false
    }
  },
  data () {
    const checkName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (validatSpace(value)) {
        callback(new Error('不能包含空字符'))
      }
      callback()
    }
    return {
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },
      eventVisible:false,
      rejectVisible:false,
      applyForm:{},
      rejectForm:{},
      applyloading: false,
      rejectloading: false,
      applyRules: {
        cause: [
          { required: true, validator: checkName, trigger: 'blur' }
        ],
      },
      rejectRules: {
        cause: [
          { required: true, validator: checkName, trigger: 'blur' }
        ],
      },
      modifyShow:false,
      revisionHistoryItem:{},
    }
  },
  methods: {
    // 取消
    cancel () {
      this.$emit('cancel')
    },
    //点击变更记录
    async modifyHistory (historyItem){
      this.revisionHistoryItem = historyItem;
      this.modifyShow = false;
      if(historyItem.id && historyItem.state == 2){
        let res = await revisionhistory.SuperButton(historyItem.id);
        if(res.obj){
          this.modifyShow = true;
        }else{
          this.modifyShow = false;
        }
      }
    },
    // 内容类样式
    contentClass(state) {
      let className = ''
      switch(state) {
        case 1: className = 'public';break;
        case 2: className = 'apply';break;
        case 3: className = 'change';break;
        case 4: className = 'reject';break;
        default:
      }
      return className
    },

    /**变更状态
    * 1、已发布，2、变更申请中，3、变更中，4、驳回
    */
    //申请变更
    applyChange (){
      this.eventVisible = true;
      this.revisionEvent.state = 2;
      this.applyForm.eventName = this.revisionEvent.eventName;
      this.applyForm.id = this.revisionEvent.id;
      this.applyForm.state = this.revisionEvent.state;
    },
    //允许变更
    async allowChange (){
      this.revisionEvent.state = 3;
       let postjson = {
        dmId:this.revisionEvent.id,
        id:this.revisionHistoryItem.id,
        state:this.revisionEvent.state,
      };
      let res = await revisionhistory.update(postjson);
      if(res.code === 0){
        this.$message.success(res.msg);
        this.$emit('success')
        this.modifyHistory(this.revisionHistoryItem);
      }else{
        this.$message.error(res.msg);
      }
    },
    //提交申请变更
    submitModify (){
      this.applyloading = true;
      this.$refs.applyForm.validate((valid) => {
        if (valid) {
          let postjson = {
            dmId:this.applyForm.id,
            state:this.applyForm.state,
            changeContent:this.applyForm.cause,
          };
            this.applySubmit(postjson);
            this.applyloading = false;
        } else {
          this.$message.error('表单验证失败')
          setTimeout(() => {this.applyloading = false},1000)
          return false;
        }
      })
    },
    //提交允许变更
    async applySubmit (postjson){
      let res = await revisionhistory.add(postjson);
        if(res.code === 0){
          this.$message.success(res.msg);
          this.$emit('success')
          this.modifyHistory(this.revisionHistoryItem);
          this.eventVisible = false;
        }else{
          this.$message.error(res.msg);
        }
    },
    //不允许变更
    notAllowChange (){
      this.revisionEvent.state = 4;
      this.rejectForm.eventName = this.revisionEvent.eventName;
      this.rejectForm.id = this.revisionEvent.id;
      this.rejectForm.state = this.revisionEvent.state;
      this.rejectVisible = true;
    },

    //提交驳回
    submitReject (){
      this.rejectloading = true;
      this.$refs.rejectForm.validate((valid) => {
        if (valid) {
          let postjson = {
            dmId:this.rejectForm.id,
            id:this.revisionHistoryItem.id,
            state:this.rejectForm.state,
            reasons:this.rejectForm.cause,
          };
            this.rejectEvent(postjson);
            this.rejectloading = false;
        } else {
          this.$message.error('表单验证失败')
          setTimeout(() => {this.rejectloading = false},1000)
          return false;
        }
      })
    },
    //提交驳回
    async rejectEvent (postjson){
      let res = await revisionhistory.update(postjson);
        if(res.code === 0){
          this.$message.success(res.msg);
          this.$emit('success')
          this.modifyHistory(this.revisionHistoryItem);
          this.rejectVisible = false;
        }else{
          this.$message.error(res.msg);
        }
    },
    //取消变更
    cancelModifyEvent (){
      this.eventVisible = false;
    },
    //取消驳回
    cancelReject (){
      this.rejectVisible = false;
    },


  }
}
</script>

<style lang="scss" scoped>
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
.contentbg {
  position:relative;
  width:100%;
  height:100%;
  padding:5px 10px;
  line-height:24px;
  background-color:rgba(45,140,240,0.8);
  border-radius:5px;
  color:#fff;
  cursor: pointer;
  i {
    position:absolute;
    right:0;
    top: -12px;
    border-radius:5px;
    line-height: 24px;
    padding: 0 10px;
    background: rgba(45,140,240,0.8);
  }
}
.public .contentbg {
  background-color:#37ccc2;
  i {
    background: #37ccc2;
  }
}
.apply .contentbg{
  background-color:#f4b205;
  i {
    background: #f4b205;
  }
}
.change .contentbg{
  background-color:#00a1e9;
  i {
    background: #00a1e9;
  }
}
.reject .contentbg{
  background-color:#ff6b5d;
  i {
    background: #ff6b5d;
  }
}
</style>
