import req from '@/utils/request'

// 办事指南
export const administrativeService = {
  // 行政许可查询
  getAll: (params) => req.get(`businessGuideAdministrativeLicensingOne/getAllList?eventType=${params}`),
  getAdministrativeShow: (params) => req.post(`businessGuideAdministrativeLicensingOne/findByEventId?eventId=${params}`),
  getRecordShow: (params) => req.post(`businessGuideFilingOne/findByEventId?eventId=${params}`),
  // 审核转报
  getAllAudit: (params) => req.post(`businessGuideAuditAndForwardingCategoryOne/findByEventId?eventId=${params}`),
  // 其他
  getAllOther: (params) => req.post(`businessGuideOtherServicesOne/findByEventId?eventId=${params}`),
  // 下拉
  // 年审或年检
  yearCarefulSelectList: (params) => req.post('annualReviewOrView/list', params),
  // 预约办理
  makeAppointmentSelectList: (params) => req.post('appointmentProcessing/list', params),
  // 大事项
  majorTypesOfEventsSelectList: (params) => req.post('catalogueEventType/list', params),
  // 事项小类型
  itemSmallTypeSelectList: (params) => req.post(`catalogueItemSubtype/list?Pid=${params}`),

  // 委托代办
  entrustedSelectList: (params) => req.post('concierge/list', params),
  // 决定公开
  decidedOpenSelectList: (params) => req.post('decidedToMakePublic/list', params),
  // 事项星级
  mattersStarSelectList: (params) => req.post('eventStar/list', params),
  // 行使层级
  exerciseHierarchySelectList: (params) => req.post('exerciseHierarchy/list', params),
  // 办理形式
  handlingFormSelectList: (params) => req.post('handlingForm/list', params),
  // 材料类型
  materialTypeSelectList: (params) => req.post('materialType/list', params),
  // 实施主体性质
  mainPropertiesSelectList: (params) => req.post('natureOfImplementingSubject/list', params),
  // 网上申请形式
  onlineApplicationSelectList: (params) => req.post('onlineApplicationForm/list', params),
  // 权限划分
  purviewDivisionSelectList: (params) => req.post('permissionDivision/list', params),
  // 批前公示
  beforePublicSelectList: (params) => req.post('prePublicationPublicity/list', params),
  // 通办范围
  scopeGeneralOfficeSelectList: (params) => req.post('scopeOfOperation/list', params),
  // 服务对象
  serviceObjectSelectList: (params) => req.post('serviceObject/list', params),
  // 来源渠道
  sourcesSelectList: (params) => req.post('sourceChannel/list', params),
  // 申请材料递送方式
  methodOfDeliveryOfApplicationMaterialsSelectList: (params) => req.post('isLogistics/list', params),
  // 服务主题
  serviceThemeSelectList: (params) => req.post(`serviceTopic/list`, params),
  // 办件类型
  typeOfOfficeSelectList: (params) => req.post('typeOfOffice/list', params),
  // 权力来源
  powerSourceSelectList: (params) => req.post('sourceOfPower/list', params),
  // 保存
  // 行政许可类保存
  administrativeSave: (params) => req.post('businessGuideAdministrativeLicensingOne/save', params),
  // 市级以下
  administrativeSaveFiilng: (params) => req.post('businessGuideAdministrativeLicensingOne/savePrivate', params),
  // 备案类
  recordSave: (params) => req.post('businessGuideFilingOne/save', params),
  // 市级以下
  recordSaveFiling: (params) => req.post('businessGuideFilingOne/savePrivate', params),
  // 其他类
  otherSave: (params) => req.post('businessGuideOtherServicesOne/save', params),
  // 市级以下
  otherSaveFiling: (params) => req.post('businessGuideOtherServicesOne/savePrivate', params),
  // 审核转报
  auditSave: (params) => req.post('businessGuideAuditAndForwardingCategoryOne/save', params),
  // 市级以下
  auditSaveFiling: (params) => req.post('businessGuideAuditAndForwardingCategoryOne/savePrivate', params)
}

// 申请类型
export const applyState = {
  applyAdd: param => req.post(`MinderData/add`, param)
}
// 审核细则
export const auditRule = {
  getDetail: param => req.get(`auditRules/selectByMaterialId?MaterialId=${param}`)
}

export const base64Server = {
  getpicture: param => req.get(`picture/getpicture?fileName=${param}`)
}

export const centralDept = {
  getAll: () => req.get(`centralBusinessGuidanceDepartmentCode/list`)
}

// 从已有输出物导入
export const dataImport = {
  importFile: (param, file) => req.upload(`dataImport/importExcel?type=${param.type}&version=${param.version}&departmentalMattersId=${param.departmentalMattersId}`, file)
}

// 部门管理
export const department = {
  checkHasChildren: param => req.get(`xboot/department/checkHasChildren?deptId=${param}`),
  selectAll: (param = '2', dataFilter = true) => req.get(`xboot/department/getall?openDataFilter=${dataFilter}&IsLocation=${param}`),
  getByParent: param => req.get(`xboot/department/getByParentId/${param}?openDataFilter=true&IsLocation=false`),
  search: param => req.get(`xboot/department/search?title=${param}&openDataFilter=true`),
  add: param => req.post(`xboot/department/add`, param),
  update: param => req.post(`xboot/department/edit`, param),
  delete: param => req.delete(`xboot/department/delByIds/${param}`)
}
// 事项管理
export const departmentalMatters = {
  // 事项列表
  selectByParam: param => req.post(`departmentalMatters/selectbyparam`, param),
  updateState: param => req.post(`departmentalMatters/submit`, param),
  // 复制
  copy: param => req.post(`departmentalMatters/copy`, param),
  // 复制
  selectPageByDept: param => req.post(`departmentalMatters/select`, param),
  stateUpdate: param => req.get(`departmentalMatters/updateState/${param}`),
  getState: param => req.get(`departmentalMatters/getRevisable/${param}`),
  changeDept: param => req.put(`departmentalMatters/changeDept`, param)
}

// 系统对接方案默认图片
export const defaultPic = {
  selectAll: () => req.get(`defaultSystemDockingPic/selectall`)
}

// 事项分配
export const eventTable = {
  getEncoding: param => req.post(`events_table/getEncoding`, param),
  getList: param => req.post(`events_table/list`, param),
  selectByDeptId: param => req.get(`events_table/selectbydeptid/${param}`),
  add: param => req.post(`events_table/add`, param),
  update: param => req.put(`events_table/update`, param),
  delete: param => req.delete(`events_table/delete?id=${param}`),
  distribution: param => req.post(`events_table/distribution`, param)
}

export const exportTable = {
  export: param => req.get(`dataExport/showExcel/${param}`)
}

// 表单说明
export const formInstructions = {
  // 点击图片查询
  getAllphotoForm: (params) => req.post('formDescription/findFormThat', params),
  // 显示材料
  formInstruction: (params) => req.get(`formDescription/findMaterial/` + params),
  // 保存
  save: (params) => req.post('formDescription/save', params),
  // 显示文本图片
  showText: (params) => req.post(`formDescription/showText?file=${params}`)
}

export const loginService = {
  login: (params) => req.post('xboot/user/login?' + params),
  // 获取用户登录信息
  userInfo: (params) => req.get('xboot/user/info', params),
  // github登录
  githubLogin: (params) => req.get('xboot/social/github/login', params),
  // qq登录
  qqLogin: (params) => req.get('xboot/social/qq/login', params),
  // 微博登录
  weiboLogin: (params) => req.get('xboot/social/weibo/login', params),
  // 获取JWT
  getJWT: (params) => req.get('xboot/social/getJWT', params),
  // 发送短信验证码
  sendSms: (mobile, params) => req.get(`xboot/common/captcha/sendSms/${mobile}`, params),
  // 短信验证码登录
  smsLogin: (params) => req.post('xboot/user/smsLogin', params)
}

export const jointSectoral = {
  selectByDeptId: param => req.post(`jointSectoralMatters/selectbydeptid`, param),
  add: param => req.post(`jointSectoralMatters/add`, param),
  update: param => req.put(`jointSectoralMatters/update`, param),
  delete: param => req.delete(`jointSectoralMatters/delete?id=${param}`)
}

// 三级四同
export const levelWithService = {
  addSave: params => req.post('eventDirectory/add', params),
  editSave: params => req.put('eventDirectory/update', params),
  delete: params => req.post(`eventDirectory/delete`, params),
  allselect: params => req.post('eventDirectory/list', params),
  selectCatalog: params => req.get(`eventDirectory/catalog/${params}`),
  findName: (eventName, id) => req.get(`eventDirectory/findNameCount?eventName=${eventName}&id=${id}`)
}

// 材料分组
export const material = {
  getByEventId: param => req.get(`materialGrouping/select/${param}`),
  save: param => req.post(`materialGrouping/save`, param)
}

// 评论
export const matterProposal = {
  add: param => req.post(`matterProposalQuestion/add`, param),
  updateState: param => req.put(`matterProposalQuestion/updateState`, param),
  findByPage: param => req.post(`matterproposal/findAllByCondition`, param),
  answer: param => req.post(`matterProposalAnswer/add`, param),
  examine: param => req.get(`matterproposal/launchedExamineAndApprove/${param}`),
  savePass: param => req.get(`matterproposal/savePass/${param}`),
  selectSheetState: param => req.post(`matterproposal/selectSheetState`, param)
}

// 菜单
export const menu = {
  getAll: () => req.get(`xboot/permission/getAllList`),
  // 获取菜单
  getMenuList: () => req.get('xboot/permission/getMenuList')
}

// 菜单管理
export const menuService = {
  // 通过类型获取字典数据
  getDictDataByType: (type, params) => req.get(`xboot/dictData/getByType/${type}`, params),
  // 获取全部权限数据
  getAllPermissionList: (params) => req.get('xboot/permission/getAllList', params),
  // 添加
  addPermission: (params) => req.post('xboot/permission/add', params),
  // 搜索权限
  searchPermission: (params) => req.get('xboot/permission/search', params),
  // 编辑权限
  editPermission: (params) => req.post('xboot/permission/edit', params),
  // 删除权限
  deletePermission: (ids, params) => req.delete(`xboot/permission/delByIds/${ids}`, params)
}

// 资格预审
export const prequalification = {
  getByEventId: param => req.get(`prequalification/eventId?eventId=${param}`),
  save: param => req.post(`prequalification/save`, param)
}

// 量化呈现
export const present = {
  getByEventId: param => req.get(`quantitativeRendering/select/${param}`),
  add: param => req.post(`quantitativeRendering/add`, param)
}

export const process = {
  addprocess: (param) => req.post('processdescriptionpic/add', param),
  getProcess: (id) => req.get('processdescriptionpic/eventId?eventId=' + id),
  saveProcessImage: (param) => req.post('quantitativeRendering/generatepngbyxml', param),
  createPng: (param) => req.post('processdescriptionpic/generatepngbyxml', param),
  saveimage: param => req.post('processdescriptionpic/saveimage', param)
}

// 流程描述
export const processTable = {
  getByEventId: param => req.get(`processdescription/eventId?eventId=${param}`),
  save: param => req.post(`processdescription/save`, param)
}

export const revisionhistory = {
  selectByDmId: param => req.get(`revisionhistory/getByDmId?id=${param}`),
  add: param => req.post(`revisionhistory/add`, param),
  update: param => req.put(`revisionhistory/update`, param),
  ComerButton: param => req.get(`revisionhistory/ComerButton?dmId=${param}`),
  SuperButton: param => req.get(`revisionhistory/SuperButton?id=${param}`),
  curriculumVitae: param => req.get(`revisionhistory/curriculumVitae?dmId=${param}`)
}

// 角色
export const role = {
  getAllByPage: param => req.post(`xboot/role/getAllByPage`, param),
  save: param => req.post(`xboot/role/save`, param),
  edit: param => req.post(`xboot/role/edit`, param),
  delete: param => req.delete(`xboot/role/delAllByIds/${param}`),
  setDefault: param => req.get(`xboot/role/setDefault${param}`),
  // 数据权限保存
  editRoleDep: param => req.get(`xboot/role/editRoleDep${param}`),
  // 菜单权限
  editRolePerm: param => req.get(`xboot/role/editRolePerm${param}`)
}

export const situationBrainMap = {
  add: param => req.post(`situationBrainMap/add`, param)
}

// 情形引导
export const situationToGuide = {
  selectByPage: param => req.get(`situationToGuide/findByEventName?page=${param.page}&size=${param.size}&eventName=${param.eventName}`),
  add: param => req.post(`situationToGuide/add`, param),
  delete: param => req.delete(`situationToGuide/delete/${param}`),
  addChildren: param => req.post(`situationToGuide/addChildren`, param),
  findChildren: param => req.get(`situationToGuide/findChildren/${param}`)
}

// 办理项归集
export const situationToGuideChild = {
  selectByPage: param => req.post(`situationToGuideChildren/selectByCondition`, param),
  edit: param => req.post(`situationToGuideChildren/saveFrom`, param),
  delete: param => req.delete(`situationToGuideChildren/delete/${param}`),
  setting: param => req.post(`situationToGuideChildren/configurationMatters`, param),
  searchConfig: param => req.get(`situationToGuideChildren/selectConfigurationMatters/${param}`)
}

// 系统对接方案
export const systemdocking = {
  getByEventId: param => req.get(`systemdocking/select/${param}`),
  add: param => req.post(`systemdocking/save`, param)
}

// 待办事项
export const todoTask = {
  selectTodo: param => req.post(`tasktodo/selectTodo`, param),
  clickTodo: param => req.get(`tasktodo/clickTodo?id=${param}`),
  // 事项统计
  getEventStatistics: param => req.get(`departmentalMatters/findDealCount`),
  // 事项级别统计
  findEventLevel: param => req.get(`departmentalMatters/findEventLevel`),
  // 各个区县事项统计
  findEveryAreaCount: param => req.get(`departmentalMatters/findEveryAreaCount`)
}

// 用户管理
export const userMangeService = {
  // 获取一级部门
  initDepartment: (params) => req.get('xboot/department/getDropList', params),
  // 通过类型获取字典数据
  getDictDataByType: (type, params) => req.get(`xboot/dictData/getByType/${type}`, params),
  // 获取全部用户数据
  getAllUserData: (params) => req.get('xboot/user/getAll', params),
  // 根据条件查询
  getUserData: (params) => req.post('xboot/user/getByCondition', params),
  // 添加用户
  addUser: (params) => req.post('xboot/user/admin/save', params),
  // 修改用户密码
  editPassword: (params) => req.post('xboot/user/admin/revisePassWord', params),
  // 重置用户密码
  reset: (params) => req.post('xboot/user/admin/resetPassWord', params),
  // 启用用户
  enableUser: (id) => req.get(`xboot/user/admin/enable/${id}`),
  // 禁用用户
  disableUser: (id) => req.get(`xboot/user/admin/disable/${id}`),
  // 删除用户
  deleteUser: (ids, params) => req.delete(`xboot/user/delByIds/${ids}`, params),
  // 获取全部角色数据
  getAllRoleList: (params) => req.get('xboot/role/getAllList', params),
  // 加载部门子级数据
  loadDepartment: (params) => req.get(`xboot/department/getDropSonList/${params}`),
  // 获取用户数据 多条件
  getUserListData: (params) => req.get('xboot/user/getAll', params)
}
