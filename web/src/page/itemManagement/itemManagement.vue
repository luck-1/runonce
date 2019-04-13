<template>
  <el-card class="container-card item">
    <el-form :model="searchForm" :rules="searchRules" ref="ruleForm" class="search-form">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="事项名称" prop="eventName" label-width="70px">
            <Input v-model="searchForm.eventName" placeholder="请输入" clearable />
          </el-form-item>
          <el-col :span="14">
            <el-form-item prop="showTree">
              <el-switch v-model="searchForm.showTree" :disabled="publicity"
                active-color="#13ce66" inactive-color="#dcdfe6"
                active-text="目录" inactive-text="办理项">
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-button size="small" type="primary" style="float:right;" icon="el-icon-search" @click="searchByPage"></el-button>
          </el-col>
        </el-col>
        <el-col :span="6">
          <el-form-item label="事项状态" prop="state" v-if="!publicity" label-width="70px">
            <el-select v-model="searchForm.state" placeholder="请选择" size="small" clearable>
              <el-option label="未完善" value="1"></el-option>
              <el-option label="已完善" value="2"></el-option>
              <el-option label="已提交" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属部门" prop="deptIdList" v-else label-width="70px">
            <el-select class="tree-select" collapse-tags multiple :popper-append-to-body="false" v-model="searchForm.deptIdList" placeholder="请选择" clearable>
              <el-tree ref="tree" default-expand-all node-key="id" :expand-on-click-node="false" :data="treeData" :render-content="renderContent"></el-tree>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="事项类别" prop="eventType" label-width="70px">
            <el-select v-model="searchForm.eventType" placeholder="请选择" size="small" clearable>
              <el-option label="行政许可" value="1"></el-option>
              <el-option label="审核转报" value="2"></el-option>
              <el-option label="备案类" value="3"></el-option>
              <el-option label="其他服务" value="4"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="审批状态" prop="examineAndApproveStateList" label-width="70px">
            <el-select v-model="searchForm.examineAndApproveStateList" collapse-tags multiple placeholder="请选择" size="small" clearable>
              <el-option label="待领导确认" value="1" v-if="!publicity"></el-option>
              <el-option label="部门领导拒绝" value="2" v-if="!publicity"></el-option>
              <el-option label="部门领导同意" value="3" v-if="!publicity"></el-option>
              <el-option label="待发起论证" value="4" v-if="!publicity"></el-option>
              <el-option label="部门领导发起论证" value="5" v-if="!publicity"></el-option>
              <el-option label="待论证" value="6"></el-option>
              <el-option label="论证通过" value="7"></el-option>
              <!-- <el-option label="评审拒绝" value="8"></el-option> -->
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-button-group style="margin-bottom: 1rem;">
      <!-- <el-button type="primary" icon="el-icon-refresh" size="small" @click="searchByPage">刷新</el-button> -->
      <el-button type="primary" icon="el-icon-arrow-down" @click="foldList = []" size="small">展开</el-button>
      <el-button type="primary" icon="el-icon-arrow-up" @click="foldList = foldAllList" size="small">折叠</el-button>
      <!-- <el-button type="primary" icon="el-icon-download" @click="exportTable" size="small">导出</el-button> -->
      <el-button type="primary" icon="el-icon-share" @click="transfer" size="small">数据同步</el-button>
    </el-button-group>

    <el-table :cell-class-name="cellName" :data="itemData" :row-style="toggleDisplayTr" border stripe height="calc(100% - 193px)" size="small">
      <el-table-column fixed="left" width="40">
        <template slot="header" slot-scope="scope">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange"></el-checkbox>
        </template>
        <template slot-scope="{row}">
          <el-checkbox :value="row.checked" :checked="row.checked" class="id-checkbox" @change="handleCheckedChange(row,$event)" v-if="!row.isMin"></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column fixed="left" align="center" width="55" label="序号">
        <template slot-scope="{row}">
          <div>{{row.index}}</div>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="办理项" align="left" :show-overflow-tooltip="true" min-width="200">
        <template slot-scope="scope">
          <p :style="`margin-top:0;margin-bottom:0`">
            <span v-if="scope.row.__level>0">
              <span class="child-name" v-for="(item, index) in Array(scope.row.__level)" :key="index"
                :style="{marginLeft: index===0?'7px':'10px'}"></span>
            </span>
            <!-- <i @click="toggleFoldingStatus(scope.row)" class="permission_toggleFold" :class="toggleFoldingClass(scope.row)"></i> -->
            <Icon class="permission_toggleFold" :class="toggleClass(scope.row)" :type="toggleFoldingClass(scope.row)"
              @click="scope.row.Children.length!==0?toggleFoldingStatus(scope.row):''"/>
            {{scope.row.eventName}}
          </p>
        </template>
      </el-table-column>
      <el-table-column prop="areaName" label="区域名称" width="120" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="deptName" label="部门名称" width="120" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="eventType" label="事项类别" width="120" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div>{{scope.row.eventType === 1?'行政许可':scope.row.eventType === 2?'审核转报':scope.row.eventType === 3?'备案类':'其他服务'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="directoryEncoding" label="办理项编码" title="业务办理项编码" width="90" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="codingImplementation" label="实施编码" title="实施清单编码" width="90" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="serviceObject" label="实施对象" width="110" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="exerciseHierarchy" label="行使层级" width="110" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="state" label="事项状态" width="140" v-if="!publicity">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.state === 1?'warning':scope.row.state === 2?'primary':'success'" disable-transitions>
            {{scope.row.state === 1?'未完善':scope.row.state === 2?'已完善':'已提交'}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="state" label="审批状态" width="140">
        <template slot-scope="scope">
          <el-tag size="small" type="primary" disable-transitions v-if="scope.row.examineAndApproveState">
            {{scope.row.examineAndApproveState === 1?'待领导确认':scope.row.examineAndApproveState === 2?'部门领导拒绝':scope.row.examineAndApproveState === 3?'部门领导同意':scope.row.examineAndApproveState === 4?'待发起论证':
            scope.row.examineAndApproveState === 5?'部门领导发起论证':scope.row.examineAndApproveState === 6?'待论证':scope.row.examineAndApproveState === 7?'论证通过':scope.row.examineAndApproveState === 8?'论证拒绝':'最终通过'}}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="examineEndTime" label="截止日期" width="140">
        <template slot-scope="{row}">
          {{row.examineEndTime?formatTime(row.examineEndTime):''}}
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" align="center" :width="publicity?85:150">
        <template slot-scope="scope">
          <div class="table-row-btn">
            <el-tooltip content="配置" placement="top" v-if="scope.row.isMin&&!publicity">
              <Button @click="itemSetting(scope.row)" type="primary" icon="ios-build" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="变更履历" placement="top" v-if="scope.row.isMin&&scope.row.isShow">
              <Button @click="modify(scope.row)" type="primary" icon="ios-book"  size="small"></Button>
            </el-tooltip>
            <el-tooltip content="导出" placement="top" v-if="scope.row.isMin&&!publicity&&scope.row.examineAndApproveState===7">
              <Button @click="exportTable(scope.row)" type="primary" icon="md-download" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="数据同步" placement="top" v-if="scope.row.isMin&&!publicity&&scope.row.examineAndApproveState===7">
              <Button @click="single=true;currentRow=row;showDel=true" type="primary" icon="md-share" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="发起评审" placement="top" v-if="scope.row.isMin&&!publicity&&userRole==='1'&&scope.row.examineAndApproveState===3&&scope.row.state===3">
              <Button @click="review(scope.row)" type="primary" icon="logo-designernews" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="公示查看" placement="top" v-if="scope.row.isMin&&publicity">
              <Button @click="publiclySee(scope.row)" type="primary" icon="md-eye" size="small"></Button>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <Revisionhistory :revisionShow="revisionShow" :modifyModel="modifyModel" :revisionEvent="revisionEvent" :historyLog="historyLog" @cancel="cancelHistory" @success="modify(revisionEvent);changeHistory(revisionEvent.id)"></Revisionhistory>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 同步 -->
    <dialogs type="warning" :visible="showDel" @cancel="showDel=false" @delete="del">
      <div>确认数据同步？数据同步不可逆</div>
    </dialogs>
  </el-card>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex'

import dialogs from '@/components/dialog'
import Revisionhistory from './revisionhistory'
import { common } from '@/utils/common'

import { departmentalMatters, exportTable, department, matterProposal,revisionhistory } from '@/api/service'
export default {
  name: 'item-management',
  components: { dialogs, Revisionhistory},
  props: {
    publicity: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapState([
      'itemDetail', 'loadingText', 'todoTask'
    ]),
    /**
     * 记录属性结构的首层节点
     */
    foldAllList () {
      return this.itemData.map(x => x.__identity)
    }
  },
  data () {
    return {
      userRole: '',
      // 树数据
      treeData: [],
      searchForm: {
        eventName: '',
        state: this.publicity?null:'',
        eventType: '',
        showTree: this.publicity?false:true,
        examineAndApproveStateList: [],
        deptId: '',
        deptIdList: []
      },
      searchRules: {},
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,

      itemData: [],
      foldList: [], // 该数组中的值 都会在列表中进行隐藏  死亡名单
      multipleSelection: [],
      isIndeterminate: false,
      checkAll: false,

      // 抽屉
      drawerTitle: '',
      drawerModel: false,
      drawerItem: {},
      modifyModel:false,
      historyLog:[],
      revisionEvent:{},
      revisionShow:false,

      // 数据同步
      showDel: false,
      single: false,
      currentRow: null
    }
  },
  mounted () {
    this.getDeptList()
    if(this.todoTask) {
      for (let item of this.todoTask.list) {
        item.index = 1
        item.eventPid = '0'
        item.Children = []
      }
      this.itemData = this.formatConversion([], this.todoTask.list)
      this.total = this.todoTask.total
    } else {
      this.searchByPage()
    }
    this.userRole = localStorage.uesrRole
  },
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL', 'SET_LOADINGTEXT'
    ]),
    formatTime(time) {
      return common.formatDateTime(time)
    },
    cellName ({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 2) {
        return 'name-cell'
      }
    },
    renderContent (h, {node, data, store }) {
      return (
        <el-option label={data.title} value={data.id}></el-option>
      )
    },
    // 组织架构
    async getDeptList () {
      let res = await department.selectAll('3', false)
      if(res.code === 0) {
        let list = []
        for (let x of res.obj) {
          if (x.parentId === '0') {
            list.push(this.formatDeptList(x, res.obj))
          }
        }
        this.treeData = list
      }
    },
    formatDeptList (data, datas) {
      let obj = data
      obj.children = []
      for (let x of datas) {
        if (obj.id === x.parentId) {
          obj.children.push(this.formatDeptList(x, datas))
        }
      }
      return obj
    },
    // 全选
    handleCheckAllChange (val) {
      let idList = this.itemData.filter(x => !x.isMin).map(x => x.id)
      this.multipleSelection = val ? idList : []
      this.isIndeterminate = false
      this.itemData.forEach(item => {
        if(!item.isMin) item.checked = val
      })
    },
    // 单选
    handleCheckedChange (row,checked) {
      row.checked = checked
      let currentIndex = this.multipleSelection.findIndex(x => x === row.id)
      if (currentIndex<0) {
        if (checked) {
          this.multipleSelection.push(row.id)
        }
      } else {
        if(!checked) {
          this.multipleSelection.splice(currentIndex,1)
        }
      }
      let idList = this.itemData.filter(x => !x.isMin).map(x => x.id)
      this.checkAll = this.multipleSelection.length === idList.length
      this.isIndeterminate = this.multipleSelection.length > 0 && this.multipleSelection.length < idList.length
    },
    // 数据同步
    transfer () {
      if(this.multipleSelection.length===0) {
        this.$message.error('至少选择一个事项')
        return
      }
      this.showDel = true
      this.single = false
    },
    del () {
      console.log(this.single,this.multipleSelection)
    },
    /**
     * 搜索
     */
    handleSizeChange (val) {
      this.pageSize = val
      this.searchByPage()
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.searchByPage()
    },
    async searchByPage () {
      const { currentPage, pageSize, searchForm } = this
      let ApproveState = searchForm.examineAndApproveStateList.map(x => Number(x))
      let obj = {
        page: currentPage,
        size: pageSize,
        ...searchForm,
        state: searchForm.state?Number(searchForm.state):searchForm.state,
        // this.publicity
        examineAndApproveStateList: this.publicity?(ApproveState.length===0?[6,7,8]:ApproveState):ApproveState,
        eventType: this.searchForm.eventType?Number(this.searchForm.eventType):null
      }
      let res = null
      this.multipleSelection = []
      this.isIndeterminate = false
      this.checkAll = false
      this.currentRow = null
      if(this.publicity) {
        // 公示查看
        res = await departmentalMatters.selectPageByDept(obj)
      } else {
        res = await departmentalMatters.selectByParam(obj)
      }
      if (res.code === 0) {
        let menuList = []
        let index = 0
        for (let item of res.obj.list) {
          if (item.eventPid === '0') {
            item.index = ++index
            menuList.push(this.formatList(item, res.obj.list))
          }
        }
        this.itemData = this.formatConversion([], menuList)
        this.total = res.obj?res.obj.total:0
      }
    },
    //递归实现树节点
    formatList (item, menus) {
      item.Children = []
      for(let x of menus) {
        if(item.eventId === x.eventPid && item.deptId === x.deptId) {
          item.Children.push(this.formatList(x, menus))
        }
      }
      return item;
    },
    /**
     * 表格树
     */
    // 切换展示 还是折叠（params：当前点击行的数据）
    toggleFoldingStatus (params) {
      this.foldList.includes(params.__identity) ? this.foldList.splice(this.foldList.indexOf(params.__identity), 1) : this.foldList.push(params.__identity)
    },
    // 对每一行数据做判断 如果foldList 列表中的元素 也存在与当前行的 __family列表中  则该行不展示
    toggleDisplayTr ({row, index}) {
      for (let i = 0; i < this.foldList.length; i++) {
        let item = this.foldList[i]
        // 如果foldList中元素存在于 row.__family中，则该行隐藏。  如果该行的自身标识等于隐藏元素，则代表该元素就是折叠点
        if (row.__family.includes(item) && row.__identity !== item) return 'display:none;'
      }
      return ''
    },
    /**
     * 如果子集长度为0，则不返回字体图标
     * 如果子集长度为不为0，根据foldList是否存在当前节点的标识返回相应的折叠或展开图标
     * 关于class说明：permission_placeholder返回一个占位符，具体查看class
     * params 当前行的数据对象
     */
    toggleFoldingClass (params) {
      return params.isMin ? 'md-document' : 'ios-folder-outline'
      // return params.Children.length === 0 ? 'permission_placeholder' : (this.foldList.indexOf(params.__identity) === -1 ? 'el-icon-arrow-down' : 'el-icon-arrow-right')
    },
    toggleClass (params) {
      return params.Children.length === 0 ? 'only-one' : (this.foldList.indexOf(params.__identity) === -1 ? 'arrow-down' : 'arrow-right')
    },
    /**
     * 将树形接口数据扁平化
     * parent 为当前累计的数组  也是最后返回的数组
     * children 为当前节点仍需继续扁平子节点的数据
     * index 默认等于0， 用于在递归中进行累计叠加 用于层级标识
     * family 装有当前包含元素自身的所有父级 身份标识
     * elderIdentity 父级的  唯一身份标识
     */
    formatConversion (parent, children, index = 0, family = [], elderIdentity = 'x') {
      // children如果长度等于0，则代表已经到了最低层
      // let page = (this.startPage - 1) * 10
      if (children.length > 0) {
        children.map((x, i) => {
          // 设置 __level 标志位 用于展示区分层级
          this.$set(x, '__level', index)
          // 设置 __family 为家族关系 为所有父级，包含本身在内
          this.$set(x, '__family', [...family, elderIdentity + '_' + i])
          // 本身的唯一标识  可以理解为个人的身份证咯 一定唯一。
          this.$set(x, '__identity', elderIdentity + '_' + i)
          parent.push(x)
          // 如果仍有子集，则进行递归
          if (x.Children.length > 0) this.formatConversion(parent, x.Children, index + 1, [...family, elderIdentity + '_' + i], elderIdentity + '_' + i)
        })
      } return parent
    },
    // 配置
    itemSetting (row) {
      // 记录返回路由
      this.SET_ITEMDETAIL(row)
      localStorage.setItem('itemDetail', JSON.stringify(row))
      // let query = { backRoute: this.$route.name }
      this.$router.push({
        // 该路由已在/router/router.js中定义好 携带id参数
        path: '/itemSetting',
        // query: query
      })
    },
    //变更履历
    async modify (row) {
      this.revisionEvent = row;
      this.changeHistory(row.id);
      let res = await revisionhistory.selectByDmId(row.id);
      if(res.code === 0){
        this.historyLog = res.obj;
      }else{
        this.$message.error(res.msg);
      }
      this.modifyModel = true;
    },
    //申请变更
    async changeHistory (id){
      let res = await revisionhistory.ComerButton(id);
      if(res.obj){
        this.revisionShow = true;
      }else{
        this.revisionShow = false;
      }
    },
	  //取消变更
    cancelHistory () {
      this.modifyModel = false
    },
    // 导出
    exportTable (row) {
      this.SET_LOADINGTEXT('导出中，请稍候...')
      setTimeout(async () => {
        let res = await exportTable.export(row.id)
        if(res.code === 0) {
          this.SET_LOADINGTEXT('拼命加载中...')
          let saveLink = document.createElement('a')
          saveLink.download = `${row.eventName}.xlsx`
          saveLink.href = process.env.BASE_API.split('/runonce/')[0] + res.obj
          saveLink.click()
        } else {
          this.SET_LOADINGTEXT('拼命加载中...')
        }
      }, 500)
    },
    // 发起评审
    async review (row) {
      let res = await matterProposal.examine(row.id)
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.searchByPage()
      }
    },
    // 公示查看
    publiclySee (row) {
      // 记录返回路由
      this.SET_ITEMDETAIL(row)
      localStorage.setItem('itemDetail', JSON.stringify(row))
      // let query = { backRoute: this.$route.name }
      this.$router.push({
        // 该路由已在/router/router.js中定义好 携带id参数
        path: '/itemSee',
        // query: query
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.item {
  & /deep/ .el-card__body{
    height: calc(100% - 1px);
  }
  .el-pagination {
    margin: 1rem 0;
    text-align: center;
  }
}
.el-table--small {
  & /deep/ .el-table__row {
    td {
      padding: 0;
    }
  }
  & /deep/ .name-cell {
    padding: 0!important;
    .cell {
      position: relative;
      line-height: 38px;
      .child-name {
        display: inline-block;
        height: 20px;
        width: 20px;
        margin-bottom: 3px;
        // border-left: 1px dashed #999;
        &:last-child{
          border-bottom: 1px dashed #999;
        }
        &::after {
          content: '';
          height: 1000px;
          margin-left: 0;
          position: absolute;
          top: 0;
          border-left: 1px dashed #999;
        }
      }
    }
    .permission_toggleFold {
      vertical-align: middle;
      padding-right: 5px;
      font-size: 16px;
      cursor: pointer;
    }
  }
}
.el-icon-arrow-down,
.ivu-icon-ios-folder-outline:not(.only-one) {
  margin-top: 10px;
  &::after {
    content: '';
    display: block;
    width: 1px;
    height: 10px;
    margin-left: 7px;
  }
  &:not(.arrow-right)::after {
    border-left: 1px dashed #999;
  }
}
.tree-select {
  & /deep/ .el-select__tags {
    overflow: hidden;
  }
  & /deep/ .el-select-dropdown__item{
    padding: 0;
    width: 100%;
    &:hover,
    &.hover {
      background-color: transparent;
    }
  }
}
</style>
