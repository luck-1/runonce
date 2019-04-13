/**
 * 名称：申请类型
 * 作者：王晓妮
 * 日期：2018-11-6
 思维导图
 */
<template>
  <div class="applyMind">
    <div class="btn-submit" @click.stop="saveTohtml" v-if="!watchPic">保存</div>
    <div class="btn-submit" @click.stop="judge" v-if="watchPic&&editState">评论</div>
    <!-- <div id="minder-container" type="application/kityminder" minder-data-type="json"/> -->
    <iframe :src='iframeUrl' id="iframeApply"  frameborder="0" width="100%" height="100%"></iframe>

    <!-- 评审 -->
    <JudgeDialog title="申请类型评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </div>
</template>
<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import { aliConfig } from '@/utils/aliyunConfig'

const OSS = require('ali-oss');
import { applyState } from '@/api/service'

import JudgeDialog from '@/components/judgeDialog';

export default {
  components: { JudgeDialog },
  props: {
    state: {
      type: Boolean,
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
    },
    isLeave: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapState([
      'itemDetail'
    ]),
    itemInfo () {
      return this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    }
  },
  watch: {
    state: function(newVal, oldVal) {
      if(newVal) {
        this.iframeUrl = '../../../static/appllyMind/apply.html';
      }else{
        this.iframeUrl = '';
      }
    },
    isLeave (val) {
      if(val) {
        window.removeEventListener("message", this.changValue);
      } else {
        window.addEventListener('message',this.changValue);
      }
    }
  },
  data () {
    return {
      // 评审
      judgeShow: false,
      judgeForm: {},
      iframeUrl: null,
      ossFile: null,
      allData: null
    }
  },
  mounted () {
    /**
     * 监听从iframe页面传回的值
     */
    window.addEventListener('message',this.changValue);
  },
  beforeDestroy ()  {
    window.removeEventListener("message", this.changValue);
  },
  methods: {
    changValue(evenData) {
      if(evenData.data.type === 'base64'){
        if (global.timess && new Date().getTime() - global.timess <= 300) {
          return;
        }
        global.timess = new Date().getTime();
        this.applyAdd(evenData.data);
      }else if(evenData.data.type === 'message'){
        this.$message[evenData.data.messageType](evenData.data.messageInfo);
      }
    },
    judge () {
      let endTime = this.itemInfo.examineEndTime
      if(endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          lineNumber: null,
          id: this.itemInfo.id,
          propertyName: '申请类型',
          sheetNum: 3,
          property: '申请类型',
          questionContent: ''
        }
      }
    },
    judgeCancel () {
      this.judgeShow = false
    },
    saveTohtml(){
      let sendData = {
        type: 'save'
      };
      document.getElementById('iframeApply').contentWindow.postMessage(sendData,'*');
    },
    formate(obj){
      let arr =[];
      if(obj.children.length === 0){
        if(!obj.data.materialId){
          obj.data.leafNode = 'leafNode';
        }
      }else{
        if(obj.data.leafNode){
          let leafChildren = obj.children;
          for(let ele of leafChildren){
            if(!ele.data.materialId){
              delete obj.data.leafNode;
            }
          }
        }else{
          obj.children.forEach(element => {
            this.formate(element);
          });
        }
      }
      return obj;
    },
    /*
    保存数据
    */
    applyAdd(evenData){
      let jsonData = JSON.parse(evenData.minderData).root;
      let allDatas = this.formate(jsonData);
      let newEventData = JSON.parse(evenData.minderData);
      newEventData.root = allDatas;
      evenData.minderData = JSON.stringify(newEventData);
      let files = this.dataURLtoFile(evenData.base64Url, new Date().getTime() + 'imgName.jpg');
      setTimeout( () => {
        this.saveToOSS(evenData, files);
      },10);
    },

    /***
     * 上传到OSS
     */
    saveToOSS(eventData, file){
      let client = new OSS(aliConfig);
      let storeAs = `${new Date().toJSON().split('T')[0]}/${file.name}`;
      let results = client.put(storeAs, file).then((results) => {
      this.ossFile = client.signatureUrl(results.name, { expires: 80000 });
      this.applyAddfun(eventData, storeAs);
      });
    },
    /**
     *保存
     */
    async applyAddfun(evenData, filePath){
      // dbb  仅有一个节点不让添加
      let jsonData = JSON.parse(evenData.minderData).root;
      if(jsonData.children.length === 0){
        this.$message.error("请插入节点")
        return
      }
      let evId = localStorage.itemDetail?JSON.parse(localStorage.itemDetail):{}
      let param = {
        minderData: {
          minderData: evenData.minderData,
          eventId: evId.id,
          picPath: filePath,
          type: 1,
        },
        version: evenData.version
      };
      let res = await applyState.applyAdd(param);
      if(res.code === 0) {
        this.$message.success(res.msg);
        let sendDatas = {
          type: 'saveApplySuccess'
        };
        document.getElementById('iframeApply').contentWindow.postMessage(sendDatas,'*');
      }else{
        this.$message.error(res.msg);
      }
    },
    /**
		 * base64转文件
		 */
    dataURLtoFile(dataurl, filename) {
      let arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
      bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
      while(n--){
          u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, {type:mime});
    }
   }
}
</script>

<style lang="scss" scoped>
  .applyMind{
    height: calc(100vh - 170px);
  }
  #minder-container {
      position: absolute;
      border: 1px solid #ccc;
      left: 10px;
      top: 10px;
      bottom: 10px;
      right: 10px;
  }
</style>
