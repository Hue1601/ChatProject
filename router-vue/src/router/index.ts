import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   name: 'home',
    //   component: HomeView
    // },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/verify',
      name: 'verify-otp',
      component: () => import('../views/Login/VerifyOtp.vue')
    },
    {
      path: '/list_conversation',
      name: 'list_conversation',
      component: () => import('../views/chat/list_conversation.vue')
    }
    ,
    // {
    //   path: '/conversation_detail',
    //   name: 'conversation_detail',
    //   component: () => import('../views/chat/conversation_detail.vue')
    // },
    {
      path: '/conversation_detail/:conversationId',
      name: 'conversation_detail',
      component: () => import('../views/chat/conversation_detail.vue'),
      props: true
    },
    {
      path: '/user',
      name: 'user-list',
      component: () => import('../views/user/ListUser.vue')
    },
    {
      path: '/create',
      name: 'create',
      component: () => import('../views/chat/create.vue')
    },
    {
      path: '/user/add',
      name: 'user-add',
      component: () => import('../views/user/AddUser.vue')
    },
    {
      path: '/user/update/:id',
      name: 'user-update',
      component: () => import('../views/user/UpdateUser.vue')
    },
  ]
})

export default router
