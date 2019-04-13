<template>
  <Drawer v-model="drawerModel" width="650" :title="title" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <el-form ref="item" :rules="rules" :model="item" label-width="100px">
      <el-form-item label="事项名称" prop="eventName" >
        <el-input size="mini" v-model="item.eventName" placeholder="请填写" />
      </el-form-item>

      <el-form-item label="服务对象" prop="serviceObject" >
        <el-checkbox-group size="mini" v-model="item.serviceObject" placeholder="请选择">
          <el-checkbox label="自然人"></el-checkbox>
          <el-checkbox label="法人"></el-checkbox>
          <el-checkbox label="个体经商户"></el-checkbox>
          <!-- <el-checkbox v-for="(item, index) in serviceObjs" :key="index" :label="item.name"></el-checkbox> -->
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="事项类别" prop="majorTypesOfEvents">
        <el-select class="select-option" v-model="item.majorTypesOfEvents" placeholder="请选择" clearable filterable>
          <el-option label="行政许可" value="1"></el-option>
          <el-option label="审核转报" value="2"></el-option>
          <el-option label="备案类" value="3"></el-option>
          <el-option label="其他服务" value="4"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="行使层级" prop="exerciseHierarchy">
        <el-checkbox-group size="mini" v-model="item.exerciseHierarchy" placeholder="请选择">
          <el-checkbox label="国家级"></el-checkbox>
          <el-checkbox label="省级"></el-checkbox>
          <el-checkbox label="市级"></el-checkbox>
          <el-checkbox label="县(区)级"></el-checkbox>
          <el-checkbox label="镇级"></el-checkbox>
          <el-checkbox label="村级"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="办理形式" prop="handlingForm">
        <el-checkbox-group size="mini" v-model="item.handlingForm" placeholder="请选择">
          <el-checkbox label="窗口办理"></el-checkbox>
          <el-checkbox label="网上办理"></el-checkbox>
          <el-checkbox label="移动端办理"></el-checkbox>
          <el-checkbox label="自助终端办理"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('item')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('item')">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { administrativeService, situationToGuideChild } from '@/api/service'
import { validatSpace, validateCharNumber } from '@/utils/validate'
export default {
  props: {
    title: {
      type: String
    },
    item: {
      type: Object,
      default: () => {
        return {
          eventName: '',
          exerciseHierarchy: [],
          handlingForm: [],
          id: null,
          majorTypesOfEvents: '',
          serviceObject: []
        }
      }
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
      keys: [],
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },
      serviceObjs: [],
      rules: {
        eventName: [
          { required: true, validator: checkName, trigger: 'blur' },
        ],
        serviceObject: [
          { required: true, message: '请选择', trigger: 'change' }
        ],
        majorTypesOfEvents: [
          { required: true, message: '请选择', trigger: 'change' }
        ],
        exerciseHierarchy: [
          { required: true, message: '请选择', trigger: 'change' }
        ],
        handlingForm: [
          { required: true, message: '请选择', trigger: 'change' }
        ],
      }
    }
  },
  mounted () {
    this.serviceObject()
  },
  methods: {
    // 实施对象
    async serviceObject(){
      let res = await administrativeService.serviceObjectSelectList({
        page: 1,
        seize: 10
      });
      if(res.code === 0){
        this.serviceObjs = res.obj.list
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
          this.edit(this.item)
        } else {
          this.$message.error('必填项输入不能为空')
          setTimeout(() => {this.loading = false},1500)
          return false
        }
      })
    },
    async edit (obj) {
      obj.exerciseHierarchy = obj.exerciseHierarchy.toString()
      obj.handlingForm = obj.handlingForm.toString()
      obj.serviceObject = obj.serviceObject.toString()
      let res = await situationToGuideChild.edit(obj)
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
