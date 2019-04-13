<template>
  <Drawer v-model="drawerModel" width="650" :title="title" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <el-form ref="item" :rules="rules" :model="item" label-width="90px">
      <el-form-item label="角色名称" prop="name">
        <el-input size="mini" v-model="item.name" placeholder="请填写" />
      </el-form-item>

      <el-form-item label="备注" prop="description">
        <el-input size="mini" type="textarea" v-model="item.description" :el-rows="4" placeholder="请填写" />
      </el-form-item>
    </el-form>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('item')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('item')">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { validatSpace } from '@/utils/validate'
import { role } from '@/api/service'
export default {
  props: {
    title: {
      type: String
    },
    item: {
      type: Object
    },
    drawerModel: {
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
      loading: false,
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },
      rules: {
        name: [
          { required: true, validator: checkName, trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
  },
  methods: {
    // 取消
    cancel (name) {
      this.loading = false
      this.$refs[name].resetFields()
      this.$emit('cancel')
    },
    // 提交
    submitForm (name) {
      this.loading = true
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.title === '新增角色') {
            this.add()
          } else {
            this.update()
          }
        } else {
          this.$message('表单验证失败')
          setTimeout(() => {this.loading = false},1500)
          return false
        }
      })
    },
    async add() {
      let res = await role.save(this.item)
      if(res.code === 0) {
        this.cancel('item')
      } else {
        this.$message.error(res.msg)
        setTimeout(() => {this.loading = false},1500)
      }
    },
    async update() {
      let res = await role.edit(this.item)
      if(res.code === 0) {
        this.cancel('item')
      } else {
        this.$message.error(res.msg)
        setTimeout(() => {this.loading = false},1500)
      }
    }
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
</style>
