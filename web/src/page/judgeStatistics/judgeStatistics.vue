<template>
  <el-card class="container-card judge-statistics">
    <el-form :model="searchForm" :rules="searchRules" ref="ruleForm" class="search-form">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="事项名称" prop="eventName" label-width="70px">
            <Input v-model="searchForm.eventName" placeholder="请输入" clearable />
          </el-form-item>
          <el-form-item prop="isApproval">
            <el-radio-group size="mini" v-model="searchForm.isApproval">
              <el-radio label="1">部门审批</el-radio>
              <el-radio label="2">评审</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="页签名称" prop="sheetNum" label-width="70px">
            <el-select v-model="searchForm.sheetNum" placeholder="请选择" size="small" clearable>
              <el-option label="办事指南" value="1"></el-option>
              <el-option label="材料分组" value="2"></el-option>
              <el-option label="申请类型" value="3"></el-option>
              <el-option label="办事情形" value="7"></el-option>
              <el-option label="表单说明" value="8"></el-option>
              <el-option label="资格预审" value="4"></el-option>
              <el-option label="现有流程" value="5"></el-option>
              <el-option label="量化呈现" value="9"></el-option>
              <el-option label="系统对接方案" value="10"></el-option>
              <el-option label="办理流程图" value="6"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="元素名称" prop="propertyName" label-width="70px">
            <Input v-model="searchForm.propertyName" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="评论状态" prop="questionState" label-width="70px">
            <el-select v-model="searchForm.questionState" placeholder="请选择" size="small" clearable>
              <el-option label="待梳理员回复" value="0"></el-option>
              <el-option label="梳理员拒绝" value="1"></el-option>
              <el-option label="沟通中" value="2"></el-option>
              <el-option label="同意" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-button size="small" type="primary" style="float:right;margin-bottom:1rem;" icon="el-icon-search" @click="searchByPage"></el-button>
        </el-col>
      </el-row>
    </el-form>

    <el-table border :data="discussContents" height="calc(100% - 145px)">
      <el-table-column type="expand" width="25">
        <template slot-scope="{row, $index}">
          <div v-if="row.matterProposalAnswer.length">
            <TimeLine :time="item.answerTime" :user="item.answererUser" v-for="(item, index) in row.matterProposalAnswer" :key="index">
              <div slot="content" class="discuss-content"><p>{{item.answerContent}}</p></div>
            </TimeLine>
          </div>
          <div v-else>无</div>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
      <el-table-column label="状态" prop="questionState">
        <template slot-scope="{row}">
          <el-tag size="small" :type="(row.questionState === 1||row.questionState === 4)?'warning':(row.questionState === 0||row.questionState === 2)?'primary':'success'" disable-transitions>
            {{row.questionState===0?'待梳理员回复':row.questionState===1?'梳理员拒绝':row.questionState===2?'沟通中':row.questionState===3?'同意':'梳理员待确认'}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="评论时间" prop="questionTime" width="140px">
        <template slot-scope="{row}">
          {{formatTime(row.questionTime)}}
        </template>
      </el-table-column>
      <el-table-column label="评论内容" :show-overflow-tooltip="true" min-width="260" prop="questionContent"></el-table-column>
      <el-table-column label="事项名称" prop="eventName"></el-table-column>
      <el-table-column label="页签名称" prop="sheetNum">
        <template slot-scope="{row}">
          {{row.sheetNum===1?'办事指南':row.sheetNum===2?'材料分组':row.sheetNum===3?'申请类型':row.sheetNum===4?'资格预审':
            row.sheetNum===5?'现有流程':row.sheetNum===6?'办理流程图':row.sheetNum===7?'办事情形':row.sheetNum===8?'表单说明':
            row.sheetNum===9?'量化呈现':'系统对接方案'}}
        </template>
      </el-table-column>
      <el-table-column label="元素名称" prop="propertyName"></el-table-column>
      <el-table-column label="提问者" prop="questionerUser"></el-table-column>
      <!-- <el-table-column label="操作"></el-table-column> -->
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
  </el-card>
</template>

<script>
import { matterProposal } from '@/api/service'
import { common } from '@/utils/common'

import TimeLine from '@/components/timeLine'
export default {
  components: { TimeLine },
  data () {
    return {
      searchForm: {
        isApproval: '1',
        eventName: '',
        sheetNum: '',
        propertyName: '',
        questionState: ''
      },
      searchRules: {},
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,
      discussContents: []
    }
  },
  mounted () {
    this.searchByPage()
  },
  methods: {
    formatTime(time) {
      return common.formatDateTime(time)
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
    // 回看评论
    async searchByPage () {
      let obj = {
        ...this.searchForm,
        isApproval: Number(this.searchForm.isApproval),
        page: this.currentPage,
        size: this.pageSize,
      }
      let res = await matterProposal.findByPage(obj)
      if(res.code === 0) {
        this.discussContents = []
        this.total = 0
        if(res.obj) {
          this.discussContents = res.obj.list
          this.total = res.obj.total
        }
      } else {
        this.discussContents = []
        this.total = 0
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.judge-statistics {
  & /deep/ .el-card__body{
    height: 100%;
  }
  .el-pagination {
    margin: 1rem 0;
    text-align: center;
  }
}
</style>
