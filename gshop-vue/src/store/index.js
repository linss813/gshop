// store/index.js
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const safeParse = (key) => {
  const value = localStorage.getItem(key);
  if (value === null || value === 'undefined' || value === 'null') {
    localStorage.removeItem(key);
    return null;
  }
  try {
    return JSON.parse(value);
  } catch (e) {
    console.error(`清除无效的localStorage数据：${key}`);
    localStorage.removeItem(key);
    return null;
  }
};

const store = new Vuex.Store({
  state: {
    // 管理员状态
    adminUser: safeParse('adminUser'),
    adminToken: localStorage.getItem('adminToken'),
    // 普通用户状态
    user: safeParse('user'),
    userToken: localStorage.getItem('userToken'),
  },
  mutations: {
    // 管理员相关mutations
    SET_ADMIN_USER(state, user) {
      state.adminUser = user;
      if (user) {
        localStorage.setItem('adminUser', JSON.stringify(user));
      } else {
        localStorage.removeItem('adminUser');
      }
    },
    SET_ADMIN_TOKEN(state, token) {
      if (token) {
        state.adminToken = token;
        localStorage.setItem('adminToken', token);
      } else {
        state.adminToken = null;
        localStorage.removeItem('adminToken');
      }
    },
    // 普通用户相关mutations
    SET_USER(state, user) {
      state.user = user;
      if (user) {
        localStorage.setItem('user', JSON.stringify(user));
      } else {
        localStorage.removeItem('user');
      }
    },
    SET_USER_TOKEN(state, token) {
      if (token) {
        state.userToken = token;
        localStorage.setItem('userToken', token);
      } else {
        state.userToken = null;
        localStorage.removeItem('userToken');
      }
    },
    // 同步状态
    SYNC_ADMIN_STATE(state) {
      state.adminUser = safeParse('adminUser');
      state.adminToken = localStorage.getItem('adminToken');
      state.user = safeParse('user');
      state.userToken = localStorage.getItem('userToken');
    },
    // 清除状态
    CLEAR_ADMIN_USER(state) {
      state.adminUser = null;
      localStorage.removeItem('adminUser');
    },
    CLEAR_ADMIN_TOKEN(state) {
      state.adminToken = null;
      localStorage.removeItem('adminToken');
    },
    CLEAR_USER(state) {
      state.user = null;
      localStorage.removeItem('user');
    },
    CLEAR_USER_TOKEN(state) {
      state.userToken = null;
      localStorage.removeItem('userToken');
    },
  },
  actions: {
    initializeStore({ commit }) {
      commit('SYNC_ADMIN_STATE');
    },
    // 管理员相关actions
    setAdminUser({ commit }, user) {
      return new Promise(resolve => {
        if (!user || typeof user !== 'object') {
          console.error('无效的管理员用户数据:', user);
          resolve();
          return;
        }
        commit('SET_ADMIN_USER', user);
        resolve();
      });
    },
    setAdminToken({ commit }, token) {
      commit('SET_ADMIN_TOKEN', token);
    },
    clearAdminUser({ commit }) {
      commit('CLEAR_ADMIN_USER');
      commit('CLEAR_ADMIN_TOKEN');
    },
    // 普通用户相关actions
    setUser({ commit }, user) {
      commit('SET_USER', user);
    },
    setUserToken({ commit }, token) {
      commit('SET_USER_TOKEN', token);
    },
    clearUser({ commit }) {
      commit('CLEAR_USER');
      commit('CLEAR_USER_TOKEN');
    },
    logout({ commit }) {
      // 普通用户登出，只清除普通用户状态
      commit('CLEAR_USER');
      commit('CLEAR_USER_TOKEN');
    },
    // 清除所有状态
    clearAll({ commit }) {
      commit('CLEAR_ADMIN_USER');
      commit('CLEAR_ADMIN_TOKEN');
      commit('CLEAR_USER');
      commit('CLEAR_USER_TOKEN');
    },
  },
  getters: {
    // 管理员相关getters
    adminUser: (state) => state.adminUser,
    adminToken: (state) => state.adminToken,
    isAdmin: (state) => !!state.adminUser,
    // 普通用户相关getters
    user: (state) => state.user,
    userToken: (state) => state.userToken,
    isUserLoggedIn: (state) => !!state.user,
    currentUser: (state) => state.user,
    currentAdmin: (state) => state.adminUser,
    // 获取当前请求需要的token
    token: (state) => {
      // 这里暂时返回adminToken或userToken，实际应该根据请求路径来判断
      // 后续会在axios拦截器中根据请求路径动态选择token
      return null;
    },
  },
});

export default store;
