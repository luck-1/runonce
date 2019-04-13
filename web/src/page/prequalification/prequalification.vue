/**
 * 名称：资格预审
 * 作者：王晓妮
 * 日期：2018-11-6
 */
<template>
  <div style="height: 100%;position: relative;">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>资格预审</h2>
    <el-form :model="thisForm" ref="thisForm" :rules="rules" label-width="0" :style="{height: watchPic?'calc(100% - 45px)':'calc(100% - 105px)'}">
      <el-table height="100%" class="prequalificate" row-key="index" size="mini" :data="thisForm.datas" border style="width: 100%" highlight-current-row
        @cell-dblclick="judge" @row-click="rowClick">
        <el-table-column class="my-handle" label="序号" align="center" width="55">
          <template slot-scope="{row,$index}">
            <div class="my-handle">{{$index + 1}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="pretrialContent" label="预审内容" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.pretrialContent'" :rules='rules.pretrialContent' v-if="!watchPic&&row.isUpdate">
              <el-input size="mini" placeholder="请输入内容" v-model="row.pretrialContent"></el-input>
            </el-form-item>
            <div v-else>{{row.pretrialContent}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="auditSpecification" label="审核规范" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.auditSpecification'" :rules='rules.auditSpecification' v-if="!watchPic&&row.isUpdate">
              <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="row.auditSpecification" />
            </el-form-item>
            <div v-else>{{row.auditSpecification}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="auditMode" label="现有审核方式" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.auditMode'" :rules='rules.auditMode' v-if="!watchPic&&row.isUpdate">
              <el-select size="mini" v-model="row.auditMode" placeholder="请选择">
                <el-option label="纸质审核" value="纸质审核"></el-option>
                <el-option label="平台查询" value="平台查询"></el-option>
              </el-select>
            </el-form-item>
            <div v-else>{{row.auditMode}}</div>
          </template>
        </el-table-column>
        <el-table-column label="现有审核操作">
          <template slot-scope="{row,$index}">
            <ImgUpload size="mini" :webFiles="[]" v-if="row.auditMode==='平台查询'&&row.auditOperationPic" :watchPic="watchPic||!row.isUpdate" :files="row.auditOperationPic?JSON.parse(row.auditOperationPic).imgs:[]" :imageNum='40' :multiple="true" @upload="uploadImg(row, $event)" @heigthChange="heigthChange"></ImgUpload>
            <el-form-item :prop="'datas.' + $index + '.auditOperation'" :rules='rules.auditOperation' v-if="row.auditMode!=='平台查询'&&!watchPic&&row.isUpdate">
              <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="row.auditOperation"/>
            </el-form-item>
            <div v-if="row.auditMode!=='平台查询'&&(watchPic||!row.isUpdate)">{{row.auditOperation}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" align="center">
          <template slot-scope="{row,$index}">
            <el-form-item :prop="'datas.' + $index + '.remarks'" :rules='rules.remarks' v-if="!watchPic&&row.isUpdate">
              <Input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }" size="small" placeholder="请输入内容" v-model="row.remarks"/>
            </el-form-item>
            <div v-else>{{row.remarks}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="60px" align="center" v-if="!watchPic">
          <template slot-scope="scope">
            <div class="table-row-btn">
              <el-tooltip content="删除" placement="top">
                <Button @click="handleDelete(scope.$index, scope.row)" type="error" icon="md-trash" size="small"></Button>
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
    <JudgeDialog title="资格预审评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </div>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import Sortable from 'sortablejs'
import DeleteDialog from '@/components/dialog'
import JudgeDialog from '@/components/judgeDialog'
import ImgUpload from '@/components/uploadPicture'

import { prequalification } from '@/api/service'
import { validatorSpace } from '@/utils/validate'
export default {
  name: 'prequalification',
  components: { DeleteDialog, ImgUpload, JudgeDialog },
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
    'itemInfo.prequalificationState' (val) {
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
      zgysVersion:null,
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
        pretrialContent: [
          { required: true, validator: checkName, trigger: 'blur' }
        ],
        auditSpecification: [],
        auditMode: [],
        auditOperationPic: [],
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
      this.allState(itemDetail.prequalificationState)
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
          sheetNum: 4,
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
      const tbody = document.querySelector('.prequalificate .el-table__body-wrapper tbody')
      Sortable.create(tbody, {
        handle: '.my-handle'
      })
    },
    // 获取数据
    async getList (id) {
      this.thisForm.datas = []
      let res = await prequalification.getByEventId(id)
      if (res.code === 0) {
        let data = res.obj.list?res.obj.list:[]
        data.forEach(item => {
          item.isUpdate = false
          item.auditOperationPic = item.auditOperationPic||'{"imgs":[]}'
        })
        this.thisForm.datas = data
        this.zgysVersion=res.obj.version
      }
    },
    // 添加
    addMasterUser () {
      this.thisForm.datas.map(item => item.isUpdate = false)
      this.thisForm.datas.push({
        id: null,
        pretrialContent: '',
        auditSpecification: '',
        auditMode: '纸质审核',
        auditOperation: '',
        auditOperationPic: '{"imgs":[]}',
        remarks: '',
        eventId: this.itemInfo.id,
        isUpdate: true
      })
    },

    // 上传图片
    uploadImg (row, imgs) {
      row.auditOperationPic = imgs
    },

    heigthChange() {
      this.$nextTick(() => {
        let currentRow = document.getElementsByClassName('current-row')
        let height = currentRow.length?currentRow[0].offsetHeight:0
        for(let x of currentRow) x.style.height = height+'px'
      })
    },

    // 判断行提交空数据
    rowIsSpace (row) {
      let spaceLength = 0
      for (let key in row) {
        if (!row[key] && key === 'pretrialContent') {
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
      let current = JSON.parse(JSON.stringify(this.thisForm.datas))
      this.thisForm.datas = []
      this.$nextTick(() => {
        this.thisForm.datas = current
      })
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
      // state = newData.length?state:1

      let res = await prequalification.save({
        state,
        eventId: this.itemInfo.id,
        prequalificationList: newData,
        version:this.zgysVersion
      })
      if (res.code === 0) {
        this.itemInfo.prequalificationState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, prequalificationState: state})
        this.$message.success(res.msg)
        this.zgysVersion=null;
        this.getList(this.itemInfo.id)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.imgs {
  img {
    width: 48px;
    height: 48px;
    margin-left: 5px;
    margin-bottom: 5px;
    border-radius: 6px;
    border: 1px solid #c0ccda;
    cursor: pointer;
  }
}
</style>
