
export const common = {
  // 将base64转换为文件
  dataURLtoFile: function (dataurl, filename) {
    let arr = dataurl.split(',')
    let mime = arr[0].match(/:(.*?);/)[1]
    let bstr = atob(arr[1])
    let n = bstr.length
    let u8arr = new Uint8Array(n)
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n)
    }
    return new File([u8arr], filename, {type: mime})
  },
  formatDateTime: function (date, oper = 'time') {
    const newDate = new Date(date)
    const year = newDate.getFullYear()
    const month = newDate.getMonth() + 1
    const day = newDate.getDate()
    const hour = newDate.getHours()
    const minute = newDate.getMinutes()
    const second = newDate.getSeconds()

    if (oper === 'time') {
      return [year, month, day].map(this.formatNumber).join('-') + ' ' + [hour, minute, second].map(this.formatNumber).join(':')
    } else if (oper === 'date') {
      return [year, month, day].map(this.formatNumber).join('-')
    }
  },
  formatNumber: function (n) {
    n = n.toString()
    return n[1] ? n : '0' + n
  },
  /** 去掉字符串的前后空格 */
  trim: function (str) {
    return str.replace(/(^\s*)|(\s*$)/g, '')
  },
  serialize: function (data) {
    if (!data) return ''
    var pairs = []
    for (var name in data) {
      if (!data.hasOwnProperty(name)) continue // 排除嵌套对象
      if (typeof data[name] === 'function') continue // 排除操作数是函数

      var value = data[name]
      if (value) {
        value = value.toString()
      }
      name = encodeURIComponent(name)
      value = encodeURIComponent(value)
      pairs.push(name + '=' + value)
    }
    return pairs.join('&')
  },
  parseTime: function (time, cFormat) {
    if (arguments.length === 0) {
      return null
    }
    const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
    let date
    if (typeof time === 'object') {
      date = time
    } else {
      if (('' + time).length === 10) time = parseInt(time) * 1000
      date = new Date(time)
    }
    const formatObj = {
      y: date.getFullYear(),
      m: date.getMonth() + 1,
      d: date.getDate(),
      h: date.getHours(),
      i: date.getMinutes(),
      s: date.getSeconds(),
      a: date.getDay()
    }
    const timeStr = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
      let value = formatObj[key]
      if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1]
      if (result.length > 0 && value < 10) {
        value = '0' + value
      }
      return value || 0
    })
    return timeStr
  },
  formatTime: function (time, option) {
    time = +time * 1000
    const d = new Date(time)
    const now = Date.now()

    const diff = (now - d) / 1000

    if (diff < 30) {
      return '刚刚'
    } else if (diff < 3600) { // less 1 hour
      return Math.ceil(diff / 60) + '分钟前'
    } else if (diff < 3600 * 24) {
      return Math.ceil(diff / 3600) + '小时前'
    } else if (diff < 3600 * 24 * 2) {
      return '1天前'
    }
    if (option) {
      // return parseTime(time, option)
    } else {
      return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
    }
  },
  fm: function (s, n) {
    if (typeof (s) === 'number') {
      s = s.toFixed(n)
    }
    if (s === '' || s == null) return ''
    n = n > 0 && n <= 20 ? n : 2
    // s = parseFloat((s + '').replace(/[^\d\.-]/g, '')).toFixed(n) + ''
    s = parseFloat((s + '').replace(/[^\d.-]/g, '')).toFixed(n) + ''
    if (s * 1 === 0) return ''
    let l = s.split('.')[0].split('').reverse()
    let r = s.split('.')[1]
    let t = ''
    for (let i = 0; i < l.length; i++) {
      t += l[i] + ((i + 1) % 3 === 0 && (i + 1) !== l.length ? ',' : '')
    }
    return t.split('').reverse().join('').replace('-,', '-') + '.' + r
  },
  getToken: function () {
    return sessionStorage.getItem('webToken') ? sessionStorage.getItem('webToken') : ''
  },
  setToken: function (val) {
    sessionStorage.setItem('webToken', val)
  },
  getStore: function (key) {
    return sessionStorage.getItem(key) ? sessionStorage.getItem(key) : ''
  },
  setStore: function (key, val) {
    sessionStorage.setItem(key, val)
  },
  download (name, url) {
    // url指向第三方资源，download会失效；url指向同源资源，是正常
    let saveLink = document.createElement('a')
    saveLink.download = name
    saveLink.href = url
    saveLink.click()
  }
}
