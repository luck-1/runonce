/**
 * 名称：材料分组
 * 作者：王晓妮
 * 日期：2018-11-6
 */
<template>
  <div style="height: 100%;position: relative;">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>材料分组</h2>
    <el-form :model="thisForm" ref="thisForm" :rules="rules" label-width="0" :style="{height: watchPic?'calc(100% - 45px)':'calc(100% - 105px)'}">
      <el-table ref="material" height="100%" class="material" row-key="index" size="mini" :data="thisForm.datas" border style="width: 100%" highlight-current-row
        @cell-dblclick="judge" @row-click="rowClick">
        <el-table-column label="序号" align="center" width="55" fixed="left">
          <template slot-scope="{row,$index}">
            <div class="my-handle">{{$index + 1}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类别" width="110" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.type'" :rules='rules.type' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" v-model="row.type" placeholder="请选择">
                <el-option label="证件" value="证件"></el-option>
                <el-option label="证明" value="证明"></el-option>
                <el-option label="申请表" value="申请表"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.type}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="材料编号" width="120" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.number'" :rules='rules.number' v-if="!watchPic&&row.isUpdate">
              <el-input size="mini" placeholder="请输入内容" v-model="row.number"></el-input>
            </el-form-item>
            <div v-else>{{row.number}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="io" label="输入(I)/输出(O)" width="115" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.io'" :rules='rules.io' v-if="!watchPic&&row.isUpdate" @change="ioChange(row.io. $index)">
              <el-radio-group size="mini" v-model="row.io">
                <el-radio label="I">I</el-radio>
                <el-radio label="O">O</el-radio>
                <el-radio label="IO" class="last-radio">IO</el-radio>
              </el-radio-group>
            </el-form-item>
            <div v-else>{{row.io}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="材料名称" width="150" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.name'" :rules='rules.name' v-if="!watchPic&&row.isUpdate">
              <el-input size="mini" placeholder="请输入内容" v-model="row.name"></el-input>
            </el-form-item>
            <div v-else>{{row.name}}</div>
          </template>
        </el-table-column>
        <el-table-column label="窗口提交材料要求" align="center">
          <el-table-column prop="originalScriptProvide" label="原件提供" width="55">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.originalScriptProvide'" :rules='rules.originalScriptProvide' v-if="!watchPic&&row.isUpdate">
                <el-switch size="mini" v-model="row.originalScriptProvide" active-color="#13ce66" inactive-color="#ff4949" @change="originalSPChange(row.originalScriptProvide, $index)"></el-switch>
              </el-form-item>
              <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.originalScriptProvide"></i>
            </template>
          </el-table-column>
          <el-table-column prop="copyProvide" label="复印件提供" align="center" width="60">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.copyProvide'" :rules='rules.copyProvide' v-if="!watchPic&&row.isUpdate">
                <el-switch size="mini" v-model="row.copyProvide" active-color="#13ce66" inactive-color="#ff4949" @change="copyChange(row.copyProvide, $index)"></el-switch>
              </el-form-item>
              <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.copyProvide"></i>
            </template>
          </el-table-column>
          <el-table-column prop="electronicProvide" label="电子件提供" align="center" width="60">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.electronicProvide'" :rules='rules.electronicProvide' v-if="!watchPic&&row.isUpdate">
                <el-switch size="mini" v-model="row.electronicProvide" active-color="#13ce66" inactive-color="#ff4949" @change="electronicChange(row.electronicProvide, $index)"></el-switch>
              </el-form-item>
              <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.electronicProvide"></i>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="审批输出物">
          <el-table-column prop="originalScriptOutput" label="原件" align="center" width="55">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.originalScriptOutput'" :rules='rules.originalScriptOutput' v-if="!watchPic&&row.isUpdate">
                <el-switch size="mini" v-model="row.originalScriptOutput" active-color="#13ce66" inactive-color="#ff4949" @change="originalChange(row.originalScriptOutput, $index)"></el-switch>
              </el-form-item>
              <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.originalScriptOutput"></i>
            </template>
          </el-table-column>
          <el-table-column prop="electronicOutput" label="电子件" align="center" width="55">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.electronicOutput'" :rules='rules.electronicOutput' v-if="!watchPic&&row.isUpdate">
                <el-switch size="mini" v-model="row.electronicOutput" active-color="#13ce66" inactive-color="#ff4949" @change="electronicOPChange(row.electronicOutput, $index)"></el-switch>
              </el-form-item>
              <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.electronicOutput"></i>
            </template>
          </el-table-column>
        </el-table-column>
        <!-- <el-table-column label="材料要求"> -->
          <el-table-column prop="count" label="份数" align="center">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.count'" :rules='rules.count' v-if="!watchPic&&row.isUpdate">
                <el-input-number :disabled="!row.originalScriptProvide&&!row.copyProvide&&!row.electronicProvide&&!row.originalScriptOutput&&!row.electronicOutput"
                  :controls="false" size="mini" v-model="row.count" controls-position="right" :min="1"></el-input-number>
              </el-form-item>
              <div v-else>{{row.count}}</div>
            </template>
          </el-table-column>
        <!-- </el-table-column> -->
        <el-table-column prop="provideWay" label="材料提供方式" width="120" align="center">
          <template slot="header" slot-scope="scope">
            材料提供方式<Icon style="margin-top: -13px;margin-left: 5px;cursor: pointer;" type="md-help-circle" title="系统集成：材料在系统中提交流转
线下共享：材料涉及多个部门，由政府工作人员跑腿提供
自行提供：材料由申请人自己提供"/>
          </template>
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.provideWay'" :rules='rules.provideWay' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" v-model="row.provideWay" placeholder="请选择" clearable :disabled="row.io==='O'">
                <el-option label="系统集成" value="系统集成"></el-option>
                <el-option label="线下共享" value="线下共享"></el-option>
                <el-option label="自行提供" value="自行提供"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.provideWay}}</div>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="canBeShortOf" label="是否可缺少" width="115" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.canBeShortOf'" :rules='rules.canBeShortOf'>
              <el-switch size="mini" v-model="row.canBeShortOf" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </template>
        </el-table-column> -->
        <el-table-column prop="isItIndispensable" label="是否可容缺" align="center" width="55">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.isItIndispensable'" :rules='rules.isItIndispensable' v-if="!watchPic&&row.isUpdate">
              <el-switch size="mini" v-model="row.isItIndispensable" active-color="#13ce66" inactive-color="#ff4949" @change="originalChange(row.isItIndispensable, $index)" :disabled="row.io==='O'"></el-switch>
            </el-form-item>
            <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.isItIndispensable"></i>
          </template>
        </el-table-column>
        <el-table-column prop="preAcceptance" width="110" align="center" label="预受理">
          <template slot="header" slot-scope="scope">
            预受理<Icon style="margin-top: -13px;margin-left: 5px;cursor: pointer;" type="md-help-circle" title="人工：事项受理前由经办人检查申请材料是否齐全
自动：事项受理前由系统自动检查申请材料是否齐全"/>
          </template>
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.preAcceptance'" :rules='rules.preAcceptance' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" v-model="row.preAcceptance" placeholder="请选择" clearable :disabled="row.io==='O'">
                <el-option label="人工" value="人工"></el-option>
                <el-option label="自动" value="自动"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.preAcceptance}}</div>
          </template>
        </el-table-column>
        <!-- <el-table-column label="材料来源" align="center">
          <el-table-column prop="source" label="职能部门/自制" width="120" align="center">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.source'" :rules='rules.source'>
                <el-input size="mini" placeholder="请输入内容" v-model="row.source"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="departmrntName" label="职能部门名称" width="120" align="center">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.departmrntName'" :rules='rules.departmrntName'>
                <el-input size="mini" placeholder="请输入内容" v-model="row.departmrntName"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="departmrntSystemName" label="职能部门系统名称" width="120" align="center">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.departmrntSystemName'" :rules='rules.departmrntSystemName'>
                <el-input size="mini" placeholder="请输入内容" v-model="row.departmrntSystemName"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="departmrntSystemUrl" label="职能部门系统网址" width="120" align="center">
            <template slot-scope="{row,$index}">
              <el-form-item :prop="'datas.' + $index + '.departmrntSystemUrl'" :rules='rules.departmrntSystemUrl'>
                <el-input size="mini" placeholder="请输入内容" v-model="row.departmrntSystemUrl"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
        </el-table-column> -->
        <el-table-column prop="remarks" label="备注说明" width="150" align="center" ref="remarks">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.remarks'" :rules='rules.remarks' v-if="!watchPic&&row.isUpdate">
              <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="row.remarks"/>
            </el-form-item>
            <div v-else>{{row.remarks}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="sampleTable" label="样表（电子版本/图片）" width="180" ref="sampleTable">
          <template slot-scope="{row}">
            <ImgUpload v-if="row.sampleTable" @webFileFun="webFileFun($event,row)" :webFiles="copyObj?(copyObj.objectDataMap[2]||[]):[]" size="mini" accept=".jpg,.jpeg,.pdf" :watchPic="watchPic||!row.isUpdate" :files="row.sampleTable?JSON.parse(row.sampleTable).imgs:[]" :imageNum='40' :multiple="true"
            @upload="uploadImgs(row, $event)" @heigthChange="heigthChange"></ImgUpload>
          </template>
        </el-table-column>
        <el-table-column prop="isStreamlining" label="是否精简" width="80" ref="isStreamlining">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.isStreamlining'" :rules='rules.isStreamlining' v-if="!watchPic&&row.isUpdate">
              <el-switch :disabled="row.io==='O'" size="mini" v-model="row.isStreamlining" active-color="#13ce66" inactive-color="#ff4949" @change="streamChange(row.isStreamlining, $index)"></el-switch>
            </el-form-item>
            <div v-else>{{row.isStreamlining?'是':'否'}}</div>
            <!-- <i class="el-icon-check checked" v-if="watchPic&&row.isStreamlining"></i> -->
          </template>
        </el-table-column>
        <el-table-column prop="cancelMerge" label="精简方式" align="center" width="130" ref="cancelMerge">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.cancelMerge'" :rules='rules.cancelMerge' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" :disabled="!row.isStreamlining||row.io==='O'" v-model="row.cancelMerge" placeholder="请选择" clearable :title="row.cancelMerge">
                <el-option label="可通过数据共享进行减免" value="可通过数据共享进行减免"></el-option>
                <el-option label="通过政策依据进行取消/合并" value="通过政策依据进行取消/合并"></el-option>
                <el-option label="可通过业务优化进行取消/合并" value="可通过业务优化进行取消/合并"></el-option>
                <el-option label="其它（用户自行创建）" value="其它（用户自行创建）"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.cancelMerge}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="belongUnit" width="135" ref="belongUnit" label="归集单位 / 精简备注">
          <template slot="header" slot-scope="scope">
            归集单位 / 精简备注
          </template>
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.belongUnit'" :rules='rules.belongUnit' v-if="!watchPic&&row.isUpdate">
              <el-input :disabled="!row.isStreamlining||row.io==='O'" size="mini" :placeholder="row.cancelMerge==='可通过数据共享进行减免'?'请输入归集单位':'请输入精简备注'" v-model="row.belongUnit"></el-input>
            </el-form-item>
            <div v-else>{{row.belongUnit}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="receivePersonReview" label="受理人审查" width="65" ref="receivePersonReview">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.receivePersonReview'" :rules='rules.receivePersonReview' v-if="!watchPic&&row.isUpdate">
              <el-switch :disabled="row.io==='O'" size="mini" v-model="row.receivePersonReview" active-color="#13ce66" inactive-color="#ff4949" @change="receiveChange(row.receivePersonReview, $index)"></el-switch>
            </el-form-item>
            <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.receivePersonReview"></i>
          </template>
        </el-table-column>
        <el-table-column prop="receivePersonReviewWay" label="受理人审查方式" width="140" ref="receivePersonReviewWay">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.receivePersonReviewWay'" :rules='rules.receivePersonReviewWay' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" :disabled="!row.receivePersonReview||row.io==='O'" v-model="row.receivePersonReviewWay" placeholder="请选择" filterable clearable>
                <el-option label="系统自动审查" value="系统自动审查"></el-option>
                <el-option label="窗口人员人工审查" value="窗口人员人工审查"></el-option>
                <el-option label="审批人员人工审查" value="审批人员人工审查"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.receivePersonReviewWay}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="approverReview" label="审批人审查" width="65" ref="approverReview">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.approverReview'" :rules='rules.approverReview' v-if="!watchPic&&row.isUpdate">
              <el-switch :disabled="row.io==='O'" size="mini" v-model="row.approverReview" active-color="#13ce66" inactive-color="#ff4949" @change="approverChange(row.approverReview, $index)"></el-switch>
            </el-form-item>
            <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.approverReview"></i>
          </template>
        </el-table-column>
        <el-table-column prop="approverReviewWay" label="审批人审查方式" width="140" ref="approverReviewWay">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.approverReviewWay'" :rules='rules.approverReviewWay' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" :disabled="!row.approverReview||row.io==='O'" v-model="row.approverReviewWay" placeholder="请选择" filterable clearable>
                <el-option label="系统自动审查" value="系统自动审查"></el-option>
                <el-option label="窗口人员人工审查" value="窗口人员人工审查"></el-option>
                <el-option label="审批人员人工审查" value="审批人员人工审查"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.approverReviewWay}}</div>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="emptyTable" label="空表（电子版本/图片）" width="180" ref="emptyTable">
          <template slot-scope="{row,$index}">
            <ImgUpload size="mini" :files="row.emptyTable?JSON.parse(row.emptyTable).imgs:[]" :imageNum='40' :multiple="true" @upload="uploadImg(row, $event)" @heigthChange="heigthChange"></ImgUpload>
          </template>
        </el-table-column> -->
        <el-table-column fixed="right" width="100px" label="操作" align="center">
          <template slot-scope="scope">
            <div class="table-row-btn">
              <el-tooltip content="删除" placement="top" v-if="!watchPic">
                <Button @click="handleDelete(scope.$index, scope.row)" type="error" icon="md-trash" size="small"></Button>
              </el-tooltip>
              <el-tooltip content="审查细则" placement="top" v-if="scope.row.io!=='O'">
                <Button @click="checkList(scope.$index, scope.row)" type="primary" icon="ios-book" size="small"></Button>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="add-row" style="width: 99.2%;" @click="addMasterUser()" v-if="!watchPic"><span>+ 添加</span></div>

      <div class="btn-submit" @click="formSubmit('thisForm')" v-if="!watchPic">保存</div>
    </el-form>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="材料分组评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>

    <AuditRule :title="auditTitle" :itemSee="itemSee" :watchPic="watchPic" :imgs="imgs" :currentRow="thisForm.datas.length?thisForm.datas[currentIndex]:{}" :auditForm="auditForm" :auditModel="auditModel" @cancel="auditCancel"></AuditRule>
  </div>
</template>

<script>
import { aliConfig } from '@/utils/aliyunConfig'
const OSS = require('ali-oss');
const client = new OSS(aliConfig);

import {mapState, mapMutations, mapActions} from 'vuex'

import Sortable from 'sortablejs'
import ImgUpload from '@/components/uploadPicture'
import JudgeDialog from '@/components/judgeDialog'
import DeleteDialog from '@/components/dialog.vue'
import AuditRule from './auditRules.vue'

import { material, auditRule } from '@/api/service'

import { validateMaterialNum, validatSpace, validatorSpace } from '@/utils/validate'
export default {
  name: 'material',
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
    }
  },
  watch: {
    'copyObj.materialGroupVersionParam'(value) {
      if(value) {
        this.clfzVersion = value.version||null
        let data = value.list===null?[]:value.list
        data.forEach(item => {
          item.sampleTable = item.sampleTable||'{"imgs":[]}'
        })
        this.thisForm.datas = data
      }
    },
    state: function(newVal, oldVal) {
      if(newVal) {
        // this.getList(this.itemInfo.id)
      }
    },
    'itemDetail.id' (val) {
      if(val) {
        this.getList(val)
      }
    },
    'itemInfo.materialGroupingState' (val) {
      this.allState(val)
    }
  },
  components: { ImgUpload, JudgeDialog, DeleteDialog, AuditRule },
  data () {
    const checkNumber = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (!validateMaterialNum(value)) {
        callback(new Error('格式不正确'))
      }
      callback()
    }
    const checkName = (rule, value, callback) => {
      let fields = rule.field.split('.')
      // if(fields[2]==='belongUnit') {
      //   if(this.thisForm.datas[fields[1]].cancelMerge==='可通过数据共享进行减免') {
      //     if (!validatorSpace(value)) {
      //       callback(new Error('不能为空'))
      //     }
      //     callback()
      //   } else {
      //     callback()
      //   }
      // } else {
        if (!validatorSpace(value)) {
          callback(new Error('不能为空'))
        }
        callback()
      // }
    }
    return {
      clfzVersion:null,
      // 评审
      judgeShow: false,
      judgeForm: {},
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      // 删除
      showDel: false,
      currentIndex: 0,
      // 审核细则
      auditTitle: '',
      imgs: [],
      auditForm: {},
      auditModel: false,
      thisForm: {
        datas: []
      },
      rules: {
        type: [{ required: true, message: '不能为空', trigger: 'change' }],
        number: [{ required: true, validator: checkNumber, trigger: 'blur' }],
        io: [],
        name: [{ required: true, validator: checkName, trigger: 'blur' }],
        originalScriptProvide: [],
        copyProvide: [],
        electronicProvide: [],
        originalScriptOutput: [],
        electronicOutput: [],
        count: [],
        provideWay: [],
        source: [],
        departmrntName: [],
        departmrntSystemName: [],
        departmrntSystemUrl: [],
        isStreamlining: [],
        remarks: [],
        belongUnit: [
          // { validator: checkName, trigger: 'blur' }
        ],
        cancelMerge: [],
        receivePersonReview: [],
        approverReview: [],
        receivePersonReviewWay: [],
        approverReviewWay: []
      }
    }
  },
  computed: {
    ...mapState([
      'itemDetail', 'copyObj'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.materialGroupingState)
      return itemDetail
    }
  },
  mounted () {
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
    this.getList(this.itemInfo.id)
    this.rowDrop()
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
    webFileFun(e, row) {
      row.sampleTable = e
    },
    async getList (id) {
      this.thisForm.datas = []
      let res = await material.getByEventId(id)
      if (res.code === 0) {
        this.clfzVersion=res.obj.version;
        let data = res.obj.list ===null ? [] :res.obj.list
        data.forEach(item => {
          item.isUpdate = false
          item.sampleTable = item.sampleTable||'{"imgs":[]}'
        })
        this.thisForm.datas = data
      }
    },
    // 评审
    judge (row, column, cell, event) {
      let endTime = this.itemInfo.examineEndTime
      if(endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          id: this.itemInfo.id,
          sheetNum: 2,
          lineNumber: Number(row.orderNum),
          propertyName: column.label,
          property: column.property,
          questionContent: ''
        }
      }
    },
    judgeCancel () {
      this.judgeShow = false
    },
    rowClick (row, column, cell, event) {
      this.thisForm.datas.map(item => item.isUpdate = false)
      row.isUpdate = true
    },
    ioChange (val, index) {
      let row = this.thisForm.datas[index]
      if (val === 'O') {
        this.thisForm.datas[index].provideWay = ''
        this.thisForm.datas[index].isItIndispensable = false
        this.thisForm.datas[index].preAcceptance = ''
        this.thisForm.datas[index].isStreamlining = false
        this.thisForm.datas[index].cancelMerge = ''
        this.thisForm.datas[index].belongUnit = ''
        this.thisForm.datas[index].receivePersonReview = false
        this.thisForm.datas[index].receivePersonReviewWay = ''
        this.thisForm.datas[index].approverReview = false
        this.thisForm.datas[index].approverReviewWay = ''
      }
    },
    // 原件提供改变(窗口)
    originalSPChange (val, index) {
      let row = this.thisForm.datas[index]
      if (!val&&!row.copyProvide&&!row.electronicProvide&&!row.originalScriptOutput&&!row.electronicOutput) {
        this.thisForm.datas[index].count = ''
      }
    },
    // 复印件提供改变(窗口)
    copyChange (val, index) {
      let row = this.thisForm.datas[index]
      if (!val&&!row.originalScriptProvide&&!row.electronicProvide&&!row.originalScriptOutput&&!row.electronicOutput) {
        this.thisForm.datas[index].count = ''
      }
    },
    // 电子件提供改变(窗口)
    electronicChange (val, index) {
      let row = this.thisForm.datas[index]
      if (!val&&!row.originalScriptProvide&&!row.copyProvide&&!row.originalScriptOutput&&!row.electronicOutput) {
        this.thisForm.datas[index].count = ''
      }
    },
    // 原件提供改变(输出物)
    originalChange (val, index) {
      let row = this.thisForm.datas[index]
      if (!val&&!row.originalScriptProvide&&!row.copyProvide&&!row.electronicProvide&&!row.electronicOutput) {
        this.thisForm.datas[index].count = ''
      }
    },
    // 电子件提供改变(输出物)
    electronicOPChange (val, index) {
      let row = this.thisForm.datas[index]
      if (!val&&!row.originalScriptProvide&&!row.copyProvide&&!row.electronicProvide&&!row.originalScriptOutput) {
        this.thisForm.datas[index].count = ''
      }
    },
    // 是否精简改变
    streamChange (val, index) {
      if (!val) {
        this.thisForm.datas[index].cancelMerge = ''
        this.thisForm.datas[index].belongUnit = ''
      }
    },
    // 受理人审查方式改变
    receiveChange(val, index) {
      if (!val) {
        this.thisForm.datas[index].receivePersonReviewWay = ''
      }
    },
    approverChange(val, index) {
      if (!val) {
        this.thisForm.datas[index].approverReviewWay = ''
      }
    },
    // 审批人审查方式改变
    //行拖拽
    rowDrop() {
      const tbody = document.querySelector('.material .el-table__body-wrapper tbody')
      Sortable.create(tbody, {
        handle: '.my-handle'
      })
    },
    // 添加
    addMasterUser () {
      this.thisForm.datas.map(item => item.isUpdate = false)
      this.thisForm.datas.push({
        id: null,
        eventId: this.itemInfo.id,
        type: '',
        number: '',
        io: 'I',
        name: '',
        originalScriptProvide: false,
        copyProvide: false,
        electronicProvide: false,
        originalScriptOutput: false,
        electronicOutput: false,
        count: 1,
        isItIndispensable: false,
        provideWay: '',
        preAcceptance: '',
        canBeShortOf: false,
        source: '',
        departmrntName: '',
        departmrntSystemName:'',
        remarks: '',
        sampleTable: '{"imgs":[]}',
        emptyTable: '',
        acceptance: [],
        approval: [],
        isStreamlining: false,
        belongUnit: '',
        cancelMerge: '',
        receivePersonReview: false,
        receivePersonReviewWay: '',
        approverReview: false,
        approverReviewWay: '',
        isUpdate: true
      })
    },

    // 上传图片
    uploadImgs (row, imgs) {
      row.sampleTable = imgs
    },
    heigthChange() {
      this.$nextTick(() => {
        let currentRow = document.getElementsByClassName('current-row')
        let height = currentRow.length?currentRow[0].offsetHeight:0
        for(let x of currentRow) x.style.height = height+'px'
        this.$refs.material.doLayout()
        // currentRow.map(x => {return x.style.height = height+'px'})
      })
    },
    uploadImg (row, imgs) {
      row.emptyTable = imgs
    },

    // 判断行提交空数据
    rowIsSpace (row) {
      let spaceLength = 0
      for (let key in row) {
        if (!row[key] && (key === 'type' || key === 'number' || key === 'name' || (row.cancelMerge==='可通过数据共享进行减免'&&key === 'belongUnit'))) {
          spaceLength += 1
        }
      }
      return (spaceLength===3||spaceLength===4)
    },
    // 删除
    handleDelete (index, row) {
      this.showDel = true
      this.currentIndex = index
    },
    cancel () {
      this.showDel = false
    },
    del () {
      this.thisForm.datas.splice(this.currentIndex, 1)
      let current = JSON.parse(JSON.stringify(this.thisForm.datas))
      this.thisForm.datas = []
      this.$nextTick(() => {
        this.thisForm.datas = current
      })
      this.showDel = false
    },
    // 审核细则
    async checkList (index, row) {
      this.currentIndex = index
      let files = row.sampleTable?JSON.parse(row.sampleTable).imgs:[]
      this.imgs = this.getFileByType(files).imgs
      this.auditTitle = `${row.name}审查细则`
      this.auditForm = {
        acceptance: row.acceptance.length?row.acceptance:[{
          content: '',
          type: 1
        }],
        approval: row.approval.length?row.approval:[{
          content: '',
          type: 2
        }]
      }
      this.auditModel = true
    },
    getFileByType(fileList) {
      let imgs = []
      let otherFiles = []
      fileList.forEach((item, index) => {
        // let signUrl = client.signatureUrl(`${new Date().toJSON().split('T')[0]}/${item}`, { expires: 80000 })
        let signUrl = client.signatureUrl(`${item}`, { expires: 80000 })
        let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp|\.png)/i.test(item)
        let file = {
          uid: new Date().getTime()-index*5,
          url: signUrl,
          name: item
        }
        if (flag) {
          // 图片
          imgs.push(file)
        } else {
          // 其他文件
          otherFiles.push(file)
        }
      })
      return { imgs, otherFiles }
    },
    // 审核细则返回
    auditCancel (obj) {
      if(this.watchPic) {
        this.auditModel = false
        return
      }
      let { acceptance, approval } = obj
      this.thisForm.datas[this.currentIndex].acceptance = acceptance
      this.thisForm.datas[this.currentIndex].approval = approval
      this.auditModel = false
    },

    // 保存
    formSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let flag = this.thisForm.datas.some(item => this.rowIsSpace(item))
          this.save(flag?1:2)
        } else {
          this.save(1)
        }
      })
    },

    async save (state) {
      let newData = []
      let orderNum = 1
      this.thisForm.datas.map(item => {
        newData.push({
          ...item,
          acceptance: item.acceptance?item.acceptance:[],
          approval: item.approval?item.approval:[],
          orderNum
        })
        ++orderNum
      })
      state = newData.length?state:1

      let res = await material.save({
        state,
        eventId: this.itemInfo.id,
        version:this.clfzVersion,
        materialGroupingParamList: newData
      })
      if (res.code === 0) {
        this.itemInfo.materialGroupingState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, materialGroupingState: state})
        this.$message.success(res.msg)
        this.getList(this.itemInfo.id)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.material {
  .el-input-number--mini {
    width: 60px;
  }
  .imgs {
    img {
      width: 48px;
      height: 48px;
      margin-left: 5px;
      margin-bottom: 5px;
      border-radius: 6px;
      border: 1px solid #ff4949;
      cursor: pointer;
    }
  }
  .checked {
    font-size: 25px;
    color: cadetblue;
  }
  .last-radio {
    margin-right: 25px;
  }
}
</style>
