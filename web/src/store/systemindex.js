import Vue from 'vue'
import Vuex from 'vuex'

import system from './modules/system'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    system
  }
})

export default store
