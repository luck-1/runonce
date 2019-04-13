<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-card class="container-card department">
        <el-button-group style="margin-bottom: 1rem;">
          <el-button type="primary" icon="el-icon-plus"  @click="regionEdit('addNew')" size="small">添加一级区域</el-button>
          <el-button type="primary" icon="el-icon-plus"  @click="regionEdit('addChild')" size="small">添加子区域</el-button>
          <el-button type="primary" icon="el-icon-edit"  @click="regionEdit('update')" size="small">修改</el-button>
          <el-button type="danger" icon="el-icon-delete"  @click="regionEdit('delete')" size="small">删除</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="small" @click="getRegion">刷新</el-button>
        </el-button-group>
        <el-tree class="dept-tree" ref="regionTree" default-expand-all  highlight-current node-key="id"
          :data="regions"
          :props="defaultProps"
          :expand-on-click-node="false"
          @node-click="selectRegion"></el-tree>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card class="container-card department">
        <el-button-group style="margin-bottom: 1rem;">
          <el-button type="primary" icon="el-icon-share"  @click="itemEdit('transfer')" size="small">数据同步</el-button>
          <el-button type="primary" icon="el-icon-plus"  @click="itemEdit('addNew')" size="small">添加一级部门</el-button>
          <el-button type="primary" icon="el-icon-plus"  @click="itemEdit('addChild')" size="small">添加子部门</el-button>
          <el-button type="primary" icon="el-icon-edit"  @click="itemEdit('update')" size="small">修改</el-button>
          <el-button type="danger" icon="el-icon-delete"  @click="itemEdit('delete')" size="small">删除</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="small" @click="getDeptList(currentRegion.id)" :disabled="currentRegion&&!currentRegion.id">刷新</el-button>
        </el-button-group>
        <el-tree class="dept-tree" ref="tree" default-expand-all  highlight-current node-key="id"
          :data="treeData"
          :props="defaultProps"
          :expand-on-click-node="false"
          @node-click="selectTree"></el-tree>
      </el-card>
    </el-col>
    <DrawerEdit :title="drawerTitle" :item="drawerItem" :drawerModel="drawerModel" @cancel="drawerCancel"></DrawerEdit>

    <!-- 删除 -->
    <DeleteDialog :type="delType" :visible="showDel" @cancel="cancel" @delete="del">
      <div>{{delType=="danger"?"确认删除？删除后不可恢复":"确认数据同步？数据同步不可逆"}}</div>
    </DeleteDialog>
  </el-row>
</template>

<script>
import DrawerEdit from './DrawerEdit'
import DeleteDialog from '@/components/dialog'

import { department } from '@/api/service'
export default {
  name: 'department',
  components: { DrawerEdit, DrawerEdit, DeleteDialog },
  data () {
    return {
      searchKey: "", // 搜索树
      loading: false,

      // 区域
      regions: [],
      currentRegion: null,
      // 树数据
      treeData: [],
      currentDept: {},

      defaultProps: {
        children: 'children',
        label: 'title'
      },

      // 抽屉（表）
      drawerTitle: '',
      drawerModel: false,
      drawerItem: {},

      // 删除
      showDel: false,
      delType: 'danger'
    }
  },
  mounted () {
    // 获取区域
    this.getRegion()
  },
  methods: {
    getRegion () {
      this.getList('1').then(res => {
        this.regions = res
        this.$nextTick(() => {
          this.currentRegion = this.currentRegion || res[0]
          this.$refs.regionTree.setCurrentKey(this.currentRegion.id);
          this.getDeptList(this.currentRegion.id)
        })
      })
    },
    // 组织架构
    getDeptList () {
      this.getList('2', this.currentRegion.id).then(res => {
        this.treeData = res
        this.currentDept = {}
      })
      // this.$nextTick(() => {
      //   this.$refs.tree.setCurrentKey(this.selectNode.id||res.obj[0].id);
      //   this.selectTree(res.obj[0])
      // })
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
    // 区域点击
    selectRegion (data) {
      this.currentRegion = data
      this.getDeptList(this.currentRegion.id)
    },
    // 区域操作
    regionEdit (oper) {
      switch (oper) {
        case 'addChild':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个区域')
            return
          }
          this.drawerTitle = '新增区域'
          this.drawerItem = {
            id: null,
            areaCode: this.currentRegion.id,
            parentId: this.currentRegion.id,
            status: true,
            isLocation: true,
            region: '1',
            centralDepartments: '',
            socialCreditCode: ''
          }
          this.drawerModel = true
          break
        case 'addNew':
          this.drawerTitle = '新增区域'
          this.drawerItem = {
            parentId: '0',
            id: null,
            areaCode: this.currentRegion.id,
            status: true,
            isLocation: true,
            region: '1',
            centralDepartments: '',
            socialCreditCode: ''
          }
          this.drawerModel = true
          break
        case 'update':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个区域')
            return
          }
          this.drawerTitle = '修改区域'
          this.drawerItem = this.currentRegion
          this.drawerItem.centralDepartments = ''
          this.drawerItem.socialCreditCode = ''
          this.drawerItem.areaCode = this.currentRegion.id
          this.drawerItem.status = Number(this.drawerItem.status) === 0
          this.drawerModel = true
          break
        case 'delete':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个区域')
            return
          }
          if(this.currentRegion.children.length) {
            this.$message.warning('请先删除子区域')
            return
          }
          department.checkHasChildren(this.currentRegion.id).then(res => {
            if(res.code===0) {
              this.showDel = true
              this.drawerItem = this.currentRegion
            }
          },res => {})
          break
        default: break
      }
    },

    // 部门树点击
    selectTree (data) {
      this.currentDept = data
    },
    // 部门操作
    itemEdit (oper) {
      switch (oper) {
        case 'transfer':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个组织')
            return
          }
          this.delType = 'warning'
          this.showDel = true
          break
        case 'addChild':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个组织')
            return
          }
          if (!this.currentDept.id) {
            this.$message.warning('请选择一个部门')
            return
          }
          this.drawerTitle = '新增部门'
          this.drawerItem = {
            id: null,
            parentId: this.currentDept.id,
            areaCode: this.currentRegion.id,
            status: true,
            isLocation: false,
            region: '1',
            centralDepartments: '',
            socialCreditCode: ''
          }
          this.drawerModel = true
          break
        case 'addNew':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个组织')
            return
          }
          this.drawerTitle = '新增部门'
          this.drawerItem = {
            parentId: this.currentRegion.id,
            id: null,
            areaCode: this.currentRegion.id,
            status: true,
            isLocation: false,
            region: '1',
            centralDepartments: '',
            socialCreditCode: ''
          }
          this.drawerModel = true
          break
        case 'update':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个组织')
            return
          }
          if (!this.currentDept.id) {
            this.$message.warning('请选择一个部门')
            return
          }
          this.drawerTitle = '修改部门'
          this.drawerItem = this.currentDept
          this.drawerItem.centralDepartments = this.drawerItem.centralDepartments || ''
          this.drawerItem.socialCreditCode = this.drawerItem.socialCreditCode || ''
          this.drawerItem.areaCode = this.currentRegion.id
          this.drawerItem.status = Number(this.drawerItem.status) === 0
          this.drawerModel = true
          break
        case 'delete':
          if (!this.currentRegion.id) {
            this.$message.warning('请选择一个组织')
            return
          }
          if (!this.currentDept.id) {
            this.$message.warning('请选择一个部门')
            return
          }
          if(this.currentDept.children.length) {
            this.$message.warning('请先删除子部门')
            return
          }
          department.checkHasChildren(this.currentDept.id).then(res => {
            if(res.code===0) {
              this.delType = 'danger'
              this.showDel = true
              this.drawerItem = this.currentDept
            }
          },res => {})
          break
        default: break
      }
    },

    // 取消
    drawerCancel () {
      this.drawerModel = false
      if (this.drawerTitle === '新增部门') {
        this.getDeptList()
        this.currentDept = {}
      } else {
        this.getRegion()
        this.currentRegion = null
      }
    },
    // 删除
    cancel () {
      this.showDel = false
    },
    async del () {
      if(this.delType == 'danger') {
        department.delete([this.drawerItem.id]).then(res => {
          if(res.code === 0) {
            this.$message.success('删除成功')
            this.showDel = false
            if (this.drawerTitle === '新增部门') {
              this.getDeptList()
              this.currentDept = {}
            } else {
              this.getRegion()
              this.currentRegion = null
            }
          }
        }, res => {})
      } else {
        console.log(this.currentRegion.id)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.department {
  & /deep/ .el-card__body {
    height: 100%;
    .dept-tree {
      height: calc(100% - 48px);
      overflow: auto;
    }
  }
}
</style>
