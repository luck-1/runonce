<template>
  <div>
    <el-card class="container-card item-allot">
      <el-row type="flex" justify="space-between" class="code-row-bg">
        <el-col v-if="expand" :span="5" class="dept">
          <div class="region-select">
            <span>区域：</span>
            <el-select class="tree-select" :popper-append-to-body="false" v-model="regionId" placeholder="请选择" @change="getDeptList">
              <el-tree ref="regionTree" default-expand-all node-key="id"
              :expand-on-click-node="false" :data="regionData" :filter-node-method="filterNode" :render-content="renderContent"></el-tree>
            </el-select>
          </div>
          <el-input placeholder="输入关键字进行过滤" v-model="searchKey"><el-button slot="append" icon="el-icon-refresh" @click="getDeptList"></el-button></el-input>
          <!-- <el-input v-model="searchKey" placeholder="输入节点名搜索" clearable>
            <el-button slot="append" icon="el-icon-search" @click="getDeptList"></el-button>
          </el-input> -->
          <div class="tree-bar">
            <el-tree ref="tree" :expand-on-click-node="false" default-expand-all :data="treeData" :filter-node-method="filterNode" :props="defaultProps" highlight-current node-key="id" @node-click="selectTree"></el-tree>
          </div>
        </el-col>
        <div class="expand">
          <Icon :type="expandIcon" size="16" class="icon" @click="changeExpand"/>
        </div>
        <el-col :span="span">
          <el-row>
            <el-form :model="searchForm" :rules="searchRules" label-width="80px" ref="ruleForm" class="search-form">
              <el-row>
                <el-col :span="6">
                  <el-form-item label="事项名称" prop="eventName">
                    <el-input v-model="searchForm.eventName" placeholder="请输入" clearable />
                  </el-form-item>
                </el-col>
                <!-- <el-col :span="6">
                  <el-form-item label="事项状态" prop="eventState">
                    <el-select v-model="searchForm.eventState" placeholder="请选择">
                      <el-option label="未完善" value="1"></el-option>
                      <el-option label="已完善" value="2"></el-option>
                      <el-option label="已提交" value="3"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col> -->
                <el-col :span="6">
                  <el-form-item label="事项类别" prop="eventType">
                    <el-select v-model="searchForm.eventType" placeholder="请选择" clearable filterable>
                      <el-option label="行政许可" value="1"></el-option>
                      <el-option label="审核转报" value="2"></el-option>
                      <el-option label="备案类" value="3"></el-option>
                      <el-option label="其他服务" value="4"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-button type="primary" icon="el-icon-search" style="margin-left: 20px" @click="searchByPage"></el-button>
                </el-col>
              </el-row>
            </el-form>
          </el-row>
          <el-button-group style="margin-bottom: 1rem;">
            <el-button type="primary" icon="el-icon-plus"  @click="itemEdit(null, 'addNew')" size="small">增加</el-button>
            <el-button type="primary" icon="el-icon-bell" :disabled="!multipleSelection.length"  @click="allot" size="small">分配</el-button>
            <!-- <el-button type="danger" icon="el-icon-delete" :disabled="!multipleSelection.length" size="small">删除</el-button> -->
            <!-- <el-button type="primary" icon="el-icon-refresh" size="small" @click="searchByPage">刷新</el-button> -->
            <el-button type="primary" icon="el-icon-arrow-down" @click="foldList = []" size="small">展开</el-button>
            <el-button type="primary" icon="el-icon-arrow-up" @click="foldList = foldAllList" size="small">折叠</el-button>
          </el-button-group>

          <div class="table-page">
            <el-table :cell-class-name="cellName" size="small" ref="itemdata" :data="itemData" :row-style="toggleDisplayTr" border stripe @select="selectRow" @select-all="selectAll" height="calc(100% - 64px)">
              <el-table-column type="selection" width="40"></el-table-column>
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
                    <Icon class="permission_toggleFold" :class="toggleClass(scope.row)" :type="toggleFoldingClass(scope.row)"
                       @click="scope.row.Children.length!==0?toggleFoldingStatus(scope.row):''"/>
                    {{scope.row.eventName}}
                  </p>
                </template>
              </el-table-column>
              <el-table-column prop="eventType" label="事项类别" width="120" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <div>{{scope.row.eventType === 1?'行政许可':scope.row.eventType === 2?'审核转报':scope.row.eventType === 3?'备案类':'其他服务'}}</div>
                </template>
              </el-table-column>
              <el-table-column prop="directoryEncoding" label="办理项编码" title="业务办理项编码" width="90" :show-overflow-tooltip="true"></el-table-column>
              <el-table-column prop="codingImplementation" label="实施编码" title="实施清单编码" width="90" :show-overflow-tooltip="true"></el-table-column>
              <el-table-column prop="serviceObject" label="实施对象" width="110" :show-overflow-tooltip="true"></el-table-column>
              <el-table-column prop="exerciseHierarchy" label="行使层级" width="110" :show-overflow-tooltip="true"></el-table-column>
              <!-- <el-table-column prop="eventState" label="事项状态">
                <template slot-scope="scope">
                  <el-tag size="small" :type="scope.row.state === 1?'warning':scope.row.state === 2||scope.row.state === 4?'primary':'success'" disable-transitions>
                    {{scope.row.state === 1?'未完善':scope.row.state === 2?'已完善':scope.row.state === 3?'已提交':scope.row.state === 4?'已公示':'确认完成'}}
                  </el-tag>
                </template>
              </el-table-column> -->
              <!-- <el-table-column prop="exerciseHierarchy" label="实施机关"></el-table-column>
              <el-table-column prop="implementingOrgan" label="通用名称"></el-table-column>
              <el-table-column prop="directoryName" label="目录名称"></el-table-column>
              <el-table-column prop="settingBasis" label="设定依据"></el-table-column> -->

              <el-table-column fixed="right" label="操作" width="130" align="center">
                <template slot-scope="scope">
                  <div class="table-row-btn">
                    <el-tooltip content="增加子项" placement="top">
                      <Button @click="itemEdit(scope.row, 'addChild')" type="primary" icon="ios-add" size="small" :disabled="scope.row.isMin"></Button>
                    </el-tooltip>
                    <el-tooltip content="修改" placement="top">
                      <Button @click="itemEdit(scope.row, 'update')" type="primary" icon="md-create" size="small"></Button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top">
                      <Button @click="itemEdit(scope.row, 'delete')" type="error" icon="md-trash" size="small"></Button>
                    </el-tooltip>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <el-row type="flex" justify="center" class="page">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30, 40]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
              </el-pagination>
            </el-row>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 增加修改 -->
    <DrawerEdit :title="drawerTitle" :parent="parentItem" :item="drawerItem" :drawerModel="drawerModel" @cancel="drawerCancel"></DrawerEdit>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>
  </div>
</template>

<script>
import DrawerEdit from './DrawerEdit'
import DeleteDialog from '@/components/dialog'

import { eventTable, department } from '@/api/service'
export default {
  name: 'item-allot',
  components: { DrawerEdit, DeleteDialog },
  computed: {
    /**
     * 记录属性结构的首层节点
     */
    foldAllList () {
      return this.itemData.map(x => x.__identity)
    }
  },
  data () {
    return {
      loading: false, // 表加载状态
      searchKey: "", // 搜索树
      expand: true,
      span: 18,
      expandIcon: "ios-arrow-back",
      selectNode: {},
      // 区域
      regionData: [],
      regionId: '',
      // 树数据
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'title'
      },

      searchForm: {
        eventName: '',
        eventState: '',
        eventType: ''
      },
      searchRules: {},
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,

      itemData: [],
      toggleList: [],
      foldList: [], // 该数组中的值 都会在列表中进行隐藏  死亡名单
      multipleSelection: [],

      // 抽屉（表）
      drawerTitle: '',
      drawerModel: false,
      drawerItem: {
        id: null,
        eventPid: null,
        eventNameVal: '',
        eventName: '',
        isMin: true,
        eventType: '',
        itemSmallType: '',
        exerciseHierarchy: [],
        serviceObject: [],
        isReproducible: false
      },
      parentItem: {},

      // 删除
      showDel: false
    }
  },
  watch: {
    searchKey(val) {
      this.$refs.tree.filter(val)
    },
    itemData (val) {

    }
  },
  mounted () {
    // 获取区域
    this.getRegion()
    this.searchByPage()
  },
  methods: {
    cellName ({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 2) {
        return 'name-cell'
      }
    },
    getRegion () {
      this.getList('1').then(res => {
        this.regionData = res
        this.$nextTick(() => {
          this.regionId = this.regionId||this.regionData[0].id
          this.$refs.regionTree.setCurrentKey(this.regionId)
          this.getDeptList()
        })
      })
    },
    renderContent (h, {node, data, store }) {
      return (
        <el-option label={data.title} value={data.id}></el-option>
      )
    },
    // 组织架构
    getDeptList () {
      this.multipleSelection = []
      this.toggleList = []
      this.selectNode = null
      this.itemData.forEach(x => {
        x.checked = false
        x.historyCheck = false
        this.$refs.itemdata.toggleRowSelection(x, false)
      })
      this.getList('2', this.regionId).then(res => {
        this.treeData = res
      })
    },
    async getList (flag, parentId=null) {
      let res = null
      if(parentId) {
        res = await department.getByParent(parentId)
      } else {
        res = await department.selectAll(flag)
      }
      if(res.code === 0) {
        let list = []
        for (let x of res.obj) {
          if (parentId) {
            if (x.parentId === parentId) {
              list.push(this.formatDeptList(x, res.obj))
            }
          } else {
            if (x.parentId === '0') {
              list.push(this.formatDeptList(x, res.obj))
            }
          }
        }
        return list
      } else {
        return []
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
    // 点击部门树
    async selectTree(data) {
      this.multipleSelection = []
      this.selectNode = data
      this.$refs.itemdata.clearSelection()
      let res = await eventTable.selectByDeptId(data.id)
      if (res.code === 0) {
        this.toggleList = res.obj ? res.obj : []
        for (let item of this.itemData) {
          item.checked = false
          item.historyCheck = false
          for(let x of this.toggleList) {
            if(item.id === x.id) {
              item.checked = true
              item.historyCheck = true
              this.multipleSelection.push(item)
              this.$refs.itemdata.toggleRowSelection(item, true)
            }
          }
        }
      }
    },
    // 树过滤
    filterNode (value, data) {
      if (!value) return true
      return data.title.indexOf(value) !== -1
    },
    // 是否折叠左边树
    changeExpand() {
      this.expand = !this.expand;
      if (this.expand) {
        this.expandIcon = "ios-arrow-back";
        this.span = 18;
      } else {
        this.expandIcon = "ios-arrow-forward";
        this.span = 23;
      }
    },
    // 搜索
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
        ...this.searchForm
      }
      this.selectNode?this.selectTree(this.selectNode):''
      this.itemData = []
      let res = await eventTable.getList(obj)
      if (res.code === 0) {
        let menuList = []
        let index = 0
        for (let item of res.obj.list) {
          if (item.eventPid === '0') {
            item.index = ++index
            item.checked = false
            item.historyCheck = false
            menuList.push(this.formatList(item, res.obj.list))
          }
        }
        this.itemData = this.formatConversion([], menuList)
        if(this.toggleList.length) {
          this.$nextTick(() => {
            for (let item of this.itemData) {
              for(let x of this.toggleList) {
                if(item.id === x.id) {
                  item.historyCheck = true
                  this.$refs.itemdata.toggleRowSelection(item)
                }
              }
            }
          })
        }
        this.total = res.obj?res.obj.total:0
      }
    },
    //递归实现树节点
    formatList (item, menus) {
      item.Children = []
      for(let x of menus) {
        if(item.id === x.eventPid) {
          item.checked = false
          x.historyCheck = false
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
    // 全选
    selectAll (selection) {
      if (selection.length) {
        // selection.forEach(x => x.checked = true)
        this.itemData.forEach(x => x.checked = true)
        this.multipleSelection = selection
      } else {
        this.itemData.forEach(x => x.checked = false)
        this.multipleSelection = []
      }
    },
    // 选择单行
    selectRow (selection, row) {
      row.checked = !row.checked
      if (row.checked) {
        this.multipleSelection.push(row)
      } else {
        let index = this.multipleSelection.findIndex(x => x.id === row.id)
        if(index>=0) {
          this.multipleSelection.splice(index, 1)
        }
      }
      this.childList(row,this.itemData)
      this.parentList(row, this.itemData)
    },
    // 查找父节点
    parentList (row, menus) {
      // for (let [index, item] of menus.entries()) {
      menus.forEach((item, index) => {
        // 找父节点
        if (item.id === row.eventPid) {
          // 获取父节点所有选中的子节点
          let checkedChild = menus.filter(x => x.eventPid === item.id && x.checked)
          item.checked = checkedChild.length ? true : false
          this.$refs.itemdata.toggleRowSelection(item, item.checked)
          if(item.checked) {
            // 若选中的集合中不存在该数据则添加
            if (this.multipleSelection.findIndex(x => x.id === item.id)<0) {
              this.multipleSelection.push(item)
            }
          } else {
            let index = this.multipleSelection.findIndex(obj => obj.id === item.id)
            if(index>=0) {
              this.multipleSelection.splice(index, 1)
            }
          }
          this.parentList(item, menus)
        }
      })
    },
    // 查找子节点
    childList (item, menus) {
      for(let x of menus) {
        if(item.id === x.eventPid) {
          x.checked = item.checked
          if(x.checked) {
            this.multipleSelection.push(x)
          } else {
            let index = this.multipleSelection.findIndex(obj => obj.id === x.id)
            if(index>=0) {
              this.multipleSelection.splice(index, 1)
            }
          }
          this.$refs.itemdata.toggleRowSelection(x, x.checked);
          this.childList(x, menus)
        }
      }
    },
    // 给组织架构树分配事项
    async allot () {
      // 当前页未被选中
      let current = []
      let eventIdList = []

      // 当前页选中（包括部门选中原始数据）
      this.itemData.map(item => {
        if (item.checked || item.historyCheck) {
          current.push(item)
        }
      })
      // 封装后的数据
      current.map(x => {
        if (x.eventPid === '0') {
          eventIdList.push(this.childIdList([], x, current))
        }
      })
      if(this.selectNode.id) {
        let res = await eventTable.distribution({
          deptId: this.selectNode.id,
          eventIdList
        })
        if(res.code === 0) {
          this.$message.success(res.msg)
          // this.selectNode = {}
          // this.$refs.tree.setCurrentKey(null)
          // this.multipleSelection = []
          // this.$refs.itemdata.clearSelection()
          // this.itemData.forEach(x => {
          //   x.checked = false
          //   x.historyCheck = false
          // })
        }
      } else {
        this.$message.warning('请选择一个部门！')
      }
    },
    childIdList (children, item, menus) {
      children.push(item)
      for(let x of menus) {
        if(item.id === x.eventPid) {
          this.childIdList(children, x, menus)
        }
      }
      return children;
    },
    /**
     * 表格内按钮（事项的操作）
     */
    itemEdit (item, oper) {
      switch (oper) {
        case 'addChild':
          this.drawerTitle = '新增事项'
          this.parentItem = item
          this.drawerItem = {
            ...this.drawerItem,
            eventPid: item.id,
            eventNameVal: item.eventName,
            isReproducible: this.parentItem.isReproducible,
            eventType: this.parentItem.eventType?String(this.parentItem.eventType):'',
            itemSmallType: this.parentItem.itemSmallType
          }
          this.drawerModel = true
          break
        case 'addNew':
          this.drawerTitle = '新增事项'
          this.parentItem = {}
          this.drawerItem = {
            ...this.drawerItem,
            eventPid: '0',
            isMin: false
          }
          this.drawerModel = true
          break
        case 'update':
          this.drawerTitle = '修改事项'
          this.parentItem = this.itemData.filter(x => x.id === item.eventPid)[0]
          this.drawerItem = {
            ...item,
            eventNameVal: item.eventName,
            isReproducible: this.parentItem.isReproducible,
            exerciseHierarchy: item.exerciseHierarchy?item.exerciseHierarchy.split(','):[],
            serviceObject: item.serviceObject?item.serviceObject.split(','):[],
            eventType: item.eventType?item.eventType.toString():''
          }
          this.drawerModel = true
          break
        case 'delete':
          if (item.Children.length !== 0) {
            this.$message.warning('请先删除子项')
            return
          }
          this.showDel = true
          this.drawerItem = item
          break
        default: break
      }
    },

    // 取消
    drawerCancel () {
      this.drawerModel = false
      this.parentItem = {}
      this.searchByPage()
    },
    // 删除
    cancel () {
      this.showDel = false
    },
    async del () {
      let res = await eventTable.delete(this.drawerItem.id)
      if (res.code === 0) {
        this.$message.success(res.msg)
        this.showDel = false
        this.searchByPage()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.item-allot {
  & /deep/ .el-card__body,
  .code-row-bg {
    height: 100%;
  }
  .dept {
    height: 100%;
  }
  .region-select {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 10px;
    .tree-select {
      flex: 1;
      width:100%;
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
  }
  .tree-bar {
    margin-top: 10px;
    height: calc(100% - 80px);
    overflow: auto;
  }
  .table-page {
    // height: calc(100% - 48px);
    height: calc(100% - 80px);
    overflow: auto;
  }
  .page {
    margin: 1rem 0;
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
.expand {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  .icon {
    cursor: pointer;
  }
  :hover {
    color: #1890ff !important;
  }
}
</style>
