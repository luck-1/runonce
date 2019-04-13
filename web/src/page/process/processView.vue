<template>
	<div style="height: calc(100% - 18px)">
    <div class="event-state" :class="eventClass" v-if="eventState" style="height:27px;position: relative;top: 1px;">
      <span class="text">{{eventState}}</span>
    </div>
		<div class="title-view">
			<span class="title-span">办理流程图</span>
			<el-radio-group v-if="!watchPic" v-model="uploadType" size="small" style="margin-left: 1rem;">
				<el-radio :label="1">用户上传</el-radio>
				<el-radio :label="2">工具绘制</el-radio>
			</el-radio-group>
			<el-button v-if="!watchPic" @click="delPic" type="text" style="marginLeft: 20px;font-size: 14px;font-weight: 500;">删除</el-button>
		</div>
		<div style="height: calc(100% - 40px);paddingTop: 10px">
			<div v-if="imgUrl.length === 0" class="upload-btn-div" @click="showImageEdit" v-loading="loadingProcess" @dblclick="judge('办理流程图', 'imgUrl')">
				<i class="el-icon-plus"></i>
			</div>
      <div v-else  v-loading="loadingProcess">
        <div class="img-avatar" :class="[ishover?'hover':'',watchPic?'watch-pic':'']" @mouseover="ishover=true" @mouseout="ishover=false">
          <div class="avatar-icon">
            <i class="el-icon-zoom-in" @click.stop="viewRenderPic(imgUrl)"></i>
            <i class="el-icon-download" @click.stop="downloadPic(imgUrl)"></i>
          </div>
          <img :src="imgUrl" style="max-width: 100%;min-height: 100px;max-height: 500px;" @click="showImageEdit"  @dblclick="judge('办理流程图', 'imgUrl')"/>
        </div>
      </div>
			<input type="file" accept='image/jpeg' ref="uploadImgs" @change="fileUpload" style="display:none;"/>
		</div>

		<el-dialog :before-close="handleClose" :visible="visible" :fullscreen="true" append-to-body  custom-class="wangxiaoniqidemingzi"
			:close-on-press-escape="false" :close-on-click-modal="false" @close="visible=false">
			<div style="height: calc(100vh - 10px)" >
				<iframe id="iframe" :src='iframeUrl'  frameborder="0" width="100%" height="100%"></iframe>
			</div>
		</el-dialog>
    <!-- 图片放大  -->
    <div ref="renderRef" style="display:none">
      <img :src="processPicture">
    </div>

    <!-- 评审 -->
    <JudgeDialog title="办理流程图评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>

		<div class="btn-submit" @click="formSubmit" v-if="!watchPic">保存</div>
	</div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
import { aliConfig } from "@/utils/aliyunConfig";
import { process } from "../../api/service";
import { common } from '@/utils/common.js'
import JudgeDialog from '@/components/judgeDialog'
import Viewer from 'viewerjs'

const OSS = require("ali-oss");
const client = new OSS(aliConfig);
export default {
  components: { JudgeDialog },
  props: {
    state: {
      type: Boolean
    },
    showFlow: {
      type: Boolean
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
  data() {
    return {
      flowVersion:null,//办理流程图
      // 评审
      judgeShow: false,
      judgeForm: {},
      loadingProcess: false,
      eventState: '未完善',
      eventClass: "btn-not-perfect",
      currentId: null,
			processInfo: {
				data: null,
				name: '',
				url: '',
				param: '',
				isClose: false
			},
      ishover: false,
      centerDialogVisible: false,
      modelInfos: "",
      imgUrl: "",
      workflowName: "",
      iframeUrl: "",
      processPicture:"",
      num: 0,
      uploadType: 1,
      visible: false,
      fileName: "",
      flowTemp: '<mxGraphModel dx="1059" dy="384" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="827" pageHeight="1169"><root><mxCell id="0"/><mxCell id="1" parent="0"/><mxCell id="17" value="办理流程图" style="swimlane;fillColor=#33FFFF;" vertex="1" parent="1"><mxGeometry x="70" y="40" width="700" height="650" as="geometry"><mxRectangle x="140" y="40" width="90" height="23" as="alternateBounds"/></mxGeometry></mxCell><mxCell id="25" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="18" target="23"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="18" value="开始" style="ellipse;whiteSpace=wrap;html=1;fillColor=#E7FF0F;" vertex="1" parent="17"><mxGeometry x="299" y="60" width="80" height="50" as="geometry"/></mxCell><mxCell id="19" value="结束" style="ellipse;whiteSpace=wrap;html=1;fillColor=#E7FF0F;" vertex="1" parent="17"><mxGeometry x="299" y="330" width="80" height="50" as="geometry"/></mxCell><mxCell id="22" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="20" target="19"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="20" value="评审" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#CCFF99;" vertex="1" parent="17"><mxGeometry x="198" y="200" width="120" height="60" as="geometry"/></mxCell><mxCell id="27" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="23" target="24"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="31" value="否" style="text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=#ffffff;" vertex="1" connectable="0" parent="27"><mxGeometry x="-0.0727" y="-4" relative="1" as="geometry"><mxPoint x="-23.5" y="-13" as="offset"/></mxGeometry></mxCell><mxCell id="28" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;exitX=0;exitY=0.5;" edge="1" parent="17" source="23" target="20"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="30" value="是" style="text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=#ffffff;" vertex="1" connectable="0" parent="28"><mxGeometry x="0.3069" y="46" relative="1" as="geometry"><mxPoint x="-12.5" y="-15" as="offset"/></mxGeometry></mxCell><mxCell id="23" value="" style="rhombus;whiteSpace=wrap;html=1;fillColor=#FFFF00;" vertex="1" parent="17"><mxGeometry x="319" y="140" width="40" height="40" as="geometry"/></mxCell><mxCell id="29" style="edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;" edge="1" parent="17" source="24" target="19"><mxGeometry relative="1" as="geometry"/></mxCell><mxCell id="24" value="评审" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#CCFF99;" vertex="1" parent="17"><mxGeometry x="369" y="200" width="120" height="60" as="geometry"/></mxCell></root></mxGraphModel>'
    };
  },
  computed: {
    ...mapState([ 'itemDetail' ]),
    itemInfo() {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {};
      switch (itemDetail.processMapState) {
        case 1: this.eventState = "未完善"; this.eventClass = "btn-not-perfect"; break;
        case 2: this.eventState = "已完善"; this.eventClass = "btn-has-perfect"; break;
        case 3: this.eventState = "已提交"; this.eventClass = "btn-submitted"; break;
        default: break
      }
      return itemDetail
    }
  },
  mounted(){
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
  },
  watch: {
    state: function(newVal, oldVal) {
      let _this = this;
      if (newVal) {
        window.addEventListener("message", this.uploadMethod);
        this.$nextTick(()=>{
          this.iframeUrl = 'http://47.92.109.76:8080/javascript/examples/grapheditor/www/index.html'
        })
        this.getData();
      } else {
        window.removeEventListener("message", this.uploadMethod);
      }
    }
  },
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL'
    ]),
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
    handleClose(done) {
      this.$confirm("关闭之前是否保存", {
        confirmButtonText: "保存",
        cancelButtonText: "关闭",
        type: "warning"
      }).then(() => {
      	document.getElementById("iframe").contentWindow.postMessage({ type: "callSave"}, "*");
      }).catch(() => {
				this.visible = false
			})
    },
    getData() {
      if (!this.showFlow) {
        this.num = 0;
      }
      if (this.num === 0 && this.itemInfo.id) {
        this.num++;
        this.$emit("changeShowFlow");
        setTimeout(() => {
          this.getProcessData(this.itemInfo.id);
        }, 300);
      }
    },
    delPic() {
      if(!this.imgUrl){
        return
      }
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
				this.imgUrl = ''
				this.processInfo =  {
					data: null,
					name: '',
					url: '',
					param: '',
					isClose: false
				}
      })
    },
    showImageEdit() {
      if (this.watchPic) {
        return;
      }
      if (this.uploadType == 1) {
        this.$refs.uploadImgs.setAttribute('type', 'file')
        this.$refs.uploadImgs.click();
        return;
      }
      if (this.uploadType == 2) {
        this.visible = true;
        this.$nextTick(() => {
          let iframe = document.getElementById("iframe");
          if (iframe.attachEvent) {
            iframe.attachEvent("onload", () => {
              let sendData = {
                type: "saveImage",
                data: this.modelInfos,
                name: "test"
              };
              setTimeout(() => {
                document.getElementById("iframe").contentWindow.postMessage(sendData, "*");
              }, 1000);
            });
          } else {
            iframe.onload = () => {
              let sendData = {
                type: "saveImage",
                data: this.modelInfos,
                name: "test"
              };
              setTimeout(() => {
                document.getElementById("iframe").contentWindow.postMessage(sendData, "*");
              }, 1000);
            };
          }
        });
        return;
      }
    },
    fileUpload(e) {
      let files = e.target.files;
      if (files.length === 0) {
        return
      }
      let storeAs = `${new Date().toJSON().split("T")[0]}/${new Date().getTime()}.${files[0].name.split(".")[files[0].name.split(".").length - 1]}`
      let validate = '.jpg,.jpeg,.gif,.png'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(storeAs)) {
        this.$message.error(`只能上传.jpg,.jpeg,.gif,.png格式`)
        this.loadingProcess = false
        return
      }
      this.loadingProcess = true
      this.loadingProcess = true
      let results = client.put(storeAs, files[0]).then(results => {
				this.loadingProcess = false
				this.imgUrl = client.signatureUrl(results.name, { expires: 80000 })
				this.processInfo.data = null
				this.processInfo.name = 'test'
				this.processInfo.url = results.name
			}).catch(err => {
				this.loadingProcess = false;
      })
      this.$refs.uploadImgs.removeAttribute('type')
    },
    uploadMethod(event) {
      let evenData = event.data;
      if (evenData.type && evenData.type === "saveJSON") {
        // 拿到保存的JSON串
        // if (this.uploadType == 2) {
        //     this.savePng(evenData.data,evenData.name);
        //     return;
        // }
        evenData.url = ''
        this.processInfo = evenData
        this.saveimage(evenData.data,evenData.name,evenData.param,evenData.isClose)
      }
    },
    async saveimage(val,name,params,isClose){
      let param = {}
      param.fileName = name
      param.xmlData  = val.xml
      param.width = val.width
      param.height = val.height
      param.codeedXmlString = params

      let res = await process.saveProcessImage(param)
      if (res.code === 0) {
        this.$message.success('图片保存成功')
        this.imgUrl = client.signatureUrl(`${res.obj}`, { expires: 80000 })
        if (isClose) {
          this.visible = false
        }
      } else {
        this.$message.error(res.msg)
      }
    },
    async savePng(data, name) {
      let param = {
        fileName: "test",
        format: "png",
        height: 1311,
        width: 573,
        xmlData: data
      };
      let res = await process.createPng(param);
      if (res.code === 0) {
        this.$message.success(res.msg);
        this.visible = false;
        this.imgUrl = client.signatureUrl(res.obj, { expires: 80000 });
      } else {
        this.$message.error(res.msg);
      }
    },
    base64ToImage(dataurl, filename) {
      var arr = dataurl.split(","),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, { type: mime });
    },
    //回显流程
    async getProcessData(id) {
      let res = await process.getProcess(id);
      if (res.code === 0) {
        this.flowVersion= res.obj.version;
        let processObj = res.obj.processDescriptionPic
        this.currentId = processObj ? processObj.id: null
        this.modelInfos = processObj ?processObj.picliu || this.flowTemp : this.flowTemp;
        this.uploadType = processObj?processObj.picType:1,
        this.fileName = processObj ? processObj.picname : "text";
        this.imgUrl = processObj ? (processObj.picbase?client.signatureUrl(processObj.picbase, { expires: 80000 }):'') : '';
        this.processInfo = {
          data: {
            width: processObj?processObj.width:0,
            height: processObj?processObj.height:0,
            xml: processObj?processObj.picliu:''
          },
          name: processObj?processObj.picname:'',
          url: processObj?processObj.picbase:'',
          param: processObj?processObj.codeedXmlString:''
        }
      } else {
        this.imgUrl = ''
        this.$message.error(res.msg);
      }
		},
		// 保存
		async formSubmit() {
      let { data, name, param } = this.processInfo
      let params = {
        processDescriptionPic: {
          id: this.currentId,
          picname: name,
          eventId: this.itemInfo.id,
          picliu: data ? data.xml : "",
          width: data ? data.width : "",
          height: data ? data.height : "",
          picType: this.uploadType,
          picbase: this.fileNameFun(this.imgUrl),
          codeedXmlString: param,
          state: this.imgUrl?2:1
        },
        version: this.flowVersion
      };
      // if(params.picType===1){
      //   // 用户上传
      //   params.state = (params.picname&&params.picbase)?2:1
      // } else {
      //   params.state = (param&&params.picbase&&data)?2:1
      // }
			let res = await process.addprocess(params);
			if (res.code === 0) {
        this.itemInfo.processMapState = this.imgUrl?2:1
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, processMapState: this.imgUrl?2:1})
        this.$message.success(res.msg)
        this.flowVersion = null
        this.getProcessData(this.itemInfo.id);
			} else {
			  this.$message.error(res.msg)
			}
    },
    fileNameFun (val) {
      if (val) {
        let splits = val.split('/')
        let newVal = `${splits[splits.length-2]}/${splits[splits.length-1]}`
        return newVal.split('?')[0]
      } else {
        return ''
      }
    },
    viewRenderPic (url) {
      this.processPicture = url
      global.thisView && global.thisView.destroy()
      global.thisView = new Viewer(this.$refs.renderRef)
      global.thisView.show()
    },
    // 下载图片
    downloadPic (url) {
      common.download(`${this.itemInfo.eventName}办理流程图`, url)
    }
  }
};
</script>

<style lang="scss" scoped>
.title-view {
  width: 100%;
  height: 30px;
  background-color: #eeeeee;
  border-left: 3px solid #2088fc;
  .title-span {
    font-size: 16px;
    margin-left: 10px;
    line-height: 30px;
    font-weight: bolder;
  }
}
.upload-btn-div {
  width: 240px;
  height: 240px;
  border: 1px solid #c6c6c6;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 5px;
  cursor: pointer;
  i {
    font-size: 24px;
    color: #c6c6c6;
    cursor: pointer;
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
</style>

<style lang="scss">
.wangxiaoniqidemingzi {
  .el-dialog__header {
    padding: 0px !important;
  }
  .el-dialog__body {
    padding: 5px;
  }
  .el-dialog__headerbtn {
    top: 10px;
  }
}
</style>
