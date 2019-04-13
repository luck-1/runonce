<template>
  <el-card id="container-card" class="container-card item-setting">
    <el-tabs class="tab-form"
      @tab-click="tabClick" :before-leave="beforeLeave" v-model="tabsValue" stretch tab-position="right" style="height: 100%">
      <el-tab-pane label="办事指南" name="1">
        <!-- itemInfo.eventType Number
            1 行政许可和备案类；2 审核转报；3 其他
        -->
        <Administrative v-if="itemInfo.eventType===1||itemInfo.eventType===3" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :state="instruction" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)"></Administrative>
        <Audit v-else-if="itemInfo.eventType===2" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :state="instruction" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)"></Audit>
        <Other v-else :state="instruction" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)"></Other>
      </el-tab-pane>

      <el-tab-pane label="材料分组" name="2">
        <Material :state="material" :itemSee="itemSee" :editState="editState.isQuestion||userRole==='0'" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></Material>
      </el-tab-pane>

      <el-tab-pane label="申请类型" name="3">
        <ApplyType :isLeave="isLeave" :state="applyType" :itemSee="itemSee" :editState="editState.isQuestion||userRole==='0'" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></ApplyType>
      </el-tab-pane>

      <el-tab-pane label="办事情形" name="7">
        <BusinessSituations :isLeave="isLeave" :state="businessSituation" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></BusinessSituations>
      </el-tab-pane>

      <el-tab-pane label="表单说明" name="8">
        <FormExplain :state="formExplainState" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></FormExplain>
      </el-tab-pane>

      <el-tab-pane label="资格预审" name="4">
        <Prequalification :state="prequalification" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></Prequalification>
      </el-tab-pane>

      <el-tab-pane label="现有流程" name="5">
        <ProcessTable :state="processTable" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></ProcessTable>
      </el-tab-pane>

      <el-tab-pane label="量化呈现" name="9">
        <PresentQuantitate :state="persent" :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></PresentQuantitate>
      </el-tab-pane>

      <el-tab-pane label="系统对接方案" name="10">
        <SystemPlan :itemSee="itemSee" :editState="editState.isQuestion||userRole==='0'" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'"></SystemPlan>
      </el-tab-pane>

      <el-tab-pane label="办理流程图" name="6" style="height:100%">
        <Process :editState="editState.isQuestion||userRole==='0'" :itemSee="itemSee" :watchPic="itemSee||(itemInfo.state===3&&!editState.isEdit)||itemInfo.region==='3'||itemInfo.region==='4'" :state="process" :showFlow="firstLoad" @changeShowFlow="changeShowFlow"></Process>
      </el-tab-pane>

      <el-tab-pane label="从已有事项复制" name="11" v-if="itemInfo.state!==3&&(itemInfo.region==='1'||itemInfo.region==='2')" class="item-see"></el-tab-pane>
      <el-tab-pane label="从已有输出物导入" name="12" v-if="itemInfo.state!==3&&(itemInfo.region==='1'||itemInfo.region==='2')" class="item-see"></el-tab-pane>
    </el-tabs>

    <div class="btn-item-group">
      <!-- userRole  0超级管理员 1部门管理员(审批员)  2梳理员  3论证员 -->
      <!-- {{userRole}}===={{itemInfo.examineAndApproveState}}==={{itemInfo.state}} -->
      <div v-if="!itemSee&&itemInfo.state===3&&userRole==='1'">
        <div class="btn-item" @click="discussTitle='待评审';sheetNum = Number(tabsValue);discussView(sheetNum, 1);selectSheetState()" v-if="itemInfo.examineAndApproveState<3">待评审</div>
        <div v-else>
          <div class="btn-item" @click="discussTitle='查看评审';sheetNum = Number(tabsValue);discussView(sheetNum, 1);selectSheetState()">查看评审</div>
          <div class="btn-item" @click="discussTitle='查看论证';sheetNum = Number(tabsValue);discussView(sheetNum, 2);selectSheetState()">查看论证</div>
        </div>
      </div>
      <div v-if="!itemSee&&itemInfo.state===3&&userRole==='2'">
        <div class="btn-item" @click="discussTitle='查看论证';sheetNum = Number(tabsValue);discussView(sheetNum, 2);selectSheetState()" v-if="itemInfo.examineAndApproveState>2">查看论证</div>
        <div class="btn-item" @click="discussTitle='查看评审';sheetNum = Number(tabsValue);discussView(sheetNum, 1);selectSheetState()">查看评审</div>
      </div>
      <div v-if="itemSee&&itemInfo.state===3&&userRole==='3'">
        <div class="btn-item" @click="discussTitle=itemInfo.examineAndApproveState===7?'查看论证':'待论证';sheetNum = Number(tabsValue);discussView(sheetNum, 2);selectSheetState()">{{itemInfo.examineAndApproveState===7?'查看论证':'待论证'}}</div>
      </div>

      <div v-if="itemInfo.state===3&&userRole==='0'">
        <div class="btn-item" @click="discussTitle='待论证';sheetNum = Number(tabsValue);discussView(sheetNum, 2);selectSheetState()" v-if="itemSee">待论证</div>
        <div class="btn-item" @click="discussTitle='待评审';sheetNum = Number(tabsValue);discussView(sheetNum, 1);selectSheetState()" v-if="!itemSee&&itemInfo.examineAndApproveState<2">待评审</div>
      </div>

      <div class="btn-item" @click="formSubmit" v-if="!itemSee&&itemInfo.state!==3">提交</div>
    </div>

    <!-- 从已有事项复制 -->
    <ItemTable :visible="copyVisible" :itemInfos="copyVisible?itemInfo:null" @close="closeTable" @dbclick="rowDbClick"></ItemTable>
    <!-- 从已有输出物导入 -->
    <ItemImport :visible="importVisible" :importForm="importForm" @close="importClose"></ItemImport>
    <!-- 查看评论 -->
    <Modal class="discuss-view" v-model="discussDialog" width="850px" draggable scrollable :title="discussTitle" @on-cancel="cancelDiscuss">
      <div class="other-context">
        <p style="margin-bottom: -10px">页签
          <div class="sheet-state" :style="{background: sheetState.state===1?'#67c23a':sheetState.state===2?'#f56c6c':'#409eff'}" v-if="sheetState">
            {{sheetState.state===1?'同意':sheetState.state===2?'拒绝':'待审批'}}
          </div>
        </p>
        <div style="padding: 10px 0">
          <el-row>
            <el-radio v-model="sheetNum" :label="1">办事指南</el-radio>
            <el-radio v-model="sheetNum" :label="2">材料分组</el-radio>
            <el-radio v-model="sheetNum" :label="3">申请类型</el-radio>
            <el-radio v-model="sheetNum" :label="7">办事情形</el-radio>
            <el-radio v-model="sheetNum" :label="8">表单说明</el-radio>
            <el-radio v-model="sheetNum" :label="4">资格预审</el-radio>
            <el-radio v-model="sheetNum" :label="5">现有流程</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="sheetNum" :label="9">量化呈现</el-radio>
            <el-radio v-model="sheetNum" :label="10">系统对接方案</el-radio>
            <el-radio v-model="sheetNum" :label="6">办理流程图</el-radio>
          </el-row>
        </div>
      </div>
      <div class="other-context">
        <p>历史评审内容
          <el-button type="primary" style="float: right;margin-top: -4px;margin-right: -10px;" @click="showDel=true"
            v-if="sheetState&&sheetState.state!==1&&itemSee&&(itemInfo.examineAndApproveState>5)&&userRole==='3'">通过当前页签</el-button>
          <el-button type="primary" style="float: right;margin-top: -4px;margin-right: -10px;" @click="showDel=true"
            v-if="sheetState&&sheetState.state!==1&&!itemSee&&(itemInfo.examineAndApproveState<5)&&userRole==='1'">通过当前页签</el-button>
          <el-button type="primary" style="float: right;margin-top: -4px;margin-right: -10px;" @click="showDel=true"
            v-if="sheetState&&sheetState.state!==1&&userRole==='0'">通过当前页签</el-button>
        </p>
        <div v-if="total!==0">
          <el-table :data="discussContents" highlight-current-row height="400px" @current-change="currentChange">
            <el-table-column type="expand" width="25">
              <template slot-scope="{row, $index}">
                <div v-if="row.matterProposalAnswer.length">
                  <TimeLine :time="item.answerTime" :user="item.answererUser" v-for="(item, index) in row.matterProposalAnswer" :key="index">
                    <div slot="content" class="discuss-content"><p>{{item.answerContent}}</p></div>
                  </TimeLine>
                </div>
                <div v-else>无</div>
              </template>
            </el-table-column>
            <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
            <el-table-column label="状态" prop="questionState">
              <template slot-scope="{row}">
                <el-tag size="small" :type="(row.questionState === 1||row.questionState === 4)?'warning':(row.questionState === 0||row.questionState === 2)?'primary':'success'" disable-transitions>
                  {{row.questionState===0?'待梳理员回复':row.questionState===1?'梳理员拒绝':row.questionState===2?'沟通中':row.questionState===3?'同意':'梳理员待确认'}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="评论时间" prop="questionTime" width="140px">
              <template slot-scope="{row}">
                {{formatTime(row.questionTime)}}
              </template>
            </el-table-column>
            <el-table-column label="评论内容" :show-overflow-tooltip="true" min-width="130" prop="questionContent"></el-table-column>
            <el-table-column label="元素名称" prop="propertyName"></el-table-column>
            <el-table-column label="提问者" prop="questionerUser"></el-table-column>
            <el-table-column label="操作" :width="editState?'120px':'50px'">
              <template slot-scope="{row}" v-if="sheetState&&sheetState.state!==1&&row.questionState!==3">
                <div class="table-row-btn">
                  <!-- 梳理员 -->
                  <div v-if="userRole==='2'">
                    <el-tooltip content="接受" placement="top">
                      <Button @click="judgeState(2, row)" type="primary" icon="ios-checkmark-circle" size="small"></Button>
                    </el-tooltip>
                    <el-tooltip content="拒绝" placement="top">
                      <Button @click="judgeState(1, row)" type="error" icon="ios-close-circle" size="small"></Button>
                    </el-tooltip>
                    <el-tooltip content="待确认" placement="top">
                      <Button @click="judgeState(4, row)" type="warning" icon="ios-alert" size="small"></Button>
                    </el-tooltip>
                  </div>
                  <!-- 评审员 -->
                  <el-tooltip content="通过" placement="top" v-if="itemSee&&(itemInfo.examineAndApproveState>5)&&userRole==='3'">
                    <Button @click="judgeState(3, row)" type="success" icon="ios-checkmark-circle" size="small"></Button>
                  </el-tooltip>
                  <!-- 部门管理员（审批员） -->
                  <el-tooltip content="通过" placement="top" v-if="!itemSee&&(itemInfo.examineAndApproveState<5)&&userRole==='1'">
                    <Button @click="judgeState(3, row)" type="success" icon="ios-checkmark-circle" size="small"></Button>
                  </el-tooltip>
                  <!-- 超级管理员 -->
                  <el-tooltip content="通过" placement="top" v-if="userRole==='0'">
                    <Button @click="judgeState(3, row)" type="success" icon="ios-checkmark-circle" size="small"></Button>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else style="padding: 20px">无</div>
        <div class="other-context" v-if="currentDiscuss&&sheetState&&sheetState.state!==1&&((userRole==='1'||userRole==='0')&&discussTitle==='待评审'||(userRole==='3'||userRole==='0')&&discussTitle==='待论证')">
          <p>回复</p>
          <Input v-model="answerContent" :maxlength="500" type="textarea" :autosize="{minRows: 3,maxRows: 6}" placeholder="请输入回复" />
        </div>
      </div>
      <el-pagination
        style="margin-top: 1rem;text-align: center"
        v-if="total!==0"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>

      <div slot="footer">
        <el-button @click="cancelDiscuss">取消</el-button>
        <el-button type="primary" @click="discussSubmit" v-if="currentDiscuss&&sheetState.state!==1&&((userRole==='1'||userRole==='0')&&discussTitle==='待评审'||(userRole==='3'||userRole==='0')&&discussTitle==='待论证')">回复</el-button>
      </div>
    </Modal>

    <!-- 通过确认 -->
    <PassDialog type="primary" title="通过确认" :visible="showDel" @cancel="showDel=false" @delete="passDiscuss">
      <div>确认通过？通过后不可恢复</div>
    </PassDialog>
  </el-card>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
// 办事指南
import Administrative from '../instruction/administrative'
import Audit from '../instruction/Audit'
import Other from '../instruction/Other'
// 申请类型
import ApplyType from '../applyType/applyType'
// 材料分组
import Material from '../material/material'
// 资格预审
import Prequalification from '../prequalification/prequalification'
// 现有流程
import ProcessTable from '../processTable/processTable'
// 办理流程图
import Process from '../process/view'
// 办事情形
import BusinessSituations from '../businessSituation/businessSituation'
// 表单说明
import FormExplain from '../formExplain/formExplain'
// 量化呈现
import PresentQuantitate from '../presentQuantitate/presentQuantitate'
// 系统对接方案
import SystemPlan from '../systemPlan/systemPlan'
import PassDialog from '@/components/dialog.vue'

import ItemTable from '@/components/itemTable'
import ItemImport from '@/components/itemImport'
import TimeLine from '@/components/timeLine'

import { validatorSpace } from '@/utils/validate'
import { departmentalMatters, matterProposal } from '@/api/service'
import { common } from '@/utils/common'
export default {
  name: 'item-setting',
  components: {
    Administrative, Audit, Other, ApplyType, Material, Prequalification,
    Process, ProcessTable, BusinessSituations, FormExplain, PresentQuantitate,
    SystemPlan, ItemTable, ItemImport, TimeLine, PassDialog
  },
  props: {
    itemSee: {
      type: Boolean
    },
    leave: {
      type: Boolean
    }
  },
  data () {
    return {
      // 删除
      showDel: false,
      userRole: localStorage.uesrRole,
      sheetState: null,
      editState: {
        isQuestion: false,
        isEdit: true,
      },
      contentRadio:'',
      // 提交按钮
      showSubmit: true,
      tabsValue: '1',
      copyVisible: false,
      // 查看评论
      discussTitle: '',
      discussDialog: false,
      sheetNum: 0,
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,
      discussContents: [],
      // 当前点击评论
      currentDiscuss: null,
      // 回复
      answerContent: '',
      // 控制各页面是否请求接口
      instruction: true,
      material: false,
      applyType: false,
      prequalification: false,
      processTable: false,
      process: false,
      businessSituation: false,
      persent: false,
      showFlow: false,
      firstLoad: true,
      formExplainState: false,
      // 导入
      importVisible: false,
      importForm: {
        departmentalMattersId:'',
        eventName: '',
        type: '',
        typeName: '',
        file: null,
        version: '',
        fileName: ''
      },
      isLeave: false
    }
  },
  computed: {
    ...mapState([
      'itemDetail'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      let {
        businessGuideState, // 办事指南
        materialGroupingState, // 材料分组
        applicationTypeState, // 申请类型
        prequalificationState, // 资格预审
        processDescriptionState, // 现有流程
        processMapState, // 办理流程图
        businessSituationState, // 办事情形
        formThatState, // 表单说明
        presentQuantitativeState // 量化呈现
      } = itemDetail
      // this.showSubmit =  (businessGuideState && materialGroupingState && applicationTypeState && prequalificationState
      // && processDescriptionState && processMapState && businessSituationState && formThatState && presentQuantitativeState)
      // this.showSubmit =  state !== '3' && state !== '5'
      this.instruction = true
      this.SET_SAVESUCCESS(false)
      if(itemDetail.state===3) {
        this.getState(itemDetail.id)
      }
      return itemDetail
    },
  },
  watch: {
    leave (val) {
      this.isLeave = false
      if(val) {
        this.tabsValue = '1';
        this.instruction = false
        this.applyType = false
        this.material = false
        this.prequalification = false
        this.processTable = false
        this.process = false
        this.businessSituation = false
        this.persent = false
        this.showFlow = false
        this.formExplainState = false
        this.discussDialog = false
        this.isLeave = true
      }
    },
    tabsValue (val) {
      if(this.itemInfo.state===3) {
        this.sheetNum = Number(val)
      }
    },
    sheetNum (val) {
      this.answerContent = ''
      this.currentDiscuss = null
      this.sheetState = null
      this.selectSheetState()
      this.discussView(val,(this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2, false)
    }
  },
  mounted () {
    this.SET_TODOTASK(null)
    // this.selectSheetState()
  },
  methods: {
    ...mapMutations([
      'SET_SAVESUCCESS','SET_TODOTASK'
    ]),
    formatTime(time) {
      return common.formatDateTime(time)
    },
    // 切换标签之前的钩子，若返回 false 则阻止切换
    beforeLeave (activeName, oldActiveName) {
      if (activeName === '11') {
        this.copyVisible = true
        return false
      } else if (activeName === '12') {
        this.importForm = {
          departmentalMattersId: this.itemInfo.id,
          eventName: this.itemInfo.eventName,
          type: this.itemInfo.eventType,
          typeName: this.typeName(this.itemInfo.eventType),
          file: null,
          fileName: ''
        }
        this.importVisible = true
        return false
      } else {
        return true
      }
    },
    // 事项名称
    typeName (type) {
      let name = ''
      switch(type) {
        case 1:name='行政许可和备案类';break;
        case 2:name='审核转报';break;
        case 3:name='行政许可和备案类';break;
        case 4:name='其他服务';break;
        default:
      }
      return name
    },
    importClose () {
      this.importVisible = false
    },
    async getState(id) {
      let res = await departmentalMatters.getState(id)
      // 返回true 是梳理员和部门管理员，false是评审员
      if (res.code === 0) {
        this.editState = res.obj
      }
    },
    // 弹层关闭
    closeTable () {
      this.copyVisible = false
    },
    // 双击选中数据
    async rowDbClick (row) {
      let res = await departmentalMatters.copy({
        copyMattersIdFrom: row.id,
        copyMattersIdTo: this.itemInfo.id,
        eventType: row.eventType
      })
      if (res.code === 0) {
        history.go(0)
        // window.location.reload()
      }
    },
    changeShowFlow() {
      this.showFlow = true
    },
    tabClick (tab, event) {
      switch (tab.name) {
        case '1':
          this.instruction = true
          this.applyType = false
          this.material = false
          this.prequalification = false
          this.processTable = false
          this.process = false
          this.businessSituation = false
          this.persent = false
          this.formExplainState = false
          break;
        case '2':
          this.instruction = false
          this.applyType = false
          this.material = true
          this.prequalification = false
          this.processTable = false
          this.businessSituation = false
          this.persent = false
          this.formExplainState = false
          break;
        case '3':
          this.instruction = false
          this.applyType = true
          this.material = false
          this.prequalification = false
          this.processTable = false
          this.process = false
          this.businessSituation = false
          this.persent = false
          this.formExplainState = false
          break;
        case '4':
          this.instruction = false
          this.applyType = false
          this.material = false
          this.prequalification = true
          this.processTable = false
          this.process = false
          this.businessSituation = false
          this.persent = false
          this.formExplainState = false
          break;
        case '5':
          this.instruction = false
          this.applyType = false
          this.material = false
          this.prequalification = false
          this.processTable = true
          this.process = false
          this.businessSituation = false
          this.persent = false
          this.formExplainState = false
          break;
        case '6':
          this.instruction = false
          this.applyType = false
          this.material = false
          this.prequalification = false
          this.processTable = false
          this.process = true
          this.businessSituation = false
          this.persent = false
          this.formExplainState = false
          this.firstLoad = this.showFlow
          this.showFlow = true

          break;
        case '7':
          this.instruction = false
          this.applyType = false
          this.material = false
          this.prequalification = false
          this.processTable = false
          this.process = false
          this.businessSituation = true
          this.persent = false
          this.formExplainState = false
          break;
        case '8':
          this.instruction = false
          this.applyType = false
          this.material = false
          this.prequalification = false
          this.processTable = false
          this.process = false
          this.businessSituation = false
          this.persent = false
          this.formExplainState = true
          break;
        case '9':
          this.instruction = false
          this.applyType = false
          this.material = false
          this.prequalification = false
          this.processTable = false
          this.process = false
          this.businessSituation = false
          this.persent = true
          this.formExplainState = false
          break;
        default: break;
      }
    },
    // 提交
    async formSubmit () {
      let res = await departmentalMatters.updateState({id: this.itemInfo.id})
      if(res.code === 0) {
        this.SET_SAVESUCCESS(true)
        this.$message.success(res.msg)
      }
    },
    currentChange (currentRow, oldCurrentRow) {
      this.currentDiscuss = currentRow
    },
    // 取消回复
    cancelDiscuss () {
      this.discussDialog = false
      this.answerContent = ''
      this.currentDiscuss = null
    },
    // 回复
    async discussSubmit () {
      if(!validatorSpace(this.answerContent)) {
        this.$message.error('回复内容不能为空')
        return
      }
      let res = await matterProposal.answer({
        questionId: this.currentDiscuss.questionId,
        answerContent: this.answerContent
      })
      if (res.code === 0) {
        this.$message.success(res.msg)
        this.selectSheetState()
        this.discussView(this.sheetNum,(this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2)
        this.answerContent = ''
        this.currentDiscuss = null
      }
    },
    // 更改状态
    async judgeState (state, row) {
      let res = await matterProposal.updateState({
        id: row.questionId,
        questionState: state
      })
      if(res.code === 0) {
        this.selectSheetState()
        this.discussView(this.sheetNum,(this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2)
        this.$message.success(res.msg)
      }
    },
    // 获取当前sheet页状态
    async selectSheetState () {
      let isApproval = (this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2
      let res = await matterProposal.selectSheetState({
        dmId: this.itemInfo.id,
        isApproval,
        sheetNumber: this.sheetNum
      })
      if(res.code === 0) {
        this.sheetState = res.obj
      }
    },
    // 通过当前sheet页
    async passDiscuss() {
      if (!this.sheetState) return
      let res = await matterProposal.savePass(this.sheetState.id)
      if (res.code === 0) {
        this.$message.success(res.msg)
        this.showDel = false
        this.selectSheetState()
        this.discussView(this.sheetNum,(this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2)
      }
    },
    /**
     * 搜索
     */
    handleSizeChange (val) {
      this.pageSize = val
      this.discussView(this.sheetNum,(this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2)
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.discussView(this.sheetNum,(this.discussTitle==='待评审'||this.discussTitle==='查看评审')?1:2)
    },
    // 回看评论
    async discussView (val, state, flag=true) {
      let obj = {
        dmId: this.itemInfo.id,
        sheetNum: val,
        isApproval: state,
        page: this.currentPage,
        size: this.pageSize,
      }
      let res = await matterProposal.findByPage(obj)
      if(res.code === 0) {
        this.discussContents = []
        this.total = 0
        if(res.obj) {
          this.discussContents = res.obj.list
          this.total = res.obj.total
          if (flag && !this.discussDialog){
            this.discussDialog = true
          }
        }
      } else {
        this.discussContents = []
        this.total = 0
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.container-card {
  & /deep/ .el-card__body {
    padding-right: 0;
    height: 100%;
  }
  .tab-form /deep/ .el-tabs__header {
    height: calc(100% - 60px);
  }
  .tab-form /deep/ .el-tabs__content {
    height: 100%;
    overflow: auto;
    .el-tab-pane {
      height: 100%;
    }
  }
  .tab-form /deep/ .el-tabs__nav>div:nth-child(12) {
    border-top: 2px solid #3e96fc;
  }
  .item-setting {
    position: relative;
    .anchor {
      position: fixed;
      right: 1rem;
      top: 50%;
      bottom: 50%;
    }
    .detail > div {
      height: 80vh;
      width: 100%;
    }
  }
}
.discuss-view /deep/ .ivu-modal-body {
  .other-context {
    &>p {
      border-left: 3px solid #2088fc;
      padding: 5px 10px;
      font-size: 1rem;
      font-weight: bold;
      background-color: #eee;
    }
  }
  & /deep/ .el-table__expanded-cell[class*=cell]{
    padding: 20px 30px;
  }
}
.discuss-view /deep/ .el-radio {
  line-height: 25px;
}
.discuss-label {
  display: block;
  &>label {
    display: block;
    & /deep/ .el-radio-button__inner {
      padding: 0 15px;
      border: none;
      width: 100%;
    }
    .discuss-content>p {
      text-align: left;
      font-size: 1rem;
      font-weight: bold;
    }
  }
}
.sheet-state {
  float: right;
  margin-top: -23px;
  padding: 10px 15px;
  border-radius: 3px;
  line-height: 1;
  font-size: 12px;
  color: #fff;
}
.btn-item-group {
  position: fixed;
  bottom: 1rem;
  right: 2rem;
  overflow: hidden;
  z-index: 2;
  .btn-item {
    display: block;
    width: 6rem;
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
  .btn-item:not(:last-child) {
    margin-bottom: 5px;
  }
}
</style>
