/**
 * 名称：现有流程说明
 * 作者：王晓妮
 * 日期：2018-11-6
 */
<template>
  <div style="height:100%;position: relative;">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>现有流程</h2>
    <el-form :model="thisForm" ref="thisForm" :rules="rules" label-width="0" :style="{height: watchPic?'calc(100% - 45px)':'calc(100% - 105px)'}">
      <el-table height="100%" class="process-table" row-key="index" size="mini" :data="thisForm.datas" border style="width: 100%" highlight-current-row
        @cell-dblclick="judge" @row-click="rowClick">
        <el-table-column class="my-handle" label="序号" align="center" width="55">
          <template slot-scope="{row,$index}">
            <div class="my-handle">{{$index + 1}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="describetion" label="现有流程环节完整描述" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.describetion'" :rules='rules.describetion' v-if="!watchPic&&row.isUpdate">
              <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="row.describetion"/>
            </el-form-item>
            <div v-else>{{row.describetion}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" align="center" :rules='rules.remarks'>
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.remarks'" v-if="!watchPic&&row.isUpdate">
              <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="row.remarks"/>
            </el-form-item>
            <div v-else>{{row.remarks}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="60px" align="center" v-if="!watchPic">
          <template slot-scope="scope">
            <div class="table-row-btn">
              <!-- <el-tooltip :content="scope.row.isUpdate?'保存':'修改'" placement="top">
                <Button @click="pwdChange(scope.row,scope.$index,true)" type="primary" :icon="scope.row.isUpdate?'ios-checkmark-circle-outline':'md-create'" size="small"></Button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top" v-if="!scope.row.isUpdate">
                <Button @click="handleDelete(scope.$index, scope.row)" type="error" icon="ios-trash" size="small"></Button>
              </el-tooltip>
              <el-tooltip content="取消" placement="top" v-else>
                <Button @click="pwdChange(scope.row,scope.$index,false)" icon="ios-close-circle-outline" size="small"></Button>
              </el-tooltip> -->
              <el-tooltip content="删除" placement="top">
                <Button @click="handleDelete(scope.$index, scope.row)" type="error" icon="md-trash" size="small"></Button>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="pwdChange(scope.row,scope.$index,true)">{{scope.row.isUpdate?'保存':"修改"}}</el-button>

            <el-popover
              v-if="!scope.row.isUpdate"
              placement="top"
              width="100">
              <p>这是一段内容这是一段内容确定删除吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button type="primary" size="mini" @click="handleDelete(scope.$index, scope.row)">确定</el-button>
              </div>
              <el-button  class="name-wrapper" size="mini" type="danger" slot="reference">删除</el-button>
            </el-popover>

            <el-button v-else size="mini" @click="pwdChange(scope.row,scope.$index,false)">取消</el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <div class="add-row" style="width: 99.2%;" @click="addMasterUser()" v-if="!watchPic"><span>+ 添加</span></div>

      <div class="btn-submit" @click="formSubmit('thisForm')" v-if="!watchPic">保存</div>
    </el-form>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="现有流程评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </div>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import Sortable from 'sortablejs'
import DeleteDialog from '@/components/dialog.vue'
import JudgeDialog from '@/components/judgeDialog'

import { processTable } from '@/api/service'
import { validatorSpace } from '@/utils/validate'
export default {
  name: 'process-table',
  components: { DeleteDialog, JudgeDialog },
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
    'itemInfo.processDescriptionState' (val) {
      this.allState(val)
    }
  },
  data () {
    const checkName = (rule, value, callback) => {
      if (!validatorSpace(value)) {
        callback(new Error('不能为空'))
      }
      callback()
    }
    return {
      // 评审
      xylcsmVersion:null,//现有流程说明
      judgeShow: false,
      judgeForm: {},
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      // 删除
      showDel: false,
      currentIndex: 0,
      thisForm: {
        datas: []
      },
      // 验证
      rules: {
        describetion: [
          { required: true, validator: checkName, trigger: 'blur' }
        ],
        remarks: []
      }
    }
  },
  mounted () {
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
    this.getList(this.itemInfo.id)
    this.rowDrop()
  },
  computed: {
    ...mapState([
      'itemDetail'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.processDescriptionState)
      return itemDetail
    }
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
    judge (row, column, cell, event) {
      let endTime = this.itemInfo.examineEndTime
      if(endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          id: this.itemInfo.id,
          sheetNum: 5,
          lineNumber: Number(row.itemNumber),
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
    //行拖拽
    rowDrop() {
      const tbody = document.querySelector('.process-table .el-table__body-wrapper tbody')
      Sortable.create(tbody, {
        handle: '.my-handle'
      })
    },
    // 全查
    async getList (id) {
      this.thisForm.datas = []
      let res = await processTable.getByEventId(id)
      if (res.code === 0) {
        let data = res.obj.list?res.obj.list:[]
        data.forEach(item => {
          item.isUpdate = false
        })
        this.thisForm.datas = data
        this.xylcsmVersion=res.obj.version;
      }
    },
    // 添加
    addMasterUser () {
      this.thisForm.datas.map(item => item.isUpdate = false)
      this.thisForm.datas.push({
        id: null,
        eventId: this.itemInfo.id,
        describetion: '',
        remarks: '',
        isUpdate: true
      })
    },

    // 判断行提交空数据
    rowIsSpace (row) {
      let spaceLength = 0
      for (let key in row) {
        if (!row[key] && key === 'describetion') {
          spaceLength += 1
        }
      }
      return spaceLength===1
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
      this.showDel = false
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
      let itemNumber = 1
      this.thisForm.datas.map(item => {
        newData.push({
          ...item,
          itemNumber
        })
        ++itemNumber
      })
      state = newData.length?state:1

      let res = await processTable.save({
        state,
        eventId: this.itemInfo.id,
        processDescriptionList: newData,
        version:this.xylcsmVersion
      })
      if (res.code === 0) {
        this.itemInfo.processDescriptionState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, processDescriptionState: state})
        this.$message.success(res.msg);
        this.xylcsmVersion=null;
        this.getList(this.itemInfo.id)
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
