<template>
  <div class="container-card form-explain">
    <div class="event-state" :class="eventClass" v-if="eventState">
      <span class="text">{{eventState}}</span>
    </div>
    <h2>表单说明</h2>
    <el-form :model="modelfrom" ref="modelfrom" :rules="rulesfrom" label-width="0" style="height:calc(100% - 45px)">
      <el-row type="flex" justify="space-between" class="code-row-bg">
        <el-col v-if="expand" :span="5" class="dept">
          <el-card style="height:58%">
            <div class="img-avatar" v-for="(item, index) in icont" :key="index" :class="item.ishover?'hover':''"
              @mouseover="item.ishover=true" @mouseout="item.ishover=false">
              <div class="avatar-icon">
                <i class="el-icon-zoom-in" @click.stop="viewRenderPic(index)" title="点击放大图片"></i>
                <i class="el-icon-tickets" v-if="!watchPic" @click.stop="showText(item)" title="点击自动识别图中文字"></i>
              </div>
              <img :src="item.url" style="width:100%;cursor: pointer;box-shadow: 2px 2px 10px rgba(0,0,0,.5);padding: 5px 5px;">
            </div>
          </el-card>
          <el-card style="margin-top: 5px;height:40%">
            <div v-for="(item, index) in tabledata.words_result" :key="index">
              <p id="msg">{{item.words}}</p>
              <button class="btn" data-clipboard-target="#msg" @click="copy"/>
              <!--使用data-clipboard-target可以直接指向需要复制的数据。 data-clipboard-text 是放置需要复制的数据-->
            </div>
          </el-card>
        </el-col>
        <div class="expand">
          <Icon :type="expandIcon" size="16" class="icon" @click="changeExpand"/>
        </div>
        <el-col :span="span">
          <el-table :data="modelfrom.tableData" :height="watchPic?'100%':'calc(100% - 50px)'" border @cell-dblclick="judge" @row-click="rowClick" class="form-explain">
            <el-table-column class="my-handle" type="index" width="50">
              <template slot-scope="{row,$index}">
                <div class="my-handle">{{$index + 1}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="formName" label="表单名称" width="180" align="center">
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.formName'" :rules="rulesfrom.formName" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-input size="mini" placeholder="请输入表单名称" v-model="row.formName"></el-input>
                </el-form-item>
                <div v-else>{{row.formName}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="fieldName" label="字段名称" width="180" align="center">
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.fieldName'" :rules="rulesfrom.fieldName" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-input size="mini" placeholder="请输入字段名称" v-model="row.fieldName"></el-input>
                </el-form-item>
                <div v-else>{{row.fieldName}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="fieldGroup" label="字段分组" width="180" align="center">
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.fieldGroup'" :rules="rulesfrom.fieldGroup" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-input size="mini" placeholder="请输入字段分组" v-model="row.fieldGroup"></el-input>
                </el-form-item>
                <div v-else>{{row.fieldGroup}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="fieldAttribute" label="字段属性" width="180" align="center">
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.fieldAttribute'" :rules="rulesfrom.fieldAttribute" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-input size="mini" placeholder="请输入字段属性" v-model="row.fieldAttribute"></el-input>
                </el-form-item>
                <div v-else>{{row.fieldAttribute}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="isExistenceOfPlural" label="是否复数存在" width="65" align="center">
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.isExistenceOfPlural'" :rules="rulesfrom.isExistenceOfPlural" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-switch size="mini" v-model="row.isExistenceOfPlural" active-color="#13ce66" inactive-color="#c0ccda"></el-switch>
                </el-form-item>
                <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.isExistenceOfPlural"></i>
              </template>
            </el-table-column>
            <el-table-column prop="isRequired" label="是否必填" width="70" align="center">
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.isRequired'" :rules="rulesfrom.isRequired" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-switch size="mini" v-model="row.isRequired" active-color="#13ce66" inactive-color="#c0ccda" ></el-switch>
                </el-form-item>
                <i class="el-icon-check checked" v-if="(watchPic||!row.isUpdate)&&row.isRequired"></i>
              </template>
            </el-table-column>
            <el-table-column prop="fillInInstructions" width="160" align="center" label="填写说明">
              <template slot="header" slot-scope="scope">
                填写说明<Icon style="margin-top: -10px;" type="md-help-circle" title="提示申请人该字段应如何填写。例：填写11位手机号码。"/>
              </template>
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.fillInInstructions'" :rules="rulesfrom.fillInInstructions" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-input size="mini" placeholder="请输入填写说明" v-model="row.fillInInstructions"></el-input>
                </el-form-item>
                <div v-else>{{row.fillInInstructions}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="remarks" width="150" align="center" label="备注">
              <template slot="header" slot-scope="scope">
                备注<Icon style="margin-top: -10px;" type="md-help-circle" title="提示申请人填写该字段时特别需要注意的事项或该字段的特殊要求。例：需加盖单位公章。"/>
              </template>
              <template slot-scope="{row,$index}">
                <el-form-item :prop="'tableData.' + $index + '.remarks'" :rules="rulesfrom.remarks" v-if="!watchPic&&row.isUpdate&&currentPicId">
                  <el-input size="mini" placeholder="请输入备注" v-model="row.remarks"></el-input>
                </el-form-item>
                <div v-else>{{row.remarks}}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="50" fixed="right" v-if="!watchPic">
              <template slot-scope="scope">
                <div class="table-row-btn">
                  <el-tooltip content="删除" placement="top">
                    <Button @click="deleteRow(scope.$index, scope.row)" type="error" icon="md-trash" size="small"></Button>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="add-row notclick" style="width: 99.2%;" @click="addMasterUser()" v-if="!watchPic"><span>+ 添加</span></div>
        </el-col>
      </el-row>
    </el-form>
    <div class="btn-submit" @click="formSubmit('modelfrom')" v-if="!watchPic">保存</div>
    <ul ref="imgView" style="display:none">
      <li v-for="(item, index) in icont" :key="index">
        <img :src="item.url" :alt="item.name">
      </li>
    </ul>
    <DeleteDialog :visible="showDel" @cancel="cancel" @delete="del">
      <div>确认删除？删除后不可恢复</div>
    </DeleteDialog>

    <!-- 评审 -->
    <JudgeDialog title="表单说明评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </div>
</template>

<script src="https://cdn.bootcss.com/clipboard.js/2.0.1/clipboard.js"></script>
<script>
import DeleteDialog from "@/components/dialog.vue";
import JudgeDialog from '@/components/judgeDialog'
import { mapState, mapMutations, mapActions } from "vuex";
import { aliConfig } from "@/utils/aliyunConfig";
import { formInstructions } from "@/api/service";
import Sortable from 'sortablejs'
import Viewer from "viewerjs";
const OSS = require("ali-oss");
const client = new OSS(aliConfig);
export default {
  components: { DeleteDialog, JudgeDialog },
  props: {
    state: {
      type: Boolean
    },
    watchPic: {
      type: Boolean,
      default: false
    },
    itemSee: {
      type: Boolean,
      default: false
    },
    editState: {
      type: Boolean,
      default: true // true 是梳理员和部门管理员，false是评审员
    }
  },
  watch: {
    state: function(newVal, oldVal) {
      if (newVal) {
        this.formInstruction();
      }
    },
    'itemDetail.id' (val) {
      if(val) {
        this.currentPicId = ''
        this.getAllform(val)
      }
    },
    'itemInfo.formThatState' (val) {
      this.allState(val)
    }
  },
  computed: {
    ...mapState(["itemDetail"]),
    itemInfo() {
      this.tabledata = ''
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.formThatState)
      return itemDetail
    }
  },
  data() {
    return {
      // 评审
      fromVerion:null,//表单说明
      judgeShow: false,
      judgeForm: {},

      dataPicture: "",
      ishover: false,
      expand: true,
      expandIcon: "ios-arrow-back",
      span: 19,
      tabledata: {},
      eventState: "未完善",
      eventClass: "btn-not-perfect",
      imageModal: false,
      modelfrom: {
        tableData: []
      },
      currentIndex: 0,
      currentPicId: "",
      showDel: false,
      icont: [],
      rulesfrom: {
        formName: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        fieldName: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        fieldGroup: [],
        fieldAttribute: [],
        isExistenceOfPlural: [],
        isRequired: [],
        fillInInstructions: [],
        remarks: []
      }
    };
  },
  beforeMount() {
    this.getAllform(this.itemInfo.id);
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
  },
  mounted () {
    this.rowDrop()
  },
  methods: {
    ...mapMutations(["SET_ITEMDETAIL"]),
    allState (val) {
      switch (val) {
        case 1: this.eventState = '未完善';this.eventClass = 'btn-not-perfect';break;
        case 2: this.eventState = '已完善';this.eventClass = 'btn-has-perfect';break;
        case 3: this.eventState = '已提交';this.eventClass = 'btn-submitted';break;
        default: break
      }
    },
    // 评审
    judge (row, column, cell, event) {
      let endTime = this.itemInfo.examineEndTime
      let state = this.itemInfo.examineAndApproveState
      if((state===1||state===6)&&endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          id: this.itemInfo.id,
          sheetNum: 8,
          lineNumber: Number(row.number),
          propertyName: column.label,
          property: column.property,
          questionContent: ''
        }
      }
    },
    judgeCancel () {
      this.judgeShow = false
    },
    // 查看图片，放大图片操作
    viewRenderPic(index) {
      global.thisView && global.thisView.destroy();
      global.thisView = new Viewer(this.$refs.imgView);
      global.thisView.view(index);
    },
    copy() {
      let clipboard = new Clipboard(".btn"); //注意要使用let或者const，不能使用var，否则会出现复制次数叠加的问题，即复制不止一次。
      //因为var是全局变量，let和const是局部
      clipboard.on("success", e => {
        alert("copy success");
        clipboard.destroy(); //使用destroy可以清楚缓存
      });
      clipboard.on("error", e => {
        alert("failed");
        clipboard.destroy();
      });
    },
    // 是否折叠左边树
    changeExpand() {
      this.expand = !this.expand;
      if (this.expand) {
        this.expandIcon = "ios-arrow-back";
        this.span = 18;
      } else {
        this.expandIcon = "ios-arrow-forward";
        this.span = 23;
      }
    },
    //显示材料
    async formInstruction() {
      let res = await formInstructions.formInstruction(this.itemInfo.id);
      if (res.code === 0) {
        this.icont = [];
        this.fromVerion=res.obj.version;
        if (res.obj !== null) {
          let allList = res.obj.list||[]
          let pdfList = res.obj.pdfList||[]
          if((allList.length+pdfList.length)===0) this.modelfrom.tableData =[]
          allList.map(item => {
            let temp = null;
            if (item.sampleTable) {
              temp = JSON.parse(item.sampleTable).imgs;
              temp.map(items => {
                let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp)/i.test(items)
                if(flag) {
                  this.icont.push({
                    ishover: false,
                    oldUrl: items,
                    id: item.id,
                    url: client.signatureUrl(items, { expires: 80000 })
                  });
                }
              });
            }
          });
          pdfList.map(item=>{
            let imgs = JSON.parse(item.imageName).imgs
            imgs.map(items => {
              let flag = /\w(\.gif|\.jpeg|\.jpg|\.bmp)/i.test(items)
              if(flag) {
                this.icont.push({
                  ishover: false,
                  oldUrl: items,
                  id: item.materialId,
                  url: client.signatureUrl(items, { expires: 80000 })
                });
              }
            });
          })
          this.allState(res.obj.state)
        } else {
          this.modelfrom.tableData =[]
        }
      }
    },
    //删除
    deleteRow(index, rows) {
      this.showDel = true;
      this.currentIndex = index;
    },
    del() {
      this.modelfrom.tableData.splice(this.currentIndex, 1);
      this.showDel = false;
    },
    cancel() {
      this.showDel = false;
    },
    // 材料内容
    async getAllform(id) {
      this.modelfrom.tableData = []
      let param = {
        eventId: id, //事项id
        materialId: this.currentPicId //材料id
      };
      let res = await formInstructions.getAllphotoForm(param);
      if (res.code === 0) {
        if(res.obj) {
          // this.modelfrom.tableData =this.icont.length?res.obj.list:[];
          let data = res.obj.list?res.obj.list:[]
          data.forEach(item => item.isUpdate = false)
          this.modelfrom.tableData = data
        }
      }
    },
    //行拖拽
    rowDrop() {
      const tbody = document.querySelector('.form-explain .el-table__body-wrapper tbody')
      Sortable.create(tbody, {
        handle: '.my-handle'
      })
    },
    rowClick (row, column, cell, event) {
      this.modelfrom.tableData.map(item => item.isUpdate = false)
      row.isUpdate = true
    },
    // 判断行提交空数据
    rowIsSpace (row) {
      let spaceLength = 0
      for (let key in row) {
        if (!row[key] && (key === 'formName'||key === 'fieldName')) {
          spaceLength += 1
        }
      }
      return spaceLength===2
    },
    // 保存
    formSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          let flag = this.modelfrom.tableData.some(item => this.rowIsSpace(item))
          let state = 1
          if(!this.icont.length) {
            if(!this.modelfrom.tableData.length) {
              state = flag?1:2
            }
          } else {
            state = this.modelfrom.tableData.length?(flag?1:2):1
          }
          this.formSave(state)
        } else {
          this.formSave(1);
        }
      });
    },
    async formSave(state) {
      let newData = [];
      let number = 1;
      this.modelfrom.tableData.map(item => {
        newData.push({
          ...item,
          number,
          materialId: this.currentPicId
        });
        ++number;
      });
      let res = await formInstructions.save({
        state,
        materialId: this.currentPicId,
        eventId: this.itemInfo.id,
        list: newData,
        version:this.fromVerion
      });
      if (res.code === 0) {
        this.itemInfo.formThatState = state;
        localStorage.setItem("itemDetail", JSON.stringify(this.itemInfo));
        this.SET_ITEMDETAIL({ ...this.itemInfo, formThatState: state });
        this.fromVerion=null
        this.$message.success(res.msg);
        this.formInstruction();

      }
      this.getAllform(this.itemInfo.id);

    },
    //显示文本
    showText(param) {
      this.currentPicId = param.id;
      this.getDataList(param)
      if(this.modelfrom.tableData.length){
        this.$confirm('是否覆盖之前填写内容','提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.getAllform(this.itemInfo.id);
        })
      }
    },
    async getDataList (param) {
      // this.modelfrom.tableData = [];
      let res = await formInstructions.showText(param.oldUrl, param.id);
      if (res.code === 0) {
        this.tabledata = JSON.parse(res.obj);
        // this.getAllform(this.itemInfo.id);
      }
    },
    // 添加按钮
    addMasterUser() {
      if(!this.currentPicId){
        this.$message('点击图片识别后添加');
        return
      }
      this.modelfrom.tableData.map(item => item.isUpdate = false)
      let dataLength = this.modelfrom.tableData.length
      let formName = dataLength>0?this.modelfrom.tableData[dataLength-1].formName:''
      this.modelfrom.tableData.push({
        id: '',
        formName,
        fieldName: '',
        fieldGroup: '',
        fieldAttribute: '',
        isExistenceOfPlural: 'true',
        isRequired: 'true',
        fillInInstructions: '',
        remarks: '',
        eventId: this.itemInfo.id,
        isUpdate: true
      })
    }
  }
};
</script>

<style lang="scss" scoped>
.container-card {
  height: 100%;
  .event-state {
    height: 23px;
    border-radius: 5px 5px 0 0;
  }
  .form-explain {
    & /deep/ .el-card__body,
    .code-row-bg {
      height: 99%;
    }
    .dept {
      height: 100%;
      .el-card {
        // height: 50%;
        overflow: auto;
      }
    }
  }
  .checked {
    font-size: 25px;
    color: cadetblue;
  }
}
.item_radio {
  border: 1px solid #dcdfec;
}
.textform {
  background-color: #eee;
  height: 35px;
  width: 99.3%;
}
.pText {
  font-weight: bold;
  font-size: 1rem;
  padding: 8px 8px;
  color: #909399;
}
.expand {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  .icon {
    cursor: pointer;
  }
  :hover {
    color: #1890ff !important;
  }
}
.img-avatar {
  position: relative;
  img {
    width: 100%;
    // height: 484px;
    margin-top: 10px;
  }
  .avatar-icon {
    position: absolute;
    left: calc(50% - 2rem);
    top: calc(50% - 1rem);
    display: none;
    font-size: 30px;
    // color: #fff;
    z-index: 2;
    & > i {
      cursor: pointer;
    }
  }
  &.hover {
    img {
      opacity: 0.5;
    }
    .avatar-icon {
      display: inline-block;
    }
  }
}
</style>
