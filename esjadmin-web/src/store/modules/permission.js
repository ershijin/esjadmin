import { constantRoutes } from '@/router'

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  addRoutes({ commit }, routers) {
    commit('SET_ROUTES', routers)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
