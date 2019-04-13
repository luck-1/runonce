<template>
  <el-dialog
    class="dialog"
    :class="type"
    :title="title"
    append-to-body
    :visible.sync="visible"
    :width="width"
    :show-close="showClose"
    :close-on-press-escape="false"
    :close-on-click-modal="false">
    <div class="custom">
      <el-form :model="judge" :rules="judgeRules" ref="judge" class="judge-form" label-width="80px">
        <el-form-item prop="lineNumber" label="行号" v-if="judge.lineNumber">
          <el-input size="mini" placeholder="请输入内容" v-model="judge.lineNumber" readonly/>
        </el-form-item>
        <el-form-item prop="propertyName" label="评审字段">
          <el-input size="mini" placeholder="请输入内容" v-model="judge.propertyName" readonly/>
        </el-form-item>
        <el-form-item prop="questionContent" label="评审内容">
          <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="judge.questionContent"/>
        </el-form-item>
      </el-form>
      <slot></slot>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button :type="type" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { validatorSpace } from '@/utils/validate'
import { matterProposal } from '@/api/service'
export default {
  props: {
    type: {
      type: String,
      default: 'primary'
    },
    title: {
      type: String
    },
    visible: {
      type: Boolean,
      default: false
    },
    width: {
      type: String,
      default: '500px'
    },
    showClose: {
      type: Boolean,
      default: false
    },
    judge: {
      type: Object
    },
    itemSee: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const validateContext = (rule, value, callback) => {
      if (!validatorSpace(value)) {
        callback(new Error('评审内容必填'))
      }
      callback()
    }
    return {
      judgeRules: {
        questionContent: [{ required: true, validator: validateContext, trigger: 'blur'}]
      }
    }
  },
  methods: {
    cancel () {
      this.$emit('cancel')
    },
    submit () {
      this.$refs.judge.validate((valid) => {
        if (valid) {
          this.judgeSub()
        } else {
          return false;
        }
      })
    },
    async judgeSub () {
      console.log(this.itemSee)
      let obj = {
        ...this.judge,
        departmentalMattersId: this.judge.id,
        id: null,
        matterProposalAnswerList: [],
        questionTime: new Date().toJSON(),
        isApproval: this.itemSee?2:1
      }
      let res = await matterProposal.add(obj)
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.$refs.judge.resetFields()
        this.cancel()
      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
$default: #409eff;
$success: #64c23a;
$warning: #e6a23c;
$danger: #f56c6c;
$info: #909399;
.default .el-dialog__header{
  background-color: $default;
}
.success .el-dialog__header{
  background-color: $success;
}
.warning .el-dialog__header{
  background-color: $warning;
}
.danger .el-dialog__header{
  background-color: $danger;
}
.info .el-dialog__header{
  background-color: $info;
}
.dialog .el-dialog__header{
  text-align: center;
  .el-dialog__title{
    color: #fff;
  }
}
.dialog /deep/ .el-dialog__body {
  padding: 10px 20px 0 20px;
}
</style>
