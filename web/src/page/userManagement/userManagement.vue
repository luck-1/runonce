<template>
  <div class="search">
    <Card>
      <Row>
        <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
          <Form-item label="用户名" prop="userName">
            <Input type="text" v-model="searchForm.userName" clearable placeholder="请输入用户名" style="width: 200px"/>
          </Form-item>
          <Form-item label="所属部门" prop="department">
            <Cascader v-model="selectDep" :data="department" :load-data="loadData" :render-format="format" @on-change="handleChangeDep" change-on-select filterable placeholder="请选择或输入搜索部门" style="width: 200px"></Cascader>
          </Form-item>

          <span v-if="drop">
            <Form-item label="手机号码" prop="mobile">
              <Input type="text" v-model="searchForm.mobile" clearable placeholder="请输入手机号" style="width: 200px"/>
            </Form-item>
            <Form-item label="邮箱" prop="email">
              <Input type="text" v-model="searchForm.email" clearable placeholder="请输入邮箱" style="width: 200px"/>
            </Form-item>
            <Form-item label="性别" prop="sex">
              <Select v-model="searchForm.sex" placeholder="请选择" clearable style="width: 200px">
                <Option v-for="(item, i) in dictSex" :key="i" :value="item.value">{{item.title}}</Option>
              </Select>
            </Form-item>
            <Form-item label="用户类型" prop="type">
              <Select v-model="searchForm.type" placeholder="请选择" clearable style="width: 200px">
                <Option value="0">普通用户</Option>
                <Option value="1">管理员</Option>
              </Select>
            </Form-item>
            <Form-item label="用户状态" prop="status">
              <Select v-model="searchForm.status" placeholder="请选择" clearable style="width: 200px">
                <Option value="0">正常</Option>
                <Option value="-1">禁用</Option>
              </Select>
            </Form-item>
          </span>
          <Form-item style="margin-left:-35px;" class="br">
            <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
            <!-- <Button @click="handleReset" >重置</Button> -->
            <a class="drop-down" @click="dropDown">{{dropDownContent}}
              <Icon :type="dropDownIcon"></Icon>
            </a>
          </Form-item>
        </Form>
      </Row>
      <Row class="operation">
        <Button @click="add" type="primary" icon="md-add">添加用户</Button>
        <Button @click="delAll" icon="md-trash">批量删除</Button>
        <Button @click="transfer" icon="ios-paper-plane">转移</Button>
      </Row>
      <Row>
        <Alert show-icon>
          已选择 <span class="select-count">{{selectCount}}</span> 项
          <a class="select-clear" @click="clearSelectAll">清空</a>
        </Alert>
      </Row>
      <Row>
        <el-table border size="small" :data="datas" height="calc(100vh - 330px)" v-loading="loading" @sort-change="changeSort" @selection-change="showSelect" ref="table">
          <el-table-column fixed="left" align="center" type="selection" width="40"></el-table-column>
          <el-table-column fixed="left" align="center" type="index" label="序号" width="55"></el-table-column>
          <el-table-column fixed="left" align="center" label="用户名" sortable width="150" prop="userName"></el-table-column>
          <el-table-column align="center" label="头像" prop="avatar" width="100">
            <template slot-scope="{row}">
              <Avatar :src="row.avatar"></Avatar>
            </template>
          </el-table-column>
          <el-table-column align="center" label="所属部门" prop="departmentTitle" width="150"></el-table-column>
          <el-table-column align="center" label="手机号码" sortable prop="mobile" width="170"></el-table-column>
          <el-table-column align="center" label="邮箱" sortable prop="email" width="200"></el-table-column>
          <el-table-column align="center" label="性别" prop="sex" :formatter="formatter" width="90"></el-table-column>
          <el-table-column align="center" label="用户类型" prop="type" width="100">
            <template slot-scope="{row}">
              {{row.type===0?'普通用户':'管理员'}}
            </template>
          </el-table-column>
          <el-table-column align="center" label="用户状态" prop="status" width="140" :filters="[{ text: '正常启用', value: 0 }, { text: '禁用', value: -1 }]" :filter-method="filterTag" :filter-multiple="false" filter-placement="bottom-end">
            <template slot-scope="{row}">
              <el-tag size="small" :type="row.status===0?'success':'danger'" disable-transitions>
                {{row.status === 0?'正常启用':'禁用'}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="创建时间" prop="createTime" width="200"></el-table-column>
          <el-table-column align="center" fixed="right" label="操作" width="180">
            <template slot-scope="{row}">
              <div class="table-row-btn">
                <el-tooltip content="重置密码" placement="top">
                  <Button @click="defaultPassword(row)" type="primary" icon="md-refresh" size="small"></Button>
                </el-tooltip>
                <el-tooltip content="禁用" placement="top" v-if="row.status===0">
                  <Button @click="disable(row)" icon="md-pause" size="small"></Button>
                </el-tooltip>
                <el-tooltip content="启用" placement="top" v-else>
                  <Button @click="enable(row)" icon="md-play" size="small"></Button>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <Button @click="edit(row)" type="primary" icon="md-create" size="small"></Button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <Button @click="remove(row)" type="error" icon="md-trash" size="small"></Button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </Row>
      <Row type="flex" justify="center" class="page">
        <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[10,20,50]" size="small" show-total show-elevator show-sizer></Page>
      </Row>
    </Card>
    <!-- 新增修改用户 -->
    <Modal :title="modalTitle" v-model="userModalVisible" :mask-closable='false' :width="600" :styles="{top: '30px'}">
      <Form ref="userForm" :model="userForm" :label-width="70" :rules="userFormValidate">
        <el-row :gutter="20">
          <el-col :span="12">
            <FormItem label="用户名" prop="userName">
              <Input v-model="userForm.userName" autocomplete="off" placeholder="请输入"/>
            </FormItem>

            <FormItem label="密码" prop="password" v-if="modalType===0" :error="errorPass">
              <Input type="password" v-model="userForm.password" placeholder="请输入"/>
            </FormItem>

            <FormItem label="邮箱" prop="email">
              <Input v-model="userForm.email" placeholder="请输入"/>
            </FormItem>

            <Form-item label="头像" prop="avatar">
              <Poptip trigger="hover" title="图片预览" placement="right" width="350">
                <Input v-model="userForm.avatar" placeholder="可直接填入网络图片链接" clearable/>
                <div slot="content">
                  <img :src="userForm.avatar" alt="无效的图片链接" style="width: 100%;margin: 0 auto;display: block;">
                  <a @click="viewPic()" style="margin-top:5px;text-align:right;display:block">查看原图</a>
                </div>
              </Poptip>
            </Form-item>
          </el-col>
          <el-col :span="12">
            <FormItem label="手机号码" prop="mobile">
              <Input v-model="userForm.mobile" :maxlength="11" placeholder="请输入"/>
            </FormItem>

            <FormItem label="确认密码" v-if="modalType===0" prop="passwdCheck" placeholder="请输入">
              <Input type="password" v-model="userForm.passwdCheck"/>
            </FormItem>

            <FormItem label="性别" prop="sex">
              <RadioGroup v-model="userForm.sex">
                <Radio v-for="(item, i) in dictSex" :key="i" :label="Number(item.value)">{{item.title}}</Radio>
              </RadioGroup>
            </FormItem>

            <Upload action=""
              :show-upload-list="false"
              :format="['jpg','jpeg','png','gif']"
              :max-size="5120"
              :on-format-error="handleFormatError"
              :on-exceeded-size="handleMaxSize"
              :before-upload="beforeUpload"
              ref="up"
              class="upload">
              <Button icon="ios-cloud-upload-outline">上传图片</Button>
            </Upload>
          </el-col>
        </el-row>
        <el-row>
          <Form-item label="所属部门" prop="departmentTitle">
            <Poptip trigger="click" placement="right" width="500" style="width:70%">
              <div slot="title">
                <el-row>
                <el-col :span="24">选择部门</el-col>
                </el-row>
              </div>
              <div style="display:flex;">
                <Input v-model="userForm.departmentTitle" placeholder="请选择" readonly style="width:calc(72% - 45px);margin-right:20px;"/>
                <Button icon="md-trash" @click="clearSelectDep">清空已选</Button>
              </div>
              <div slot="content" class="tree-bar">
                <!-- <Input v-model="searchKey" suffix="ios-search" @on-change="searchDp" placeholder="输入部门名搜索" clearable/> -->
                <!-- <Tree :data="dataDep" :load-data="loadDataTree"  @on-select-change="selectTree"   ></Tree>
                <Spin size="large" fix v-if="dpLoading"></Spin> -->
                  <el-row>
                <el-col :span="13">
                    <el-tree class="dept-tree" ref="regionTree" default-expand-all  highlight-current node-key="id" :data="listArea" :props="defaultProps" :expand-on-click-node="false" @node-click="selectRegion"></el-tree>
                </el-col>
                <el-col :span="11">
                    <el-tree class="dept-tree" ref="tree" default-expand-all  highlight-current node-key="id" :data="depTree" :props="defaultProps" :expand-on-click-node="false" @node-click="clickDepTree"></el-tree>
                </el-col>
                </el-row>
              </div>
            </Poptip>
          </Form-item>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <FormItem label="用户类型" prop="type">
              <Select v-model="userForm.type" placeholder="请选择">
                <Option :value="0">普通用户</Option>
                <Option :value="1">管理员</Option>
              </Select>
            </FormItem>
          </el-col>
          <el-col :span="12">
            <FormItem label="角色分配" prop="roles">
              <Select v-model="userForm.roles" v-if="userInfo.id!==userForm.id || modalTitle==='添加用户'">
                <Option v-for="item in roleList" :value="item.id" :key="item.id" :label="item.description">
                  <span style="margin-right:10px;">{{ item.name }}</span>
                  <span style="color:#ccc;">{{ item.description }}</span>
                </Option>
              </Select>
              <Input v-else v-model="userForm.description" readonly/>
            </FormItem>
          </el-col>
        </el-row>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelUser">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitUser('userForm')">提交</Button>
      </div>
    </Modal>
    <!-- 查看用户图像 -->
    <Modal title="图片预览" v-model="viewImage" :styles="{top: '30px'}" draggable>
      <img :src="userForm.avatar" alt="无效的图片链接" style="width: 100%;margin: 0 auto;display: block;">
      <div slot="footer">
        <Button @click="viewImage=false">关闭</Button>
      </div>
    </Modal>

    <!-- 转移 -->
    <change title="人员转移" :visible="modalVisible" :changeValue="multipleSelection" @cancel="modalVisible=false" @changed="modalVisible=false;handleSearch()"></change>
  </div>
</template>
<script>
import expandRow from "./expand.vue";
import Change from '@/components/change'

import { aliConfig } from "@/utils/aliyunConfig";
import { validatSpace } from "@/utils/validate";
import expandVue from "./expand.vue";
import { department, userMangeService } from '@/api/service'
const OSS = require("ali-oss");
const client = new OSS(aliConfig);
export default {
  name: "user-manage",
  components: { expandRow, Change },
  data() {
    const validateuserName = (rule, value, callback) => {
      if (!value) {
        callback(new Error("用户名不能为空"));
      } else if (validatSpace(value)) {
        callback(new Error("用户名不能包含空字符"));
      }
      callback();
    };
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error("密码不能为空"));
      } else if (value.length < 6) {
        callback(new Error("密码长度不得小于6位"));
      } else {
        if (this.userForm.passwdCheck) {
          // 对第二个密码框单独验证
          this.$refs.userForm.validateField("passwdCheck");
        }
        callback();
      }
    };
    const validatePassCheck = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.userForm.password) {
        callback(new Error("两个输入密码不匹配!"));
      } else {
        callback();
      }
    };
    const validateMobile = (rule, value, callback) => {
      var reg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (value === "" || value === null || value === undefined) {
        callback(new Error("手机号码不能为空"));
      } else if (!reg.test(value)) {
        callback(new Error("手机号码格式错误"));
      } else {
        callback();
      }
    };
    const validatedepartment = (rule, value, callback) => {
      if (value === "" || value === null || value === undefined) {
        callback(new Error("部门不能为空"));
      } else {
        callback();
      }
    };
    return {
      userInfo: JSON.parse(localStorage.userInfo),
      imageUrl: "",
      fileList: [{ name: "", url: "" }],
      dpLoading: false, // 部门树加载
      loading: true,
      operationLoading: false,
      loadingExport: true,
      drop: false,
      dropDownContent: "展开",
      dropDownIcon: "ios-arrow-down",
      selectCount: 0,
      selectList: [],
      multipleSelection: [],
      viewImage: false,
      department: [],
      selectDep: [],
      dataDep: [],
      searchKey: "",
      searchForm: {
        userName: null,
        departmentId: null,
        mobile: null,
        email: null,
        sex: null,
        type: null,
        status: null,
        pageNumber: 1,
        pageSize: 10,
        sort: "createTime",
        order: "desc",
        startDate: null,
        endDate: null
      },
      fid: "",
      selectDate: null,
      modalType: 0,
      userModalVisible: false,
      modalTitle: "",
      userForm: {
        sex: 1,
        type: 0,
        avatar: "https://s1.ax1x.com/2018/05/19/CcdVQP.png",
        roles: "",
        departmentId: "",
        departmentTitle: "",
        email: "",
        // avatar: "",
        password: "",
        userName: "",
        mobile: "",
        locationId:null
      },
      userRoles: [],
      roleList: [],
      errorPass: "",
      userFormValidate: {
        userName: [
          { required: true, validator: validateuserName, trigger: "blur" }
        ],
        password: [
          { required: true, validator: validatePassword, trigger: "blur" }
        ],
        passwdCheck: [
          { required: true, validator: validatePassCheck, trigger: "blur" }
        ],
        mobile: [
          { required: true, validator: validateMobile, trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" },
          { type: "email", message: "邮箱格式不正确", trigger: "blur" }
        ],
        departmentTitle: [
          { required: true, message: "部门不能为空", trigger: "change" }
        ],
        roles: [{ required: true, message: "角色不能为空", trigger: "change" }]
      },
      submitLoading: false,
      datas: [],
      total: 0,
      dictSex: [],
      listArea:[], //区域
      depTree:[],//部门
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      // 转移
      modalVisible: false,
    };
  },
  mounted() {
    this.init();
    this.getRoleList();
  },
  methods: {
    init() {
      this.initDepartmentData();
      this.getUserList();
      this.initDepartmentTreeData();
      this.getDictSexData();
      // this.getUserData();
      this.getList('1');
    },
    async getList (flag, parentId=null) {
      let res  = await department.selectAll(flag)
      if(res.code === 0) {
        let list = []
        for (let x of res.obj) {
          if (parentId) {
            if (x.parentId === parentId) {
              list.push(this.formatDeptList(x, res.obj))
            }
          } else {
            if (x.parentId === '0') {
              list.push(this.formatDeptList(x, res.obj))
            }
          }
        }
         this.listArea=[...list]
        return list
      } else {
        return []
      }
    },
    formatDeptList (data, datas) {
      let obj = data
      obj.children = []
      for (let x of datas) {
        if (obj.id === x.parentId) {
          obj.children.push(this.formatDeptList(x, datas))
        }
      }
      return obj
    },
    // 区域点击
    selectRegion (data) {
      this.currentRegion = data
      this.getDeptTreeList(data.id)
    },

    async getDeptTreeList(id,parentId=null){
      let res  = await department.getByParent(id)
      if(res.code === 0) {
        let arr = []
        for (let x of res.obj) {
          arr.push(this.formatDeptList(x, res.obj))
        }
        this.depTree=[...arr];
        return arr
      } else {
          return []
      }
    },

    //选部门
    clickDepTree(val){
      this.userForm.departmentId = val.id;
      this.userForm.departmentTitle = val.title;
      this.userForm.locationId= val.parentId;
    },

    getDictSexData() {
      // 获取性别字典数据
      userMangeService.getDictDataByType("sex").then(res => {
        if (res.code === 0) {
          this.dictSex = res.obj;
        }
      });
    },
    // 部门菜单
    initDepartmentData() {
      userMangeService.initDepartment().then(res => {
        if (res.code === 0) {
          res.obj.forEach(item => {
            if (item.isExistSon === 1) {
              item.value = item.id;
              item.label = item.title;
              item.loading = false;
              item.children = [];
            } else {
              item.value = item.id;
              item.label = item.title;
            }
            if (item.status === -1) {
              item.label = "[已禁用] " + item.label;
              item.disabled = true;
            }
          });
          this.department = res.obj;
        }
      });
    },
    // 初始化部门
    initDepartmentTreeData() {
      userMangeService.initDepartment().then(res => {
        if (res.code === 0) {
          res.obj.forEach(function(e) {
            if (e.isExistSon === 1) {
              e.loading = false;
              e.children = [];
              // if (e.isExistSon) {
              //   e.children = [];
              // }
            }
            if (e.status === -1) {
              e.title = "[已禁用] " + e.title;
              e.disabled = true;
            }
          });

          this.dataDep = res.obj;
        }
      });
    },
    // 下一级部门
    loadData(item, callback) {
      item.loading = true;
      userMangeService.loadDepartment(item.value).then(res => {
        item.loading = false;
        if (res.code === 0) {
          res.obj.forEach(function(e) {
            if (e.isExistSon === 1) {
              e.value = e.id;
              e.label = e.title;
              e.loading = false;
              e.children = [];
            } else {
              e.value = e.id;
              e.label = e.title;
            }
            if (e.status === -1) {
              e.label = "[已禁用] " + e.label;
              e.disabled = true;
            }
          });
          item.children = res.obj;
          callback();
        }
      });
    },
    // 选择部门
    handleChangeDep(value, selectedData) {
      // 获取最后一个值
      if (value && value.length > 0) {
        this.searchForm.departmentId = value[value.length - 1];
      } else {
        this.searchForm.departmentId = "";
      }
    },
    // 格式化选中的值
    format(labels, selectedData) {
      const index = labels.length - 1;
      return labels[index];
    },
    loadDataTree(item, callback) {
      userMangeService.loadDepartment(item.id).then(res => {
        if (res.code === 0) {
          res.obj.forEach(function(e) {
            if (e.isExistSon === 1) {
              e.loading = false;
              e.children = [];
              // if (e.isExistSon) {
              //   e.children = [];
              // } else {
              //   delete e.children;
              // }
            }
            if (e.status === -1) {
              e.title = "[已禁用] " + e.title;
              e.disabled = true;
            }
          });
          callback(res.obj);
        }
      });
    },
    selectTree(v) {
      if (v.length > 0) {
        // 转换null为""
        for (let attr in v[0]) {
          if (v[0][attr] === null) {
            v[0][attr] = "";
          }
        }
        let str = JSON.stringify(v[0]);
        let thisData = JSON.parse(str);
        this.userForm.departmentId = thisData.id;
        this.userForm.departmentTitle = thisData.title;
      }
    },
    clearSelectDep() {
        this.userForm.locationId=""
      this.userForm.departmentId = "";
      this.userForm.departmentTitle = "";
    },
    handleChangeUserFormDep(value, selectedData) {
      // 获取最后一个值
      if (value && value.length > 0) {
        this.userForm.departmentId = value[value.length - 1];
      } else {
        this.userForm.departmentId = "";
      }
    },
    changePage(v) {
      this.searchForm.pageNumber = v;
      this.getUserList();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.searchForm.pageSize = v;
      this.getUserList();
    },
    getUserList() {
      this.getUserData();
    },
    handleSearch() {
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.getUserList();
      this.getUserData();
    },
    handleReset() {
      this.$refs.searchForm.resetFields();
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.selectDate = null;
      this.searchForm.startDate = "";
      this.searchForm.endDate = "";
      this.selectDep = [];
      this.searchForm.departmentId = "";
      // 重新加载数据
      this.getUserList();
    },
    formatter (row, column) {
      let title = ''
      this.dictSex.forEach(item => {
        if (item.value == row.sex) {
          title = item.title
        }
      })
      return title
    },
    filterTag (value, row) {
      return row.status === value
    },
    changeSort({column, prop, order}) {
      this.searchForm.sort = prop
      this.searchForm.order = order?(order==='descending'?'desc':'asc'):''
      this.getUserList();
    },
    // 重置密码
    defaultPassword(row){
      userMangeService.reset({ passWord: '000000', userId: row.id }).then(res => {
        if(res.code === 0) {
          this.$message.success('重置密码为000000')
        }
      })
    },
    getRoleList() {
      userMangeService.getAllRoleList().then(res => {
        if (res.code === 0) {
          this.roleList = res.obj;
        }
      });
    },
    cancelUser() {
      this.userModalVisible = false;
    },
    async getUserData() {
      let param = {
        ...this.searchForm,
        currentPage: this.searchForm.pageNumber,
        pageSize: this.searchForm.pageSize
      };
      let res = await userMangeService.getUserData(param);
      if (res.code === 0) {
        this.loading = false;
        res.obj.list.forEach(item => {
          item.avatar = item.avatar
            ? item.avatar === "https://s1.ax1x.com/2018/05/19/CcdVQP.png"
              ? item.avatar
              : client.signatureUrl(item.avatar, { expires: 80000 })
            : "https://s1.ax1x.com/2018/05/19/CcdVQP.png";
          item.description = item.roles.length ? item.roles[0].description : "";
          item.roles = item.roles.length ? item.roles[0].id : "";
        });
        this.datas = res.obj.list;
        this.total = res.obj.total;
      }
    },
    fileName(val) {
      if (val) {
        let splits = val.split("/");
        let newVal = `${splits[splits.length - 2]}/${
          splits[splits.length - 1]
        }`;
        return newVal.split("?")[0];
      } else {
        return "";
      }
    },
    submitUser(name) {
      this.$refs[name].validate(valid => {
        if (!valid) {
          this.$message.error("必填项不能为空");
          return;
        }
        if (this.modalType === 0) {
          // 添加用户 避免编辑后传入id
          delete this.userForm.id;
          delete this.userForm.status;
          if (
            this.userForm.password == "" ||
            this.userForm.password == undefined
          ) {
            this.errorPass = "密码不能为空";
            return;
          }
          if (this.userForm.password.length < 6) {
            this.errorPass = "密码长度不得少于6位";
            return;
          }
          // this.submitLoading = true;
          this.addUser();
        } else {
          this.editUser();
        }
      });
    },
    async editUser() {
      let param = {
        roles: this.userForm.roles ? [this.userForm.roles] : [],
        user: {
          ...this.userForm,
          avatar:
            this.userForm.avatar === "https://s1.ax1x.com/2018/05/19/CcdVQP.png"
              ? this.userForm.avatar
              : this.fileName(this.userForm.avatar),
          password: null,
          roles: []
        }
      };
      let res = await userMangeService.addUser(param);
      if (res.code === 0) {
        this.$Message.success("操作成功");
        this.getUserList();
        this.userModalVisible = false;
        this.$store.state.system.avatorImgPath = this.userForm.avatar;
      }
    },
    async addUser() {
      let param = {
        roles: this.userForm.roles ? [this.userForm.roles] : [],
        user: {
          ...this.userForm,
          avatar:
            this.userForm.avatar === "https://s1.ax1x.com/2018/05/19/CcdVQP.png"
              ? this.userForm.avatar
              : this.fileName(this.userForm.avatar),
          roles: []
        }
      };
      let res = await userMangeService.addUser(param);
      if (res.code === 0) {
        this.getUserList();
        this.$Message.success("操作成功");
        this.$store.state.system.avatorImgPath = this.userForm.avatar;
        this.userModalVisible = false;
      }
    },
    viewPic() {
      this.viewImage = true;
    },
    handleFormatError(file) {
      this.$Notice.warning({
        title: "不支持的文件格式",
        desc:
          "所选文件‘ " +
          file.name +
          " ’格式不正确, 请选择 .jpg .jpeg .png .gif格式文件"
      });
    },
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "文件大小过大",
        desc: "所选文件‘ " + file.name + " ’大小过大, 不得超过 5M."
      });
    },
    beforeUpload(file) {
      let storeAs = `${
        new Date().toJSON().split("T")[0]
      }/${new Date().getTime()}.${
        file.name.split(".")[file.name.split(".").length - 1]
      }`;
      let results = client.put(storeAs, file).then(results => {
        let signUrl = client.signatureUrl(results.name, { expires: 80000 });
        this.userForm.avatar = signUrl;
      });
      return true;
    },
    add() {
      this.modalType = 0;
      this.modalTitle = "添加用户";
      this.$refs.userForm.resetFields();
      this.userForm.password = "";
      this.userModalVisible = true;
    },
    edit(v) {
      this.modalType = 1;
      this.modalTitle = "编辑用户";
      this.$refs.userForm.resetFields();
      // 转换null为""
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = "";
        }
      }
     this.getDeptTreeList(v.locationId)
      let str = JSON.stringify(v);
      let userInfo = JSON.parse(str);
      this.userForm = userInfo;
      let selectRolesId = [];
      // this.userForm.roles.forEach(function(e) {
      //   selectRolesId.push(e.id);
      // });
      // this.userForm.roles = selectRolesId;
      this.userModalVisible = true;
    },
    enable(v) {
      this.$Modal.confirm({
        title: "确认启用",
        content: "您确认要启用用户 " + v.userName + " ?",
        onOk: () => {
          this.operationLoading = true;
          userMangeService.enableUser(v.id).then(res => {
            this.operationLoading = false;
            if (res.code === 0) {
              // this.$Message.code === 0("操作成功");
              this.$Message.success("操作成功");
              this.getUserList();
            }
          });
        }
      });
    },
    disable(v) {
      this.$Modal.confirm({
        title: "确认禁用",
        content: "您确认要禁用用户 " + v.userName + " ?",
        onOk: () => {
          this.operationLoading = true;
          userMangeService.disableUser(v.id).then(res => {
            this.operationLoading = false;
            if (res.code === 0) {
              // this.$Message.code === 0("操作成功");
              this.$Message.success("操作成功");
              this.getUserList();
            }
          });
        }
      });
    },
    remove(v) {
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除用户 " + v.userName + " ?",
        onOk: () => {
          this.operationLoading = true;
          userMangeService.deleteUser(v.id).then(res => {
            this.operationLoading = false;
            if (res.code === 0) {
              // this.$Message.code === 0("删除成功");
              this.$Message.success("操作成功");
              this.getUserList();
            }
          });
        }
      });
    },
    dropDown() {
      if (this.drop) {
        this.dropDownContent = "展开";
        this.dropDownIcon = "ios-arrow-down";
      } else {
        this.dropDownContent = "收起";
        this.dropDownIcon = "ios-arrow-up";
      }
      this.drop = !this.drop;
    },
    showSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    clearSelectAll() {
      this.$refs.table.clearSelection()
    },
    // 转移
    transfer () {
      if(this.selectList.length===0) {
        this.$message.error('至少选择一个事项')
        return
      }
      this.multipleSelection = this.selectList.map(x => x.id)
      this.modalVisible = true
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
        onOk: () => {
          let ids = "";
          this.selectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          this.operationLoading = true;
          userMangeService.deleteUser(ids).then(res => {
            this.operationLoading = false;
            if (res.code === 0) {
              // this.$Message.code === 0("删除成功");
              this.$Message.success("操作成功");
              this.clearSelectAll();
              this.getUserList();
            }
          });
        }
      });
    },
    handleRemove(file, fileList) {},
    handlePreview(file) {},
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 3 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    }
  }
};
</script>
<style lang="scss" scoped>
.operation {
  margin-bottom: 2vh;
}
.select-count {
  font-size: 13px;
  font-weight: 600;
  color: #40a9ff;
}
.select-clear {
  margin-left: 10px;
}
.page {
  margin-top: 2vh;
}
.drop-down {
  font-size: 13px;
  margin-left: 5px;
}

.upload {
  margin-top: 10px;
}

.ivu-poptip {
  display: inline-block;
  width: 100%;
  & /deep/ .ivu-poptip-rel {
    display: inline-block;
    position: relative;
    width: 100%;
  }
}


.tree-bar {
  max-height: 500px;
  overflow: auto;
  margin-top: 5px;
}

.tree-bar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.tree-bar::-webkit-scrollbar-thumb {
  border-radius: 4px;
  -webkit-box-shadow: inset 0 0 2px #d1d1d1;
  background: #e4e4e4;
}
</style>
