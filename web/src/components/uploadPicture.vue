<template>
  <div class="img-handle" :class="size">
    <div v-if="imgs.length" style="display:inline-block;">
      <div class="echo-img" :class="[item.imgHover?'hover':'',watchPic?'watch-pic':'']" v-for="(item, index) in imgs" :key="index"
        @mouseover="item.imgHover=true" @mouseout="item.imgHover=false">
        <div class="avatar-icon">
          <i class="el-icon-zoom-in" @click.stop="currentIndex=index;viewRenderPic(item, index)"></i>
          <i class="el-icon-edit" @click.stop="currentIndex=index;$refs.uploadImg.click()" v-if="!watchPic"></i>
          <i class="el-icon-delete" @click.stop="deletePic(index)" v-if="!watchPic"></i>
        </div>
        <img :src="item.url" :alt="item.name">
      </div>
    </div>
    <p class="p-file" v-for="(item, idx) in otherFiles" :key="idx">
      <a :href="item.url" :download="item.url.split('/').splice(-1)[0].split('?')[0]" v-if="otherPosition">
        <i class="el-icon-document"></i>
        {{item.url.split('/').splice(-1)[0].split('?')[0]}}
      </a>
      <i class="el-icon-close" @click="deleteFile(idx)" v-if="otherPosition&&!watchPic"></i>
    </p>
    <div class="upload" :class="hideAdd" v-loading="loading" @click="imgtype=true" v-if="!watchPic"><i class="el-icon-plus"></i></div>

    <div v-if="!otherPosition">
      <p v-for="(item, idx) in otherFiles" :key="idx">
        <a :href="item.url" :download="item.url.split('/').splice(-1)[0].split('?')[0]">
          <i class="el-icon-document"></i>
          {{item.url.split('/').splice(-1)[0].split('?')[0]}}
        </a>
        <i class="el-icon-close" @click="deleteFile(idx)" v-if="!watchPic"></i>
      </p>
    </div>

    <el-dialog title="选择上传图片方式" class="img-type" top="40vh" width="500px" append-to-body @close="imgtype=false"
      :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="imgtype">
      <el-button @click="imgtype=false;$refs.imgupload.click()">本地文件</el-button>
      <el-button @click="imgtype=false;webShow=true" :disabled="webFiles.length===0">网络(导入)文件</el-button>
    </el-dialog>

    <el-dialog title="网络(导入)文件" append-to-body width="900px" :visible="webShow" :show-close="false"
      :close-on-click-modal="false" :close-on-press-escape="false" class="default-dialog">
      <div class="default-img">
        <p v-for="(item, index) in defaultFiles" :key="-index-1" style="width:100%">
          <el-radio v-model="defaultImgId" :label="item.name">{{item.current}}</el-radio>
        </p>
        <div class="img-div">
          <div v-for="(item, index) in defaultImgs" :key="index">
            <img :src="item.url" width="100%" @click="defaultImgId=item.name">
            <p><el-radio v-model="defaultImgId" :label="item.name">{{item.current}}</el-radio></p>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="webShow=false;defaultImgId=''">取 消</el-button>
        <el-button type="primary" @click="webFileFun">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 上传图片 -->
    <input type="file" accept='image/jpeg' ref="uploadImg" @change="imgUpload" style="display:none;"/>
    <input type="file" :multiple="multiple" :accept='accept' ref="imgupload" @change="fileUpload" style="display:none;"/>

    <!-- 图片放大 -->
    <div ref="imgView" style="display:none">
      <ul ref="imgView" style="display:none">
        <li v-for="(item, index) in imgs" :key="index"><img :src="item.url" :alt="item.name"></li>
      </ul>
    </div>

    <!-- 图片脱敏 -->
    <el-dialog class="img-setting" title="图片处理" fullscreen append-to-body
      :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible">
      <!-- <div><i class="el-icon-close" style="font-size: 20px;float:right;cursor:pointer;" @click.stop="dialogVisible=false"></i></div> -->
      <imgSettingS :imgUrl="imgUrl" :dealPic='dealPic' :imgName="imgName" @successSubmit="successSubmit" @close="dialogVisible=false"></imgSettingS>
    </el-dialog>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancelDel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>
  </div>
</template>
<script>
import Viewer from 'viewerjs'
import imgSetting from '@/page/imgSetting/imgSetting'
import DeleteDialog from '@/components/dialog.vue'

import { aliConfig } from '@/utils/aliyunConfig'

const OSS = require('ali-oss');
const client = new OSS(aliConfig);
export default {
  props: {
    // 是否脱敏
    imgHandle: {
      type: Boolean,
      default: true
    },
    files: {
      type: Array,
      default: ()=>[]
    },
    // 其他文件行内显示还是块显示
    otherPosition: {
      type: Boolean,
      default: false
    },
    // 上传文件个数显示
    imageNum: {
      type: Number,
      default: 1
    },
    // 图片显示大小（small,''，large）
    size: {
      type: String,
      default: ''
    },
    // 是否多传
    multiple: {
      type: Boolean,
      default: false
    },
    // 是否只读
    watchPic: {
      type: Boolean,
      default: false
    },
    // 接受上传的文件格式
    accept: {
      type: String,
      default: '.jpg,.jpeg,.gif,.png,.bmp,.pdf,.JPG,.JPEG,.PBG,.GIF,.BMP,.PDF,.xlsx,.xls,.doc,.docx'
    },
    // 导入的图片
    webFiles: {
      type: Array,
      default: []
    }
  },
  components: { 'imgSettingS':imgSetting, DeleteDialog },
  data () {
    return {
      loading: false,
      // 删除
      showDel: false,
      currentType: '',
	    dealPic: false,
      hideAdd: '',
      imgs: [],
      otherFiles: [],
      // 选择图片上传的方式
      imgtype: false,
      webShow: false,
      defaultImgId:'',
      // 显示图片
      currentIndex: 0,
      visible: false,
      imgUrl: '',
      currentImg: null,
      imgName : '',
      // 图片脱敏
      dialogVisible: false,
      defaultFiles: [],
      allFiles: []
    }
  },
  computed: {
    defaultImgs() {
      let defaultImgs = []
      this.defaultFiles = []
      this.allFiles = []
      this.webFiles.forEach((item, index) => {
        let signUrl = client.signatureUrl(item.url, { expires: 80000 });
        let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(item.url.split('/').splice(-1)[0])
        let file = {
          uid: new Date().getTime()-index*5,
          imgHover: false,
          name: item.url,
          current: item.name,
          url: signUrl
        }
        if(flag) {
          defaultImgs.push(file)
        } else {
          this.defaultFiles.push(file)
        }
        this.allFiles.push(file)
      })
      return defaultImgs
    },
  },
  watch: {
    files (val) {
      // this.getFileByType(val)
    },
    imgs (val) {
      let arr = [...val, ...this.otherFiles].map(item => item.name)
      this.$emit('upload', JSON.stringify({imgs: arr}))
      this.$emit('heigthChange')
    },
    otherFiles (val) {
      let arr = [...val, ...this.imgs].map(item => item.name)
      this.$emit('upload', JSON.stringify({imgs: arr}))
      this.$emit('heigthChange')
    }
  },
  mounted () {
    this.getFileByType(this.files)
  },
  methods: {
    webFileFun () {
      this.webShow = false
      let current = this.allFiles.filter(item => item.name===this.defaultImgId)
      let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(current[0].current)
      if (flag) {
        // 图片
        this.imgs.push(current[0])
      } else {
        // 其他文件
        this.otherFiles.push(current[0])
      }
      let files = [...this.imgs, ...this.otherFiles]
      if (files.length >= this.imageNum) {
        this.hideAdd = 'hideAdd'
      }
      let arr = files.map(item => item.name)
      this.$emit('webFileFun',JSON.stringify({imgs: arr}))
      this.defaultImgId = ''
    },
    getFileByType(files) {
      this.imgs = []
      this.otherFiles = []
      files.forEach((item, index) => {
        // let signUrl = client.signatureUrl(`${new Date().toJSON().split('T')[0]}/${item}`, { expires: 80000 })
        let signUrl = client.signatureUrl(`${item}`, { expires: 80000 })
        let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(item)
        let file = {
          uid: new Date().getTime()-index*5,
          url: signUrl,
          name: item,
          imgHover: false
        }
        if (flag) {
          // 图片
          this.imgs.push(file)
        } else {
          // 其他文件
          this.otherFiles.push(file)
        }
        let files = [...this.imgs, ...this.otherFiles]
        if (files.length >= this.imageNum) {
          this.hideAdd = 'hideAdd'
        }
      })
    },
    // 图片脱敏处理成功提交
    successSubmit (imgUrl) {
	    this.dealPic = false
      this.imgUrl = imgUrl
      this.$nextTick(() => {
        global.thisView.update()
        this.currentImg.url = imgUrl
        this.currentImg.name = this.fileName(imgUrl)
        this.imgs[this.currentIndex] = this.currentImg
        this.imgs.splice(this.currentIndex, 1, this.currentImg)
      })
      this.dialogVisible = false;
    },
    fileName (val) {
      if (val) {
        let splits = val.split('/')
        let newVal = `${splits[splits.length-2]}/${splits[splits.length-1]}`
        return newVal.split('?')[0]
      } else {
        return ''
      }
    },
    fileUpload (e) {
      let files = e.target.files
      let validate = this.accept.split(',').join('|\\')
      for (let item of files) {
        let fileName = item.name.split('.').splice(-1)
        if(!new RegExp(`\\w(\\${validate})`,'gim').test(`1.${fileName}`)) {
          this.$message.error(`只能上传${this.accept}格式`)
          this.loading = false
          return
        }

        // 文件是否超出个数限制
        let length = [...this.imgs, ...this.otherFiles].length
        if (length+1 < this.imageNum) {
          this.hideAdd = ''
        } else {
          this.$message.error(`最多上传 ${this.imageNum} 张图片`)
          this.hideAdd = 'hideAdd'
        }

        this.loading = true
        // 命名规则
        let storeAs = `${new Date().toJSON().split('T')[0]}/${new Date().getTime()}.${item.name.split('.').splice(-1)}`;
        let results = client.put(storeAs, item).then((results) => {
          let signUrl = client.signatureUrl(results.name, { expires: 80000 });
          let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(signUrl)
          let thisFile = {
            name: storeAs,
            url: signUrl,
            imgHover: false
          }
          this.loading = false
          if (flag) {
            // 图片
            this.imgs.push(thisFile)
          } else {
            // 其他文件
            this.otherFiles.push(thisFile)
          }
          let files = [...this.imgs, ...this.otherFiles]
          if (files.length >= this.imageNum) {
            this.hideAdd = 'hideAdd'
          }
        }).catch(err => {
          this.loading = false
        })
      }
    },
    imgUpload (e) {
      let files = e.target.files
      let validate = '.jpg,.jpeg,.png'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(files[0].name)) {
        this.$message.error(`只能上传.jpg,.jpeg,.png格式`)
        this.loading = false
        return
      }
      let storeAs = `${new Date().toJSON().split('T')[0]}/${new Date().getTime()}.${files[0].name.split('.')[files[0].name.split('.').length-1]}`;
      let results = client.put(storeAs, files[0]).then((results) => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        // let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(signUrl)
        // 图片
        this.imgs.splice(this.currentIndex, 1, {
          name: storeAs,
          url: signUrl,
          imgHover: false
        })
      })
    },
    // 图片预览
    viewRenderPic (img, index) {
      this.currentImg = img
      this.imgUrl = img.url
      global.thisView && global.thisView.destroy()
      global.thisView = new Viewer(this.$refs.imgView, {
        // toolbar: {
        //   prev: function() {
        //     global.thisView.prev(true);
        //   },
        //   play: true,
        //   next: function() {
        //     global.thisView.next(true);
        //   },
        // },
        // viewed() {
        //   // global.thisView.view(index)
        // }
      })
      global.thisView.view(index)
      console.log(global.thisView)
      if(this.imgHandle&&!this.watchPic) {
        let li = document.createElement('li')
        li.className = 'icon-picture'
        li.innerHTML = '脱敏'
        // appendChild(append一个对象)
        document.querySelector('.viewer-toolbar ul').appendChild(li)
        $('.icon-picture').click(()=>{
          this.dealPic = true
          this.dialogVisible = true
          this.imgName = img.name.split('/')[1]
        })
      }
    },
    cancelDel() {
      this.showDel = false
    },
    del(){
      if(this.currentType==='deletePic') {
        this.imgs.splice(this.currentIndex, 1)
        this.hideAdd = ''
      } else {
        this.otherFiles.splice(this.currentIndex, 1)
      }
      this.showDel = false
    },
    // 图片删除
    deletePic (index) {
      this.showDel = true
      this.currentIndex = index
      this.currentType='deletePic'
    },
    // 其他文件删除
    deleteFile (index) {
      this.showDel = true
      this.currentIndex = index
      this.currentType='deleteFile'
    }
  }
}
</script>
<style lang="scss" scoped>
.img-type /deep/ .el-dialog__body {
  padding-top: 0;
}
.default-dialog {
  & /deep/ .el-dialog__body {
    max-height: 550px;
    overflow: auto;
  }
  .default-img {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-self: center;
    flex-wrap: wrap;
    width: 100%;
    .img-div {
      flex: 1;
      div{
        display: inline-block;
        height: 300px;
        max-width: calc(50% - 10px);
        img {
          box-shadow: 5px 5px 5px rgba(0,0,0,.3);
          height: 100%;
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
}
.p-file {
  display: inline-block;
  vertical-align: text-bottom;
}
.img-handle {
  // 图片回显
  .echo-img {
    position: relative;
    display: inline-block;
    margin-right: 10px;
    margin-bottom: 5px;
    img {
      width: 148px;
      height: 148px;
      box-shadow: 2px 2px 10px rgba(0,0,0,.5);
    }
    .avatar-icon {
      position: absolute;
      left: calc(50% - 3rem);
      display: none;
      font-size: 30px;
      line-height: 148px;
      z-index: 2;
      &>div{
        position: relative;
        display: inline-block;
        cursor: pointer;
      }
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
  .upload {
    display: inline-block;
    width: 148px;
    height: 148px;
    margin-right: 10px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    &:hover {
      border-color: #409EFF;
    }
    i {
      position: absolute;
      width: 148px;
      height: 148px;
      font-size: 28px;
      line-height: 148px;
      text-align: center;
      color: #8c939d;
    }
  }
}
// mini
.img-handle.mini {
  .echo-img {
    img {
      width: 65px;
      height: 65px;
      box-shadow: 1px 1px 5px rgba(0,0,0,.5);
    }
    .avatar-icon {
      left: calc(50% - 1.8rem);
      font-size: 1rem;
      line-height: 65px;
    }
    &.watch-pic {
      .avatar-icon {
        left: calc(50% - 1rem);
      }
    }
  }
  .upload {
    width: 65px;
    height: 65px;
    i {
      width: 65px;
      height: 65px;
      line-height: 65px;
      font-size: 18px;
    }
  }
}
// large
.img-handle.large {
  .echo-img {
    img {
      width: 484px;
      height: 484px;
      box-shadow: 2px 2px 10px rgba(0,0,0,.5);
    }
    .avatar-icon {
      left: calc(50% - 3.5rem);
      font-size: 2rem;
      line-height: 484px;
    }
    &.watch-pic {
      .avatar-icon {
        left: calc(50% - 1rem);
      }
    }
  }
  .upload {
    width: 484px;
    height: 484px;
    i {
      width: 484px;
      height: 484px;
      line-height: 484px;
      font-size: 38px;
    }
  }
}
.hideAdd {
  display: none;
}
.icon-picture {
  position: relative;
  &::after {
    content: '脱敏';
    position: absolute;
  }
}
.img-setting {
  z-index: 5000!important;
  width: 90%!important;
  left: 5%!important;
  height: 90%!important;
  top: 5%!important;
  & /deep/ .el-dialog__header {
    display: none!important;
  }
  & /deep/ .el-dialog__body {
    padding: 0!important;
  }
}
</style>
