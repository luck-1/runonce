<template>
  <Drawer v-model="editEventModel" width="700" title="选择办理项" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel('item')">
    <!-- <el-form    label-width="100px">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="事项名称"  >
            <el-input  placeholder="请填写" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="所属部门" prop="directoryEncoding" >
            <el-input  placeholder="请填写" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="事项类别" prop="remarks">
            <el-input  placeholder="请填写" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form> -->
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
          <el-button size="small" type="primary" style="float: right" icon="el-icon-search" @click="searchByForm"></el-button>
        </el-col>
      </el-row>
    </el-form>
   
    <el-table :cell-class-name="cellName" border stripe :data="tableData" @selection-change="selectionChange" height="calc(50vh - 150px)" :row-style="toggleDisplayTr" ref="topTable">
      <el-table-column type="selection" width="50" fixed></el-table-column>
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

    <el-table :cell-class-name="cellName" border stripe :data="childrenData" @selection-change="currentSelectionChange" height="calc(50vh - 150px)" :row-style="toggleDisplayTr" ref="bottomTable">
      <el-table-column type="selection" width="50" fixed></el-table-column>
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
    </el-table>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel">取消</Button>
      <Button type="primary" :loading="loading" @click="submitForm">提交</Button>
    </div>
  </Drawer>
</template>

<script>
import { situationToGuide,situationToGuideChild } from '@/api/service'
import { validatSpace, validateCharNumber } from '@/utils/validate'
export default {
  props: {
    editEventModel: {
      type: Boolean,
      default: false
    },
    parent:{
      type: Object,
    },
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
      // 服务对象
      serviceObjs: [],
      // 事项小类型
      samllPro: [],

      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,
      // 展开
      foldList: [],
      tableData:[],
      childrenData:[],
      searchForm: {
        eventName: '',
        handlingForm: '',
        serviceObject: '',
        exerciseHierarchy: ''
      },
      searchRules: {},
      currentSelection: [],
      selection: [],
    }
  },
  mounted () {
    this.searchByForm()
  },
  watch: {
    'parent.id' (val) {
      if(val) {
        this.searchByPId(val)
      }
    },
    'this.editEventModel'(){
      this.searchByPId(parent.id)
    }
  },
  methods: {
    selectionChange(selection) {
      this.selection = selection
    },
    currentSelectionChange (selection) {
      this.currentSelection = selection
    },
    arrowDown () {
      this.selection.forEach(item => {
        let current = this.childrenData.filter(x => x.id === item.id)
        if(current.length===0) {
          this.childrenData.push(item)
        }
      })
      this.$refs.topTable.clearSelection()
      // this.selection = []
    },
    arrowUp () {
      this.currentSelection.forEach(item => {
        let index = this.childrenData.findIndex(x => x.id === item.id)
        this.childrenData.splice(index, 1)
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
    async submitForm () {
      this.loading = true
      let eventChildrenList = this.childrenData.map(x => x.id)
      let res = await situationToGuide.addChildren({
        childId:eventChildrenList,
        pid: this.parent.id
      })
      if(res.code === 0) {
        this.$message.success(res.msg)
        this.loading = false
        this.$emit('submitEdit')
      } else {
        this.$message.error(res.msg)
        setTimeout(() => {this.loading = false},1500)
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
      this.searchByForm()
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.searchByForm()
    },
    
    async searchByForm(){
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
        this.tableData = res.obj.list?res.obj.list:[]
        this.total = res.obj?res.obj.total:0
      }
    },
    async searchByPId (id) {
      let res = await situationToGuide.findChildren(id)
      if (res.code === 0) {
        res.obj.forEach((item, index) => {
          item.index = index+1
        })
        this.childrenData = res.obj?res.obj:[]
      }
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
