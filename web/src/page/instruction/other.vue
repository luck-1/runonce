<template>
<!-- 3其他 -->
  <div class="model-input-form"  style="position: relative;">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>其他类</h2>
    <el-form :model="ruleForm" :rules="rules" :inline="true" ref="ruleForm" label-width="150px" class="model-form">
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项大类型" prop="majorTypesOfEvents" >
            <el-select v-model="ruleForm.majorTypesOfEvents" disabled style="width: 100%;" >
              <el-option v-for="item in selectList.bigPro"  :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项小类型" prop="itemSmallType" @dblclick.native="judge('事项小类型', 'itemSmallType')">
            <el-select disabled v-model="ruleForm.itemSmallType" clearable placeholder="请选择事项小类型"  >
              <el-option v-for="item in selectList.samllPro"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12"  style="margin-top: -16px">
          <el-form-item label="事项名称目录" prop="itemCatalogue" @dblclick.native="judge('事项名称目录', 'itemCatalogue')">
            <el-input disabled :maxlength="50" clearable  v-model="ruleForm.itemCatalogue" placeholder="请输入事项名称目录"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理项编码" title="业务办理项编码" prop="basicCoding" @dblclick.native="judge('办理项编码', 'basicCoding')">
            <el-input disabled :maxlength="50" v-model="ruleForm.basicCoding" clearable placeholder="请输入办理项编码"></el-input>
          </el-form-item>
          </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="实施编码" title="实施清单编码" prop="codingImplementation" @dblclick.native="judge('实施编码', 'codingImplementation')">
            <el-input disabled :maxlength="50" v-model="ruleForm.codingImplementation" clearable placeholder="请输入实施编码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="办理事项名称" prop="itemName" @dblclick.native="judge('办理事项名称', 'itemName')">
          <el-input disabled :maxlength="50" clearable v-model="ruleForm.itemName" placeholder="请输入办理事项名称"  ></el-input>
        </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="实施机关" prop="implementingOrgan" @dblclick.native="judge('实施机关', 'implementingOrgan')">
            <el-input :readonly="watchPic" :maxlength="50" v-model="ruleForm.implementingOrgan" clearable placeholder="请输入实施机关"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="实施机构科室" prop="implementingBody" @dblclick.native="judge('实施机构科室', 'implementingBody')">
            <el-input :readonly="watchPic" :maxlength="50" v-model="ruleForm.implementingBody" clearable placeholder="请输入实施机构科室"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px" class="admin-select-let-space">
          <el-form-item label="服务对象" prop="serviceObject" @dblclick.native="judge('服务对象', 'serviceObject')">
            <el-input disabled v-model="ruleForm.serviceObject" clearable placeholder="请输入服务对象"></el-input>
            <!-- <el-select class="tag-size" disabled multiple v-model="ruleForm.serviceObject" filterable clearable placeholder="请选择服务对象">
              <el-option v-for="item in selectList.serObj"  :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select> -->
          </el-form-item>
        </el-col>
        <!-- dbb 去掉 -->
        <!-- <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="服务主题" prop="serviceTopic" @dblclick.native="judge('服务主题', 'serviceTopic')">
            <el-select collapse-tags filterable allow-create :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.serviceTopic" clearable placeholder="请选择服务主题"  >
              <el-option v-for="item in selectList.servTheme"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="办件类型" :maxlength="20" prop="typeOfOffice" @dblclick.native="judge('办件类型', 'typeOfOffice')">
            <el-select allow-create filterable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.typeOfOffice" clearable placeholder="请选择办件类型"  >
              <el-option v-for="item in selectList.typeOff"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>

        <el-col :span="12" style="margin-top: -17px" >
          <el-form-item label="时限类型" prop="timeLimitType" @dblclick.native="judge('时限类型', 'timeLimitType')" class="optionLabel">
            <el-select allow-create filterable :maxlength="10" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.timeLimitType" clearable placeholder="请选择时限类型"  >
              <el-option v-for="(item,index) in timetype"  :key="index" :label="item.label" :value="item.label"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="承诺办结时限" prop="commitmenTimeLimit" @dblclick.native="judge('承诺办结时限', 'commitmenTimeLimit')" class="textareaInput">
            <el-input placeholder="请输入承诺办结时限" v-model="ruleForm.commitmenTimeLimit" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="10" clearable>
              <span slot="append">{{ruleForm.timeLimitType}}</span>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>

      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item  label="承诺办结时限说明" prop="statementOfCommitmentDeadline" @dblclick.native="judge('承诺办结时限说明', 'statementOfCommitmentDeadline')">
            <el-tooltip placement="top" v-if="ruleForm.statementOfCommitmentDeadline">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.statementOfCommitmentDeadline}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')"  v-model="ruleForm.statementOfCommitmentDeadline" clearable placeholder="请输入承诺办结时限说明" ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.statementOfCommitmentDeadline" clearable placeholder="请输入承诺办结时限说明" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否收费" prop="charge" @dblclick.native="judge('是否收费', 'charge')" class="item_radio">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.charge"  @change="changeRadio">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="收费标准" prop="chargingStandard" @dblclick.native="judge('收费标准', 'chargingStandard')">
            <el-tooltip placement="top" v-if="ruleForm.chargingStandard">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.chargingStandard}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.chargingStandard" clearable placeholder="请输入收费标准" :disabled="according" ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')"  v-model="ruleForm.chargingStandard" clearable placeholder="请输入收费标准" :disabled="according" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="收费依据" prop="chargingBasis" @dblclick.native="judge('收费依据', 'chargingBasis')">
            <el-tooltip placement="top" v-if="ruleForm.chargingBasis">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.chargingBasis}}</div>
              <el-input v-model="ruleForm.chargingBasis" :maxlength="2000" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入收费依据" :disabled="according"></el-input>
            </el-tooltip>
            <el-input v-else v-model="ruleForm.chargingBasis" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入收费依据" :disabled="according"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="支付方式 " prop="paymentMethod" @dblclick.native="judge('支付方式', 'paymentMethod')">
            <el-select class="tag-size" allow-create multiple filterable :maxlength="10" :disabled="according||watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.paymentMethod" clearable placeholder="请选择支付方式">
              <el-option v-for="(item,index) in method" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="申请材料递送方式" prop="methodOfDeliveryOfApplicationMaterials" @dblclick.native="judge('申请材料递送方式', 'methodOfDeliveryOfApplicationMaterials')">
            <el-select class="tag-size" multiple allow-create filterable :maxlength="255" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.methodOfDeliveryOfApplicationMaterials" clearable placeholder="请选择申请材料递送方式"  >
              <el-option v-for="item in selectList.methodMater"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="网上服务系统网址" prop="webSiteOfOnlineServiceSystem" @dblclick.native="judge('网上服务系统网址', 'webSiteOfOnlineServiceSystem')">
            <el-input :readonly="watchPic" :maxlength="500" v-model="ruleForm.webSiteOfOnlineServiceSystem" clearable placeholder="请输入网上服务系统网址 示例'http://www.baidu.com'"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理形式" prop="handlingForm" @dblclick.native="judge('办理形式', 'handlingForm')">
            <el-select allow-create filterable :disabled="watchPic" :maxlength="50" v-model="ruleForm.handlingForm" clearable placeholder="请选择办理形式"  >
              <el-option v-for="item in selectList.handForm"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="到现场大厅窗口次数" prop="numberOfVisitsToTheFieldHall" @dblclick.native="judge('到现场大厅窗口次数', 'numberOfVisitsToTheFieldHall')">
            <el-select allow-create filterable :maxlength="10" :disabled="watchPic" v-model="ruleForm.numberOfVisitsToTheFieldHall" clearable placeholder="请选择到现场大厅窗口次数"  >
              <el-option v-for="(item,index) in options" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16.5px">
          <el-form-item label="办理时间或地点类型" prop="typeOfProcessingTimeOrPlace" @dblclick.native="judge('办理时间或地点类型', 'typeOfProcessingTimeOrPlace')">
            <el-input v-model="ruleForm.typeOfProcessingTimeOrPlace" :maxlength="100" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px" class="flow-map">
          <el-form-item label="办理时间或地点附件" prop="addendumToProcessingTimeOrPlace" @dblclick.native="judge('办理时间或地点附件', 'addendumToProcessingTimeOrPlace')" style="border:0.2px solid #dcdfec ;height:36px;">
            <ImgUpload @webFileFun="webFile($event, 'addendumToProcessingTimeOrPlace')" :webFiles="copyObj?(copyObj.objectDataMap[1]||[]):[]" v-if="ruleForm.addendumToProcessingTimeOrPlace" :watchPic="watchPic" :imgHandle="false" :isMini="true" :otherPosition="true" :files="ruleForm.addendumToProcessingTimeOrPlace?JSON.parse(ruleForm.addendumToProcessingTimeOrPlace).imgs:[]" :imageNum='40' :multiple="true"
              @upload="uploadimgs( $event)"></ImgUpload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="办理时间" prop="processingTime" @dblclick.native="judge('办理时间', 'processingTime')">
            <el-input :readonly="watchPic" :maxlength="1000" v-model="ruleForm.processingTime" clearable placeholder="请输入办理时间 比如 上午：9:00-12:00 下午14:00-18:00"  ></el-input>
            <!-- <el-time-picker is-range v-model="ruleForm.processingTime" range-separator="至" clearable start-placeholder="开始时间" format='HH:mm' style="width:100%" end-placeholder="结束时间" placeholder="选择时间范围"></el-time-picker>
            <el-date-picker type="date" placeholder="请选择办理时间" clearable v-model="ruleForm.processingTime" style="width: 100%;">
              <template slot="prepend">办理时间</template>
            </el-date-picker> -->
          </el-form-item>
        </el-col>
         <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="办理地点" prop="placeOfProcessing" @dblclick.native="judge('办理地点', 'placeOfProcessing')">
            <el-tooltip placement="top" v-if="ruleForm.placeOfProcessing">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.placeOfProcessing}}</div>
              <el-input :readonly="watchPic" :maxlength="1000" v-model="ruleForm.placeOfProcessing" clearable placeholder="请输入办理地点"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.placeOfProcessing" clearable placeholder="请输入办理地点"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理窗口" prop="processingWindow" @dblclick.native="judge('办理窗口', 'processingWindow')">
            <el-input :readonly="watchPic" :maxlength="2000" v-model="ruleForm.processingWindow" clearable placeholder="请输入办理窗口"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否中介服务" prop="intermediaryService" @dblclick.native="judge('是否中介服务', 'intermediaryService')" class="item_radio">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.intermediaryService">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- dbb 去掉 -->
      <!-- <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否支持网上支付" prop="supportOnlinePayment" @dblclick.native="judge('是否支持网上支付', 'supportOnlinePayment')" class="item_radio">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.supportOnlinePayment">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否支持物流快递" prop="isSupportLogistics" @dblclick.native="judge('是否支持物流快递', 'isSupportLogistics')" class="item_radio">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.isSupportLogistics">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row> -->
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="受理申请条件" prop="acceptingConditions" @dblclick.native="judge('受理申请条件', 'acceptingConditions')" class="textareaLabel">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.acceptingConditions" clearable placeholder="请输入受理申请条件" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="办理依据" prop="handlingBasis" @dblclick.native="judge('办理依据', 'handlingBasis')" class="textareaLabel">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.handlingBasis" clearable placeholder="请输入办理依据" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理查询" prop="enquiry" @dblclick.native="judge('办理查询', 'enquiry')">
            <el-tooltip placement="top" v-if="ruleForm.enquiry">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.enquiry}}</div>
              <el-input :readonly="watchPic" :maxlength="2000" v-model="ruleForm.enquiry" clearable placeholder="请输入办理查询"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.enquiry" clearable placeholder="请输入办理查询"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="常见问题" prop="commonProblem" @dblclick.native="judge('常见问题', 'commonProblem')">
            <el-tooltip placement="top" v-if="ruleForm.commonProblem">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.commonProblem}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.commonProblem" clearable placeholder="请输入常见问题"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.commonProblem" clearable placeholder="请输入常见问题"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="是否有服务限制" prop="serviceRestrictions" class="item_radio" @dblclick.native="judge('是否有服务限制', 'serviceRestrictions')">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.serviceRestrictions">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16.5px">
          <el-form-item label="时间(服务限制)" prop="serviceTimeConstraint" @dblclick.native="judge('时间(服务限制)', 'serviceTimeConstraint')">
            <el-tooltip placement="top" v-if="ruleForm.serviceTimeConstraint">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.serviceTimeConstraint}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')||serviceDisable" :maxlength="100" v-model="ruleForm.serviceTimeConstraint" clearable placeholder="请输入时间服务限制"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')||serviceDisable" v-model="ruleForm.serviceTimeConstraint" clearable placeholder="请输入时间服务限制"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16.5px">
          <el-form-item label="频次(服务限制)" prop="serviceFrequencyRestriction" @dblclick.native="judge('频次(服务限制)', 'serviceFrequencyRestriction')">
            <el-tooltip placement="top" v-if="ruleForm.serviceFrequencyRestriction">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.serviceFrequencyRestriction}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')||serviceDisable" :maxlength="100" v-model="ruleForm.serviceFrequencyRestriction" clearable placeholder="请输入行政复议行政诉讼"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')||serviceDisable" v-model="ruleForm.serviceFrequencyRestriction" clearable placeholder="请输入行政复议行政诉讼"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="备注(服务限制)" prop="remarks" @dblclick.native="judge('备注(服务限制)', 'remarks')">
            <el-tooltip placement="top" v-if="ruleForm.remarks">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.remarks}}</div>
              <el-input :readonly="watchPic||serviceDisable" :maxlength="2000" v-model="ruleForm.remarks" clearable placeholder="请输入备注说明"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||serviceDisable" v-model="ruleForm.remarks" clearable placeholder="请输入备注说明"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="其他(服务限制)" prop="otherServiceRestrictions" @dblclick.native="judge('其他(服务限制)', 'otherServiceRestrictions')">
            <el-tooltip placement="top" v-if="ruleForm.otherServiceRestrictions">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.otherServiceRestrictions}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.otherServiceRestrictions" clearable placeholder="请输入其他服务限制"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.otherServiceRestrictions" clearable placeholder="请输入其他服务限制"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="咨询途径" prop="consultationApproach" @dblclick.native="judge('咨询途径', 'consultationApproach')">
            <el-tooltip placement="top" v-if="ruleForm.consultationApproach">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.consultationApproach}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="200" v-model="ruleForm.consultationApproach" clearable placeholder="请输入咨询途径"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.consultationApproach" clearable placeholder="请输入咨询途径"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="咨询地址" prop="advisoryAddress" @dblclick.native="judge('咨询地址', 'advisoryAddress')">
            <el-tooltip placement="top" v-if="ruleForm.advisoryAddress">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.advisoryAddress}}</div>
              <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.advisoryAddress" clearable placeholder="请输入咨询地址"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.advisoryAddress" clearable placeholder="请输入咨询地址"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16.5px">
          <el-form-item label="监督电话" prop="serviceSupportHotline" @dblclick.native="judge('监督电话', 'serviceSupportHotline')">
            <el-input :readonly="watchPic" :maxlength="1000" v-model="ruleForm.serviceSupportHotline" clearable placeholder="请输入监督电话" minlength="12"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
         <el-col :span="12" style="margin-top: -16.5px">
          <el-form-item label="投诉途径" prop="complaintChannels" @dblclick.native="judge('投诉途径', 'complaintChannels')">
            <el-tooltip placement="top" v-if="ruleForm.complaintChannels">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.complaintChannels}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="200" v-model="ruleForm.complaintChannels" clearable placeholder="请输入投诉途径"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.complaintChannels" clearable placeholder="请输入投诉途径"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="投诉地址" prop="complaintAddress" @dblclick.native="judge('投诉地址', 'complaintAddress')">
            <el-tooltip placement="top" v-if="ruleForm.complaintAddress">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.complaintAddress}}</div>
              <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.complaintAddress" clearable placeholder="请输入投诉地址 示例'http://www.baidu.com'"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.complaintAddress" clearable placeholder="请输入投诉地址 示例'http://www.baidu.com'"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <!-- <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项排序" prop="itemSequencing" >
            <el-input v-model="ruleForm.itemSequencing" clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入事项排序" disabled></el-input>
          </el-form-item>
        </el-col> -->
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否有申请材料" prop="applicationMaterials" @dblclick.native="judge('是否有申请材料', 'applicationMaterials')" class="item_radio">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.applicationMaterials" style="margin-left:20px">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="结果类型" prop="resultType" @dblclick.native="judge('结果类型', 'resultType')">
            <el-select class="tag-size" allow-create :collapse-tags="!(watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2'))" multiple filterable :maxlength="20" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.resultType" clearable placeholder="请选择结果类型"  >
              <el-option v-for="item in results"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="材料提交说明" prop="materialSubmissionStatement" class="textareaLabel"  @dblclick.native="judge('材料提交说明', 'materialSubmissionStatement')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.materialSubmissionStatement" placeholder="请输入材料提交说明" type="textarea" clearable style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>

        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="是否有结果样本" prop="havaResultSample" @dblclick.native="judge('是否有结果样本', 'havaResultSample')" class="item_radio">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.havaResultSample" >
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px" class="flow-map">
          <el-form-item label="结果样本" prop="resultSample" @dblclick.native="judge('结果样本', 'resultSample')" ref="resultSample" style="border:0.2px solid #dcdfec ;height:35px;">
            <ImgUpload @webFileFun="webFile($event, 'resultSample')" :webFiles="copyObj?(copyObj.objectDataMap[1]||[]):[]" v-if="ruleForm.resultSample" :watchPic="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :otherPosition="true" :imgHandle="false" :isMini="true" :files="ruleForm.resultSample?JSON.parse(ruleForm.resultSample).imgs:[]" :imageNum='40' :multiple="true"
              @upload="uploadImgs( $event)"></ImgUpload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>

        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="结果名称" prop="resultName" @dblclick.native="judge('结果名称', 'resultName')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.resultName" clearable placeholder="请输入结果名称"></el-input>
          </el-form-item>
        </el-col>
          <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="行使层级" prop="exerciseHierarchy" @dblclick.native="judge('行使层级', 'exerciseHierarchy')">
            <el-input disabled v-model="ruleForm.exerciseHierarchy" clearable placeholder="请输入行使层级"></el-input>
            <!-- <el-select class="tag-size" allow-create :maxlength="10" filterable multiple :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.exerciseHierarchy" clearable placeholder="请选择活动区域"  >
              <el-option v-for="item in selectList.exerHier"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select> -->
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>

        <!-- dbb 去掉 -->
        <!-- <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="咨询电话 " prop="advisoryPhone" @dblclick.native="judge('咨询电话', 'advisoryPhone')">
            <el-input :readonly="watchPic" v-model="ruleForm.advisoryPhone" clearable placeholder="请输入咨询电话"  ></el-input>
          </el-form-item>
        </el-col> -->
      </el-row>
      <div class="btn-submit" @click="formSubmit" v-if="!watchPic">保存</div>
		</el-form>

    <!-- 评审 -->
    <JudgeDialog title="办事指南评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </div>
</template>
<script>
import {mapState, mapMutations, mapActions} from 'vuex'
import { administrativeService } from '@/api/service';
import ImgUpload from '@/components/uploadPicture';
import JudgeDialog from '@/components/judgeDialog'
import { validatSpace,validatorSpace } from '@/utils/validate'
export default {
  components: { ImgUpload, JudgeDialog },
  props: {
    state: {
      type: Boolean,
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
    'copyObj.businessGuide'(val) {
      if(val) {
        this.ruleForm.addendumToProcessingTimeOrPlace = ''
        this.ruleForm.resultSample = ''
        this.otherVersion = null
        let obj = Object.assign({},val.businessGuideOtherServicesOne,val.businessGuideOtherServicesOnePrivate)
        this.formRegroup(obj)
      }
    },
    state: function(newVal, oldVal) {
      if(newVal) {
        // this.init()

      }
    },
    'itemDetail.id' (val) {
      if (val) {
        this.getAllOther(val)
        this.getAllOtherList()
      }
    },
    'itemInfo.businessGuideState' (val) {
      this.allState(val)
    },
    'ruleForm.serviceRestrictions' (val) {
      this.serviceDisable = false
      if(val === '否') {
        this.serviceDisable = true
        this.ruleForm.otherServiceRestrictions = '无'
        this.ruleForm.serviceTimeConstraint = '无'
        this.ruleForm.serviceFrequencyRestriction = '无'
        this.ruleForm.remarks = '无'
      }
    }
  },
  computed: {
    ...mapState([
      'itemDetail', 'copyObj'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.allState(itemDetail.businessGuideState)
      this.ruleForm.majorTypesOfEvents = itemDetail.eventType.toString();
      return itemDetail
    }
  },
  data () {
    const checkobject = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error(' '))
      }
      callback()
    }
    const checkName = (rule, value, callback) => {
      value = value===null?'':value
      if (!validatorSpace(value)) {
        callback(new Error(' '))
      }
      callback()
    }
    //  const validateUrl = (rule, value, callback) => {
    //     var art =/^((https|http|ftp|rtsp|mms){0,1}(:\/\/){0,1})www\.(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/;
    //     if (value === '' ||value === null || value === undefined) {
    //       callback(new Error(' '));
    //     }else if (!art.test(value)) {
    //       callback(new Error(' '));
    //     }else{
    //       callback();
    //     }
    //   };
    return {
      otherVersion:null,
      // 评审
      judgeShow: false,
      judgeForm: {},
      according:false,
      dialogVisible: false,
      dialogImageUrl: '',
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      serviceDisable: false,
      method:['微信','支付宝','刷卡'],
      ruleForm:{
        majorTypesOfEvents: '',
        itemSmallType: '',
        itemCatalogue: '',
        basicCoding: '',
        codingImplementation: '',
        itemName: '',
        implementingOrgan: '',
        implementingBody: '',
        serviceObject: '',
        serviceTopic: '',
        typeOfOffice: '',
        timeLimitType: '',
        commitmenTimeLimit: '',
        statementOfCommitmentDeadline: '',
        charge: '',
        applicationMaterials: '',
        havaResultSample: '',
        chargingStandard: '',
        methodOfDeliveryOfApplicationMaterials: '',
        chargingBasis: '',
        paymentMethod: [],
        webSiteOfOnlineServiceSystem: '',
        handlingForm: '',
        numberOfVisitsToTheFieldHall: '',
        processingTime: '',
        processingWindow: '',
        addendumToProcessingTimeOrPlace: '',
        placeOfProcessing: '',
        enquiry: '',
        intermediaryService: '',
        supportOnlinePayment: '',
        isSupportLogistics: '',
        serviceLimit: '',
        typeOfProcessingTimeOrPlace: '手动填写',
        commonProblem: '',
        acceptingConditions: '',
        basisDesign: '',
        remarks: '无',
        handlingBasis: '',
        serviceRestrictions: '',
        otherServiceRestrictions: '无',
        serviceTimeConstraint: '无',
        serviceFrequencyRestriction: '无',
        advisoryAddress: '',
        consultationApproach: '',
        complaintChannels: '',
        serviceSupportHotline: '',
        itemSequencing: '不用填写',
        complaintAddress: '',
        materialSubmissionStatement: '参见材料分组',
        resultType: [],
        resultName: '',
        resultSample: '',
        exerciseHierarchy:'',
        sourcesChannelDescription: '',//来源渠道说明
        sources: '' , // 来源渠道
        materialType: '', //材料类型
      },
      results: ['许可证','执照','其他许可证书','资格证','资质证','其他合格证书','行政机关的批准文件','证明文件','加贴标签','加盖检验印章','加盖检测印章','加盖检疫印章'],
      options: ['0 次', '1 次', '2 次', '3 次', '全程网办'],
      timetype:[{
        value: '1',
        label:'自然日'
      },{
        value:'2',
        label:'工作日'
      }],
      rules:{
        majorTypesOfEvents: [{
          required: true, message: ' ', trigger: 'blur'
          // validator: valname,
        }],
        itemSmallType: [{
          required: true, message: ' ', trigger: 'blur'
          // validator: valname,
        }],
        itemCatalogue: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        basicCoding: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        codingImplementation: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        itemName: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        implementingOrgan: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        implementingBody: [
          { required: true,  validator:checkName, trigger: 'blur' }
        ],
        serviceObject: [
          // { required: true, validator:checkobject, trigger: 'change' }
        ],
        typeOfOffice: [
          { required: true, validator:checkName, trigger: 'change' }
        ],
        exerciseHierarchy: [
          // { required: true, validator: checkobject, trigger: 'blur' }
        ],
        applicationMaterials: [
          { required: true,  message: ' ', trigger: 'blur' }
        ],
        charge: [
          { required: true, message: ' ', trigger: 'blur' }
        ],
        handlingForm: [
          { required: true, validator:checkName, trigger: 'change' }
        ],
        numberOfVisitsToTheFieldHall: [
          { required: true, validator:checkName, trigger: 'change' }
        ],
        processingTime: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        placeOfProcessing: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
         materialSubmissionStatement:[
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        intermediaryService: [
          { required: true, message: ' ', trigger: 'blur' }
        ],
        commonProblem: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        acceptingConditions:[
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        basisDesign: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        serviceSupportHotline: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        yearCareful: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        resultName: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
      },
      selectList: {
        bigPro:[],
        samllPro: [],
        exerHier: [],
        handForm: [],
        materType:[],
        serObj: [],
        sour: [],
        methodMater:[],
        typeOff:[],
        empList:[],
        servTheme:[]
      }
    }
  },
  beforeMount () {
    this.getAllOther(this.itemInfo.id)
    this.init();
  },
  mounted(){
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.SET_ITEMDETAIL(itemDetail)
  },
  methods: {
    ...mapMutations([
      'SET_ITEMDETAIL', 'SET_SEARCHOBJ'
    ]),
    allState (val) {
      switch (val) {
        case 1: this.eventState = '未完善';this.eventClass = 'btn-not-perfect';break;
        case 2: this.eventState = '已完善';this.eventClass = 'btn-has-perfect';break;
        case 3: this.eventState = '已提交';this.eventClass = 'btn-submitted';break;
        default: break
      }
    },
    webFile (e, field) {
      this.ruleForm[field] = e
    },
    init() {
      this.getAllOtherList()
    },
    judge (propertyName, property) {
      let endTime = this.itemInfo.examineEndTime
      if(endTime&&endTime>=new Date().getTime()) {
        this.judgeShow = true
        this.judgeForm = {
          lineNumber: null,
          sheetNum: 1,
          id: this.itemInfo.id,
          propertyName,
          property,
          questionContent: ''
        }
      }
    },
    judgeCancel () {
      this.judgeShow = false
    },
    uploadImgs (imgs) {
      this.ruleForm.resultSample = imgs;
    },
    uploadimgs (img) {
      this.ruleForm.addendumToProcessingTimeOrPlace= img
    },
    changeRadio(val){
      if(val ==='否'){
        this.ruleForm.chargingBasis = '无'
        this.ruleForm.chargingStandard = '无'
        this.ruleForm.paymentMethod = ['无']
        this.according = true
      }else{
        this.ruleForm.chargingBasis = ''
        this.ruleForm.chargingStandard = ''
        this.ruleForm.paymentMethod = []
        this.according = false
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
      handleRemove(file, fileList) {
    },
    beforeAvatarUpload(file) {
      var testmsg=/^image\/(jpeg|jpg)$/.test(file.type)
      const isLt4M = file.size / 1024/1024 <=4//图片大小不超过2MB
      if (!testmsg) {
        this.$message.error('上传图片格式不对!');
        return
      }
      if(!isLt4M) {
        this.$message.error('上传图片大小不能超过 4M!');
        return
      }
      return testmsg  && isLt4M
    },
    submit(){
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.$Message.success('Success!');
        } else {
          this.$Message.error('Fail!');
        }
      })
    },
    async getAllOther(id){
      this.ruleForm.addendumToProcessingTimeOrPlace = ''
      this.ruleForm.resultSample = ''
      this.otherVersion = null
      let res = await administrativeService.getAllOther(id);
      if (res.code === 0) {
        if(res.obj) {
          let obj = Object.assign({},res.obj.businessGuideOtherServicesOne,res.obj.businessGuideOtherServicesOnePrivate)
          this.formRegroup(obj)
        } else {
          this.$refs.ruleForm.resetFields()
          this.ruleForm.resultSample = '{"imgs":[]}'
          this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
        }
      } else {
        this.$refs.ruleForm.resetFields()
        this.ruleForm.resultSample = '{"imgs":[]}'
        this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
      }
    },
    formRegroup(obj){
      // obj.exerciseHierarchy = obj.exerciseHierarchy?obj.exerciseHierarchy.split(','):[];
      obj.methodOfDeliveryOfApplicationMaterials = obj.methodOfDeliveryOfApplicationMaterials?obj.methodOfDeliveryOfApplicationMaterials.split(','):[];
      obj.resultType = obj.resultType?obj.resultType.split(','):[]
      obj.paymentMethod= obj.paymentMethod?obj.paymentMethod.split(','):[]
      this.according = obj.charge === '否';
      this.ruleForm = obj
      this.ruleForm.resultSample = this.ruleForm.resultSample || '{"imgs":[]}'
      this.ruleForm.addendumToProcessingTimeOrPlace = this.ruleForm.addendumToProcessingTimeOrPlace || '{"imgs":[]}'
      this.otherVersion=obj.version
    },
    //保存
    async otherSave(state){
      // this.according = res.obj.charge === '否';
      let param={
        businessGuideOtherServicesOne: {
          ...this.ruleForm,
          id:this.ruleForm.id,
          eventId:this.itemInfo.id,
          // exerciseHierarchy:this.ruleForm.exerciseHierarchy.toString(),
          resultType: this.ruleForm.resultType.toString(),
          paymentMethod: this.ruleForm.paymentMethod.toString(),
          methodOfDeliveryOfApplicationMaterials: this.ruleForm.methodOfDeliveryOfApplicationMaterials.toString(),
          state,
        },
        businessGuideOtherServicesOnePrivate: {
          ...this.ruleForm,
          eventId:this.itemInfo.id,
          departmentalMattersId: this.itemInfo.id,
          state,
          version:this.otherVersion
        }
      }
      let res = await administrativeService.otherSave(param);
      if(res.code === 0){
        // this.$refs['ruleForm'].resetFields();
        // this.ruleForm = {};
        this.itemInfo.businessGuideState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
        this.$message.success(res.msg);
        this.otherVersion=null;
        this.getAllOther(this.itemInfo.id)
      }else{
        this.$message.error(res.msg);
      }
    },
    saveFiling(state){
      if(this.itemInfo.region === '1'||this.itemInfo.region === '2'){
        this.otherSave(state)
      } else {
        this.otherSaveFiling(state)
      }
    },
    formSubmit(){
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.saveFiling(2);
        }else{
          this.saveFiling(1);
        }
      })
    },
    //市级以下
    async otherSaveFiling(state){
      let param = {
        ...this.ruleForm,
        eventId:this.itemInfo.id,
        departmentalMattersId: this.itemInfo.id,
        resultType: this.ruleForm.resultType.join(','),
        state,
      }
      let res = await administrativeService.otherSaveFiling(param);
      if(res.code === 0){
        this.itemInfo.businessGuideState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
        this.$message.success(res.msg);
        this.otherVersion=null;
        this.getAllOther(this.itemInfo.id)
      }else{
        this.$message.error(res.msg);
      }
    },
    // 获取其他list内容的接口
    async getAllOtherList () {
      let res = await administrativeService.getAll(4);
      this.SET_SEARCHOBJ(null)
      if(res.code === 0){
        this.SET_SEARCHOBJ(res.obj)
        // // 行使层级
        // this.selectList.exerHier = res.obj.exerciseHierarchy || []
        // 办理形式
        this.selectList.handForm = res.obj.handlingForm || []
        // // 事项小类型
        // this.selectList.samllPro = res.obj.itemSmallType || []
        // 大事项
        this.selectList.bigPro = res.obj.majorTypesOfEvents || []
        // 申请材料递送方式
        this.selectList.methodMater = res.obj.methodOfDeliveryOfApplicationMaterials || []
        // 服务主题
        this.selectList.servTheme = res.obj.serviceTopic || []
        // 办件类型
        this.selectList.typeOff = res.obj.typeOfOffice || []
        // // 服务对象
        // this.selectList.serObj = res.obj.serviceObject || []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.container{
  background:#fff;
  height: 100%
}
.model-form{
  height: 100%;
  padding-top: 20px;
  padding-right: 20px;
  background: #fff;
  .textareaLabel {
    /deep/ .el-form-item__label {
    height:52px!important;
    }
    /deep/ .el-textarea{
        /deep/ .el-textarea__inner{
              border-radius: 0px !important;
              height:52px !important
        }
      }
  }
  // .optionLabel /deep/ .el-form-item__label {
  //   height:52px!important;
  // }
}
.item_radio{
  height:35px !important;
  border: 0.2px solid #dcdfec;
  // width: calc(100% - 150px);
}
.event-state{
  height:27px
}
.tag-size /deep/ .el-tag{
  font-size: 14px;
  color: #606266;
}
</style>
