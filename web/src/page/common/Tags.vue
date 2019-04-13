<template>
  <div class="tags" v-if="showTags" :style="{width: tagWidth}">
    <ul>
      <li class="tags-li" v-for="(item,index) in tagsList" :class="{'active': isActive(item.path)}" :key="index">
        <router-link :to="item.path" class="tags-li-title">
          {{item.title}}
        </router-link>
        <span class="tags-li-icon" @click="closeTags(index)"><i class="el-icon-close"></i></span>
      </li>
    </ul>
    <div class="tags-close-box">
      <el-dropdown @command="handleTags">
        <el-button size="mini" type="primary">
          标签选项<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu size="small" slot="dropdown">
          <el-dropdown-item command="other">关闭其他</el-dropdown-item>
          <el-dropdown-item command="all">关闭所有</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions} from 'vuex'
import bus from './bus'
export default {
  name: 'common-tags',
  data () {
    return {
      tagsList: localStorage.openMenu?JSON.parse(localStorage.openMenu):[]||[],
      tagWidth: 'calc(100% - 200px)'
    }
  },
  computed: {
    ...mapState([
      'saveSuccess', 'itemDetail'
    ]),
    showTags () {
      return this.tagsList.length > 0
    }
  },
  watch: {
    saveSuccess (val) {
      if (val) {
        // ||x.title === '事项配置'
        let settingIndex = this.tagsList.findIndex(x => x.title === '事项配置')
        if (settingIndex>0){
          this.closeTags(settingIndex,false)
        }
      }
    },
    $route (newValue, oldValue) {
      this.setTags(newValue)
      this.SET_TODOTASK(null)
    }
  },
  created () {
    this.setTags(this.$route)
    bus.$on('collapse', msg => {
      this.tagWidth = msg ? 'calc(100% - 64px)' : 'calc(100% - 200px)'
    })
  },
  mounted () {},
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL', 'SET_TODOTASK'
    ]),
    isActive (path) {
      return path === this.$route.fullPath
    },
    // 关闭单个标签
    closeTags (index,flag=true) {
      if(flag && this.tagsList[index].title === '事项配置') {
        this.$confirm('可能存在未保存页签，确定离开?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.closeSingle(index)
        }).catch(() => { })
        return
        // localStorage.setItem('itemDetail', {state: this.itemDetail.state})
        // this.SET_ITEMDETAIL({state: this.itemDetail.state})
      }
      this.closeSingle(index)
    },
    closeSingle(index) {
      const delItem = this.tagsList.splice(index, 1)[0]
      const item = this.tagsList[index] ? this.tagsList[index] : this.tagsList[index - 1]
      let current = this.$store.state.system.userMenu[0]
      if (item) {
        delItem.path === this.$route.fullPath && this.$router.push(item.path)
      } else {
        this.$router.push(current.path)
      }
      if (!this.tagsList.length) {
        this.tagsList = [{
          name: current.path,
          path: current.path,
          title: current.title
        }]
      }
      localStorage.setItem('openMenu', JSON.stringify(this.tagsList))
    },
    // 关闭全部标签
    closeAll () {
      let setting = this.tagsList.filter(x => x.title === '事项配置')
      if(setting.length===1) {
        this.$confirm('可能存在未保存页签，确定离开?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let current = this.$store.state.system.userMenu[0]
          this.tagsList = [{
            name: current.path,
            path: current.path,
            title: current.title
          }]
          this.$router.push(current.path)
          localStorage.setItem('openMenu', JSON.stringify(this.tagsList))
        }).catch(() => { })
      }
    },
    // 关闭其他标签
    closeOther () {
      const curItem = this.tagsList.filter(item => {
        return item.path === this.$route.fullPath
      })
      let setting = this.tagsList.filter(x => x.title === '事项配置' && x.path !== this.$route.fullPath)
      if(setting.length===1) {
        this.$confirm('可能存在未保存页签，确定离开?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.tagsList = curItem
          localStorage.setItem('openMenu', JSON.stringify(this.tagsList))
        }).catch(() => { })
        return
      }
      this.tagsList = curItem
      localStorage.setItem('openMenu', JSON.stringify(this.tagsList))
    },
    // 设置标签
    setTags (route) {
      const isExist = this.tagsList.some(item => {
        return item.path === route.fullPath
      })
      !isExist && this.tagsList.push({
        title: route.meta.title,
        path: route.fullPath,
        name: route.matched[1].components.default.name
      })
      bus.$emit('tags', this.tagsList)
      localStorage.setItem('openMenu', JSON.stringify(this.tagsList))
    },
    handleTags (command) {
      command === 'other' ? this.closeOther() : this.closeAll()
    }
  }
}
</script>

<style lang="scss" scoped>
.tags {
  // position: relative;
  position: fixed;
  height: 30px;
  right: 0;
  overflow: hidden;
  background: #fff;
  padding-right: 120px;
  transition: width .35s;
  z-index: 5;
}
.tags ul {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
}
.tags-li {
  float: left;
  margin: 3px 5px 2px 3px;
  border-radius: 3px;
  font-size: 12px;
  overflow: hidden;
  cursor: pointer;
  height: 23px;
  line-height: 23px;
  border: 1px solid #e9eaec;
  background: #fff;
  padding: 0 5px 0 12px;
  vertical-align: middle;
  color: #666;
  -webkit-transition: all 0.3s ease-in;
  -moz-transition: all 0.3s ease-in;
  transition: all 0.3s ease-in;
}
.tags-li:not(.active):hover {
  background: #f8f8f8;
}
.tags-li.active {
  color: #fff;
}
.tags-li.active {
  border: 1px solid #409EFF;
  background-color: #409EFF;
}
.tags-li-title {
  float: left;
  max-width: 80px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-right: 5px;
  color: #666;
}
.tags-li.active .tags-li-title {
  color: #fff;
}
.tags-close-box {
  position: absolute;
  right: 0;
  top: 0;
  box-sizing: border-box;
  padding-top: 1px;
  text-align: center;
  width: 110px;
  height: 30px;
  background: #fff;
  box-shadow: -3px 0 15px 3px rgba(0, 0, 0, 0.1);
  z-index: 10;
}
</style>
