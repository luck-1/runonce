<template>
<!-- :show-close="false" -->
  <el-dialog :before-close="handleClose" :visible="processVisible" append-to-body  custom-class="wangxiaoniqidemingzi"
    :close-on-press-escape="false" :fullscreen="true" :close-on-click-modal="false" @close="closeDialog">
    <div style="height: calc(100vh - 10px)" >
      <iframe id="view" :src='iframeUrl'  frameborder="0" width="100%" height="100%"></iframe>
    </div>
  </el-dialog>
</template>

<script>
import {process} from '../../api/service'
import { aliConfig } from '@/utils/aliyunConfig'

const OSS = require('ali-oss');
const client = new OSS(aliConfig);
export default {
  props: {
    processVisible: {
      type: Boolean
    },
    imgJson: {
      type: String
    },
    showFlow: {
      type: Boolean
    },
    uploadType: {
      type: String
    }
  },
  watch: {
    processVisible: function(newVal, oldVal) {
      let _this = this;
      if(newVal) {
        window.addEventListener('message',this.uploadMethod);
        this.$nextTick(()=>{
          let iframe = document.getElementById("view");
          this.iframeUrl = 'http://47.92.109.76:8080/javascript/examples/grapheditor/www/index.html'
          if (iframe.attachEvent){
            iframe.attachEvent("onload", () =>{ // IE
              this.getProcessData();
            });
          } else {
            iframe.onload = ()=>{ // 非IE
              this.getProcessData();
            };
          }
        })
        // this.getProcessData();
      } else {
        window.removeEventListener('message',this.uploadMethod);
      }
    },
    imgJson: function (newVal) {
      this.$nextTick(()=>{
        this.getProcessData()
      });
    },
  },
  data () {
    return {
      iframeUrl:'http://47.92.109.76:8080/javascript/examples/grapheditor/www/index.html',
    }
  },
  mounted () {

  },
  methods: {
    handleClose(done) {
      this.$confirm('关闭之前是否保存', {
        confirmButtonText: '保存',
        cancelButtonText: '关闭',
        type: 'warning',
      }).then(()=> {
        let sendData = {
            type: "callSave",
        };
        document.getElementById('view').contentWindow.postMessage(sendData,'*');
      }).catch(()=>{
        this.$emit('close')
      })
    },
    /**
     * 保存后
     * this.$emit(presentUpload, {json:'123',imgUrl:''})
     */
     getProcessData() {
        let name = "<mxGraphModel dx=\"1416\" dy=\"597\" grid=\"1\" gridSize=\"10\" guides=\"1\" tooltips=\"1\" connect=\"1\" arrows=\"1\" fold=\"1\" page=\"1\" pageScale=\"1\" pageWidth=\"1169\" pageHeight=\"827\"><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/><mxCell id=\"2\" value=\"流程名称\" style=\"rounded=0;whiteSpace=wrap;html=1;fillColor=#6F9C9D;fontColor=#FFFFFF;fontSize=17;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"10\" y=\"20\" width=\"1140\" height=\"40\" as=\"geometry\"/></mxCell><mxCell id=\"3\" value=\"现有流程\" style=\"rounded=0;whiteSpace=wrap;html=1;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;strokeColor=#000000;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"10\" y=\"60\" width=\"1140\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"6\" value=\"角色\" style=\"swimlane;strokeColor=#000000;fillColor=#B4DEDF;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"10\" y=\"120\" width=\"350\" height=\"570\" as=\"geometry\"/></mxCell><mxCell id=\"19\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"7\" target=\"18\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"20\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;exitX=0.5;exitY=1;\" parent=\"6\" source=\"7\" target=\"17\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"7\" value=\"开始\" style=\"ellipse;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FA8556;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"132\" y=\"60\" width=\"76\" height=\"40\" as=\"geometry\"/></mxCell><mxCell id=\"9\" value=\"结束\" style=\"shape=mxgraph.flowchart.terminator;whiteSpace=wrap;html=1;fillColor=#FA8556;strokeColor=#000000;strokeWidth=2;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"134.5\" y=\"510\" width=\"81\" height=\"40\" as=\"geometry\"/></mxCell><mxCell id=\"31\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;entryPerimeter=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"13\" target=\"9\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"13\" value=\"描述\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#99D5D6;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"35\" y=\"370\" width=\"120\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"32\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;entryPerimeter=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"16\" target=\"9\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"16\" value=\"描述\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#00B050;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"200\" y=\"370\" width=\"120\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"23\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"17\" target=\"16\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"24\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;exitX=0.5;exitY=1;\" parent=\"6\" source=\"17\" target=\"13\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"><Array as=\"points\"><mxPoint x=\"260\" y=\"340\"/><mxPoint x=\"95\" y=\"340\"/></Array></mxGeometry></mxCell><mxCell id=\"17\" value=\"判断\" style=\"rhombus;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#00B050;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"220\" y=\"210\" width=\"80\" height=\"45\" as=\"geometry\"/></mxCell><mxCell id=\"21\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"18\" target=\"13\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"25\" value=\"是\" style=\"text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=#ffffff;fontSize=13;fontColor=#1A1A1A;\" parent=\"21\" vertex=\"1\" connectable=\"0\"><mxGeometry x=\"0.2364\" y=\"1\" relative=\"1\" as=\"geometry\"><mxPoint x=\"-16\" y=\"2\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"22\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;exitX=0.5;exitY=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"18\" target=\"16\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"><Array as=\"points\"><mxPoint x=\"95\" y=\"300\"/><mxPoint x=\"260\" y=\"300\"/></Array></mxGeometry></mxCell><mxCell id=\"18\" value=\"判断\" style=\"rhombus;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#99D5D6;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"55\" y=\"210\" width=\"80\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"28\" value=\"否\" style=\"text;html=1;resizable=0;points=[];autosize=1;align=left;verticalAlign=top;spacingTop=-4;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"170\" y=\"283.5\" width=\"30\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"29\" value=\"否\" style=\"text;html=1;resizable=0;points=[];autosize=1;align=left;verticalAlign=top;spacingTop=-4;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"180\" y=\"470\" width=\"30\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"30\" value=\"是\" style=\"text;html=1;resizable=0;points=[];autosize=1;align=left;verticalAlign=top;spacingTop=-4;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"273\" y=\"441\" width=\"30\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"33\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"926\" y=\"490\" width=\"224\" height=\"260\" as=\"geometry\"/></mxCell><mxCell id=\"34\" value=\"图例\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1020\" y=\"505\" width=\"40\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"36\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;dashed=1;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"550\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"37\" value=\"系统自动步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"550\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"38\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"590\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"39\" value=\"人工步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"590\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"40\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#00B050;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"630\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"43\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#99D5D6;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"670\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"47\" value=\"\" style=\"endArrow=block;dashed=1;endFill=0;endSize=12;html=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" edge=\"1\"><mxGeometry width=\"160\" relative=\"1\" as=\"geometry\"><mxPoint x=\"942\" y=\"719\" as=\"sourcePoint\"/><mxPoint x=\"1032\" y=\"719\" as=\"targetPoint\"/></mxGeometry></mxCell><mxCell id=\"48\" value=\"线上步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"630\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"49\" value=\"线下步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"670\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"50\" value=\"系统自动流程\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"710\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell></root></mxGraphModel>";
        let names = "<mxGraphModel dx=\"1416\" dy=\"597\" grid=\"1\" gridSize=\"10\" guides=\"1\" tooltips=\"1\" connect=\"1\" arrows=\"1\" fold=\"1\" page=\"1\" pageScale=\"1\" pageWidth=\"1169\" pageHeight=\"827\"><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/><mxCell id=\"2\" value=\"流程名称\" style=\"rounded=0;whiteSpace=wrap;html=1;fillColor=#6F9C9D;fontColor=#FFFFFF;fontSize=17;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"10\" y=\"20\" width=\"1140\" height=\"40\" as=\"geometry\"/></mxCell><mxCell id=\"3\" value=\"优化后流程\" style=\"rounded=0;whiteSpace=wrap;html=1;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;strokeColor=#000000;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"10\" y=\"60\" width=\"1140\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"6\" value=\"角色\" style=\"swimlane;strokeColor=#000000;fillColor=#B4DEDF;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"10\" y=\"120\" width=\"350\" height=\"570\" as=\"geometry\"/></mxCell><mxCell id=\"19\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"7\" target=\"18\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"20\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;exitX=0.5;exitY=1;\" parent=\"6\" source=\"7\" target=\"17\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"7\" value=\"开始\" style=\"ellipse;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FA8556;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"132\" y=\"60\" width=\"76\" height=\"40\" as=\"geometry\"/></mxCell><mxCell id=\"9\" value=\"结束\" style=\"shape=mxgraph.flowchart.terminator;whiteSpace=wrap;html=1;fillColor=#FA8556;strokeColor=#000000;strokeWidth=2;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"134.5\" y=\"510\" width=\"81\" height=\"40\" as=\"geometry\"/></mxCell><mxCell id=\"31\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;entryPerimeter=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"13\" target=\"9\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"13\" value=\"描述\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#99D5D6;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"35\" y=\"370\" width=\"120\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"32\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;entryPerimeter=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"16\" target=\"9\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"16\" value=\"描述\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#00B050;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"200\" y=\"370\" width=\"120\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"23\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"17\" target=\"16\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"24\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;exitX=0.5;exitY=1;\" parent=\"6\" source=\"17\" target=\"13\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"><Array as=\"points\"><mxPoint x=\"260\" y=\"340\"/><mxPoint x=\"95\" y=\"340\"/></Array></mxGeometry></mxCell><mxCell id=\"17\" value=\"判断\" style=\"rhombus;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#00B050;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"220\" y=\"210\" width=\"80\" height=\"45\" as=\"geometry\"/></mxCell><mxCell id=\"21\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"18\" target=\"13\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"25\" value=\"是\" style=\"text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=#ffffff;fontSize=13;fontColor=#1A1A1A;\" parent=\"21\" vertex=\"1\" connectable=\"0\"><mxGeometry x=\"0.2364\" y=\"1\" relative=\"1\" as=\"geometry\"><mxPoint x=\"-16\" y=\"2\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"22\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;exitX=0.5;exitY=1;entryX=0.5;entryY=0;jettySize=auto;orthogonalLoop=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" source=\"18\" target=\"16\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"><Array as=\"points\"><mxPoint x=\"95\" y=\"300\"/><mxPoint x=\"260\" y=\"300\"/></Array></mxGeometry></mxCell><mxCell id=\"18\" value=\"判断\" style=\"rhombus;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#99D5D6;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"55\" y=\"210\" width=\"80\" height=\"50\" as=\"geometry\"/></mxCell><mxCell id=\"28\" value=\"否\" style=\"text;html=1;resizable=0;points=[];autosize=1;align=left;verticalAlign=top;spacingTop=-4;fontSize=13;fontColor=#1A1A1A;\" parent=\"6\" vertex=\"1\"><mxGeometry x=\"170\" y=\"283.5\" width=\"30\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"29\" value=\"否\" style=\"text;html=1;resizable=0;points=[];autosize=1;align=left;verticalAlign=top;spacingTop=-4;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"180\" y=\"470\" width=\"30\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"30\" value=\"是\" style=\"text;html=1;resizable=0;points=[];autosize=1;align=left;verticalAlign=top;spacingTop=-4;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"273\" y=\"441\" width=\"30\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"33\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"926\" y=\"490\" width=\"224\" height=\"260\" as=\"geometry\"/></mxCell><mxCell id=\"34\" value=\"图例\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1020\" y=\"505\" width=\"40\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"36\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;dashed=1;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"550\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"37\" value=\"系统自动步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"550\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"38\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#FFFFFF;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"590\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"39\" value=\"人工步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"590\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"40\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#00B050;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"630\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"43\" value=\"\" style=\"rounded=0;whiteSpace=wrap;html=1;strokeColor=#000000;fillColor=#99D5D6;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"940\" y=\"670\" width=\"90\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"47\" value=\"\" style=\"endArrow=block;dashed=1;endFill=0;endSize=12;html=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" edge=\"1\"><mxGeometry width=\"160\" relative=\"1\" as=\"geometry\"><mxPoint x=\"942\" y=\"719\" as=\"sourcePoint\"/><mxPoint x=\"1032\" y=\"719\" as=\"targetPoint\"/></mxGeometry></mxCell><mxCell id=\"48\" value=\"线上步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"630\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"49\" value=\"线下步骤\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"670\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell><mxCell id=\"50\" value=\"系统自动流程\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;dashed=1;fontSize=13;fontColor=#1A1A1A;\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"1060\" y=\"710\" width=\"80\" height=\"20\" as=\"geometry\"/></mxCell></root></mxGraphModel>";
        let dataBase = "";

        if (this.uploadType === 'quantitativePicType') {
          dataBase = this.imgJson||name;
        }
        if (this.uploadType === 'systemDockingPicType') {
          dataBase = this.imgJson||names;
        }
        if (!this.uploadType) {
          dataBase = this.imgJson || ""
        }
        let sendData = {
          type: "saveImage",
          data: dataBase,
          name: 'text.xml'
        }
        setTimeout(()=>{
          document.getElementById('view')&&document.getElementById('view').contentWindow.postMessage(sendData,'*');
        }, 400)
     },
     uploadMethod(event) {
      let evenData = event.data;
      if (evenData.type && evenData.type === 'saveJSON') {
        // 拿到保存的JSON串
        this.addProcess(evenData.data,evenData.name,evenData.param,evenData.isClose)
      }
    },
    async addProcess(val,name,params,isClose){
      let param = {};
      param.fileName = name;
      param.xmlData  = val.xml;
      param.width = val.width;
      param.height = val.height;
      param.codeedXmlString = params;
      let res = await process.saveProcessImage(param);
      if(res.code === 0){
        this.$message.success('图片保存成功');
        this.$emit('presentUpload', {json: val.xml, imgUrl: client.signatureUrl(`${res.obj}`, { expires: 80000 })})
        if (isClose) {
          this.$emit('close')
        }
      } else {
        this.$message.error(res.msg);
      }
    },
    closeDialog () {
      this.$emit('close')
    }
  }
}
</script>

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
