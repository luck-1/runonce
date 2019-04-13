<template>
  <Drawer v-model="editEventModel" width="700" title="选择办理项" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <el-form :model="searchForm" :rules="searchRules" ref="ruleForm" class="search-form">
      <el-row :gutter="20">
        <el-col :span="10">
          <el-form-item label="事项名称" prop="eventName" label-width="70px">
            <Input v-model="searchForm.eventName" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="事项类别" prop="eventType" label-width="70px">
            <el-select v-model="searchForm.eventType" placeholder="请选择" size="small" clearable>
              <el-option label="行政许可" value="1"></el-option>
              <el-option label="审核转报" value="2"></el-option>
              <el-option label="备案类" value="3"></el-option>
              <el-option label="其他服务" value="4"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-button size="small" type="primary" style="float:right" icon="el-icon-search" @click="searchByPage"></el-button>
        </el-col>
      </el-row>
    </el-form>

    <el-table :cell-class-name="cellName" border stripe :data="tableData" @selection-change="selectionChange" height="calc(50vh - 150px)" :row-style="toggleDisplayTr" ref="topTable">
      <el-table-column type="selection" width="50" fixed></el-table-column>
      <el-table-column type="index" width="55" label="序号"></el-table-column>
      <el-table-column  label="办理项" align="left" :show-overflow-tooltip="true" min-width="200">
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
      <el-table-column label="部门名称" prop="deptName"  width="120"></el-table-column>
      <el-table-column label="事项类型" prop="eventType"  width="120" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div>{{scope.row.eventType === 1?'行政许可':scope.row.eventType === 2?'审核转报':scope.row.eventType === 3?'备案类':'其他服务'}}</div>
        </template>
      </el-table-column>
      <el-table-column label="实施对象" prop="serviceObject" width="120"></el-table-column>
      <el-table-column label="行使层级" prop="exerciseHierarchy" width="80"></el-table-column>
    </el-table>
    <el-pagination style="margin-top:1rem;text-align:center;" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" >
    </el-pagination>

    <el-row :gutter="24" style="padding: 10px 0">
      <el-col :span="12">
        <el-button type="primary" icon="el-icon-arrow-down" circle style="float:right" :disabled="selection.length===0" @click="arrowDown"></el-button>
        </el-col>
      <el-col :span="12">
        <el-button type="primary" icon="el-icon-arrow-up" circle :disabled="currentSelection.length===0" @click="arrowUp"></el-button>
      </el-col>
    </el-row>

    <el-table :cell-class-name="cellName" border stripe :data="currentData" @selection-change="currentSelectionChange" height="calc(50vh - 150px)" :row-style="toggleDisplayTr" ref="bottomTable">
      <el-table-column type="selection" width="50" fixed></el-table-column>
      <el-table-column type="index" width="55" label="序号"></el-table-column>
      <el-table-column  label="办理项" align="left" :show-overflow-tooltip="true" min-width="200">
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
      <el-table-column label="部门名称" prop="deptName"  width="120"></el-table-column>
      <el-table-column label="事项类型" prop="eventType"  width="120" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div>{{scope.row.eventType === 1?'行政许可':scope.row.eventType === 2?'审核转报':scope.row.eventType === 3?'备案类':'其他服务'}}</div>
        </template>
      </el-table-column>
      <el-table-column label="实施对象" prop="serviceObject" width="120"></el-table-column>
      <el-table-column label="行使层级" prop="exerciseHierarchy" width="80"></el-table-column>
    </el-table>

    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel('item')">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm('item')">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { departmentalMatters, situationToGuideChild } from '@/api/service'
import { validatSpace, validateCharNumber } from '@/utils/validate'
export default {
  props: {
    editEventModel: {
      type: Boolean,
      default: false
    },
    item: {
      type: Object
    }
  },
   computed: {
    /**
     * 记录属性结构的首层节点
     */
    foldAllList () {
      return this.tableData.map(x => x.__identity)
    }
  },
  data () {
    const checkName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'))
      } else if (validatSpace(value)) {
        callback(new Error('不能包含空字符'))
      }
      callback()
    }
    return {
      loading: false,
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },
      searchForm: {
        eventName: '',
        eventType: '',
        showTree: false,
      },
      searchRules: {},
      // 服务对象
      serviceObjs: [],
      // 事项小类型
      samllPro: [],

      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,
      // 展开
      currentData: [],
      currentSelection: [],
      foldList: [],
      selection: [],
      tableData: []
    }
  },
  watch: {
    'item.id' (val) {
      if(val) {
        this.searchCurrent(val)
      }
    }
  },
  mounted () {},
  methods: {
    selectionChange(selection) {
      this.selection = selection
    },
    currentSelectionChange (selection) {
      this.currentSelection = selection
    },
    arrowDown () {
      this.selection.forEach(item => {
        let current = this.currentData.filter(x => x.id === item.id)
        if(current.length===0) {
          this.currentData.push(item)
        }
      })
      this.$refs.topTable.clearSelection()
      // this.selection = []
    },
    arrowUp () {
      this.currentSelection.forEach(item => {
        let index = this.currentData.findIndex(x => x.id === item.id)
        this.currentData.splice(index, 1)
      })
      this.$refs.bottomTable.clearSelection()
    },
    // 取消
    cancel (name) {
      this.loading = false
      // this.$refs[name].resetFields()
      this.$emit('cancelEdit')
    },
    // 提交
    async submitForm (name) {
      this.loading = true
      let departmentalMattersList = this.currentData.map(x => x.id)
      let res = await situationToGuideChild.setting({
        departmentalMattersList,
        id: this.item.id
      })
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.loading = false
        this.searchCurrent(this.item.id)
        // this.$emit('submitEdit')
      } else {
        this.$message.error(res.msg)
        setTimeout(() => {this.loading = false},1500)
      }
    },
    async searchCurrent (id) {
      let res = await situationToGuideChild.searchConfig(id)
      if(res.code === 0) {
        let menuList = []
        let index = 0
        for (let item of res.obj) {
          if (item.eventPid === '0') {
            item.index = ++index
            menuList.push(this.formatList(item, res.obj))
          }
        }
        this.currentData = this.formatConversion([], menuList)
      } else {
        this.$message.error(res.msg)
      }
    },
    cellName ({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 2) {
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
      const { currentPage, pageSize, searchForm } = this
      let obj = {
        page: currentPage,
        size: pageSize,
        ...searchForm
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
        this.tableData = this.formatConversion([], menuList)
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
  },
}
</script>

<style lang="scss" scoped>
.drawer-footer{
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
  z-index: 1;
}
.select-option {
  width: 100%;
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
</style>
