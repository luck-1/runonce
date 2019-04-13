<template>
  <Drawer v-model="drawerModel" width="650" title="数据权限" :styles="styles"
    :mask-closable="false" :closable="false" @on-close="cancel">
    <el-input placeholder="输入关键字进行过滤" v-model="searchKey"> </el-input>
    <el-tree
      class="tree"
      :data="treeData"
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
      <Button style="margin-right: 8px" @click="cancel">确定</Button>
      <!-- <Button type="primary" :loading="loading" @click="save">确定</Button> -->
    </div>
  </Drawer>
</template>

<script>
import { department, role } from '@/api/service'

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
      }
    }
  },
  watch: {
    searchKey(val) {
      this.$refs.dataTree.filter(val)
    }
  },
  mounted () {
    this.getDeptList()
  },
  methods: {
    // 组织架构
    async getDeptList () {
      let res = await department.selectAll('3')
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
      let obj = `?roleId=${this.item.id}&dataType?1&depIds=${depIds}`
      let res = await role.editRoleDep(obj)
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
