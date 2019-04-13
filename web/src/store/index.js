import Vue from 'vue'
import Vuex from 'vuex'

import system from './modules/system'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    itemDetail: {},
    loadingText: '拼命加载中...',
    saveSuccess: false,
    copyObj: null,
    todoTask: null,
    situationTitle: '',
    searchObj: null
  },
  mutations: {
    SET_ITEMDETAIL (state, res) {
      state.itemDetail = res
    },
    SET_LOADINGTEXT (state, res) {
      state.loadingText = res
    },
    SET_SAVESUCCESS (state, res) {
      state.saveSuccess = res
    },
    SET_COPYOBJ (state, res) {
      state.copyObj = res
    },
    SET_TODOTASK (state, res) {
      state.todoTask = res
    },
    SET_SITUATIONTITLE (state, res) {
      state.situationTitle = res
    },
    SET_SEARCHOBJ (state, res) {
      state.searchObj = res
    }
  },
  actions: {
    itemDetail: ({ commit }, param) => {
      commit('SET_ITEMDETAIL', param)
    },
    loadingText: ({ commit }, param) => {
      commit('SET_LOADINGTEXT', param)
    },
    saveSuccess: ({ commit }, param) => {
      commit('SET_SAVESUCCESS', param)
    },
    copyObj: ({ commit }, param) => {
      commit('SET_COPYOBJ', param)
    },
    todoTask: ({ commit }, param) => {
      commit('SET_TODOTASK', param)
    },
    situationTitle: ({ commit }, param) => {
      commit('SET_SITUATIONTITLE', param)
    },
    searchObj: ({ commit }, param) => {
      commit('SET_SEARCHOBJ', param)
    }
  },
  modules: {
    system
  }
})

export default store
