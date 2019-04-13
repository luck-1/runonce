<template>
  <div class="wrapper">
    <v-head></v-head>
    <v-sidebar></v-sidebar>
    <div class="content-box" :class="{'content-collapse': collapse}">
      <v-tags></v-tags>
      <div class="content">
        <!-- <transition name="move" mode="out-in"> -->
          <!-- <keep-alive :include="tagsList">
            <router-view></router-view>
          </keep-alive> -->
          <keep-alive>
            <router-view v-if="$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!$route.meta.keepAlive"></router-view>
        <!-- </transition> -->
      </div>
    </div>
  </div>
</template>

<script>
import vHead from './Header.vue'
import vSidebar from './Sidebar.vue'
import vTags from './Tags.vue'
import bus from './bus'
export default {
  name: 'common-home',
  data () {
    return {
      tagsList: [],
      collapse: false
    }
  },
  components: {
    vHead,
    vSidebar,
    vTags
  },
  created () {
    bus.$on('collapse', msg => {
      this.collapse = msg
    })

    // 只有在标签页列表里的页面才使用keep-alive，即关闭标签之后就不保存到内存中了。
    bus.$on('tags', msg => {
      let arr = []
      for (let i = 0, len = msg.length; i < len; i++) {
        msg[i].name && arr.push(msg[i].name)
      }
      this.tagsList = arr
    })
  },
  methods: {

  }
}
</script>
<style lang="scss" scoped>
.header {
  background-color: #242f42;
  background-color: #28313c;
}
.login-wrap {
  background: #324157;
}
.plugins-tips {
  background: #eef1f6;
}
.plugins-tips a {
  color: #20a0ff;
}
.el-upload--text em {
  color: #20a0ff;
}
.pure-button {
  background: #20a0ff;
}
.message-title {
  color: #20a0ff;
}
.collapse-btn:hover {
  background: rgb(40,52,70);
}
.item-card:not(:last-child) {
  margin-bottom: 1rem;
}
</style>
