<template>
  <el-form ref="presentData" :rules="rules" :model="presentData" class="present-quantitate">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>系统对接方案</h2>
    <div>
      <p>系统对接方案
        <el-radio-group v-model="presentData.picType" size="small" style="float:right;line-height:34px" v-if="!watchPic">
          <el-radio label="1">用户上传</el-radio>
          <el-radio label="2">工具绘制</el-radio>
          <el-radio label="3">默认</el-radio>
        </el-radio-group>
      </p>
      <el-form-item prop="picPath">
        <div v-if="presentData.picPath" v-loading="loading">
          <div class="img-avatar" :class="[ishover?'hover':'',watchPic?'watch-pic':'']" @mouseover="ishover=true" @mouseout="ishover=false">
            <div class="avatar-icon">
              <i class="el-icon-zoom-in" @click.stop="viewRenderPic(presentData.picPath)"></i>
              <i class="el-icon-download" @click.stop="downloadPic(presentData.picPath)"></i>
              <i class="el-icon-edit" style="margin: 1rem;" @click.stop="edit" v-if="!watchPic"></i>
              <i class="el-icon-delete" @click.stop="deleteRenderPic" v-if="!watchPic"></i>
            </div>
            <img :src="presentData.picPath" @dblclick="judge('系统对接方案', 'picPath')">
          </div>
        </div>
        <div class="upload" @click="edit" v-else v-loading="loading" @dblclick="judge('系统对接方案', 'picPath')"><i class="el-icon-plus"></i></div>
        <el-input style="display: none" v-model="presentData.picPath" size="small"></el-input>
      </el-form-item>
    </div>
    <el-row>
      <p>描述</p>
      <el-form-item prop="reMarks" @dblclick.native="judge('描述', 'reMarks')">
        <Input class="textarea" v-model="presentData.reMarks" type="textarea" :readonly="watchPic" :autosize="{minRows: 5,maxRows: 10}" placeholder="请输入" />
      </el-form-item>
    </el-row>

    <ImgProcess :processVisible="process" :imgJson="imgJson" @presentUpload="presentUpload" @close="process=false"></ImgProcess>

    <!-- 上传图片 -->
    <input type="file" accept='image/jpeg' ref="uploadImg" @change="fileUpload" style="display:none;"/>

    <el-dialog title="选择默认" append-to-body width="900px"  :visible="defaule" :show-close="false"
      :close-on-click-modal="false" :close-on-press-escape="false" class="default-dialog">
      <div class="default-img">
        <div v-for="(item, index) in defaultImgs" :key="index">
          <img :src="item.dockingPicName" width="100%" @click="watchImg(index)">
          <p><el-radio v-model="defaultImgId" :label="item.id">{{item.dockingName}}</el-radio></p>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="defaule=false;defaultImgId=''">取 消</el-button>
        <el-button type="primary" @click="selectDefaultImg">确 定</el-button>
      </div>
    </el-dialog>

    <ul ref="imgView" style="display:none">
      <li v-for="(item, index) in defaultImgs" :key="index"><img :src="item.dockingPicName" :alt="item.dockingName"></li>
    </ul>

    <!-- 图片放大 -->
    <div ref="renderRef" style="display:none">
      <img :src="dataPicture">
    </div>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancelDel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="系统对接方案评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>

    <div class="btn-submit" @click="formSubmit('presentData')" v-if="!watchPic">保存</div>
  </el-form>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import Viewer from 'viewerjs'

import ImgProcess from './imgProcess'
import DeleteDialog from '@/components/dialog.vue'
import JudgeDialog from '@/components/judgeDialog'

import { aliConfig } from '@/utils/aliyunConfig'
import { systemdocking, defaultPic } from '@/api/service'

import { validateNumber, validateMoney } from '@/utils/validate'
import { common } from '@/utils/common'

const OSS = require('ali-oss');
const client = new OSS(aliConfig);

export default {
  components: { ImgProcess, DeleteDialog, JudgeDialog },
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
    state (val) {
      if (val) {
        this.drawLine()
        this.windowResize()
      }
    },
    'presentData.picType' (val) {
      if(val === '3') {
        this.presentData.picPath = ''
      }
    },
    'itemDetail.id' (val) {
      if(val) {
        this.getList(val)
      }
    },
    'itemInfo.systemDockingState' (val) {
      this.allState(val)
    }
  },
  data () {
    return {
      systemVersion:null,
      // 评审
      judgeShow: false,
      judgeForm: {},
      loading: false,
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      // 图片上传
      ishover: false,
      dockingHover: false,
      imgJson: '',
      dataPicture: '',
      process: false,
      defaule: false,
      defaultImgId: '',
      defaultImgs: [],
      presentData: {
        id: null,
        picPath: '',
        picType: '1',
        picData: '',
        reMarks: '',
        state: 1,
      },
      rules: {
        picPath: [
          { required: true, message: '请上传系统对接方案图', trigger: 'change' }
        ],
      },
      // 删除
      showDel: false
    }
  },
  computed: {
    ...mapState([
      'itemDetail'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.systemDockingState)
      return itemDetail
    }
  },
  mounted () {
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
    this.getPics()
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
          sheetNum: 10,
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
    // 获取默认图片
    async getPics () {
      let res = await defaultPic.selectAll()
      if (res.code === 0) {
        res.obj.forEach((item, index) => {
          let signUrl = client.signatureUrl(item.dockingPicName, { expires: 80000 });
          item.dockingPicName = signUrl
        })
        this.defaultImgs = res.obj
      } else {
        this.$message.error(res.msg)
      }
    },
    fileUpload (e) {
      let files = e.target.files
      this.loading = true
      let fileName = `${new Date().getTime()}.${files[0].name.split('.')[files[0].name.split('.').length-1]}`
      let validate = '.jpg,.jpeg,.gif,.png'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(fileName)) {
        this.$message.error(`只能上传.jpg,.jpeg,.gif,.png格式`)
        this.loading = false
        return
      }
      let storeAs = `${new Date().toJSON().split('T')[0]}/${fileName}`;
      let results = client.put(storeAs, files[0]).then((results) => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        // let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(signUrl)
        this.presentData.picPath = signUrl
        this.loading = false
      }).catch(err => {
				this.loading = false;
			})
      this.$refs.uploadImg.removeAttribute('type')
    },
    // 流程图保存图片回显
    presentUpload ({json, imgUrl}) {
      this.presentData.picData = json
      this.imgJson = json;
      this.presentData.picPath = imgUrl
    },
    // 获取页面数据
    async getList (id) {
      let res = await systemdocking.getByEventId(id)
      if (res.code === 0) {
        if (res.obj) {
          this.defaultImgId = res.obj.defaultPicId || ''
          res.obj.picType = res.obj.picType?res.obj.picType:'1'
          this.presentData = res.obj
          this.$nextTick(() => {
            if(this.presentData.picType === '3') {
              let current = res.obj.defaultPicId?this.defaultImgs.filter(x=>x.id === res.obj.defaultPicId):[]
              this.presentData.picPath = current.length?current[0].dockingPicName:''
              this.imgJson =  this.presentData.picPath
            } else {
              this.presentData.picPath = res.obj.picPath?client.signatureUrl(res.obj.picPath, { expires: 80000 }):''
            }
          })
          this.systemVersion=res.obj.version;
        } else {
          this.$refs.presentData.resetFields()
        }
      }
    },
    persentImg (img) {
      this.presentData.picPath = img
    },
    // 查看图片，放大图片操作
    viewRenderPic (url) {
      this.dataPicture = url
      global.thisView && global.thisView.destroy()
      global.thisView = new Viewer(this.$refs.renderRef)
      global.thisView.show()
    },
    // 下载图片
    downloadPic (url) {
      common.download(`${this.itemInfo.eventName}办理流程图`, url)
    },
    // 修改
    edit () {
      let type = this.presentData.picType
      if(this.watchPic) {
        return
      }
      if(type === '1') {
        this.$refs.uploadImg.setAttribute('type', 'file')
        this.$refs.uploadImg.click()
      } else if (type === '2') {
        this.process = true
      } else {
        this.getPics()
        this.defaule = true
      }
    },
    // 选择默认图片
    selectDefaultImg () {
      let current = this.defaultImgs.filter(x => x.id === this.defaultImgId)
      this.presentData.picPath = current.length?current[0].dockingPicName:'';
      this.defaule = false
    },
    // 默认图片放大
    watchImg (index) {
      this.defaultImgId = this.defaultImgs[index].id
      global.thisView && global.thisView.destroy()
      global.thisView = new Viewer(this.$refs.imgView)
      global.thisView.options.zIndex = 5000
      global.thisView.view(index)
    },
    // 删除
    cancelDel() {
      this.showDel = false
    },
    del () {
      this.presentData.picPath = ''
      this.presentData.picData = ''
      this.defaultImgId = ''
      this.showDel = false
    },
    deleteRenderPic () {
      this.showDel = true
    },
    formSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.submitThisForm(2)
        } else {
          this.submitThisForm(1)
          return false
        }
      })
    },
    async submitThisForm (state) {
      let { picPath, reMarks, picType } = this.presentData
      let res = await systemdocking.add({
        ...this.presentData,
        picPath: this.fileName(picPath),
        eventId: this.itemInfo.id,
        defaultPicId: picType==='3' ? this.defaultImgId : '',
        reMarks,
        state,
        version:this.systemVersion
      })
      if (res.code === 0) {
        this.itemInfo.systemDockingState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, systemDockingState: state})
        this.$message.success(res.msg)
        this.systemVersion=null;
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
.present-quantitate {
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
  margin-top: 10px;
  width: 100%;
  max-height: 500px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  &:hover {
    border-color: #409eff;
  }
  i {
    font-size: 28px;
    line-height: 500px;
    color: #8c939d;
  }
}
.default-dialog {
  .default-img {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-self: center;
    flex-wrap: wrap;
    width: 100%;
    div{
      flex: 1;
      min-width: 45%;
      // margin: 10px;
      // width:calc(50% - 20px);
      img {
        cursor: pointer;
      }
      p {
        margin-bottom: 10px;
        text-align:center;
      }
    }
    div:not(:last-child) {
      margin-right: 10px;
    }
  }
}
.detail /deep/ .el-card__body {
  display: flex;
  width: 100%;
  height: 193px;
  padding: 0;
  flex-direction: row;
  justify-content: space-between;
  &>strong {
    padding-top: 60px;
    height: 100%;
    font-size: 1rem;
    writing-mode: vertical-lr;
    letter-spacing: 5px;
    color: #fff;
    background-color: cadetblue;
  }
  &>div {
    flex: 1;
    margin-right: 8px;
  }
}
.img-avatar {
  position: relative;
  display: flex;
  width: 100%;
  justify-content: center;
  img {
    min-height: 200px;
    max-height: 500px;
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
.detail /deep/ .el-card__body:nth-child(2)>strong {
  background-color: burlywood;
}
#charts-des {
  height: 386px;
}
</style>
