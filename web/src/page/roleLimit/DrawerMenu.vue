<template>
  <Drawer v-model="drawerModel" width="650" title="数据权限" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel">
    <el-input placeholder="输入关键字进行过滤" v-model="searchKey"> </el-input>
    <el-tree
      class="tree"
      :data="treeData"
      show-checkbox
      default-expand-all
      check-on-click-node
      node-key="id"
      ref="dataTree"
      :expand-on-click-node="false"
      :filter-node-method="filterNode"
      highlight-current
      :props="limitProps">
    </el-tree>
    <div class="drawer-footer">
      <Button style="margin-right: 8px" @click="cancel">取消</Button>
      <Button type="primary" :loading="loading" @click="save">确定</Button>
    </div>
  </Drawer>
</template>

<script>
import { menu, role } from '@/api/service'

export default {
  props: {
    item: {
      type: Object
    },
    drawerModel: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      loading: false,
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static'
      },

      searchKey: "", // 搜索树
      loading: false,
      // 树数据
      treeData: [],
      limitProps: {
        children: 'children',
        label: 'title'
      },
      checkedKeys: []
    }
  },
  watch: {
    searchKey(val) {
      this.$refs.dataTree.filter(val)
    },
    'item.permissions' (val) {
      this.checkedKeys = []
      if(val.length) {
        this.checkPermTree(this.treeData, val)
        this.$refs.dataTree.setCheckedNodes(this.checkedKeys)
      }
    }
  },
  mounted () {
    this.getDeptList()
  },
  methods: {
    // 组织架构
    async getDeptList () {
      let res = await menu.getAll()
      if(res.code === 0) {
        this.treeData = res.obj
      }
    },
    // 递归判断子节点
    checkPermTree(permData, rolePerms) {
      permData.forEach((p) => {
        if (this.hasPerm(p, rolePerms)) {
          this.checkedKeys.push(p)
          p.selected = true;
        } else {
          p.selected = false;
        }
        if (p.children && p.children.length > 0) {
          this.checkPermTree(p.children, rolePerms);
        }
      });
    },
    // 判断角色拥有的权限节点勾选
    hasPerm(p, rolePerms) {
      let flag = false;
      for (let i = 0; i < rolePerms.length; i++) {
        if (p.id === rolePerms[i].permissionId) {
          flag = true;
          break;
        }
      }
      if (flag) {
        return true;
      }
      return false;
    },
    // 树过滤
    filterNode (value, data) {
      if (!value) return true
      return data.title.indexOf(value) !== -1
    },
    // 取消
    cancel () {
      this.loading = false
      this.$emit('cancel')
    },
    formatMenuList (data, datas) {
      let obj = data
      obj.children = []
      for (let x of datas) {
        if (obj.fId === x.fParentId) {
          obj.children.push(this.formatMenuList(x, datas))
        }
      }
      return obj
    },
    // 保存
    async save () {
      this.loading = true
      let depIds = [...this.$refs.dataTree.getCheckedKeys(), ...this.$refs.dataTree.getHalfCheckedKeys()]
      let obj = `?roleId=${this.item.id}&&permIds=${depIds}`
      let res = await role.editRolePerm(obj)
      if(res.code === 0) {
        this.cancel()
      } else {
        setTimeout(() => {this.loading = false},1500)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.tree {
  height: calc(100% - 32px);
  overflow: auto;
}
.drawer-footer{
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
