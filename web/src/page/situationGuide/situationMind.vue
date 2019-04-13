/**
 * 名称：情形导图,情形导图
 * 作者：王晓妮
 * 日期：2018-11-6
 * 思维导图
 */
<template>
  <div class="applyMind">
    <div class="mind-save" @click.stop="saveTohtml">保存</div>
    <!-- <div id="minder-container" type="application/kityminder" minder-data-type="json"/> -->
    <iframe :src='iframeUrl' id="situationBrainMap" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>
<script>
import { aliConfig } from '@/utils/aliyunConfig'

const OSS = require('ali-oss');
import { situationBrainMap } from '@/api/service'

export default {
  props: {
    title: {
      type: String
    }
  },
  computed: {
    itemInfo () {
      let itemDetail = localStorage.situation ? JSON.parse(localStorage.situation) : {}
      return itemDetail
    },
    iframeUrl () {
      let url = ''
      if(this.title==='情形导图') {
        url = '../../../static/appllyMind/sitiuationMind.html';
      } else {
        url = '../../../static/appllyMind/situationPath.html';
      }
      return url
    }
  },
  data () {
    return {
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
    saveTohtml(){
      let sendData = {
        type: 'save'
      };
      document.getElementById('situationBrainMap').contentWindow.postMessage(sendData,'*');
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
      let evId = localStorage.situation?JSON.parse(localStorage.situation):{}
      let param = {
        situationBrainMap: {
          minderData: evenData.minderData,
          situationId: evId.id,
          picPath: filePath,
          type: this.title==='情形导图'?1:2,
        },
        version: evenData.version
      };
      let res = await situationBrainMap.add(param);
      if(res.code === 0) {
        this.$message.success(res.msg);
        let sendDatas = {
          type: 'saveApplySuccess'
        };
        document.getElementById('situationBrainMap').contentWindow.postMessage(sendDatas,'*');
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
.applyMind {
  height: calc(100% - 5px);
}
.mind-save {
  position: absolute;
  right: 1rem;
  top: 0;
  width: 4rem;
  height: 2rem;
  border-radius: 5px;
  line-height: 2rem;
  text-align: center;
  font-size: 16px;
  color: #fff;
  cursor: pointer;
  z-index: 5;
  background-color: #409eff;
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
