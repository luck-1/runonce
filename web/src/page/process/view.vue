<template>
  <div class="process-view">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>办理流程图</h2>
    <el-row :gutter="20" style="margin-right:0">
      <template v-if="processList.length">
        <el-col :span="12" v-for="(item, index) in processList" :key="index" style="margin-bottom:10px">
          <p>{{item.conditionList.join('--')}}
            <el-button-group>
              <el-button type="primary" size="mini" @click.stop="processClick(item)" v-if="!watchPic">修改</el-button>
              <el-button type="primary" size="mini" @click.stop="deletePic(item)" v-if="!watchPic">删除</el-button>
            </el-button-group>
          </p>
          <div class="img-avatar">
            <img :src="item.picbase" @dblclick="judge('办理流程图', 'picbase')">
          </div>
        </el-col>
      </template>
      <el-col :span="12">
        <div class="upload" @click="processClick(null)" @dblclick="judge('办理流程图', 'picbase')"><i class="el-icon-plus"></i></div>
      </el-col>
    </el-row>

    <!-- 编辑流程图 -->
    <el-dialog :title="processTitle" :visible="processVisible" :show-close="false" :close-on-press-escape="false" :close-on-click-modal="false" width="710px">
      <el-form ref="processData" :rules="rules" :model="processData">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="办理形式" prop="handlingForm">
              <el-select allow-create :maxlength="30" filterable :disabled="watchPic" v-model="processData.handlingForm" clearable placeholder="请选择办理形式">
                <el-option v-for="item in handForm" :key="item.id" :label="item.name" :value="item.name"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请类型" prop="onlineApp">
              <el-select allow-create filterable :maxlength="30" :disabled="watchPic" v-model="processData.onlineApp" clearable placeholder="请选择网上申请形式">
                <el-option v-for="item in onlineApp" :key="item.id" :label="item.name" :value="item.name"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="上传方式" prop="picType">
              <el-radio-group v-model="processData.picType" size="small" v-loading="loading">
                <el-radio :label="1">用户上传</el-radio>
                <el-radio :label="2">工具绘制</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="picbase">
              <div class="img-avatar" v-if="processData.picbase" :class="[ishover?'hover':'',watchPic?'watch-pic':'']" @mouseover="ishover=true" @mouseout="ishover=false">
                <div class="avatar-icon">
                  <i class="el-icon-zoom-in" @click.stop="viewRenderPic(processData.picbase)"></i>
                  <i class="el-icon-download" @click.stop="downloadPic(processData.picbase)"></i>
                  <i class="el-icon-edit" v-if="!watchPic" style="margin: 1rem;" @click.stop="processPicture"></i>
                  <i class="el-icon-delete" v-if="!watchPic" @click.stop="deletePicOne"></i>
                </div>
                <img :src="processData.picbase">
              </div>
              <el-button type="primary" @click.stop="processPicture" v-else>{{processData.picType==1?'上传':'绘制'}}</el-button>
              <el-input style="display: none" v-model="processData.picbase" size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="processVisible=false">取 消</el-button>
        <el-button type="primary" @click="processSubmit('processData')">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 流程图绘制 -->
    <process-dialog :processVisible="process" :imgJson="imgJson" @processUpload="processUpload" @close="process=false"></process-dialog>

    <!-- 上传流程图片 -->
    <input type="file" accept='image/jpeg' ref="uploadImg" @change="fileUpload" style="display:none;"/>

    <!-- 流程图片放大 -->
    <div ref="renderRef" style="display:none">
      <img :src="dataPicture">
    </div>

    <!-- 删除流程图 -->
    <DeleteDialog :visible="showDel" @cancel="cancelDel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="办理流程图评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>

    <div class="btn-submit" @click="formSubmit" v-if="!watchPic">保存</div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex'
import Viewer from 'viewerjs'

import processDialog from './process'
import DeleteDialog from '@/components/dialog.vue'
import JudgeDialog from '@/components/judgeDialog'

import { aliConfig } from '@/utils/aliyunConfig'
import { process } from '@/api/service'

import { validateNumber, validateMoney } from '@/utils/validate'
import { common } from '@/utils/common'

const OSS = require('ali-oss');
const client = new OSS(aliConfig);

export default {
  components: { processDialog, DeleteDialog, JudgeDialog },
  props: {
    state: {
      type: Boolean,
      default: false
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
  watch: {
    'itemDetail.id' (val) {
      if(val) {
        this.getList(val)
      }
    },
    'itemInfo.processMapState' (val) {
      this.allState(val)
    },
    searchObj (val) {
      // 办理形式
      this.handForm = val ? val.handlingForm : []
      // 申请类型
      this.onlineApp = val ? val.onlineApplicationForm : []
    }
  },
  data () {
    return {
      // 办理形式
      handForm: [],
      // 申请类型
      onlineApp: [],
      flowVersion:null,
      // 评审
      judgeShow: false,
      judgeForm: {},
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      processList: [],
      // 添加弹框
      processVisible: false,
      processData: {
        id: null,
        eventId: null,
        picbase: '',
        picType: 1,
        picliu: '',
        conditionList: [],
        handlingForm: '',
        onlineApp: ''
      },
      processTitle: '',
      loading: false,
      rules: {
        picbase: [
          { required: true, message: '请上传流程图', trigger: 'change' }
        ],
        handlingForm: [
          { required: true, message: '请选择办理形式', trigger: 'change' }
        ],
        onlineApp: [
          // { required: true, message: '请选择申请类型', trigger: 'change' }
        ]
      },
      // 图片上传
      ishover: false,
      dockingHover: false,
      imgJson: '',
      dataPicture: '',
      process: false,
      // 删除
      deleteOne: false,
      currentProcess: null,
      showDel: false,
      flowTemp: '<mxGraphModel dx="1059" dy="384" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="827" pageHeight="1169"><root><mxCell id="0"/><mxCell id="1" parent="0"/><mxCell id="17" value="办理流程图" style="swimlane;fillColor=#33FFFF;" vertex="1" parent="1"><mxGeometry x="70" y="40" width="700" height="650" as="geometry"><mxRectangle x="140" y="40" width="90" height="23" as="alternateBounds"/></mxGeometry></mxCell><mxCell id="25" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="18" target="23"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="18" value="开始" style="ellipse;whiteSpace=wrap;html=1;fillColor=#E7FF0F;" vertex="1" parent="17"><mxGeometry x="299" y="60" width="80" height="50" as="geometry"/></mxCell><mxCell id="19" value="结束" style="ellipse;whiteSpace=wrap;html=1;fillColor=#E7FF0F;" vertex="1" parent="17"><mxGeometry x="299" y="330" width="80" height="50" as="geometry"/></mxCell><mxCell id="22" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="20" target="19"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="20" value="评审" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#CCFF99;" vertex="1" parent="17"><mxGeometry x="198" y="200" width="120" height="60" as="geometry"/></mxCell><mxCell id="27" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="23" target="24"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="31" value="否" style="text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=#ffffff;" vertex="1" connectable="0" parent="27"><mxGeometry x="-0.0727" y="-4" relative="1" as="geometry"><mxPoint x="-23.5" y="-13" as="offset"/></mxGeometry></mxCell><mxCell id="28" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;exitX=0;exitY=0.5;" edge="1" parent="17" source="23" target="20"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="30" value="是" style="text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=#ffffff;" vertex="1" connectable="0" parent="28"><mxGeometry x="0.3069" y="46" relative="1" as="geometry"><mxPoint x="-12.5" y="-15" as="offset"/></mxGeometry></mxCell><mxCell id="23" value="" style="rhombus;whiteSpace=wrap;html=1;fillColor=#FFFF00;" vertex="1" parent="17"><mxGeometry x="319" y="140" width="40" height="40" as="geometry"/></mxCell><mxCell id="29" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="24" target="19"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="24" value="评审" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#CCFF99;" vertex="1" parent="17"><mxGeometry x="369" y="200" width="120" height="60" as="geometry"/></mxCell></root></mxGraphModel>'
    }
  },
  computed: {
    ...mapState([
      'itemDetail', 'searchObj'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.processMapState)
      return itemDetail
    }
  },
  mounted () {
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
    this.getList(this.itemInfo.id)
  },
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL'
    ]),
    allState (val) {
      switch (val) {
        case 1: this.eventState = '未完善';this.eventClass = 'btn-not-perfect';break;
        case 2: this.eventState = '已完善';this.eventClass = 'btn-has-perfect';break;
        case 3: this.eventState = '已提交';this.eventClass = 'btn-submitted';break;
        default: break
      }
    },
    // 评审
    judge (propertyName, property) {
      let endTime = this.itemInfo.examineEndTime
      if(endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          id: this.itemInfo.id,
          sheetNum: 6,
          lineNumber: null,
          propertyName,
          property,
          questionContent: ''
        }
      }
    },
    judgeCancel () {
      this.judgeShow = false
    },
    processClick (item) {
      if (this.watchPic) {
        return
      }
      this.processTitle = item ? '修改流程图' : '新增流程图'
      this.processData = item || {
        id: null,conditionList: [],picbase: '',picType: 1,
        picliu: '',handlingForm: '',onlineApp: ''
      }
      this.processVisible = true
      // this.$refs.processData.resetFields()
    },
    processSubmit (name) {
      let { handlingForm, onlineApp } = this.processData
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.processData.conditionList = [handlingForm, onlineApp]
          if (this.processTitle === '新增流程图') {
            this.processList.push(this.processData)
          } else {
            let currentIndex = this.processList.findIndex(x => x.id === this.processData.id)
            this.processList[currentIndex] = this.processData
          }
          // this.$refs.processData.resetFields()
          this.processVisible = false
        } else {
          this.$message.warning('表单验证失败')
        }
      })
    },
    processPicture () {
      let type = this.processData.picType
      if(type == 1) {
        this.$refs.uploadImg.setAttribute('type', 'file')
        this.$refs.uploadImg.click()
      } else {
        this.process = true
      }
      this.imgJson = this.processData.picliu || this.flowTemp
    },
    fileUpload (e) {
      let files = e.target.files
      let fileName = `${new Date().getTime()}.${files[0].name.split('.')[files[0].name.split('.').length-1]}`
      let validate = '.jpg,.jpeg,.gif,.png'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(fileName)) {
        this.$message.error(`只能上传.jpg,.jpeg,.gif,.png格式`)
        this.loading = false
        return
      }
      this.loading = true
      let storeAs = `${new Date().toJSON().split('T')[0]}/${fileName}`;
      let results = client.put(storeAs, files[0]).then((results) => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        // let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(signUrl)
        this.processData.picbase = signUrl
        this.loading = false
      }).catch(err => {
        this.processData.picbase = ''
        this.loading = false
			})
      this.$refs.uploadImg.removeAttribute('type')
    },
    // 流程图保存图片回显
    processUpload ({json, imgUrl}) {
      this.processData.picliu = json
      this.processData.picbase = imgUrl
    },
    // 获取页面数据
    async getList (id) {
      let res = await process.getProcess(id)
      this.flowVersion= res.obj.version
      this.processData.id = null
      if (res.code === 0) {
        if (res.obj) {
          res.obj.processDescriptionPicList.forEach(x => {
            x.picbase = x.picbase?client.signatureUrl(x.picbase, { expires: 80000 }):''
            x.handlingForm = x.conditionList[0]
            x.onlineApp = x.conditionList[1]
          })
          this.processList = res.obj.processDescriptionPicList
          this.flowVersion = res.obj.version
        }
      }
    },
    persentImg (img) {
      this.processData.picbase = img
    },
    // 查看图片，放大图片操作
    viewRenderPic (url) {
      this.dataPicture = url
      global.thisView &&　global.thisView.destroy();
      global.thisView = new Viewer(this.$refs.renderRef)
      global.thisView.options.zIndex = 5000
      global.thisView.show()
    },
    // 下载图片
    downloadPic (url) {
      common.download(`${this.itemInfo.eventName}办理流程图`, url)
    },
    deletePic (item) {
      this.currentProcess = item
      this.showDel = true
      this.deleteOne = false
    },
    deletePicOne () {
      this.deleteOne = true
      this.showDel = true
    },
    uploadImg (img) {
      this.processData.dockingModePicture = img
    },
    cancelDel() {
      this.currentProcess = this.deleteOne?this.currentProcess:null
      this.showDel = false
    },
    del () {
      this.processData.picbase = ''
      this.processData.picliu = ''
      if(this.deleteOne) {
        this.showDel = false
        return
      }
      let currentIndex = this.processList.findIndex(x => x.id === this.currentProcess.id)
      this.processList.splice(currentIndex, 1)
      this.showDel = false
    },
    formSubmit () {
      this.submitThisForm(this.processList.length?2:1)
      // this.$refs[name].validate((valid) => {
      //   if (valid) {
      //     this.submitThisForm(2)
      //   } else {
      //     this.submitThisForm(1)
      //     return false
      //   }
      // })
    },
    async submitThisForm (state) {
      let processDescriptionPicList = []
      this.processList.forEach(x => {
        processDescriptionPicList.push({
          ...x,
          eventId: this.itemInfo.id,
          picbase: this.fileName(x.picbase)
        })
      })
      let res = await process.addprocess({
        processDescriptionPicList,
        eventId: this.itemInfo.id,
        state,
        version:this.flowVersion
      })
      if (res.code === 0) {
        this.itemInfo.processMapState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, processMapState: state})
        this.$message.success(res.msg)
        this.flowVersion=null
        this.getList(this.itemInfo.id)
      }
    },
    fileName (val) {
      if (val) {
        let splits = val.split('/')
        let newVal = `${splits[splits.length-2]}/${splits[splits.length-1]}`
        return newVal.split('?')[0]
      } else {
        return ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.process-view {
  position: relative;
  height: 100%;
  width: calc(100% - 20px);
  .event-state {
    height: 23px;
    border-radius: 5px 5px 0 0;
  }
  p{
    border-left: 3px solid #2088fc;
    padding: 5px 10px;
    font-size: 1rem;
    font-weight: bold;
    background-color: #eee;
    .el-button-group {
      float:right;
      margin-top: -3px;
      margin-right: -10px;
    }
    .subtitle {
      margin-left: 1rem;
      font-size: 14px;
      font-weight: normal;
    }
  }
  .upload-imgs,
  .textarea {
    margin: 10px 0;
  }
}
.upload {
  width: 100%;
  margin-bottom: 10px;
  max-height: 444px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  &:hover {
    border-color: #409EFF;
  }
  i {
    font-size: 28px;
    line-height: 444px;
    color: #8c939d;
  }
}
.img-avatar {
  position: relative;
  display: flex;
  width: 100%;
  justify-content: center;
  img {
    max-width: 100%;
    max-height: 400px;
    margin-top: 10px;
    box-shadow: 2px 2px 10px rgba(0,0,0,.5);
  }
  .avatar-icon {
    position: absolute;
    left: calc(50% - 4rem);
    top: calc(50% - 1em);
    display: none;
    font-size: 30px;
    // color: #fff;
    z-index: 2;
    &>i {
      cursor: pointer;
    }
  }
  &.hover {
    img {
      opacity: .5;
    }
    .avatar-icon {
      display: inline-block;
    }
  }
  &.watch-pic {
    .avatar-icon {
      left: calc(50% - 1rem);
    }
  }
}
</style>
