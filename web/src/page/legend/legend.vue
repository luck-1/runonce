/**
 * 名称：图例样表
 * 作者：王晓妮
 * 日期：2018-11-6
 */
<template>
  <el-card class="container-card">
    <h2>图例样表</h2>

    <el-row :gutter="20">
      <div class="form-container" v-for="(item, index) in legends" :key="index">
        <el-row class="form-header">
          <el-col :span="15">
            <el-input v-model="item.inputValue"></el-input>
          </el-col>
          <el-col :span="9" class="btn-group">
            <el-button @click.prevent="remove(index)" v-if="legends.length>1">删除</el-button>
          </el-col>
        </el-row>
        <el-card>
          <p class="detail-num">{{index+1}}</p>

          <img-upload :imageNum='40' :multiple="true" @upload="uploadImg"></img-upload>

          <div class="hover-div" @mouseover="showStyle(index)" @mouseout="hideStyle(index)">
            <div class="add-new" @click="addNew" :style="{display: item.addStyle}">+ 添加</div>
          </div>
        </el-card>
      </div>
    </el-row>
    <div class="btn-submit" @click="formSubmit">提交</div>
  </el-card>
</template>

<script>
import imgUpload from '@/components/uploadPicture'

export default {
  components: {
    imgUpload
  },
  data () {
    return {
      legends: [{
        inputValue: '',
        addStyle: 'none',
        imgs: []
      }]
    }
  },
  mounted () {

  },
  methods: {
    remove (index) {
      this.legends.splice(index, 1)
    },
    uploadImg (imgs) {
      let currentIndex = this.legends.length - 1
      this.legends[currentIndex].imgs = imgs
    },
    showStyle (index) {
      this.legends[index].addStyle = 'block'
    },
    hideStyle (index) {
      this.legends[index].addStyle = 'none'
    },
    addNew () {
      let judge = this.judgeLast()
      if (judge) {
        this.legends.push({
          inputValue: '',
          addStyle: 'none',
          imgs: []
        })
      }
    },
    judgeLast () {
      let index = this.legends.length
      if (index) {
        let current = this.legends[index - 1]
        if (!current.inputValue) {
          this.$message.error(`第${index}个样表材料种类未填!`)
          return false
        }
        if (!current.imgs.length) {
          this.$message.error(`第${index}个样表未上传文件！`)
          return false
        }
        return true
      }
    },
    formSubmit () {
      let judge = this.judgeLast()
      if (judge) {
        let obj = {
          imgNumber: this.legends.imgs.length,
          ...this.legends
        }
        console.log(obj)
      } else {
        this.$message.error(`请补全表单信息!`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.form-container {
  &:not(:last-child) {
    margin-bottom: 1rem;
  }
  & /deep/ .el-card__body {
    position: relative;
    padding-bottom: 0;
    .detail-num {
      position: absolute;
      right: 20px;
      top: 10px;
      font-size: 2rem;
      color: #fff;
      z-index: 2;
    }
    &::after {
      content: '';
      position: absolute;
      top: 0;
      right: 0;
      width: 0;
      height: 0;
      overflow: hidden;
      border-top: 85px solid #e6a23c;
      border-left: 90px solid transparent;
    }
  }
  .form-header {
    .btn-group {
      text-align: right;
      & button:nth-child(2) {
        margin-left: 0
      }
    }
  }
  .hover-div {
    position: relative;
    bottom: -10px;
    height: 40px;
    cursor: pointer;
    background: transparent;
    .add-new {
      position: absolute;
      right: -20px;
      height: 30px;
      width: calc(100% + 40px);
      border-radius: 0 0 3px 3px;
      font-size: 12px;
      text-align: center;
      line-height: 30px;
      background: #ecf7fd;
      color: #3071a9;
    }
  }
}
</style>
