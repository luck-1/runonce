import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// 主路由
export const mainRouter = {
  path: '/',
  // beforeEnter: (from, to, next) => {
  //   if (localStorage.getItem('userName')) {
  //     next()
  //   } else {
  //     next('/login')
  //   }
  // },
  name: 'main',
  // redirect: '/itemManagement',
  component: resolve => require(['../page/common/Home.vue'], resolve),
  meta: {
    title: '首页',
    requireAuth: true, // 添加该字段，表示进入这个路由是需要登录的
    keepAlive: false
  },
  children: [
    // 首页
    { path: '/index', name: 'index', component: resolve => require(['../page/index/index.vue'], resolve), meta: { title: '首页', requireAuth: true, keepAlive: false } },
    // 事项管理
    { path: '/itemManagement', name: 'itemManagement', component: resolve => require(['../page/itemManagement/itemManagement.vue'], resolve), meta: { title: '事项管理', requireAuth: true, keepAlive: false } },
    // 事项分配
    { path: '/itemAllot', name: 'itemAllot', component: () => import('@/page/itemAllot/itemAllot.vue'), meta: { title: '事项分配', requireAuth: true, keepAlive: false } },
    // 组织架构
    { path: '/department', name: 'department', component: () => import('@/page/department/department.vue'), meta: { title: '组织架构', requireAuth: true, keepAlive: false } },
    // 菜单管理
    { path: '/menuManagement', name: 'menuManagement', component: () => import('@/page/menu/menu.vue'), meta: { title: '菜单管理', requireAuth: true, keepAlive: false } },
    // 角色权限
    { path: '/roleLimit', name: 'roleLimit', component: () => import('@/page/roleLimit/roleLimit.vue'), meta: { title: '角色权限', requireAuth: true, keepAlive: false } },
    // 用户管理
    { path: '/userManagement', name: 'userManagement', component: () => import('@/page/userManagement/userManagement.vue'), meta: { title: '用户管理', requireAuth: true, keepAlive: false } },
    // 评审查看
    { path: '/publicity', name: 'publicity', component: () => import('@/page/publicity/publicity.vue'), meta: { title: '评审查看', requireAuth: true, keepAlive: false } },
    // 目录管理
    { path: '/levelWith', name: 'levelWith', component: () => import('@/page/levelWith/levelWith.vue'), meta: { title: '目录管理', requireAuth: true, keepAlive: false } },
    // 评审统计
    { path: '/judgeStatistics', name: 'judgeStatistics', component: () => import('@/page/judgeStatistics/judgeStatistics.vue'), meta: { title: '评审统计', requireAuth: true, keepAlive: false } },
    // 情形引导
    { path: '/situationGuide', name: 'situationGuide', component: () => import('@/page/situationGuide/situationGuide.vue'), meta: { title: '情形引导', requireAuth: true, keepAlive: false } },
    // 办理项归集
    { path: '/itemCollection', name: 'itemCollection', component: () => import('@/page/itemCollection/itemCollection.vue'), meta: { title: '办理项归集', requireAuth: true, keepAlive: false } }
  ]
}

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
  path: '/',
  name: 'otherRouter',
  redirect: '/itemSetting',
  component: resolve => require(['../page/common/Home.vue'], resolve),
  children: [
    { path: '/itemSetting', name: 'itemSetting', meta: { title: '事项配置', keepAlive: true, requireAuth: true }, component: () => import('@/page/itemSetting/itemSetting.vue') },
    { path: '/itemSee', name: 'itemSee', meta: { title: '公示查看详情', keepAlive: true, requireAuth: true }, component: () => import('@/page/itemSee/itemSee.vue') }
  ]
}

// 不建议这样做，会导致内存溢出
const router = new Router({
  routes: [
    mainRouter,
    {
      path: '/login',
      name: 'login',
      component: resolve => require(['../page/login.vue'], resolve),
      meta: { title: '登录', keepAlive: false }
    },
    {
      path: '*',
      redirect: '/404'
    },
    {
      path: '/404',
      component: resolve => require(['../page/404.vue'], resolve)
    },
    otherRouter
  ]
})

router.beforeEach((to, from, next) => {
  // if (to.meta.requireAuth) { // 判断该路由是否需要登录权限
  if (to.meta.requireAuth) {
    if (localStorage.getItem('accessToken') === null) { // 判断token是否存在
      next({path: '/login'})
    } else {
      next()
    }
  } else {
    next()
  }
  // }
})

export default router
