<template>
  <ItemSettings :itemSee="true" :leave="leave"></ItemSettings>
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
  mounted () {},
  beforeRouteLeave(to, from, next) {
    if (from.path === '/itemSee') {
      this.leave = true
    }
    this.SET_ITEMDETAIL({...this.itemInfo, id: null})
    next();
  },
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL'
    ])
  }
}
</script>

<style lang="scss" scoped>
</style>
