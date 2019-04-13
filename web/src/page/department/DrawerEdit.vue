<template>
  <Drawer v-model="drawerModel" width="650" :title="title" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <el-form ref="item" :rules="rules" :model="item" label-width="90px">
      <el-form-item :label="(title==='新增部门'||title==='修改部门')?'部门名称':'区域名称'" prop="title">
        <el-input size="mini" v-model="item.title" placeholder="请填写" />
      </el-form-item>

      <el-form-item label="排序值" prop="sortOrder">
        <el-input-number size="mini" v-model="item.sortOrder" controls-position="right" :min="0"></el-input-number>
      </el-form-item>

      <el-form-item label="是否启用" prop="status">
        <el-switch size="mini" v-model="item.status"
          active-color="#13ce66" inactive-color="#c0ccda" active-text="启用" inactive-text="禁用"></el-switch>
      </el-form-item>

      <el-form-item label="层级" prop="region">
        <el-radio v-model="item.region" label="1">省</el-radio>
        <el-radio v-model="item.region" label="2">市</el-radio>
        <el-radio v-model="item.region" label="3">区县</el-radio>
        <el-radio v-model="item.region" label="4">街道</el-radio>
      </el-form-item>

      <el-form-item label="指导部门" title="中央业务指导(实施)部门" prop="centralDepartments" v-if="title.includes('部门')">
        <el-select class="select-option" size="mini" v-model="item.centralDepartments" placeholder="请选择">
          <el-option v-for="(item, index) in centerDepts" :key="index" :label="item.name" :value="item.id" :title="item.fillName">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.code }}</span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="信用代码" title="统一社会信用代码" prop="socialCreditCode" v-if="title.includes('部门')">
        <el-input size="mini" v-model="item.socialCreditCode" placeholder="请填写" />
      </el-form-item>

    </el-form>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('item')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('item')">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { department, centralDept } from '@/api/service'

import { validatSpace } from '@/utils/validate'
export default {
  props: {
    title: {
      type: String
    },
    drawerModel: {
      type: Boolean,
      default: false
    },
    item: {
      type: Object
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
    const checkOrder = (rule, value, callback) => {
      if (value < 0) {
        callback(new Error('不能小于0'))
      }
      callback()
    }
    const checkSocialCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (!/^[0-9A-HJ-NP-RTUWXY]{18}$/.test(value)) {
        callback(new Error('由18位数字或大写字母组成(I、O、S、V、Z除过)'))
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
      centerDepts: [],
      rules: {
        title: [
          { required: true, validator: checkName, trigger: 'blur' },
        ],
        sortOrder: [
          { validator: checkOrder, trigger: 'change' },
        ],
        centralDepartments: [
          { required: true, message: '指导部门不能为空', trigger: 'change' },
        ],
        socialCreditCode: [
          { required: true, validator: checkSocialCode, trigger: 'blur' },
        ]
      }
    }
  },
  mounted () {
    this.getCentralDept()
  },
  methods: {
    async getCentralDept () {
      let res = await centralDept.getAll()
      if(res.code === 0) {
        this.centerDepts = res.obj || []
      }
    },
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
          if(this.title.includes('新增')) {
            this.addOne()
          } else {
            this.update()
          }
        } else {
          this.$message.error('输入不规范')
          setTimeout(() => {this.loading = false},1500)
          return false
        }
      })
    },
    async addOne () {
      this.item.status = this.item.status?0:-1
      let res = await department.add(this.item)
      if (res.code === 0) {
        this.cancel('item')
      } else if(res.code === -1){
        this.$message.warning(res.msg)
        setTimeout(() => {this.loading = false},1500)
      } else {
        this.$message.error(res.msg)
        setTimeout(() => {this.loading = false},1500)
      }
    },
    async update () {
      this.item.status = this.item.status?0:-1
      let res = await department.update(this.item)
      if(res.code === 0) {
        this.cancel('item')
      } else if(res.code === -1){
        this.$message.warning(res.msg)
        setTimeout(() => {this.loading = false},1500)
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
.select-option {
  width: 100%;
}
</style>
