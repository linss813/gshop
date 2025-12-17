import Vue from 'vue';
import App from './App.vue';
import axios from 'axios';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueQuillEditor from 'vue-quill-editor';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import VueRouter from 'vue-router'; // 确保正确导入
import locale from 'element-ui/lib/locale/lang/zh-CN'; // 使用中文
import * as echarts from 'echarts';

Vue.config.productionTip = false;

Vue.prototype.$echarts = echarts;

Vue.use(ElementUI, {
  locale // 设置默认语言为中文
});
Vue.use(VueQuillEditor, {
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      ['image', 'code-block']
    ]
  }
});
Vue.prototype.$axios = axios;

// 请求拦截器
axios.interceptors.request.use(
  config => {
    let token = null;
    // 根据请求路径动态选择token
    if (config.url && config.url.startsWith('/admin')) {
      // 管理员相关请求使用adminToken
      token = store.getters.adminToken;
    } else {
      // 普通用户请求使用userToken
      token = store.getters.userToken;
    }
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);
// 设置默认后端接口路径
axios.defaults.baseURL = process.env.VUE_APP_BASE_URL;
// 响应拦截器
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      // 根据当前路由判断是用户还是管理员，跳转到对应登录页
      if (router.currentRoute.path.startsWith('/admin')) {
        router.push('/admin/login');
      } else {
        router.push('/login');
      }
    }
    return Promise.reject(error);
  }
);

// 全局错误处理
Vue.config.errorHandler = (err, vm, info) => {
  console.error('Global Vue Error:', err);
  // 添加错误上报逻辑
};

// 路由重复导航处理
const originalPush = VueRouter.prototype.push; // 使用导入的 VueRouter
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      console.error('Router error:', err);
    }
    return Promise.resolve();
  });
};

// 初始化 Store
store.dispatch('initializeStore').then(() => {
  new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount('#app');
});
