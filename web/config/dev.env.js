'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"http://47.92.109.76:8082/runonce/"'
  //  BASE_API: '"http://192.168.1.73:8083/"'
  // BASE_API: '"http://192.168.1.10:8083/"'
})
