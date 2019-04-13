export function isvalidUsername (str) {
  const validMap = ['admin', 'editor']
  return validMap.indexOf(str.trim()) >= 0
}

// 合法uri
export function validateURL (textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

// 字母和数字
export function validateCharNumber (str) {
  const reg = /^[a-zA-Z0-9]+$/
  return reg.test(str)
}

export function validateMaterialNum (str) {
  const reg = /^[a-zA-Z0-9-]+$/
  return reg.test(str)
}

// 纯数字
export function validateNumber (str) {
  const reg = /^[0-9]+$/
  return reg.test(str)
}

export function validateMoney (str) {
  const reg = /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/
  return reg.test(str)
}

// 小写字母
export function validateLowerCase (str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

// 大写字母
export function validateUpperCase (str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

// 大小写字母
export function validatAlphabets (str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

// 手机号码
export function validatTellphone (str) {
  const reg = /^1[34578]\d{9}$/
  return reg.test(str)
}

// 空格验证
export function validatSpace (str) {
  const reg = /\s/
  return reg.test(str)
}
// 验证前后空格
export function validatorSpace (str) {
  return str.replace(/(^\s*)|(\s*$)/g, '')
}
