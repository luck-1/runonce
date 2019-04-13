<template>
  <el-form ref="presentData" :rules="rules" :model="presentData" class="present-quantitate">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>量化呈现</h2>
    <el-row :gutter="20">
      <el-col :span="12">
        <p>现有流程
          <el-radio-group v-model="presentData.quantitativePicType" size="small" style="float:right;line-height:34px" v-if="!watchPic">
            <el-radio label="1">用户上传</el-radio>
            <el-radio label="2">工具绘制</el-radio>
          </el-radio-group>
        </p>
        <el-form-item prop="renderPicture">
          <div v-if="presentData.renderPicture" v-loading="renderLoading">
            <div class="img-avatar" :class="[ishover?'hover':'',watchPic?'watch-pic':'']" @mouseover="ishover=true" @mouseout="ishover=false">
              <div class="avatar-icon">
                <i class="el-icon-zoom-in" @click.stop="viewRenderPic(presentData.renderPicture)"></i>
                <i class="el-icon-download" @click.stop="downloadPic(presentData.renderPicture)"></i>
                <i class="el-icon-edit" v-if="!watchPic" style="margin: 1rem;" @click.stop="renderPictureClick"></i>
                <i class="el-icon-delete" v-if="!watchPic" @click.stop="deleteRenderPic"></i>
              </div>
              <img :src="presentData.renderPicture" @dblclick="judge('现有流程', 'renderPicture')">
            </div>
          </div>
          <div class="upload" @click="renderPictureClick" v-else v-loading="renderLoading" @dblclick="judge('现有流程', 'renderPicture')"><i class="el-icon-plus"></i></div>
          <el-input style="display: none" v-model="presentData.renderPicture" size="small"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <p>优化后流程
          <el-radio-group v-model="presentData.systemDockingPicType" size="small" style="float:right;line-height:34px" v-if="!watchPic">
            <el-radio label="1">用户上传</el-radio>
            <el-radio label="2">工具绘制</el-radio>
          </el-radio-group>
          <!-- <em class="subtitle">填写说明：按照事项现有办事流程，结合系统建设情况，完成系统对接方案</em> -->
        </p>
        <el-form-item prop="dockingModePicture">
          <div v-if="presentData.dockingModePicture" v-loading="systemLoading">
            <div class="img-avatar" :class="[dockingHover?'hover':'',watchPic?'watch-pic':'']" @mouseover="dockingHover=true" @mouseout="dockingHover=false">
              <div class="avatar-icon">
                <i class="el-icon-zoom-in" @click.stop="viewRenderPic(presentData.dockingModePicture)"></i>
                <i class="el-icon-download" @click.stop="downloadPic(presentData.dockingModePicture)"></i>
                <i class="el-icon-edit" v-if="!watchPic" style="margin: 1rem;" @click.stop="dockingClick"></i>
                <i class="el-icon-delete" v-if="!watchPic" @click.stop="deletePic"></i>
              </div>
              <img :src="presentData.dockingModePicture" @dblclick="judge('优化后流程', 'dockingModePicture')">
            </div>
          </div>
          <div class="upload" @click="dockingClick" v-else v-loading="systemLoading" @dblclick="judge('优化后流程', 'dockingModePicture')"><i class="el-icon-plus"></i></div>
          <el-input style="display: none" v-model="presentData.dockingModePicture" size="small"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <p>优化量化说明</p>
      <el-row>
        <el-col :span="14">
          <el-card class="detail">
            <strong>减材料</strong>
            <div>
              <el-form-item prop="reductionMaterialDetail" @dblclick.native="judge('减材料删减内容', 'reductionMaterialDetail')">
                <Input v-model="presentData.reductionMaterialDetail" :maxlength='2000' size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 8,maxRows: 8}" placeholder="请填写删减内容" />
              </el-form-item>
            </div>
            <div>
              <el-form-item prop="reductionMaterialProblem" @dblclick.native="judge('减材料问题及需核实', 'reductionMaterialProblem')">
                <Input v-model="presentData.reductionMaterialProblem" :maxlength='2000' size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 3,maxRows: 3}" placeholder="请填写问题及需核实的内容" />
              </el-form-item>
              <el-form-item prop="beforeMateriaReduction" @dblclick.native="judge('减材料优化前提交材料个数', 'beforeMateriaReduction')">
                <el-input v-model="presentData.beforeMateriaReduction" size="small" :readonly="watchPic" placeholder="请填写">
                  <template slot="prepend">优化前提交</template>
                  <el-button slot="append">个</el-button>
                </el-input>
              </el-form-item>
              <el-form-item prop="afterReducingMaterial" @dblclick.native="judge('减材料优化后提交材料个数', 'afterReducingMaterial')">
                <el-input v-model="presentData.afterReducingMaterial" size="small" :readonly="watchPic" placeholder="请填写">
                  <template slot="prepend">优化后提交</template>
                  <el-button slot="append">个</el-button>
                </el-input>
              </el-form-item>
            </div>
          </el-card>
          <el-card class="detail">
            <strong>减环节</strong>
            <div>
              <el-form-item prop="reductionLinkDetail" @dblclick.native="judge('减环节删减内容', 'reductionLinkDetail')">
                <Input v-model="presentData.reductionLinkDetail" :maxlength='2000' size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 8,maxRows: 8}" placeholder="请填写删减内容" />
              </el-form-item>
            </div>
            <div>
              <el-form-item prop="reductionLinkProblem" @dblclick.native="judge('减环节问题及需核实', 'reductionLinkProblem')">
                <Input v-model="presentData.reductionLinkProblem" size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 3,maxRows: 3}" placeholder="请填写问题及需核实的内容" />
              </el-form-item>
              <el-form-item prop="beforeLinkReduction" @dblclick.native="judge('减环节优化前所需环节数', 'beforeLinkReduction')">
                <el-input v-model="presentData.beforeLinkReduction" size="small" :readonly="watchPic" placeholder="请填写">
                  <template slot="prepend">优化前需要</template>
                  <el-button slot="append">环节</el-button>
                </el-input>
              </el-form-item>
              <el-form-item prop="afterLinkReduction" @dblclick.native="judge('减环节优化后所需环节数', 'afterLinkReduction')">
                <el-input v-model="presentData.afterLinkReduction" size="small" :readonly="watchPic" placeholder="请填写">
                  <template slot="prepend">优化后需要</template>
                  <el-button slot="append">环节</el-button>
                </el-input>
              </el-form-item>
            </div>
          </el-card>
          <el-card class="detail">
            <strong>减时效</strong>
            <div>
              <el-form-item prop="reductionTimeDetail" @dblclick.native="judge('减时效删减内容', 'reductionTimeDetail')">
                <Input v-model="presentData.reductionTimeDetail" :maxlength='2000' size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 8,maxRows: 8}" placeholder="请填写删减内容" />
              </el-form-item>
            </div>
            <div>
              <el-form-item prop="reductionTimeProblem" @dblclick.native="judge('减时效问题及需核实', 'reductionTimeProblem')">
                <Input v-model="presentData.reductionTimeProblem" size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 3,maxRows: 3}" placeholder="请填写问题及需核实的内容" />
              </el-form-item>
              <el-form-item prop="beforeTimeReduction" @dblclick.native="judge('减时效优化前所需工作日', 'beforeTimeReduction')">
                <el-input v-model="presentData.beforeTimeReduction" size="small" :readonly="watchPic" placeholder="请填写">
                  <template slot="prepend">优化前平均</template>
                  <el-select v-model="presentData.timeUnit" slot="append" placeholder="请选择" class="time-unit">
                    <el-option :label="item.label" :value="item.value" v-for="item in timeUnits" :key="item.value"></el-option>
                  </el-select>
                </el-input>
              </el-form-item>
              <el-form-item prop="afterTimeReduction" @dblclick.native="judge('减时效优化后所需工作日', 'afterTimeReduction')">
                <el-input v-model="presentData.afterTimeReduction" size="small" :readonly="watchPic" placeholder="请填写">
                  <template slot="prepend">优化后平均</template>
                  <el-select v-model="presentData.timeUnit" slot="append" placeholder="请选择" class="time-unit">
                    <el-option :label="item.label" :value="item.value" v-for="item in timeUnits" :key="item.value"></el-option>
                  </el-select>
                </el-input>
              </el-form-item>
            </div>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card class="detail">
            <strong>总体效果</strong>
            <div>
              <el-form-item prop="overallEffectDetail" @dblclick.native="judge('总体效果删减内容', 'overallEffectDetail')">
                <Input v-model="presentData.overallEffectDetail" :maxlength='2000' size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 8,maxRows: 8}" placeholder="请填写删减内容" />
              </el-form-item>
            </div>
            <div>
              <el-form-item prop="overallProblem" @dblclick.native="judge('总体效果问题及需核实', 'overallProblem')">
                <Input v-model="presentData.overallProblem" :maxlength='2000' size="small" type="textarea" :readonly="watchPic" :autosize="{minRows: 3,maxRows: 3}" placeholder="请填写问题及需核实的内容" />
              </el-form-item>
              <div>总体效果 = 减材料、减环节、减时效的平均权重</div>
            </div>
          </el-card>
          <el-card id="charts-des">
          </el-card>
        </el-col>
      </el-row>
    </el-row>
    <el-row>
      <p>描述</p>
      <Input @dblclick.native="judge('描述', 'remarks')" class="textarea" v-model="presentData.remarks" :maxlength='2000' type="textarea" :readonly="watchPic" :autosize="{minRows: 5,maxRows: 10}" placeholder="请输入" />
    </el-row>

    <ImgProcess :processVisible="process" :imgJson="imgJson" :uploadType="uploadType" @presentUpload="presentUpload" @close="process=false"></ImgProcess>

    <!-- 上传图片 -->
    <input type="file" accept='image/jpeg' ref="uploadImg" @change="fileUpload" style="display:none;"/>

    <!-- 图片放大 -->
    <div ref="renderRef" style="display:none">
      <img :src="dataPicture">
    </div>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancelDel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="量化呈现评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>

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
import { present } from '@/api/service'

import { validateNumber, validateMoney } from '@/utils/validate'
import { common } from '@/utils/common'

// 引入基本模板
let echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/bar')
// 引入提示框
require('echarts/lib/component/tooltip')
require('echarts/lib/component/legend')

let chartDest = null
let total = [0,0,0,0]
let totalNew = [0,0,0,0]
let deletes = [0,0,0,0]
let other = [0,0,0,0]
let otherNew = [0,0,0,0]
let max = 0

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
    'itemDetail.id' (val) {
      if(val) {
        this.getList(val)
      }
    },
    'itemInfo.presentQuantitativeState' (val) {
      this.allState(val)
    },
    'presentData.beforeMateriaReduction' (val) {
      let {afterReducingMaterial, beforeLinkReduction, afterLinkReduction, beforeTimeReduction, afterTimeReduction} = this.presentData
      this.calculate({
        beforeMat: val?Number(afterReducingMaterial):0,
        afterMat: afterReducingMaterial?Number(afterReducingMaterial):0,
        beforeLink: beforeLinkReduction?Number(beforeLinkReduction):0,
        aftereLink: afterLinkReduction?Number(afterLinkReduction):0,
        beforeTime: beforeTimeReduction?Number(beforeTimeReduction):0,
        afterTime: afterTimeReduction?Number(afterTimeReduction):0
      })
    },
    'presentData.afterReducingMaterial' (val) {
      let {beforeMateriaReduction, beforeLinkReduction, afterLinkReduction, beforeTimeReduction, afterTimeReduction} = this.presentData
      this.calculate({
        beforeMat: beforeMateriaReduction?Number(beforeMateriaReduction):0,
        afterMat: val?Number(val):0,
        beforeLink: beforeLinkReduction?Number(beforeLinkReduction):0,
        aftereLink: afterLinkReduction?Number(afterLinkReduction):0,
        beforeTime: beforeTimeReduction?Number(beforeTimeReduction):0,
        afterTime: afterTimeReduction?Number(afterTimeReduction):0
      })
    },
    'presentData.beforeLinkReduction' (val) {
      let {beforeMateriaReduction, afterReducingMaterial, afterLinkReduction, beforeTimeReduction, afterTimeReduction} = this.presentData
      this.calculate({
        beforeMat: beforeMateriaReduction?Number(beforeMateriaReduction):0,
        afterMat: afterReducingMaterial?Number(afterReducingMaterial):0,
        beforeLink: val?Number(val):0,
        aftereLink: afterLinkReduction?Number(afterLinkReduction):0,
        beforeTime: beforeTimeReduction?Number(beforeTimeReduction):0,
        afterTime: afterTimeReduction?Number(afterTimeReduction):0
      })
    },
    'presentData.afterLinkReduction' (val) {
      let {beforeMateriaReduction, afterReducingMaterial, beforeLinkReduction, beforeTimeReduction, afterTimeReduction} = this.presentData
      this.calculate({
        beforeMat: beforeMateriaReduction?Number(beforeMateriaReduction):0,
        afterMat: afterReducingMaterial?Number(afterReducingMaterial):0,
        beforeLink: beforeLinkReduction?Number(beforeLinkReduction):0,
        aftereLink: val?Number(val):0,
        beforeTime: beforeTimeReduction?Number(beforeTimeReduction):0,
        afterTime: afterTimeReduction?Number(afterTimeReduction):0
      })
    },
    'presentData.beforeTimeReduction' (val) {
      let {beforeMateriaReduction, afterReducingMaterial, beforeLinkReduction, afterLinkReduction, afterTimeReduction} = this.presentData
      this.calculate({
        beforeMat: beforeMateriaReduction?Number(beforeMateriaReduction):0,
        afterMat: afterReducingMaterial?Number(afterReducingMaterial):0,
        beforeLink: beforeLinkReduction?Number(beforeLinkReduction):0,
        aftereLink: afterLinkReduction?Number(afterLinkReduction):0,
        beforeTime: val?Number(val):0,
        afterTime: afterTimeReduction?Number(afterTimeReduction):0
      })
    },
    'presentData.afterTimeReduction' (val) {
      let {beforeMateriaReduction, afterReducingMaterial, beforeLinkReduction, afterLinkReduction, beforeTimeReduction} = this.presentData
      this.calculate({
        beforeMat: beforeMateriaReduction?Number(beforeMateriaReduction): 0,
        afterMat: afterReducingMaterial?Number(afterReducingMaterial):0,
        beforeLink: beforeLinkReduction?Number(beforeLinkReduction):0,
        aftereLink: afterLinkReduction?Number(afterLinkReduction):0,
        beforeTime: beforeTimeReduction?Number(beforeTimeReduction):0,
        afterTime: val?Number(val):0
      })
    }
  },
  data () {
    const checkNumber = (rule, value, callback) => {
      if (!value) {
        // callback(new Error('不能为空'))
        this.presentData[rule.field] = '0'
      } else if(!validateNumber(value)) {
        // callback(new Error('只能输入整数'))
        this.presentData[rule.field] = '0'
      }
      callback()
    }
    const checkMoney = (rule, value, callback) => {
      if (!value) {
        // callback(new Error('不能为空'))
        this.presentData[rule.field] = '0'
      } else if(!validateMoney(value)) {
        // callback(new Error('最多两位小数'))
        this.presentData[rule.field] = '0'
      }
      callback()
    }
    return {
      lhcxVersion:null,
      // 评审
      judgeShow: false,
      judgeForm: {},
      renderLoading: false,
      systemLoading: false,
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      // 图片上传
      ishover: false,
      dockingHover: false,
      uploadType: '',
      imgJson: '',
      dataPicture: '',
      process: false,
      timeUnits: [{
        label: '工作日',
        value:'工作日'
      }, {
        label: '小时',
        value:'小时'
      }, {
        label: '分钟',
        value:'分钟'
      }],
      presentData: {
        id: null,
        timeUnit:'工作日',
        reductionMaterialDetail: '',
        reductionMaterialProblem: '',
        beforeMateriaReduction: 0,
        afterReducingMaterial: 0,
        reductionLinkDetail: '',
        reductionLinkProblem: '',
        beforeLinkReduction: 0,
        afterLinkReduction: 0,
        reductionTimeDetail: '',
        reductionTimeProblem: '',
        beforeTimeReduction: 0,
        afterTimeReduction: 0,
        overallEffectDetail: '',
        overallProblem: '',
        // 减材料、减环节、减时效的平均权重
        overallEffect: '',
        renderPicture: '',
        quantitativePicType: '1',
        quantitativePicData: '',
        dockingModePicture: '',
        systemDockingPicType: '1',
        systemDockingPicData: '',
        remarks: '',
        state: 1,
        eChartsPicPath: ''
      },
      rules: {
        renderPicture: [
          { required: true, message: '请上传现有流程', trigger: 'change' }
        ],
        dockingModePicture: [
          { required: true, message: '请上传优化后流程', trigger: 'change' }
        ],
        reductionMaterialDetail: [],
        reductionMaterialProblem: [],
        beforeMateriaReduction: [
          { required: true, validator: checkNumber, trigger: 'blur' }
        ],
        afterReducingMaterial: [
          { required: true, validator: checkNumber, trigger: 'blur' }
        ],
        reductionLinkDetail: [],
        reductionLinkProblem: [],
        beforeLinkReduction: [
          { required: true, validator: checkNumber, trigger: 'blur' }
        ],
        afterLinkReduction: [
          { required: true, validator: checkNumber, trigger: 'blur' }
        ],
        reductionTimeDetail: [],
        reductionTimeProblem: [],
        beforeTimeReduction: [
          { required: true, validator: checkMoney, trigger: 'blur' }
        ],
        afterTimeReduction: [
          { required: true, validator: checkMoney, trigger: 'blur' }
        ],
        overallEffectDetail: [],
        overallProblem: []
      },
      // 删除
      showDel: false,
      currentType: ''
    }
  },
  computed: {
    ...mapState([
      'itemDetail'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.presentQuantitativeState)
      return itemDetail
    }
  },
  mounted () {
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
    this.drawLine()
    if (chartDest) {
      this.windowResize()
    }
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
          sheetNum: 9,
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
    windowResize () {
      chartDest.resize()
      window.onresize = function() {
        chartDest.resize()
      }
      window.onload = function() {
        chartDest.resize()
      }
    },
    calculate ({beforeMat,afterMat,beforeLink,aftereLink,beforeTime,afterTime}) {
      let sub = (beforeMat+beforeLink+beforeTime) - (afterMat+aftereLink+afterTime)
      total = [beforeMat, beforeLink, beforeTime, this.twoDecimal((beforeMat+beforeLink+beforeTime)/3)]
      max = Math.max.apply(null,total)
      totalNew = [total[0]!==0?max:0, total[1]!==0?max:0, total[2]!==0?max:0, total[3]!==0?max:0]
      other = [afterMat,aftereLink,afterTime,this.twoDecimal((afterMat+aftereLink+afterTime)/3)]
      deletes = [beforeMat-afterMat,beforeLink-aftereLink,beforeTime-afterTime,this.twoDecimal(sub/3)]
      total.forEach((item, index) => {
        switch (index) {
          case 0:
            otherNew[index] = item!==0?this.twoDecimal(afterMat/item*max):0
            break
          case 1:
            otherNew[index] = item!==0?this.twoDecimal(aftereLink/item*max):0
            break
          case 2:
            otherNew[index] = item!==0?this.twoDecimal(afterTime/item*max):0
            break
          case 3:
            otherNew[index] = item!==0?this.twoDecimal(other[3]/item*max):0
            break
        }
      })
      this.drawLine()
    },
    twoDecimal(val){
      return Math.round(val*10)/10
      // return Math.round(val)
    },
    renderPictureClick () {
      if (this.watchPic||this.systemLoading) {
        return
      }
      let type = this.presentData.quantitativePicType
      if(type === '1') {
        this.$refs.uploadImg.setAttribute('type', 'file')
        this.$refs.uploadImg.click()
      } else {
        this.process = true
      }
      this.imgJson = this.presentData.quantitativePicData
      this.uploadType = 'quantitativePicType'
    },
    dockingClick () {
      if(this.watchPic||this.renderLoading) {
        return
      }
      let type = this.presentData.systemDockingPicType
      if(type === '1') {
        this.$refs.uploadImg.setAttribute('type', 'file')
        this.$refs.uploadImg.click()
      } else {
        this.process = true
      }
      this.imgJson = this.presentData.systemDockingPicData
      this.uploadType = 'systemDockingPicType'
    },
    fileUpload (e) {
      let files = e.target.files
      let fileName = `${new Date().getTime()}.${files[0].name.split('.')[files[0].name.split('.').length-1]}`
      let validate = '.jpg,.jpeg,.gif,.png'.split(',').join('|\\')
      if(!new RegExp(`\\w(\\${validate})`,'gim').test(fileName)) {
        this.$message.error(`只能上传.jpg,.jpeg,.gif,.png格式`)
        this.renderLoading = false
        this.systemLoading = false
        return
      }
      if (this.uploadType==='quantitativePicType') {
        this.renderLoading = true
      } else {
        this.systemLoading = true
      }
      let storeAs = `${new Date().toJSON().split('T')[0]}/${fileName}`;
      let results = client.put(storeAs, files[0]).then((results) => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        // let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(signUrl)
        if (this.uploadType==='quantitativePicType') {
          this.presentData.renderPicture = signUrl
          this.renderLoading = false
        } else {
          this.presentData.dockingModePicture = signUrl
          this.systemLoading = false
        }
      }).catch(err => {
				if (this.uploadType==='quantitativePicType') {
          this.presentData.renderPicture = ''
          this.renderLoading = false
        } else {
          this.presentData.dockingModePicture = ''
          this.systemLoading = false
        }
			})
      this.$refs.uploadImg.removeAttribute('type')
    },
    // 流程图保存图片回显
    presentUpload ({json, imgUrl}) {
      if (this.uploadType==='quantitativePicType') {
        this.presentData.quantitativePicData = json
        this.presentData.renderPicture = imgUrl
      } else {
        this.presentData.systemDockingPicData = json
        this.presentData.dockingModePicture = imgUrl
      }
    },
    // 获取页面数据
    async getList (id) {
      let res = await present.getByEventId(id)
      this.lhcxVersion=null
      this.$refs.presentData.resetFields()
      this.presentData.remarks = ''
      this.presentData.id = null
      if (res.code === 0) {
        if (res.obj) {
          let datas = res.obj.quantitativeRendering
          if(datas) {
            datas.renderPicture = datas.renderPicture?client.signatureUrl(datas.renderPicture, { expires: 80000 }):''
            datas.dockingModePicture = datas.dockingModePicture?client.signatureUrl(datas.dockingModePicture, { expires: 80000 }):''
            datas.timeUnit = datas.timeUnit||'工作日'
            this.presentData = datas
            this.calculate({
              beforeMat: this.presentData.beforeMateriaReduction||0,
              afterMat: this.presentData.afterReducingMaterial||0,
              beforeLink: this.presentData.beforeLinkReduction||0,
              aftereLink: this.presentData.afterLinkReduction||0,
              beforeTime: this.presentData.beforeTimeReduction||0,
              afterTime: this.presentData.afterTimeReduction||0,
            })
          }
          this.lhcxVersion=res.obj.version;
        }
      }
    },
    persentImg (img) {
      this.presentData.renderPicture = img
    },
    // 查看图片，放大图片操作
    viewRenderPic (url) {
      this.dataPicture = url
      global.thisView &&　global.thisView.destroy();
      global.thisView = new Viewer(this.$refs.renderRef)
      global.thisView.show()
    },
    // 下载图片
    downloadPic (url) {
      common.download(`${this.itemInfo.eventName}办理流程图`, url)
    },
    deleteRenderPic () {
      this.showDel = true
      this.currentType = 'renderPic'
    },
    uploadImg (img) {
      this.presentData.dockingModePicture = img
    },
    deletePic () {
      this.showDel = true
      this.currentType = 'picture'
    },
    cancelDel() {
      this.showDel = false
    },
    del () {
      if(this.currentType === 'renderPic') {
        this.presentData.renderPicture = ''
        this.presentData.quantitativePicData = ''
        // this.presentData.quantitativePicType = ''
      } else {
        this.presentData.dockingModePicture = ''
        this.presentData.systemDockingPicData = ''
        // this.presentData.systemDockingPicType = ''
      }
      this.showDel = false
    },
    drawLine () {
      // 基于准备好的dom，初始化echarts实例
      chartDest = echarts.init(document.getElementById('charts-des'))
      // 绘制图表
      chartDest.setOption({
        tooltip: {
          trigger: 'item',
          axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: function (params) {
            let num = 0
            if(params.seriesName === '未删减') {
              num = total[params.dataIndex] - deletes[params.dataIndex]
            } else {
              num = deletes[params.dataIndex]
            }
            return `${params.name} <br/>${params.seriesName}: ${num}`
          }
        },
        legend: {
          data: ['未删减', '删减']
        },
        grid: {
          // left: '3%',
          // right: '4%',
          // bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          axisTick: {
            show: false
          },
          data: ['减材料', '减环节','减时效', '总体效果']
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [{
          name: '删减',
          type: 'bar',
          barWidth: 40,
          barGap: '-100%',
          itemStyle: {
            // color: '#88e7da'
          },
          label: {
            normal: {
              show: true,
              position:'insideTop',
              formatter: function (params) {
                if (total[params.dataIndex]!==0) {
                  return (deletes[params.dataIndex] / total[params.dataIndex] * 100).toFixed(1) + ' %'
                } else {
                  return 0
                }
              },
            }
          },
          data: totalNew
        }, {
          name: '未删减',
          type: 'bar',
          barWidth: 40,
          itemStyle: {
            // color: '#5dd0c0'
          },
          z: 10,
          label: {
            normal: {
              show: true,
              position:'insideBottom',
              formatter: function (params) {
                if (total[params.dataIndex]!==0) {
                  return ((total[params.dataIndex] - deletes[params.dataIndex]) / total[params.dataIndex] * 100).toFixed(1) + ' %'
                } else {
                  return 0
                }
              },
            }
          },
          data: otherNew
        }]
      });
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
    submitThisForm (state) {
      let { renderPicture, dockingModePicture, remarks, eChartsPicPath } = this.presentData
      let src = chartDest.getDataURL({
        // pixelRatio: 2, // 导出的图片分辨率比例，默认1
          backgroundColor: '#fff'
      })
      let storeAs = `${new Date().toJSON().split('T')[0]}/${new Date().getTime()}.jpeg`
      let file = common.dataURLtoFile(src, storeAs)

      let results = client.put(storeAs, file).then((results) => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 })
        eChartsPicPath = storeAs
      }).then( async () => {
        let res = await present.add({
          quantitativeRendering: {
            ...this.presentData,
            renderPicture: this.fileName(renderPicture),
            dockingModePicture: this.fileName(dockingModePicture),
            eventId: this.itemInfo.id,
            remarks,
            eChartsPicPath,
            state
          },
          version:this.lhcxVersion
        })
        if (res.code === 0) {
          this.itemInfo.presentQuantitativeState = state
          localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
          this.SET_ITEMDETAIL({...this.itemInfo, presentQuantitativeState: state})
          this.$message.success(res.msg)
          this.lhcxVersion=null;
          this.getList(this.itemInfo.id);
        }
      })
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
  max-height: 400px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  &:hover {
    border-color: #409EFF;
  }
  i {
    font-size: 28px;
    line-height: 400px;
    color: #8c939d;
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
.time-unit {
  width: 90px;
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
.detail /deep/ .el-card__body:nth-child(2)>strong {
  background-color: burlywood;
}
#charts-des {
  height: 386px;
}
</style>
