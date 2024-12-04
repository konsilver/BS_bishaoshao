// 引入 Vue 3 的路由模块
//import { DEFAULT_FORMATS_DATE } from 'element-plus';
import {createRouter, createWebHistory} from 'vue-router';


// 定义路由
const routes = [
    {
        path: '/',
        redirect: '/bishaoshao/user/login'
    },
    {
        path: '/bishaoshao/user/login',
        name: 'login',
        component: () => import('../views/begin/LoginPage.vue')
    },
    {
        path: "/bishaoshao/user/searchboard",
        name: 'searchboard',
        component: () => import("@/views/dashboard/SearchBoard.vue"),
    },
    {
        path: "/bishaoshao/user/register",
        name: 'register',
        component: () => import('@/views/begin/RegisterPage.vue'),
    },
    {
        path: "/bishaoshao/user/forget",
        name: 'forget',
        component: () => import('@/views/begin/ForgetPage.vue'),
    },
    {
        path: "/bishaoshao/searchresults",
        name: 'SearchResults',
        component: () => import('@/views/dashboard/ResultPage.vue'),
    },
    {
        path: "/bishaoshao/details",
        name: 'Details',
        component: () => import('@/views/dashboard/DetailPage.vue'),
    },
    
];

// 创建并配置路由
const router = createRouter({
    history: createWebHistory(),
    routes,  // 等同于 routes: routes
});

export default router;