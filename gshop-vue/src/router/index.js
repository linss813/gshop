// router/index.js
import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store';

Vue.use(VueRouter);

const routes = [
    {
        path: '/shop',
        name: 'Shop',
        component: () => import('../views/Shop/Shop.vue'),
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/User/Login/Login.vue'),
    },
    {
        path: '/Register',
        name: 'Register',
        component: () => import('../views/User/Register/Register.vue'),
    },
    {
        path: '/goods/detail/:gid',
        name: 'GoodsDetail',
        component: () => import('@/views/Shop/GoodsDetail.vue'),
        meta: {requiresAuth: true}
    },
    // {
    //   path: '/profile',
    //   name: 'UserProfile',
    //   component: () => import('@/views/Shop/UserProfile.vue'), // 确保路径正确
    // },
    // {
    //   path: '/cart',
    //   name: 'Cart',
    //   component: () => import('@/views/Shop/Cart.vue'), // 确保路径正确
    // },
    // {
    //   path: '/orders',
    //   name: 'OrderList',
    //   component: () => import('@/views/Shop/OrderList.vue'), // 确保路径正确
    // },
    {
        path: '/user',
        name: 'UserIndex',
        component: () => import('@/views/Shop/UserIndex.vue'),
        meta: {requiresAuth: true},
        children: [
            {path: 'profile', name: 'UserProfile', component: () => import('@/views/Shop/UserProfile.vue')},
            {path: 'orders', name: 'OrderList', component: () => import('@/views/Shop/OrderList.vue')},
            {path: 'cart', name: 'Cart', component: () => import('@/views/Shop/Cart.vue')},
            {path: 'password', name: 'Password', component: () => import('@/views/Shop/Password.vue')}
        ]
    },


    {
        path: '/admin/login',
        name: 'AdminLogin',
        component: () => import('../views/Admin/AdminLogin/AdminLogin.vue')
    },

    {
        path: '/admin/home',
        name: 'AdminHome',
        component: () => import('../views/Home/Home.vue'),
        meta: {requiresAdmin: true},

        children: [
            {
                path: 'index',
                name: 'Index',
                component: () => import('../views/Home/Index.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'goods-management',
                name: 'GoodsManagement',
                component: () => import('../views/Management/GoodsManagement/GoodsManagement.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'order-management',
                name: 'OrderManagement',
                component: () => import('../views/Management/OrderManagement/OrderManagement.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'data-statistics',
                name: 'DataStatistics',
                component: () => import('../views/Management/DataStatistics/DataStatistics.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'shop-management',
                name: 'ShopManagement',
                component: () => import('../views/Management/ShopManagement/ShopManagement.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'goods-type',
                name: 'GoodsType',
                component: () => import('../views/Management/ProductCategory/GoodsType.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'category',
                name: 'Category',
                component: () => import('../views/Management/ProductCategory/Category.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'user-management',
                name: 'UserManagement',
                component: () => import('../views/Management/UserManagement/UserManagement.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'banner-management',
                name: 'BannerManagement',
                component: () => import('../views/Management/BannerManagement/BannerManagement.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'review-management',
                name: 'ReviewManagement',
                component: () => import('../views/Management/ReviewManagement/ReviewManagement.vue'),
                meta: {requiresAdmin: true},
            },
            {
                path: 'adminadd',
                name: 'AdminAdd',
                component: () => import('../views/Admin/AdminAdd/AdminAdd.vue'),
                meta: {requiresAdmin: true},
            },
        ],
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

router.beforeEach(async (to, from, next) => {
    await store.dispatch('initializeStore');

    // 管理员权限检查（仅检查 isAdmin）
    const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin);
    const isAdmin = store.getters.isAdmin;
    if (requiresAdmin && !isAdmin) {
        return next({
            name: 'AdminLogin',
            query: {redirect: to.fullPath}
        });
    }

    // 普通用户权限检查
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    if (requiresAuth && !store.getters.isUserLoggedIn) {
        return next({
            name: 'Login',
            query: {redirect: to.fullPath}
        });
    }

    next();
});

export default router;
