const routes = [
  {
    path: '/user',
    name: 'UserIndex',
    redirect: '/user/profile',
    component: () => import('@/views/Shop/UserIndex.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/Shop/UserProfile.vue'), // 确保路径正确
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('@/views/Shop/Cart.vue'), // 确保路径正确
      },
      {
        path: 'orders',
        name: 'OrderList',
        component: () => import('@/views/Shop/OrderList.vue'), // 确保路径正确
      }
    ]
  },
];