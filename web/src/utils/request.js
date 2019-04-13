import axios from 'axios'
import { common } from './common.js'
import {Loading, Message} from 'element-ui'
import router from '../router/index'
import store from '../store'

let loadingOptions = {
  lock: true,
  text: store.state.loadingText,
  spinner: 'el-icon-loading',
  background: 'rgba(0, 0, 0, 0.7)',
  customClass: 'loading-zindex'
}
setInterval(() => {
  loadingOptions.text = store.state.loadingText
}, 500)

export default {
  get: async (url, param, flag = true) => {
    let loading = null
    if (flag) {
      loading = Loading.service(loadingOptions)
    }
    url = process.env.BASE_API + url
    if (param) {
      url += '?' + common.serialize(param)
    }
    let rconfig = {
      credentials: 'include',
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'x-access-token': localStorage.getItem('accessToken')
      },
      mode: 'cors',
      cache: 'force-cache'
    }
    const response = await axios.get(url, rconfig)
    const result = response.data
    if (flag) {
      if (result.code === 0) {
        setTimeout(() => {
          loading.close()
        }, 500)
      } else {
        setTimeout(() => {
          loading.close()
        }, 1000)
        Message.error(result.msg)
      }
    }
    return result
  },
  upload: async (url, data = {}, flag = true) => {
    let loading = null
    if (flag) {
      loading = Loading.service(loadingOptions)
    }
    url = process.env.BASE_API + url
    let rconfig = {
      credentials: 'include',
      method: 'POST',
      xhrFields: {
        withCredentials: true
      },
      headers: {
        'Accept': '*/*',
        'Content-type': 'multipart/form-data',
        'x-access-token': localStorage.getItem('accessToken'),
        'withCredentials': true
      },
      mode: 'cors',
      cache: 'force-cache'
    }
    const response = await axios.post(url, data, rconfig)
    const result = response.data
    if (flag) {
      if (result.code === 0) {
        setTimeout(() => {
          loading.close()
        }, 500)
      } else {
        setTimeout(() => {
          loading.close()
        }, 1000)
        Message.error(result.msg)
      }
    }
    return result
  },
  delete: async (url, param, flag = true) => {
    let loading = null
    if (flag) {
      loading = Loading.service(loadingOptions)
    }
    url = process.env.BASE_API + url
    if (param) {
      url += '?' + common.serialize(param)
    }
    let rconfig = {
      credentials: 'include',
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'x-access-token': localStorage.getItem('accessToken')
      },
      mode: 'cors',
      cache: 'force-cache'
    }
    const response = await axios.delete(url, rconfig)
    const result = response.data
    if (flag) {
      if (result.code === 0) {
        setTimeout(() => {
          loading.close()
        }, 500)
      } else {
        setTimeout(() => {
          loading.close()
        }, 1000)
        Message.error(result.msg)
      }
    }
    return result
  },

  post: async (url, data = {}, flag = true) => {
    let loading = null
    if (flag) {
      loading = Loading.service(loadingOptions)
    }
    url = process.env.BASE_API + url
    let rconfig = {
      credentials: 'include',
      method: 'POST',
      xhrFields: {
        withCredentials: true
      },
      headers: {
        'Accept': 'application/json',
        'Content-type': 'application/json',
        'x-access-token': localStorage.getItem('accessToken'),
        'withCredentials': true
      },
      mode: 'cors',
      cache: 'force-cache'
    }
    const response = await axios.post(url, data, rconfig)
    const result = response.data
    if (flag) {
      if (result.code === 0) {
        setTimeout(() => {
          loading.close()
        }, 500)
      } else {
        setTimeout(() => {
          loading.close()
        }, 1000)
        Message.error(result.msg)
      }
    }
    return result
  },
  put: async (url, data = {}, flag = true) => {
    let loading = null
    if (flag) {
      loading = Loading.service(loadingOptions)
    }
    url = process.env.BASE_API + url
    let rconfig = {
      credentials: 'include',
      method: 'PUT',
      xhrFields: {
        withCredentials: true
      },
      headers: {
        'Accept': 'application/json',
        'Content-type': 'application/json',
        'x-access-token': localStorage.getItem('accessToken'),
        'withCredentials': true
      },
      mode: 'cors',
      cache: 'force-cache'
    }
    const response = await axios.put(url, data, rconfig)
    const result = response.data
    if (flag) {
      if (result.code === 0) {
        setTimeout(() => {
          loading.close()
        }, 500)
      } else {
        setTimeout(() => {
          loading.close()
        }, 1000)
        Message.error(result.msg)
      }
    }
    return result
  }
}

axios.interceptors.response.use(function (response) {
  return response
}, function (err) {
  const loading = Loading.service(loadingOptions)
  let errData = {
    ...err
  }
  if (typeof err === 'string') {
    errData.message = err
    setTimeout(() => {
      loading.close()
    }, 1000)
    return Promise.reject(errData)
  }
  if (err && err.response) {
    switch (errData.response.status) {
      case 400:
        errData.message = '请求错误(400)'
        break
      case 401:
        errData.message = '登录失效，请重新登录(401)'
        router.push('/login')
        break
      case 403:
        errData.message = '拒绝访问(403)'
        break
      case 404:
        errData.message = '请求出错(404)'
        break
      case 408:
        errData.message = '请求超时(408)'
        break
      case 500:
        errData.message = '服务器错误(500)'
        break
      case 501:
        errData.message = '服务未实现(501)'
        break
      case 502:
        errData.message = '网络错误(502)'
        break
      case 503:
        errData.message = '服务不可用(503)'
        break
      case 504:
        errData.message = '网络超时(504)'
        break
      case 505:
        errData.message = 'HTTP版本不受支持(505)'
        break
      default:
        errData.message = `连接出错(${errData.response.status})!`
    }
  } else {
    errData.message = '连接服务器失败!'
  }
  setTimeout(() => {
    loading.close()
  }, 1000)
  return Promise.reject(errData)
})
