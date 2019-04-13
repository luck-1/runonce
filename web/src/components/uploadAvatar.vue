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
      <a :href="item.url" :download="item.url.split('/')[item.url.split('/').length-1].split('?')[0]" v-if="otherPosition">
        <i class="el-icon-document"></i>
        {{item.url.split('/')[item.url.split('/').length-1].split('?')[0]}}
      </a>
      <i class="el-icon-close" @click="deleteFile(idx)" v-if="otherPosition&&!watchPic"></i>
    </p>
    <el-upload
      action=""
      :show-file-list="false"
      :on-success="uploadSuccess"
      :before-upload="beforeUpload"
      :multiple="multiple"
      class="img-upload"
      :class="hideAdd"
      :accept='accept'
      v-loading="loading"
      v-if="!watchPic">
      <i class="el-icon-plus uploader-icon"></i>
    </el-upload>

    <div v-if="!otherPosition">
      <p v-for="(item, idx) in otherFiles" :key="idx">
        <a :href="item.url" :download="item.url.split('/')[item.url.split('/').length-1].split('?')[0]">
          <i class="el-icon-document"></i>
          {{item.url.split('/')[item.url.split('/').length-1].split('?')[0]}}
        </a>
        <i class="el-icon-close" @click="deleteFile(idx)" v-if="!watchPic"></i>
      </p>
    </div>

    <!-- 图片放大 -->
    <div ref="imgView" style="display:none">
      <!-- <img :src="imgUrl"> -->
      <ul ref="imgView" style="display:none">
        <li v-for="(item, index) in imgs" :key="index"><img :src="item.url" :alt="item.name"></li>
      </ul>
    </div>

    <!-- 上传图片 -->
    <input type="file" accept='image/jpeg' ref="uploadImg" @change="fileUpload" style="display:none;"/>

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
      default: '.jpg,.jpeg,.gif,.bmp,.pdf,.JPG,.JPEG,.PBG,.GIF,.BMP,.PDF,.xlsx,.xls,.doc,.docx'
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
      // 显示图片
      currentIndex: 0,
      visible: false,
      imgUrl: '',
      currentImg: null,
      imgName : '',
      // 图片脱敏
      dialogVisible: false
    }
  },
  watch: {
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
    getFileByType(files) {
      this.imgs = []
      this.otherFiles = []
      files.forEach((item, index) => {
        // let signUrl = client.signatureUrl(`${new Date().toJSON().split('T')[0]}/${item}`)
        let signUrl = client.signatureUrl(`${item}`, { expires: 80000 })
        let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp)/i.test(item)
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
      let validate = '.jpg,.jpeg'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(files[0].name)) {
        this.$message.error(`只能上传${this.accept}格式`)
        this.loading = false
        return
      }
      let storeAs = `${new Date().toJSON().split('T')[0]}/${new Date().getTime()}.${files[0].name.split('.')[files[0].name.split('.').length-1]}`;
      let results = client.put(storeAs, files[0]).then((results) => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        // let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp)/i.test(signUrl)
        // 图片
        this.imgs.splice(this.currentIndex, 1, {
          name: storeAs,
          url: signUrl,
          imgHover: false
        })
      })
    },
    // 文件上传前钩子
    beforeUpload (file) {
      let fileName = `${new Date().getTime()}.${file.name.split('.')[file.name.split('.').length-1]}`
      // let fileName = file.name
      let validate = this.accept.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(fileName)) {
        this.$message.error(`只能上传${this.accept}格式`)
        this.loading = false
        return
      }
      // console.log(fileName)
      // 命名规则
      // let storeAs = new Date().getTime() + Math.ceil(Math.random() * 100000).toString() + '.' + file.name.split('.')[1]
      let storeAs = `${new Date().toJSON().split('T')[0]}/${fileName}`;
      this.loading = true
      let results = client.put(storeAs, file).then((results) => {
        // 过期时间10分钟, 图片处理式样"image/resize,w_300"
        // let signUrl = client.signatureUrl('example.jpg', {expires: 600, 'process' : 'image/resize,w_300'});
        // console.log(results.name)
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp)/i.test(signUrl)
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
    },
    // 文件上传成功的钩子
    uploadSuccess (response, file, fileList) {},
    // 文件超出个数限制时的钩子
    handleExceed (file, fileList) {
      this.$message.error(`最多上传 ${this.imageNum} 张图片`)
      if ([...this.imgs, ...this.otherFiles].length < this.imageNum) {
        this.hideAdd = ''
      } else {
        this.hideAdd = 'hideAdd'
      }
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
  .img-upload {
    display: inline-block;
    width: 148px;
    height: 148px;
    margin-right: 10px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    & /deep/ .el-upload{
      display: block!important;
      text-align: left!important;
    }
    &:hover {
      border-color: #409EFF;
    }
    .uploader-icon {
      position: absolute;
      width: 148px;
      height: 148px;
      line-height: 148px;
      text-align: center;
      font-size: 28px;
      color: #8c939d;
    }
  }
}
.p-file {
  display: inline-block;
  vertical-align: text-bottom;
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
  .img-upload {
    width: 65px;
    height: 65px;
    .uploader-icon {
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
  .img-upload {
    width: 484px;
    height: 484px;
    .uploader-icon {
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
