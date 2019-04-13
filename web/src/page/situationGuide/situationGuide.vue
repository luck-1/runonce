<template>
  <el-card class="container-card item">
    <el-form :model="searchForm" :rules="searchRules" ref="ruleForm" class="search-form">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="事项名称" prop="eventName" label-width="70px">
            <Input v-model="searchForm.eventName" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button size="small" type="primary" icon="el-icon-search" @click="searchByPage"></el-button>
        </el-col>
      </el-row>
    </el-form>

    <el-button-group style="margin-bottom: 1rem;">
      <el-button type="primary" icon="el-icon-plus"  @click="itemEdit(null, 'addNew')" size="small">增加</el-button>
      <el-button type="primary" icon="el-icon-arrow-down" @click="foldList = []" size="small">展开</el-button>
      <el-button type="primary" icon="el-icon-arrow-up" @click="foldList = foldAllList" size="small">折叠</el-button>
    </el-button-group>

    <el-table :cell-class-name="cellName" :data="itemData" :row-style="toggleDisplayTr" border stripe height="calc(100% - 193px)" size="small" :loading="loading">
      <el-table-column fixed="left" align="center" width="55" label="序号">
        <template slot-scope="{row}">
          <div>{{row.index}}</div>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="事项名称" align="left" :show-overflow-tooltip="true" min-width="200">
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
      <el-table-column prop="eventOfApplication" label="申请原由" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="keyword" label="关键字" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-for="(word,index) in scope.row.keyword" :key="index">{{word}};</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="{row}" v-if="row.isParent" >
          <div class="table-row-btn" >
            <el-tooltip content="修改" placement="top">
              <Button @click="itemEdit(row, 'update')" type="primary" icon="md-create" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <Button @click="itemEdit(row, 'delete')" type="error" icon="md-trash" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="配置办理项" placement="top">
              <Button type="primary" @click="editEventDeploy(row)"  icon="ios-build" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="情形导图" placement="top">
              <Button type="primary" @click="mindPic('情形导图', row)" icon="ios-bookmarks" size="small"></Button>
            </el-tooltip>
            <el-tooltip content="路径引导图" placement="top">
              <Button type="primary" @click="mindPic('路径引导图', row)" icon="logo-designernews" size="small"></Button>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 增加修改 -->
    <DrawerEdit :title="drawerTitle" :item="drawerItem" :drawerModel="drawerModel" @cancel="drawerCancel" @submit="drawerSubmit"></DrawerEdit>
    <!-- 配置办理项 -->
    <EditEvent :parent="parent"  :editEventModel="editEventModel" @cancelEdit="cancelEdit" @submitEdit="submitEdit"></EditEvent>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

     <!-- 情形导图,情形导图 -->
    <el-dialog class="img-setting" :title="situationTitle" fullscreen append-to-body
      :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible">
      <mind :title="situationTitle"></mind>
    </el-dialog>
  </el-card>
</template>

<script>
import mind from './situationMind'
import DrawerEdit from './DrawerEdit'
import EditEvent from './editEvent'
import DeleteDialog from '@/components/dialog'
import { common } from '@/utils/common'

import { situationToGuide } from '@/api/service'
export default {
  name: 'sitiation-guide',
  components: { DrawerEdit, DeleteDialog, EditEvent, mind },
  props: {},
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
      searchForm: {
        eventName: '',
      },
      searchRules: {},
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,

      itemData: [],
      foldList: [], // 该数组中的值 都会在列表中进行隐藏  死亡名单
      multipleSelection: [],

      // 抽屉（表）
      drawerTitle: '',
      drawerModel: false,
      editEventModel:false,
      drawerItem: {},

      // 删除
      showDel: false,
      // 导图
      situationTitle: '',
      dialogVisible: false,
      parent:{},//事项
      loading:false,
    }
  },
  mounted () {
    this.searchByPage()
  },
  methods: {
    formatTime(time) {
      return common.formatDateTime(time)
    },
    cellName ({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1) {
        return 'name-cell'
      }
    },
    /**
     * 搜索
     */
    // 页面显示条数变化
    handleSizeChange (val) {
      this.pageSize = val
      this.searchByPage()
    },
    // 显示页数变化
    handleCurrentChange (val) {
      this.currentPage = val
      this.searchByPage()
    },
    // 查询内容
    async searchByPage () {
      this.loading = true;
      const { currentPage, pageSize, searchForm } = this
      let obj = {
        page: currentPage,
        size: pageSize,
        eventName:searchForm.eventName
      }
      let res = await situationToGuide.selectByPage(obj)
      if (res.code === 0) {
        res.obj.list.forEach((item, index) => {
          item.index = index+1
          item.isParent = true;
          this.formatList(item)
        })
        this.itemData = this.formatConversion([], res.obj.list)
        this.total = res.obj?res.obj.total:0
        this.loading = false;
      }
    },
    //递归实现树节点
    formatList (item) {
      item.Children = item.children || []
      item.isMin = !item.isParent
      for(let x of item.Children) {
        this.formatList(x)
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
    /**
     * 表格内按钮（事项的操作）
     */
    itemEdit (item, oper) {
      switch (oper) {
        case 'addNew':
          this.drawerTitle = '新增事项'
          this.drawerItem = {
            id: null,
            eventName: '',
            eventOfApplication: '',
            keyword: []
          }
          this.drawerModel = true
          break
        case 'update':
          this.drawerTitle = '修改事项'
          this.drawerItem = JSON.parse(JSON.stringify(item))
          this.drawerModel = true
          break
        case 'delete':
          this.showDel = true
          this.drawerItem = item
          break
        default: break
      }
    },
    // 取消
    drawerCancel () {
      this.drawerModel = false
    },
    drawerSubmit() {
      this.drawerModel = false
      this.searchByPage()
    },
    // 取消编辑事项
    cancelEdit () {
      this.editEventModel = false
      this.parent = {};
    },
    // 提交编辑事项
    submitEdit() {
      this.parent = {};
      this.editEventModel = false
      this.searchByPage()
    },
    // 删除
    cancel () {
      this.showDel = false
    },
    //编辑办理项
    editEventDeploy (row){
      this.editEventModel = true
      this.parent = row;
    },
    async del () {
      let res = await situationToGuide.delete(this.drawerItem.id)
      if (res.code === 0) {
        this.$message.success(res.msg)
        this.showDel = false
        this.searchByPage()
      }else{
         this.showDel = false
      }
    },
    mindPic(name,row) {
      localStorage.setItem('situation',JSON.stringify(row))
      this.situationTitle = name
      this.dialogVisible = true
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
.img-setting {
  // z-index: 5000!important;
  width: 90%!important;
  left: 5%!important;
  height: 90%!important;
  top: 5%!important;
  & /deep/ .el-dialog__header {
    // display: none!important;
    font-weight: bold;
    border-left: 6px solid #409eff;
  }
  & /deep/ .el-dialog__body {
    position: relative;
    padding: 0!important;
    height: calc(100% - 54px)!important;
  }
}
</style>
