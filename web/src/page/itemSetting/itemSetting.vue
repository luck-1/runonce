<template>
  <ItemSettings :itemSee="false" :leave="leave"></ItemSettings>
</template>

<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import ItemSettings from '../itemSettings/itemSettings'
export default {
  name: 'item-setting',
  components: { ItemSettings },
  data () {
    return {
      leave: false
    }
  },
  computed: {
    ...mapState([ 'itemDetail' ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      return itemDetail
    }
  },
  beforeRouteLeave(to, from, next) {
    if (from.path === '/itemSetting'||from.path === '/itemSee') {
      this.leave = true
    }
    this.SET_ITEMDETAIL({...this.itemInfo, id: null})
    setTimeout(()=> {
      this.leave = false
    },300)
    next();
  },
  mounted () {},
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL'
    ])
  }
}
</script>

<style lang="scss" scoped>
</style>
