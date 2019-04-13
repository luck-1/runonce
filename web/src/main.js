// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import $ from 'jquery'
import router from './router'
import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'
import './css/theme.scss'
import store from './store'
import '../static/icon_ps/iconfont.css'
import 'babel-polyfill'

Vue.config.productionTip = false
window.$ = $
window.jQuery = $

Vue.use(ElementUI, { size: 'small' })
Vue.use(iView)
Vue.use(Viewer)
Vue.prototype.$axios = axios

// 自定义指令（锚 url）
Vue.directive('anchor', {
  inserted: function (el, binding) {
    el.onclick = function () {
      document.getElementById('container-card').scrollTop = $('#anchor-' + binding.value).offset().top - 130
      // document.documentElement.scrollTop = $('#anchor-' + binding.value).offset().top
    }
  }
})
/* -拖拽 */
Vue.directive('drag', {
  bind: function (el) {
    let oDiv = el
    oDiv.onmousedown = function (ev, limit = [-150, 300, -150, 300]) {
      let disX = ev.clientX - oDiv.offsetLeft
      let disY = ev.clientY - oDiv.offsetTop
      if (oDiv.setCapture) {
        /**
         * 该函数在属于当前线程的指定窗口里设置鼠标捕获。
         * 一旦窗口捕获了鼠标，所有鼠标输入都针对该窗口，无论光标是否在窗口的边界内。
         * 同一时刻只能有一个窗口捕获鼠标。
         * 如果鼠标光标在另一个线程创建的窗口上，只有当鼠标键按下时系统才将鼠标输入指向指定的窗口。
         */
        oDiv.setCapture()
      }
      ev.preventDefault()
      document.onmousemove = function (ev) {
        let left = ev.clientX - disX
        let top = ev.clientY - disY
        // let width = oDiv.offsetWidth
        // let height = oDiv.offsetHeight
        // left = left <= limit[0] ? limit[0] : ((left >= limit[1] - width) ? limit[1] - width : left)
        // top = top <= limit[2] ? limit[2] : ((top >= limit[3] - height) ? limit[3] - height : top)

        oDiv.style.left = left + 'px'
        oDiv.style.top = top + 'px'
        ev.preventDefault()
      }
      document.onmouseup = function (ev) {
        document.onmousemove = null
        document.onmouseup = null
        ev.preventDefault()
      }
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>',
  mounted () {
    // 初始化菜单
    // util.initRouter(this);
    this.currentPageName = this.$route.name
  }
})
