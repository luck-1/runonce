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
          <el-form-item label="办理形式" prop="handlingForm" label-width="70px">
            <el-select v-model="searchForm.handlingForm" placeholder="请选择" size="small" clearable>
              <el-option label="窗口办理" value="窗口办理"></el-option>
              <el-option label="网上办理" value="网上办理"></el-option>
              <el-option label="移动端办理" value="移动端办理"></el-option>
              <el-option label="自助端办理" value="自助端办理"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="服务对象" prop="serviceObject" label-width="70px">
            <el-select v-model="searchForm.serviceObject" placeholder="请选择" size="small" clearable>
              <el-option label="自然人" value="自然人"></el-option>
              <el-option label="法人" value="法人"></el-option>
              <el-option label="个体经商户" value="个体经商户"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="行使层级" prop="exerciseHierarchy" label-width="70px">
            <el-select v-model="searchForm.exerciseHierarchy" placeholder="请选择" size="small" clearable>
              <el-option label="国家级" value="国家级"></el-option>
              <el-option label="省级" value="省级"></el-option>
              <el-option label="市级" value="市级"></el-option>
              <el-option label="县(区)级" value="县(区)级"></el-option>
              <el-option label="镇级" value="镇级"></el-option>
              <el-option label="村级" value="村级"></el-option>
            </el-select>
          </el-form-item>
          <el-button size="small" type="primary" style="float: right" icon="el-icon-search" @click="searchByPage"></el-button>
        </el-col>
      </el-row>
    </el-form>

    <el-button-group style="margin-bottom: 1rem;">
      <el-button type="primary" icon="el-icon-plus"  @click="itemEdit(null, 'addNew')" size="small">增加</el-button>
      <el-button type="primary" icon="el-icon-arrow-down" @click="foldList = []" size="small">展开</el-button>
      <el-button type="primary" icon="el-icon-arrow-up" @click="foldList = foldAllList" size="small">折叠</el-button>
    </el-button-group>

    <el-table :cell-class-name="cellName" :data="itemData" border stripe height="calc(100% - 193px)" size="small">
      <el-table-column fixed="left" align="center" width="55" label="序号" type="index"></el-table-column>
      <el-table-column prop="eventName" fixed="left" label="办理事项名称" align="left" :show-overflow-tooltip="true" min-width="150"></el-table-column>
      <el-table-column prop="serviceObject" label="服务对象" width="120" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="majorTypesOfEvents" label="事项类别" width="120" :show-overflow-tooltip="true">
        <template slot-scope="{row}">
          <div>{{row.majorTypesOfEvents === 1?'行政许可':row.majorTypesOfEvents === 2?'审核转报':row.majorTypesOfEvents === 3?'备案类':'其他服务'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="exerciseHierarchy" label="行使层级" width="110"></el-table-column>
      <el-table-column prop="handlingForm" label="是否适合窗口办理" width="100">
        <template slot-scope="{row}">
          <i class="el-icon-check checked" v-if="row.handlingForm.includes('窗口办理')"></i>
        </template>
      </el-table-column>
      <el-table-column prop="handlingForm" label="是否适合网上办理" width="100">
        <template slot-scope="{row}">
          <i class="el-icon-check checked" v-if="row.handlingForm.includes('网上办理')"></i>
        </template>
      </el-table-column>
      <el-table-column prop="handlingForm" label="是否适合移动端办理" width="100">
        <template slot-scope="{row}">
          <i class="el-icon-check checked" v-if="row.handlingForm.includes('移动端办理')"></i>
        </template>
      </el-table-column>
      <el-table-column prop="handlingForm" label="是否适合自助端办理" width="100">
        <template slot-scope="{row}">
          <i class="el-icon-check checked" v-if="row.handlingForm.includes('自助终端办理')"></i>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center" width="120">
        <template slot-scope="{row}">
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
    <EditEvent :item="itemSetting" :editEventModel="editEventModel" @cancelEdit="cancelEdit" @submitEdit="submitEdit"></EditEvent>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>
  </el-card>
</template>

<script>
import DrawerEdit from './DrawerEdit'
import EditEvent from './editEvent'
import DeleteDialog from '@/components/dialog'
import { common } from '@/utils/common'

import { situationToGuideChild } from '@/api/service'
export default {
  name: 'item-collection',
  components: { DrawerEdit, DeleteDialog, EditEvent },
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
        handlingForm: '',
        serviceObject: '',
        exerciseHierarchy: ''
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
      itemSetting: {},
      drawerItem: {},

      // 删除
      showDel: false,
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
        pageBean: {
          currentPage,
          pageSize
        },
        ...searchForm
      }
      let res = await situationToGuideChild.selectByPage(obj)
      if (res.code === 0) {
        this.itemData = res.obj.list?res.obj.list:[]
        this.total = res.obj?res.obj.total:0
      }
    },
    /**
     * 表格内按钮（事项的操作）
     */
    itemEdit (item, oper) {
      switch (oper) {
        case 'addNew':
          this.drawerTitle = '新增事项'
          this.drawerItem = {
            eventName: '',
            exerciseHierarchy: [],
            handlingForm: [],
            id: null,
            majorTypesOfEvents: '',
            serviceObject: []
          }
          this.drawerModel = true
          break
        case 'update':
          this.drawerTitle = '修改事项'
          let currentItem = JSON.parse(JSON.stringify(item))
          currentItem.serviceObject = currentItem.serviceObject.split(',')
          currentItem.exerciseHierarchy = currentItem.exerciseHierarchy.split(',')
          currentItem.handlingForm = currentItem.handlingForm.split(',')
          this.drawerItem = currentItem
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
    // 取消编辑事项
    cancelEdit () {
      this.editEventModel = false
      this.itemSetting = {}
    },
    drawerSubmit() {
      this.drawerModel = false
      this.searchByPage()
    },
    // 提交编辑事项
    submitEdit() {
      this.editEventModel = false
      this.itemSetting = {}
      this.searchByPage()
    },
    // 删除
    cancel () {
      this.showDel = false
    },
    //编辑办理项
    editEventDeploy (row){
      this.editEventModel = true
      this.itemSetting = row
    },
    async del () {
      let res = await situationToGuideChild.delete(this.drawerItem.id)
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
.checked {
  font-size: 25px;
  color: cadetblue;
}
</style>
