<template>
  <div class="sidebar">
    <!-- background-color="#324157" text-color="#bfcbd9" active-text-color="#20a0ff" -->
    <el-menu class="sidebar-el-menu" :default-active="activeIndex" :collapse="collapse" unique-opened :router="true"
      background-color="#495264"
      text-color="#fff"
      active-text-color="#ffd04b">
      <div v-for="(item, index) in items" :key="index">
        <template v-if="item.subs">
          <el-submenu :index="item.path" :key="item.path">
            <template slot="title">
              <Icon :type="item.icon" /><span slot="title">{{ item.title }}</span>
              <!-- <i :class="item.icon"></i><span slot="title">{{ item.title }}</span> -->
            </template>
            <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="subItem.path">
              <template slot="title">
                <i :class="subItem.icon"></i><span slot="title">{{ subItem.title }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.path" :key="item.path">
            <Icon :type="item.icon" /><span slot="title">{{ item.title }}</span>
            <!-- <i :class="item.icon"></i><span slot="title">{{ item.title }}</span> -->
          </el-menu-item>
        </template>
      </div>
    </el-menu>
  </div>
</template>

<script>
import bus from '../common/bus'
import { menu } from "@/api/service";
export default {
  name: 'common-sidebar',
  data () {
    return {
      activeIndex: '0',
      collapse: false,
      items: []
    }
  },
  computed: {
    getMenuList() {
      return this.$store.state.system.userMenu;
    }
  },
  watch: {
    getMenuList(val) {
      this.items = val;
    },
    $route (val) {
      this.handleSelect(val.path)
    }
  },
  created () {
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    bus.$on('collapse', msg => {
      this.collapse = msg
    })
  },
  mounted() {
    this.activeIndex = this.$route.path;
    this.getAllMenuList()
    // this.handleSelect(this.$route.path);
    // this.items = this.$store.state.system.userMenu;
  },
  methods: {
    getAllMenuList () {
      this.items = []
      menu.getMenuList().then(res=>{
        if(res.code === 0){
          this.$store.state.system.userMenu = res.obj
          this.items = res.obj
        }
      })
    },
    handleSelect(index) {
      // this.getAllMenuList()
      this.$nextTick(()=>{
        this.activeIndex = index;
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 50px;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 200px;
}
.sidebar-el-menu {
  .el-menu-item i {
    color: #fff;
  }
  .is-active i {
    color: #ffd04b;
  }
  i {
    margin-right: 10px;
    vertical-align: middle;
    font-size: 22px;
  }
}
.sidebar > ul {
  height: 100%;
  overflow: auto;
}
</style>
