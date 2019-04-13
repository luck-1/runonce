<template>
  <div>
    <el-dialog title="从已有输出物导入" append-to-body width="600px"
      :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" :visible="visible">
      <el-form :model="importForm" :rules="importRules" ref="importForm" class="search-form">
        <el-form-item label="事项名称" prop="eventName" label-width="95px">
          <el-input v-model="importForm.eventName" readonly></el-input>
        </el-form-item>

        <el-form-item label="输出物模板" prop="version" label-width="95px">
          <el-radio v-model="importForm.version" label="2.0">V2.0</el-radio>
          <el-radio v-model="importForm.version" label="3.0">V3.0</el-radio>
        </el-form-item>

        <el-form-item label="事项类别" prop="typeName" label-width="95px">
          <el-input v-model="importForm.typeName" readonly></el-input>
        </el-form-item>

        <el-form-item label="导入文件" prop="fileName" label-width="95px">
          <el-row>
            <el-col :span="12">
              <el-input v-model="importForm.fileName" placeholder="请选择要上传的文件" readonly></el-input>
            </el-col>
            <el-col :span="12">
              <el-button type="primary" @click="editImg" style="margin-left:20px">浏 览</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="show=true">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="importItem">导 入</el-button>
      </span>
    </el-dialog>

    <!-- 上传文件 -->
    <input type="file" accept='.csv, application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ref="uploadImg" @change="fileUpload" style="display:none;"/>
    <!-- <input type="file" accept='.csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ref="uploadImg" @change="fileUpload" style="display:none;"/> -->

    <!-- 删除 -->
    <ImportDialog type="warning" title="取消确认" :visible="show" @cancel="show=false" @delete="del">
      <div>确认取消？取消后不可恢复</div>
    </ImportDialog>
  </div>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import ImportDialog from './dialog'
import { dataImport } from '@/api/service'
export default {
  components: { ImportDialog },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    importForm: {
      type: Object,
      default: null
    }
  },
  watch: {
  },
  data () {
    return {
      loading: false,
      importRules: {
        version: [
          { required: true, message: '请选择输出物模板', trigger: 'change' }
        ],
        fileName: [
          { required: true, message: '请选择要上传的文件', trigger: 'change' }
        ],
      },
      // 导入确认
      show: false
    }
  },
  computed: {
    ...mapState([
      'copyObj', 'loadingText'
    ]),
  },
  methods: {
    ...mapMutations([
      'SET_COPYOBJ', 'SET_LOADINGTEXT'
    ]),
    // 事项名称
    typeName (type) {
      let name = ''
      switch(type) {
        case 1:name='行政许可和备案类';break;
        case 2:name='审核转报';break;
        case 3:name='行政许可和备案类';break;
        case 4:name='其他服务';break;
        default:
      }
      return name
    },
    editImg() {
      this.$refs.uploadImg.setAttribute('type', 'file')
      this.$refs.uploadImg.click()
    },
    fileUpload (e) {
      let file = e.target.files[0]
      this.fileLoading = true
      let fileName = file.name.split('.').splice(-1)
      let validate = '.xlsx,.xls'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(`1.${fileName}`)) {
        this.$message.error(`只能上传.xlsx,.xls格式`)
        this.fileLoading = false
        return
      }
      this.importForm.file = file
      this.importForm.fileName = file.name
      this.fileLoading = false
      this.$refs.uploadImg.removeAttribute('type')
    },
    // 取消确定
    del () {
      this.show = false
      this.$refs.importForm.resetFields()
      this.$emit('close')
    },
    importItem () {
      this.loading = true
      this.$refs.importForm.validate((valid) => {
        if (valid) {
          this.loading = false
          this.importObj()
        } else {
          this.$message.error('必填项输入不能为空')
          setTimeout(() => {this.loading = false},1500)
          return false
        }
      })
    },
    async importObj() {
      this.SET_LOADINGTEXT('导入中，请稍候...')
      let formData = new FormData();
      formData.append('file', this.importForm.file); // 文件对象
      setTimeout(async ()=>{
        let res = await dataImport.importFile(this.importForm,formData)
        if(res.code === 0) {
          this.loading = false
          this.SET_COPYOBJ(res.obj)
          this.del()
          this.SET_LOADINGTEXT('拼命加载中...')
        } else {
          this.$message.error(res.msg)
          this.SET_LOADINGTEXT('拼命加载中...')
          setTimeout(() => {this.loading = false},1500)
        }
      },500)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
