const system = {
  state: {
    menu: {},
    avatorImgPath: '',
    userMenu: [],
    enterFlow: false
  },

  mutations: {
    SET_VISITOR: (state, data) => {
      state.visitor = data
    }
  },
  actions: {
    setvisitor (context, data) {
      context.commit('SET_VISITOR', data)
    }
  }
}

export default system
