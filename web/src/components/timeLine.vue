<template>
  <div class="time-line">
    <div class="left">
      <span class="time">{{formatTime(time)}}</span>
      <span class="title-label">{{user}}</span>
      <slot name="version"></slot>
    </div>
    <div :class="['cirle', state===1?'success':state===2?'error':'']"></div>
    <div class="right">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script>
import { common } from '@/utils/common'
export default {
  data () {
    return {
    };
  },
  props: {
    state: {
      type: Number
    },
    time: {
      type: Number
    },
    user: {
      type: String
    }
  },
  components: {},
  computed: {},
  mounted() {},
  methods: {
    formatTime(time) {
      return common.formatDateTime(time)
    }
  }
}
</script>

<style lang='scss' scoped>
  .time-line {
    display: flex;
    flex-direction: row;
    width: 100%;
    position: relative;
    .left {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      width: 145px;
      min-height: 40px;
      padding: 10px;
      padding-right: 20px;
      box-sizing: border-box;
      .title-label {
        margin-top: 5px;
        color: #3084cc;
        font-weight: bolder;
      }
      .time {
        font-size: 12px;
      }
    }
    .right {
      flex: 1;
      min-height: 40px;
      border-left: 1px solid #dadde3;
      padding: 10px;
      padding-left: 20px;
      box-sizing: border-box;
      word-wrap: break-word;
      word-break: break-all
    }
    .cirle {
      position: absolute;
      left: 138px;
      top: 10px;
      display: flex;
      justify-content: center;
      align-items: center;
      width: 15px;
      height: 15px;
      border-radius: 50%;
      border: 2px solid #409eff;
      background-color: #fff;
      &.success {
        border-color: #67c23a;
      }
      &.error {
        border-color: #f56c6c;
      }
    }
  }
</style>
