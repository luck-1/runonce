<template>
  <Drawer v-model="drawerModel" width="650" :title="title" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <el-form ref="item" :rules="rules" :model="item" label-width="100px">
      <el-form-item label="事项名称" prop="eventName" >
        <el-input size="mini" v-model="item.eventName" placeholder="请填写事项名称" />
      </el-form-item>

      <el-form-item label="申请原由" prop="eventOfApplication" >
        <el-input size="mini" v-model="item.eventOfApplication" placeholder="请填写办事原由或目的
" />
      </el-form-item>

      <el-form-item label="关键字" prop="keyword">
        <el-select style="width:100%" v-model="item.keyword" multiple filterable :allow-create="true" default-first-option placeholder="请填写搜索该事项所用关键字 ">
          <el-option v-for="(value,index) in keys" :key="index" :label="value" :value="value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('item')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('item')">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { levelWithService, jointSectoral, administrativeService } from '@/api/service'
import { validatSpace, validateCharNumber } from '@/utils/validate'
import { situationToGuide } from '@/api/service'
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
    const checkKeyword = (rule, value, callback) => {
      if(value.length===0){
        callback(new Error('不能为空'))
      } else {
        for(let item of value){
          if (validatSpace(item)) {
            callback(new Error('不能包含空字符'))
          }
        }
        callback()
      }
    }
    return {
      loading: false,
      keys: [],
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },
      // 服务对象
      serviceObjs: [],
      // 事项小类型
      samllPro: [],
      rules: {
        eventName: [
          { required: true, validator: checkName, trigger: 'blur' },
        ],
        eventOfApplication: [
          { required: true, validator: checkName, trigger: 'blur' },
        ],
        keyword: [
          { required: true, validator: checkKeyword, trigger: 'change' },
        ],
      }
    }
  },
  mounted () {},
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
          if(this.title === '新增事项') {
            this.addOne(this.item)
          } else {
            this.update(this.item)
          }
        } else {
          this.$message.error('必填项输入不能为空')
          setTimeout(() => {this.loading = false},1500)
          return false
        }
      })
    },
    async addOne (obj) {
      let res = await situationToGuide.add(obj)
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.loading = false
        this.$refs.item.resetFields()
        this.$emit('submit')
      } else {
        this.$message.error(res.msg)
        setTimeout(() => {this.loading = false},1500)
      }
    },
    async update (obj) {
      let res = await situationToGuide.add(obj)
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.loading = false
        this.$refs.item.resetFields()
        this.$emit('submit')
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
  z-index: 1;
}
.select-option {
  width: 100%;
}
</style>
