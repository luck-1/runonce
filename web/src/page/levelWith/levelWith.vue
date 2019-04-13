<template>
    <el-card>
			<el-form :model="ruleForm" ref="ruleForm" :inline="true" label-width="90px" class="demo-ruleForm">
					<el-form-item label="实施部门" prop="implementatioDepartment">
							<el-input v-model="ruleForm.implementatioDepartment" clearable placeholder="请输入"></el-input>
					</el-form-item>
					<el-form-item label="通用名称" prop="genericName">
							<el-input v-model="ruleForm.genericName" clearable placeholder="请输入"></el-input>
					</el-form-item>
					<!-- <el-form-item label="省级名称" prop="provincialName">
							<el-input v-model.number="ruleForm.provincialName" clearable placeholder="请输入"></el-input>
					</el-form-item> -->
					<el-form-item label="事项大类型" prop="bigTypesOfEvents">
						<el-select class="select-option" size="small" v-model="ruleForm.bigTypesOfEvents" clearable placeholder="请选择" @change="itemSmallTypeSelect">
							<el-option label="行政许可" value="1"></el-option>
							<el-option label="审核转报" value="2"></el-option>
							<el-option label="备案类" value="3"></el-option>
							<el-option label="其他服务" value="4"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="事项小类型" prop="itemSubtype">
						<el-select class="select-option" size="small" v-model="ruleForm.itemSubtype" filterable clearable placeholder="先选择事项大类型">
							<el-option v-for="item in samllPro" :key="item.id" :label="item.name" :value="item.name"></el-option>
						</el-select>
					</el-form-item>

					<el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">
              <i class="el-icon-search"></i>
            </el-button>
					</el-form-item>
			</el-form>

			<el-button-group class="btn">
				<el-button type="primary" @click="addModal" class="el-icon-circle-plus-outline"> 新增</el-button>
				<el-button type="primary" @click="editModal" class="el-icon-edit"> 修改</el-button>
				<el-button type="primary" @click="deleteModal" class="el-icon-delete"> 删除</el-button>
			</el-button-group>

			<el-table border :data="tableData" @selection-change="selectChange" height="calc(100vh - 290px)">
					<el-table-column type="expand">
						<template slot-scope="props">
							<el-form label-position="left" class="demo-table-expand">
								<el-form-item label="实施依据:" style="width:100%">
									<span>{{ props.row.establishmentBasis }}</span>
								</el-form-item>
							</el-form>
						</template>
					</el-table-column>
					<el-table-column type="selection" width="50"></el-table-column>
					<el-table-column type="index" width="55" label="序号"></el-table-column>
					<el-table-column label="实施部门" prop="implementatioDepartment"  width="120"></el-table-column>
					<el-table-column label="通用名称" prop="genericName"  width="120"></el-table-column>
					<el-table-column label="事项大类型" prop="bigTypesOfEvents"  width="120" :show-overflow-tooltip="true">
						<template slot-scope="scope">
							<span v-if="scope.row.bigTypesOfEvents === '1'">行政许可</span>
							<span v-if="scope.row.bigTypesOfEvents === '2'">审核转报</span>
							<span v-if="scope.row.bigTypesOfEvents === '3'">备案类</span>
							<span v-if="scope.row.bigTypesOfEvents === '4'">其他服务</span>
            </template>
					</el-table-column>
					<el-table-column label="事项小类型" prop="itemSubtype" width="120"></el-table-column>
					<el-table-column label="省级序号" prop="provincialsSerialNumber" width="80"></el-table-column>
					<el-table-column label="省级名称" prop="provincialName" width="120"></el-table-column>
					<el-table-column label="市级序号" prop="municipalSerialNumber" width="80"></el-table-column>
					<el-table-column label="市级名称" prop="municipalName" width="120"></el-table-column>
					<el-table-column label="县级序号" prop="countySerialnNmber" width="80"></el-table-column>
					<el-table-column label="县级名称" prop="nameOfCounty" width="120"></el-table-column>
					<el-table-column label="是否区县私有" width="120" prop="isPrivate">
						<template slot-scope="scope">
							<span v-if="scope.row.isPrivate === false">共通目录</span>
							<span v-if="scope.row.isPrivate === true">区县私有目录</span>
            </template>
					</el-table-column>
					<el-table-column label="备注" prop="remarks"></el-table-column>
			</el-table>
			<el-pagination style="margin-top:1rem" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
				:page-sizes="[10, 20, 30, 40]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
			</el-pagination>

			<el-dialog :title="modalTitle" :visible="addModals" :close-on-press-escape="false" :close-on-click-modal="false" @close="closedModal('formRef')">
				<el-form :model="formRef" ref="formRef" :rules="relus" label-width="110px">
					<el-row>
						<el-col :span="12">
							<el-form-item label="实施部门" prop="implementatioDepartment">
								<el-input v-model="formRef.implementatioDepartment" clearable placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="事项大类型" prop="bigTypesOfEvents">
									<el-select class="select-option" clearable v-model="formRef.bigTypesOfEvents" placeholder="请选择" @change="itemSmallTypeSelect">
										<el-option label="行政许可" value="1"></el-option>
										<el-option label="审核转报" value="2"></el-option>
										<el-option label="备案类" value="3"></el-option>
										<el-option label="其他服务" value="4"></el-option>
									</el-select>
							</el-form-item>
							<el-form-item label="省级序号" prop="provincialsSerialNumber">
								<el-input v-model="formRef.provincialsSerialNumber" :maxlength="20" clearable placeholder="请输入 例 '1'"></el-input>
							</el-form-item>
							<el-form-item label="市级序号" porp="municipalSerialNumber">
								<el-input v-model="formRef.municipalSerialNumber" :maxlength="20" clearable placeholder="请输入 例 '1'"></el-input>
							</el-form-item>
							<el-form-item label="县级序号" prop="countySerialnNmber">
								<el-input v-model="formRef.countySerialnNmber" :maxlength="20" clearable placeholder="请输入 例 '1'"></el-input>
							</el-form-item>
							<el-form-item label="是否区县私有">
								<el-switch v-model="formRef.isPrivate" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="通用名称" prop="genericName">
								<el-input v-model="formRef.genericName" clearable placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="事项小类型" prop="itemSubtype">
								<el-select class="select-option" v-model="formRef.itemSubtype" filterable clearable placeholder="先选择事项大类型">
									<el-option v-for="item in samllPro" :key="item.id" :label="item.name" :value="item.name"></el-option>
								</el-select>
							</el-form-item>
							<el-form-item label="省级名称" prop="provincialName">
								<el-input v-model="formRef.provincialName" clearable placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="市级名称" prop="municipalName">
								<el-input v-model="formRef.municipalName" clearable placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="县级名称" prop="nameOfCounty">
								<el-input v-model="formRef.nameOfCounty" clearable placeholder="请输入"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="实施依据" prop="establishmentBasis">
									<el-input type="textarea" resize="none" :autosize="{ minRows: 5, maxRows: 10}" v-model="formRef.establishmentBasis" clearable placeholder="请输入实施依据,如果依据不明确可以填写‘空’"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="备注" prop="remarks">
									<el-input type="textarea" resize="none" :autosize="{ minRows: 5, maxRows: 10}" clearable v-model="formRef.remarks" placeholder="请输入"></el-input>
							</el-form-item>
						</el-col>
						</el-row>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closedModal('formRef')">取 消</el-button>
					<el-button type="primary" @click="addsave('formRef')">确 定</el-button>
				</div>
			</el-dialog>

			<DeleteDialog :visible="delModal" @cancel="delModal = false" @delete="del">
				<div>确认删除？删除后不可恢复</div>
			</DeleteDialog>
	</el-card>
</template>
<script>
import { levelWithService, administrativeService } from "@/api/service";
import { validatorSpace } from '@/utils/validate'
import DeleteDialog from '@/components/dialog'
export default {
	components: {
		DeleteDialog
	},
  data() {
		const checkName= (rule,value,callback) => {
			 if(!value){
				 callback(new Error(' '));
			 } else if(!validatorSpace) {
				 callback(new Error(' '))
			 } else {
				 callback()
			 }
		};
    const validateNumber = (rule, value, callback) => {
			if(value === ''){
				callback();
			}else{
				if (!(/^\+?[1-9][0-9]*$/).test(value)) {
					callback(new Error("请输入大于0的数字"));
				} else {
					callback();
				}
			}

    };
    return {
      delModal: false,
      addModals: false,
      modalTitle: "增加",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      formModel: false,
      ruleForm2: {
        pass: "",
        checkPass: "",
        age: ""
      },
			samllPro: [],
			tableData: [],
			selectionList:[],
      ruleForm: {
				implementatioDepartment: '',
        bigTypesOfEvents: '',
        itemSubtype: '',
        genericName: '',
        provincialsSerialNumber: '',
        provincialName: '',
        municipalSerialNumber: '',
        municipalName: '',
        countySerialnNmber: '',
        nameOfCounty: '',
        establishmentBasis: '无',
        remarks: ''
			},
      //新增
      relus: {
				municipalSerialNumber: [
					{ required: false, validator: validateNumber, trigger: "blur" }
				],
        provincialsSerialNumber: [
					{ required: false, validator: validateNumber, trigger: "blur" }
        ],
        countySerialnNmber: [
					{ required: false, validator: validateNumber, trigger: "blur" }
				],
				implementatioDepartment: [
					{ required: true, validator: checkName, trigger: "blur" }
				],
				genericName: [
					{ required: true, validator: checkName, trigger: "blur" }
				],
				establishmentBasis: [
					{ required: true, validator: checkName, trigger: "blur" }
				],
				bigTypesOfEvents: [
					{ required: true, validator: checkName, trigger: "change" }
				],
				itemSubtype: [
					{ required: true, validator: checkName, trigger: "change" }
				]
      },
      formRef: {
				id: "",
        implementatioDepartment: "",
				bigTypesOfEvents: "",
        itemSubtype: "",
        genericName: "",
        provincialsSerialNumber: "",
        provincialName: "",
        municipalSerialNumber: "",
        municipalName: "",
        countySerialnNmber: "",
        nameOfCounty: "",
        establishmentBasis: "",
        remarks: "",
				isPrivate:false
      }
    };
  },
  methods: {
    handleSizeChange(val) {
      this.pageSize = val;
      this.allselect();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.allselect();
    },
    submitForm() {
      this.allselect();
    },
    async allselect() {
      let param = {
				...this.ruleForm,
        page: this.currentPage,
        size: this.pageSize
      };
      let res = await levelWithService.allselect(param);
      if (res.code === 0) {
				res.obj.list.forEach(item => {
					item.provincialsSerialNumber = item.provincialsSerialNumber || ''
					item.municipalSerialNumber = item.municipalSerialNumber || ''
					item.countySerialnNmber = item.countySerialnNmber || ''
					item.bigTypesOfEvents = item.bigTypesOfEvents || ''
					item.bigTypesOfEvents = item.bigTypesOfEvents || ''
				})
				this.tableData = res.obj.list;
        this.total = res.obj.total;
      } else {
        this.$message.error(res.msg);
      }
		},
		selectChange(selection){
			this.selectionList = selection
		},
    //新增
    addModal() {
			this.modalTitle = "增加";
      this.addModals = true;
    },
    async addSave() {
			let param={
				...this.formRef,
				createTime:new Date()
			}
      let res = await levelWithService.addSave(param);
      if (res.code === 0) {
				this.closedModal('formRef')
				this.allselect();
      } else {
        this.$message.error(res.msg);
			}
    },
    editModal() {
			if(!this.selectionList.length||this.selectionList.length>1){
				this.$message.warning('只能选择一个进行修改')
				return
			}
			this.formRef = JSON.parse(JSON.stringify(this.selectionList[0]))
      this.modalTitle = "修改";
			this.addModals = true;
    },
    async editSave() {
      let res = await levelWithService.editSave(this.formRef);
      if (res.code === 0) {
				this.closedModal('formRef')
				this.allselect();
      } else {
        this.$message.error(res.msg);
      }
    },
    //取消
    closedModal(name) {
			this.formRef =  {
				id: "",
        implementatioDepartment: "",
				bigTypesOfEvents: "",
        itemSubtype: "",
        genericName: "",
        provincialsSerialNumber: "",
        provincialName: "",
        municipalSerialNumber: "",
        municipalName: "",
        countySerialnNmber: "",
        nameOfCounty: "",
        establishmentBasis: "",
        remarks: "",
        isPrivate: false
      }
      this.$refs[name].resetFields();
			this.addModals = false;
    },
    //新增确定
    addsave(name) {
    	this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.modalTitle === "增加") {
						this.addSave();
          } else {
						this.editSave();
          }
        } else {
					this.$message.error('必填项不能为空')
				}
      });
		},
		async del(){
			let ids = this.selectionList.map((x)=>x.id);
      let res = await levelWithService.delete(ids);
      if (res.code === 0) {
				this.delModal = false;
				this.allselect()
      }
		},
    deleteModal() {
			if(!this.selectionList.length){
				this.$message.warning("至少选择一条删除")
				return
			}
			this.delModal = true
		},
    async itemSmallTypeSelect(val) {
      this.samllPro = [];
      this.formRef.itemSubtype = "";
      this.ruleForm.itemSubtype = ''
      if (val) {
        let res = await administrativeService.itemSmallTypeSelectList(val);
        if (res.code === 0) {
          this.samllPro = res.obj.list;
        }
      }
    }
  },
  mounted() {
		this.allselect();
  }
};
</script>
<style lang="scss" scoped>
.el-pagination {
  margin-left: 555px;
}
.btn {
  margin-bottom: 1rem;
}
.demo-ruleForm {
  margin-left: 6px;
}
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.el-col {
	.el-select,.select-option{
		width:100%;
	}
}
// .el-input{
// 	width:314px;
// }
</style>
<style>
	.el-table th.gutter{
    	display: table-cell!important;
	}
	 
	.el-table colgroup.gutter{
	    display: table-cell!important;
	}
</style>