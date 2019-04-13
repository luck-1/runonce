<template>
  <el-dialog title="从已有事项复制" append-to-body width="920px"
    :close-on-click-modal="false" :close-on-press-escape="false" :visible="visible" @close="closeDialog">
    <el-form :model="searchForm" :rules="searchRules" ref="ruleForm" class="search-form">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="事项名称" prop="eventName" label-width="70px">
            <Input v-model="searchForm.eventName" placeholder="请输入" clearable />
          </el-form-item>

          <!-- <el-form-item label="事项状态" prop="state" label-width="70px">
            <el-select v-model="searchForm.state" placeholder="请选择" size="small" clearable>
              <el-option label="已提交" value="3"></el-option>
              <el-option label="已公示" value="4"></el-option>
              <el-option label="确认完成" value="5"></el-option>
            </el-select>
          </el-form-item> -->
        </el-col>
        <el-col :span="8">
          <el-form-item label="所属部门" prop="deptName" label-width="70px">
            <el-popover trigger="click" title="选择部门">
              <el-row style="width:600px;height: 300px;overflow:auto;">
                <el-col :span="13">
                  <el-tree class="dept-tree" ref="regionTree" default-expand-all highlight-current node-key="id" :data="listArea" :props="defaultProps" :expand-on-click-node="false" @node-click="selectRegion"></el-tree>
                </el-col>
                <el-col :span="11">
                  <el-tree class="dept-tree" ref="tree" default-expand-all highlight-current node-key="id" :data="depTree" :props="defaultProps" :expand-on-click-node="false" @node-click="clickDepTree"></el-tree>
                </el-col>
              </el-row>
              <Input slot="reference" v-model="searchForm.deptName" placeholder="请输入" clearable />
            </el-popover>
          </el-form-item>
          <!-- <el-form-item label="所属部门" prop="deptIdList" label-width="70px">
            <el-select class="tree-select" collapse-tags multiple :popper-append-to-body="false" v-model="searchForm.deptIdList" placeholder="请选择" clearable>
              <el-tree ref="tree" default-expand-all node-key="id" :expand-on-click-node="false" :data="treeData" :filter-node-method="filterNode" :render-content="renderContent"></el-tree>
            </el-select>
          </el-form-item> -->
          <!-- <el-popover trigger="click">
            <el-tree ref="tree" default-expand-all highlight-current node-key="id" @node-click="selectTree" style="width:225px;height: 300px;overflow:auto;"
             :expand-on-click-node="false" :data="treeData" :filter-node-method="filterNode" :props="defaultProps"></el-tree>
            <Input slot="reference" v-model="searchForm.deptName" placeholder="请输入" clearable />
          </el-popover>
          <el-form-item prop="showTree">
            <el-switch v-model="searchForm.showTree"
              active-color="#13ce66" inactive-color="#dcdfe6"
              active-text="目录" inactive-text="办理项">
            </el-switch>
          </el-form-item> -->
        </el-col>
        <el-col :span="8">
          <el-form-item label="事项类别" prop="eventType" label-width="70px">
            <el-select v-model="searchForm.eventType" placeholder="请选择" size="small" clearable :disabled="itemInfos?true:false">
              <el-option label="行政许可" value="1"></el-option>
              <el-option label="审核转报" value="2"></el-option>
              <el-option label="备案类" value="3"></el-option>
              <el-option label="其他服务" value="4"></el-option>
            </el-select>
          </el-form-item>
          <el-button-group style="float: right;margin-bottom:1rem;">
            <el-button size="small" type="primary" icon="el-icon-search" @click="searchByPage">搜索</el-button>
            <el-button type="primary" icon="el-icon-arrow-down" @click="foldList = []" size="small">展开</el-button>
            <el-button type="primary" icon="el-icon-arrow-up" @click="foldList = foldAllList" size="small">折叠</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-form>

    <el-table :cell-class-name="cellName" :data="itemData" :row-style="toggleDisplayTr" @row-dblclick="rowDblclick" border stripe height="430px" size="small">
      <el-table-column fixed="left" align="center" width="50" label="序号">
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
          <div>{{scope.row.eventType === 1?'行政许可':scope.row.eventType === 2?'审核转报':scope.row.eventType === 3?'备案':'其他服务'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="directoryEncoding" label="办理项编码" title="业务办理项编码" width="90" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="codingImplementation" label="实施编码" title="实施清单编码" width="90" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="serviceObject" label="实施对象" width="110" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="exerciseHierarchy" label="行使层级" width="110" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="state" label="事项状态">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.state === 1?'warning':scope.row.state === 2||scope.row.state === 4?'primary':'success'" disable-transitions>
            {{scope.row.state === 1?'未完善':scope.row.state === 2?'已完善':scope.row.state === 3?'已提交':scope.row.state === 4?'已公示':'确认完成'}}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="margin-top: 1rem;text-align:center;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
  </el-dialog>
</template>

<script>
import { department, departmentalMatters } from '@/api/service'
export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    itemInfos: {
      type: Object,
      default: null
    }
  },
  computed: {
    /**
     * 记录属性结构的首层节点
     */
    foldAllList () {
      return this.itemData.map(x => x.__identity)
    }
  },
  watch: {
    'searchForm.deptName'(val) {
      this.searchForm.deptId = ''
      this.searchForm.deptIdList = []
      // this.$refs.tree.filter(val)
    },
    itemInfos: {
      handler: function (newVal, oldVal) {
        if (newVal) {
          this.searchForm.eventType = newVal.eventType.toString()
          this.searchForm.deptIdList = [newVal.deptId]
          this.searchForm.deptId = newVal.deptId
          this.searchForm.deptName = newVal.deptName
          this.searchByPage()
        }
      },
      deep: true
    },
  },
  data () {
    return {
      // 树数据
      treeData: [],
      listArea:[], //区域
      depTree:[],//部门
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      searchForm: {
        eventName: '',
        state: null,
        examineAndApproveStateList: [7],
        eventType: '',
        deptId: '',
        deptName: '',
        showTree: false,
        deptIdList: []
      },
      searchRules: {},
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,

      itemData: [],
      foldList: [], // 该数组中的值 都会在列表中进行隐藏  死亡名单
    }
  },
  mounted () {
    this.getRegionList()
  },
  methods: {
    renderContent (h, {node, data, store }) {
      return (
        <el-option label={data.title} value={data.id}></el-option>
      )
    },
    // 获取区域
    async getRegionList () {
      let res = await department.selectAll('1', false)
      if(res.code === 0) {
        let list = []
        for (let x of res.obj) {
          if (x.parentId === '0') {
            list.push(this.formatDeptList(x, res.obj))
          }
        }
        this.listArea = list
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
    // 获取部门
    async selectRegion (data) {
      let res = await await department.getByParent(data.id)
      if(res.code === 0) {
        let list = []
        for (let x of res.obj) {
          list.push(this.formatDeptList(x, res.obj))
        }
        this.depTree = list
      }
    },
    //选部门
    clickDepTree(data){
      this.searchForm.deptName = data.title
      this.searchForm.deptId = data.id
      this.searchForm.deptIdList = [data.id]
    },
    // 点击部门树
    async selectTree(data) {
      this.searchForm.deptName = data.title
      this.searchForm.deptId = data.id
    },
    // 树过滤
    filterNode (value, data) {
      if (!value) return true
      return data.title.indexOf(value) !== -1
    },
    cellName ({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1) {
        return 'name-cell'
      }
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
      const { currentPage, pageSize } = this
      let obj = {
        page: currentPage,
        size: pageSize,
        ...this.searchForm,
        eventType: this.searchForm.eventType?Number(this.searchForm.eventType):null
      }
      let res = await departmentalMatters.selectPageByDept(obj)
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
    // 关闭弹层
    closeDialog () {
      this.$refs.ruleForm.resetFields()
      this.$emit('close')
    },
    // 双击表格中数据
    rowDblclick (row, event) {
      this.$refs.ruleForm.resetFields()
      this.$emit('dbclick', row)
    }
  }
}
</script>

<style lang="scss" scoped>
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
