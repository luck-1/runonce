<template>
  <el-card class="container-card role">
    <!-- <el-form :model="searchForm" :rules="searchRules" label-width="90px" ref="ruleForm" class="search-form">
      <el-row>
        <el-col :span="6">
          <el-form-item label="角色名称" prop="eventName">
            <el-input v-model="searchForm.eventName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" icon="el-icon-search" @click="searchByPage"></el-button>
        </el-col>
      </el-row>
    </el-form> -->

    <el-button-group style="margin-bottom: 1rem;">
      <!-- <el-button type="primary" icon="el-icon-plus" @click="itemEdit(null, 'addNew')" size="small">增加</el-button> -->
      <el-button type="danger" icon="el-icon-delete" size="small" @click="showDel = true" :disabled="!multipleSelection.length">批量删除</el-button>
      <el-button type="primary" icon="el-icon-refresh" size="small" @click="searchByPage">刷新</el-button>
    </el-button-group>

    <el-table :data="itemData" border stripe @selection-change="selectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" fixed="left" label="角色名称"></el-table-column>
      <el-table-column prop="description" label="备注"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间"></el-table-column>
      <el-table-column prop="defaultRole" label="注册用户默认角色">
        <template slot-scope="scope">
          <el-button v-if="scope.row.defaultRole" type="success" size="small" @click="cancelDefault(scope.row)">取消默认</el-button>
          <el-button v-else size="small" type="info" @click="setDefault(scope.row)">设为默认</el-button>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="180">
        <template slot-scope="scope">
          <div class="table-row-btn">
            <el-tooltip content="菜单权限" placement="top">
              <Button @click="editPerm(scope.row)" type="primary" icon="ios-folder-open" size="small"></Button>
            </el-tooltip>
            <!-- <el-tooltip content="数据权限" placement="top">
              <Button @click="editDep(scope.row)" type="primary" icon="md-grid" size="small"></Button>
            </el-tooltip> -->
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

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 新增修改角色 -->
    <DrawerEdit :title="drawerTitle" :item="drawerItem" :drawerModel="drawerModel" @cancel="drawerCancel"></DrawerEdit>

    <!-- 菜单权限 -->
    <DrawerMenu :item="drawerItem" :drawerModel="menuModel" @cancel="menuCancel"></DrawerMenu>

    <!-- 数据权限 -->
    <DrawerData :item="drawerItem" :drawerModel="dataModel" @cancel="dataCancel"></DrawerData>

    <!-- 删除 -->
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>
  </el-card>
</template>

<script>
import DrawerEdit from './DrawerEdit'
import DrawerMenu from './DrawerMenu'
import DrawerData from './DrawerData'
import DeleteDialog from '@/components/dialog'

import { role } from '@/api/service'
export default {
  name: 'role-limit',
  components: { DrawerEdit, DrawerMenu, DrawerData, DeleteDialog },
  data () {
    return {
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
      multipleSelection: [],
      // 抽屉(增加修改)
      drawerTitle: '',
      drawerModel: false,
      drawerItem: {},
      // 菜单权限
      menuModel: false,
      // 数据权限
      dataModel: false,

      // 删除
      showDel: false
    }
  },
  mounted () {
    this.searchByPage()
  },
  methods: {
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
        pageNumber: currentPage,
        pageSize: pageSize,
        sort: 'createTime'
        // ...this.searchForm
      }
      let res = await role.getAllByPage(obj)
      if (res.code === 0) {
        this.itemData = res.obj?res.obj.content:[]
        this.total = res.obj?res.obj.totalElements:0
      }
    },
    // 取消默认
    cancelDefault (row) {
      let arr = `?id=${row.id}&isDefault=false`
      this.$confirm('确定取消默认？').then(async _ => {
        let res = await role.setDefault(arr)
        if(res.code === 0) {
          this.$message.success(res.msg)
          this.searchByPage()
          done();
        }
      }).catch(_ => {})
    },
    // 设为默认
    setDefault (row) {
      let arr = `?id=${row.id}&isDefault=true`
      this.$confirm('确定设为默认？').then(async _ => {
        let res = await role.setDefault(arr)
        if(res.code === 0) {
          this.$message.success(res.msg)
          this.searchByPage()
          done();
        }
      }).catch(_ => {})
    },
    // 选中复选框
    selectionChange (val) {
      this.multipleSelection = val
    },
    /**
     * 表格内按钮（事项的操作）
     */
    // 菜单权限
    editPerm (row) {
      this.drawerItem = row
      this.menuModel = true
    },
    menuCancel () {
      this.menuModel = false
    },
    // 数据权限
    editDep (row) {
      this.drawerItem = row
      this.dataModel = true
    },
    dataCancel () {
      this.dataModel = false
    },
    // 增删改
    itemEdit (item, oper) {
      switch (oper) {
        case 'addNew':
          this.drawerTitle = '新增角色'
          this.drawerItem = {
            departments: [],
            permissions: [],
            id: null
          }
          this.drawerModel = true
          break
        case 'update':
          this.drawerTitle = '修改角色'
          this.drawerItem = item
          this.drawerModel = true
          break
        case 'delete':
          this.showDel = true
          this.multipleSelection = [item]
          break
        default: break
      }
    },
    // 取消
    drawerCancel () {
      this.drawerModel = false
      this.searchByPage()
    },
    // 删除
    cancel () {
      this.showDel = false
    },
    async del () {
      let ids = this.multipleSelection.map(x => x.id)
      let res = await role.delete(ids)
      if(res.code === 0) {
        this.showDel = false
        this.$message.success(res.msg)
        this.searchByPage()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.role {
  display: flex;
  flex-direction: column;
  .item-card {
    flex: 1;
  }
  .el-pagination {
    margin: 1rem 0;
    text-align: center;
  }
}
</style>
