<template>
  <Modal :title="title" v-model="visible" :mask-closable='false' :width="600">
    <Form ref="changeForm" :model="changeForm" :label-width="70" :rules="formValidate">
      <Form-item label="所属部门" prop="departmentTitle">
        <Poptip trigger="click" placement="right" width="500" style="width:70%">
          <div slot="title">
            <el-row>
            <el-col :span="24">选择部门</el-col>
            </el-row>
          </div>
          <div style="display:flex;">
            <Input v-model="changeForm.departmentTitle" placeholder="请选择" readonly style="width:calc(72% - 45px);margin-right:20px;"/>
            <Button icon="md-trash" @click="clearSelectDep">清空已选</Button>
          </div>
          <div slot="content" class="tree-bar">
            <el-row>
              <el-col :span="13">
                <el-tree class="dept-tree" ref="regionTree" default-expand-all  highlight-current node-key="id" :data="regionData" :props="defaultProps" :expand-on-click-node="false" @node-click="selectRegion"></el-tree>
              </el-col>
              <el-col :span="11">
                <el-tree class="dept-tree" ref="tree" default-expand-all  highlight-current node-key="id" :data="depTree" :props="defaultProps" :expand-on-click-node="false" @node-click="clickDepTree"></el-tree>
              </el-col>
            </el-row>
          </div>
        </Poptip>
      </Form-item>
    </Form>
    <div slot="footer">
      <Button type="text" @click="cancelChange">取消</Button>
      <Button type="primary" :loading="submitLoading" @click="submitChange('changeForm')">提交</Button>
    </div>
  </Modal>
</template>

<script>
import { department, departmentalMatters } from '@/api/service'
import { setTimeout } from 'timers';
export default {
  props: {
    title: String,
    visible: {
      type: Boolean,
      default: false
    },
    changeValue: {
      type: Array,
      default: []
    }
  },
  data () {
    return {
      // 树数据
      regionData: [],
      depTree:[],
      defaultProps: {
        children: 'children',
        label: 'title'
      },

      changeForm: {
        departmentId: '',
        departmentTitle: ''
      },
      formValidate: {
        departmentTitle: [
          { required: true, message: "部门不能为空", trigger: "change" }
        ]
      },
      submitLoading: false,
    }
  },
  mounted () {
    this.getRegion()
  },
  methods: {
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
    getRegion () {
      this.getList('1').then(res => {
        this.regionData = res
      })
    },
    // 区域点击
    selectRegion (data) {
      this.getList('2', data.id).then(res => {
        this.depTree = res
      })
    },
    //选部门
    clickDepTree(val){
      this.changeForm.departmentId = val.id
      this.changeForm.departmentTitle = val.title
    },
    clearSelectDep() {
      this.changeForm.departmentId = ''
      this.changeForm.departmentTitle = ''
    },
    cancelChange() {
      this.$refs.changeForm.resetFields()
      this.submitLoading = false
      this.$emit('cancel')
    },
    submitChange (name) {
      this.$refs[name].validate(valid => {
        if (!valid) {
          this.$message.error("必填项不能为空")
          return
        }
        this.submitLoading = true
        let obj = {
          ids: this.changeValue,
          newDeptId: this.changeForm.departmentId,
          type: this.title === '事项转移' ? 1 : 0
        }
        departmentalMatters.changeDept(obj).then(res => {
          if (res.code === 0) {
            this.$refs.changeForm.resetFields()
            this.submitLoading = false
            this.$emit('changed')
          } else {
            setTimeout(() => {this.submitLoading = false},1500)
          }
        }, res => {
          setTimeout(() => {this.submitLoading = false},1500)
				})
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
