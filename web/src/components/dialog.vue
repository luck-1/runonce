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
      <slot></slot>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button :type="type" @click="del">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
export default {
  props: {
    type: {
      type: String,
      default: 'danger'
    },
    title: {
      type: String,
      default: '删除确认'
    },
    visible: {
      type: Boolean,
      default: false
    },
    width: {
      type: String,
      default: '360px'
    },
    showClose: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    cancel () {
      this.$emit('cancel')
    },
    del () {
      this.$emit('delete')
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #409eff;
$success: #64c23a;
$warning: #e6a23c;
$danger: #f56c6c;
$info: #909399;
.primary /deep/ .el-dialog__header{
  background-color: $primary;
}
.success /deep/ .el-dialog__header{
  background-color: $success;
}
.warning /deep/ .el-dialog__header{
  background-color: $warning;
}
.danger /deep/ .el-dialog__header{
  background-color: $danger;
}
.info /deep/ .el-dialog__header{
  background-color: $info;
}
.dialog /deep/ .el-dialog__header{
  text-align: center;
  .el-dialog__title{
    color: #fff;
  }
}
.custom {
  text-align: center;
}
</style>
