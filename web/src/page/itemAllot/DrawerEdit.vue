<template>
  <Drawer v-model="drawerModel" width="650" :title="title" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <el-form ref="item" :rules="rules" :model="item" label-width="100px">
      <el-form-item label="是否办理项" prop="isMin">
        <el-switch size="mini" v-model="item.isMin" :disabled="title==='修改事项'||(title==='新增事项'&&item.eventPid==='0')"
          active-color="#13ce66" inactive-color="#c0ccda" active-text="是" inactive-text="否"></el-switch>
      </el-form-item>

      <el-form-item label="事项目录" prop="parentName" v-if="item.isMin">
        <el-input size="mini" v-model="parent.eventName" placeholder="请填写" disabled/>
      </el-form-item>

      <div v-else>
        <el-form-item label="事项目录" prop="eventNameVal"  v-if="item.eventPid==='0'">
          <el-select class="select-option" size="mini" v-model="item.eventNameVal" placeholder="请选择" filterable clearable>
            <el-option v-for="(item, index) in levels" :key="index" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事项目录" prop="eventName" v-else>
          <el-input size="mini" v-model="item.eventName" placeholder="请填写"/>
        </el-form-item>
      </div>

      <el-form-item label="事项名称" prop="eventName" v-if="item.isMin">
        <el-input size="mini" v-model="item.eventName" placeholder="请填写" />
      </el-form-item>

      <el-form-item label="事项大类型" prop="eventType">
        <el-select class="select-option" :disabled="title==='修改事项'" size="mini" v-model="item.eventType" placeholder="请选择" @change="itemSmallTypeSelect">
          <el-option label="行政许可" value="1"></el-option>
          <el-option label="审核转报" value="2"></el-option>
          <el-option label="备案类" value="3"></el-option>
          <el-option label="其他服务" value="4"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="事项小类型" prop="itemSmallType">
        <el-select class="select-option" size="mini" v-model="item.itemSmallType" filterable clearable placeholder="先选择事项大类型" @change="getEncoding">
          <el-option v-for="(item, index) in samllPro" :key="index" :label="item.name" :value="item.name"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="目录编码" title="基本目录编码" prop="baseDirectoryEncoding">
        <el-input size="mini" v-model="item.baseDirectoryEncoding" placeholder="请填写" readonly/>
      </el-form-item>

      <el-form-item label="实施编码" title="实施清单编码" prop="codingImplementation" v-if="item.isMin">
        <el-input size="mini" v-model="item.codingImplementation" placeholder="请填写" readonly/>
      </el-form-item>

      <el-form-item label="办理项编码" title="业务办理项编码" prop="directoryEncoding" v-if="item.isMin">
        <el-input size="mini" v-model="item.directoryEncoding" placeholder="请填写" readonly/>
      </el-form-item>

      <el-form-item label="实施对象" prop="serviceObject" v-if="item.isMin">
        <el-checkbox-group size="mini" v-model="item.serviceObject" placeholder="请选择">
          <el-checkbox v-for="(item, index) in serviceObjs" :key="index" :label="item.name"></el-checkbox>
        </el-checkbox-group>
        <!-- <el-select class="select-option" size="mini" v-model="item.serviceObject" multiple clearable placeholder="请选择">
          <el-option v-for="(item, index) in serviceObjs" :key="index" :label="item.name" :value="item.name"></el-option>
        </el-select> -->
      </el-form-item>

      <el-form-item label="行使层级" prop="exerciseHierarchy" v-if="item.isMin">
        <el-checkbox-group size="mini" v-model="item.exerciseHierarchy" placeholder="请选择">
          <el-checkbox label="国家级"></el-checkbox>
          <el-checkbox label="省级"></el-checkbox>
          <el-checkbox label="市级"></el-checkbox>
          <el-checkbox label="县(区)级"></el-checkbox>
          <el-checkbox label="镇级"></el-checkbox>
          <el-checkbox label="村级"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- <el-form-item label="事项状态" prop="eventState">
        <el-select size="mini" v-model="item.eventState" placeholder="请选择">
          <el-option label="未完善" value="1"></el-option>
          <el-option label="已完善" value="2"></el-option>
          <el-option label="已提交" value="3"></el-option>
        </el-select>
      </el-form-item> -->

      <!-- <el-form-item label="实施机关" prop="exerciseHierarchy">
        <el-input size="mini" v-model="item.exerciseHierarchy" placeholder="请填写" />
      </el-form-item>

      <el-form-item label="通用名称" prop="implementingOrgan">
        <el-input size="mini" v-model="item.implementingOrgan" placeholder="请填写" />
      </el-form-item>

      <el-form-item label="设定依据" prop="settingsBasis">
        <el-input size="mini" v-model="item.settingBasis" placeholder="请填写" />
      </el-form-item> -->

      <el-form-item label="描述" prop="remarks">
        <Input size="small" type="textarea" v-model="item.remarks" :autosize="{minRows: 5, maxRows: 15}" placeholder="请填写" />
      </el-form-item>
    </el-form>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('item')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('item')">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { levelWithService, jointSectoral, administrativeService, eventTable } from '@/api/service'
import { validatSpace, validateCharNumber } from '@/utils/validate'
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
    },
    parent: {
      type: Object
    },
    levels: {
      type: Array,
      default: []
    }
  },
  data () {
    const checkName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (validatSpace(value)) {
        callback(new Error('不能包含空字符'))
      }
      //  else if (rule.field === 'eventName') {
      //   this.findName(value, callback)
      // }
      callback()
    }
    const checkNameVal = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (validatSpace(value)) {
        callback(new Error('不能包含空字符'))
      } else if (rule.field === 'eventNameVal') {
        this.findName(value).then(res => {
          if(res) {
            callback(new Error(res))
          } else {
            callback()
          }
        })
      } else {
        callback()
      }
    }
    const checkNumber = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (!validateCharNumber(value)) {
        callback(new Error('事项编号格式不正确'))
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
      // 服务对象
      serviceObjs: [],
      // 事项小类型
      samllPro: [],
      rules: {
        eventName: [
          { required: true, validator: checkName, trigger: 'blur' },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        baseDirectoryEncoding: [
          { required: true, validator: checkName, trigger: 'blur' }
        ],
        eventNameVal: [
          { required: true, validator: checkNameVal, trigger: 'change' },
        ],
        eventNumber: [
          { required: true, validator: checkNumber, trigger: 'change' },
        ],
        eventType: [
          { required: true, message: '请选择', trigger: 'change' }
        ],
        itemSmallType: [
          { required: true, message: '请选择', trigger: 'change' },
        ],
        directoryEncoding: [
          { required: true, validator: checkName, trigger: 'change' },
        ],
        codingImplementation: [
          { required: true, validator: checkName, trigger: 'change' },
        ],
        serviceObject: [
          { type: 'array', required: true, message: '请至少选择一个实施对象', trigger: 'change' }
        ],
        exerciseHierarchy: [
          { type: 'array', required: true, message: '请至少选择一个行使层级', trigger: 'change' }
        ],
      }
    }
  },
  watch: {
    'item.isMin'(val) {
      let item = JSON.parse(JSON.stringify(this.item))
      if (val && this.title === '新增事项') {
        // 办理项
        this.item.eventType = String(this.parent.eventType)
        this.itemSmallTypeSelect(item.eventType)
        this.item.itemSmallType = this.parent.itemSmallType
      } else {
        this.itemSmallTypeSelect(item.eventType)
        setTimeout(() => {
          this.item.itemSmallType = item.itemSmallType?item.itemSmallType:''
        },300)
      }
    },
  },
  mounted () {
    this.serviceObject()
  },
  methods: {
    async findName (val) {
      let res = await levelWithService.findName(val, this.item.id)
      if (res.code !== 0) {
        return res.msg
      } else {
        return ''
      }
    },
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
    // 事项小类型
    async itemSmallTypeSelect (val){
      this.samllPro = []
      this.item.itemSmallType = ''
      if(val) {
        let res = await administrativeService.itemSmallTypeSelectList(val)
        if(res.code === 0){
          this.samllPro = res.obj.list
          if(this.title === '新增事项') {
            this.getEncoding(this.parent.itemSmallType)
          }
        }
      }
    },
    // 选择事项小类型获取基本目录编码、业务办理项编码、实施清单编码
    async getEncoding (name) {
      if (name) {
        let catalogueItemSubtype = this.samllPro.filter(x => x.name === name)[0].id
        let res = await eventTable.getEncoding({
          deptId: this.item.deptId,
          catalogueItemSubtype,
          isMin: this.item.isMin,
          directoryEncoding: this.item.baseDirectoryEncoding
        })
        if(res.code === 0){
          this.item.baseDirectoryEncoding = this.item.isMin?this.item.baseDirectoryEncoding:res.obj.baseDirectoryEncoding || ''
          this.item.directoryEncoding = res.obj.directoryEncoding || ''
          this.item.codingImplementation = res.obj.codingImplementation || ''
        }
      } else {
        this.item.baseDirectoryEncoding = this.item.isMin ? this.item.baseDirectoryEncoding : ''
        this.item.directoryEncoding = ''
        this.item.codingImplementation = ''
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
          let obj = {
            deptId: this.item.deptId,
            eventsTable: {
              ...this.item,
              eventName: (!this.item.isMin && this.item.eventPid==='0')?this.item.eventNameVal:this.item.eventName,
              exerciseHierarchy: this.item.exerciseHierarchy.join(','),
              serviceObject: this.item.serviceObject.join(','),
              eventState: 1
            },
            groupId: this.item.groupId
          }
          if(this.title === '新增事项') {
            this.addOne(obj)
          } else {
            this.update(obj)
          }
        } else {
          this.$message.error('必填项输入不能为空')
          setTimeout(() => {this.loading = false},1500)
          return false
        }
      })
    },
    async addOne (obj) {
      let res = await jointSectoral.add(obj)
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
      let res = await jointSectoral.update(obj)
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
