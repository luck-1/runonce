<template>
<!--2 审核转报 -->
  <div class="model-input-form" style="position: relative;">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>审核转报类</h2>
    <el-form :model="ruleForm" :rules="rules" :inline="true" ref="ruleForm" label-width="150px" class="demo-ruleForm">
      <div>
        <el-row style="height:100%;background: #fff;" class="model-form">
          <el-col>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="事项大类型 " prop="majorTypesOfEvents" >
                  <el-select v-model="ruleForm.majorTypesOfEvents" disabled style="width: 100%;" >
                    <el-option v-for="item in selectList.bigPro"  :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="事项小类型 " prop="itemSmallType" @dblclick.native="judge('事项小类型', 'itemSmallType')">
                  <el-select v-model="ruleForm.itemSmallType" clearable placeholder="请选择事项小类型" disabled>
                    <el-option v-for="item in selectList.samllPro" :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="事项名称目录 " prop="itemCatalogue" @dblclick.native="judge('事项名称目录', 'itemCatalogue')">
                  <el-input clearable :maxlength="50"  v-model="ruleForm.itemCatalogue" disabled placeholder="请输入事项名称目录" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="初审机关 " prop="firstInstanceOrgan" @dblclick.native="judge('初审机关', 'firstInstanceOrgan')">
                  <el-input v-model="ruleForm.firstInstanceOrgan" :maxlength="50" clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入市级初审机关名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <!-- <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理项编码 " title="业务办理项编码" prop="basicCoding" @dblclick.native="judge('办理项编码', 'basicCoding')">
                  <el-input disabled :maxlength="50" v-model="ruleForm.basicCoding" placeholder="自动生成办理项编码"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="实施编码 " title="实施清单编码" prop="codingImplementation" @dblclick.native="judge('实施编码', 'codingImplementation')">
                  <el-input disabled :maxlength="50" v-model="ruleForm.codingImplementation" placeholder="自动生成实施编码"></el-input>
                </el-form-item>
              </el-col>
            </el-row> -->
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="省级初审机关名称 " prop="nameOfProvincialFirstInspectionAuthority" @dblclick.native="judge('省级初审机关名称', 'nameOfProvincialFirstInspectionAuthority')">
                  <el-input :maxlength="50" v-model="ruleForm.nameOfProvincialFirstInspectionAuthority" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable  placeholder="请输入省级初审机关名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理事项名称 " prop="itemName" @dblclick.native="judge('办理事项名称', 'itemName')">
                  <el-input clearable v-model="ruleForm.itemName" :maxlength="50" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="市级初审机关名称 " prop="nameOfMunicipalFirstInspectionAuthority" @dblclick.native="judge('市级初审机关名称', 'nameOfMunicipalFirstInspectionAuthority')">
                  <el-input :maxlength="50" v-model="ruleForm.nameOfMunicipalFirstInspectionAuthority" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请输入市级初审机关名称" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="县级初审机关名称 " prop="nameOfPreliminaryInspectionOrgansAtCountyLevel" @dblclick.native="judge('县级初审机关名称', 'nameOfPreliminaryInspectionOrgansAtCountyLevel')">
                  <el-input clearable  :maxlength="50" v-model="ruleForm.nameOfPreliminaryInspectionOrgansAtCountyLevel" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入县级初审机关名称" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="终审机关" prop=" courtOfFinalAppeal" @dblclick.native="judge('终审机关', 'courtOfFinalAppeal')">
                  <el-input clearable  :maxlength="50" v-model="ruleForm.courtOfFinalAppeal" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入终审机关" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="中央终审机关名称 " prop="nameOfCentralFinalAppealAuthority" @dblclick.native="judge('中央终审机关名称', 'nameOfCentralFinalAppealAuthority')">
                  <el-input clearable :maxlength="50" v-model="ruleForm.nameOfCentralFinalAppealAuthority" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入中央终审机关名称" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="省级终审机关名称 " prop="nameOfProvincialFinalAppealAuthority" @dblclick.native="judge('省级终审机关名称', 'nameOfProvincialFinalAppealAuthority')">
                  <el-input clearable :maxlength="50" v-model="ruleForm.nameOfProvincialFinalAppealAuthority" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入省级终审机关名称" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px" class="admin-select-let-space">
                <el-form-item label="服务对象 " prop="serviceObject" @dblclick.native="judge('服务对象', 'serviceObject')">
                  <el-input disabled v-model="ruleForm.serviceObject" clearable placeholder="请输入服务对象"></el-input>
                  <!-- <el-select class="tag-size" filterable v-model="ruleForm.serviceObject" multiple disabled clearable placeholder="请选择服务对象">
                    <el-option v-for="item in selectList.serObj"  :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select> -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px" class="admin-select-let-space">
                <el-form-item label="服务主题 " prop="serviceTopic" @dblclick.native="judge('服务主题', 'serviceTopic')">
                  <el-select allow-create :collapse-tags="!(watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2'))" filterable v-model="ruleForm.serviceTopic" clearable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请选择服务主题" >
                    <el-option v-for="item in selectList.servTheme"  :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item  label="市级终审机关名称 " prop="nameOfMunicipalFinalAppealAuthority" @dblclick.native="judge('市级终审机关名称', 'nameOfMunicipalFinalAppealAuthority')">
                  <el-input :maxlength="50" v-model="ruleForm.nameOfMunicipalFinalAppealAuthority" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请输入市级终审机关名称" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="转报审批机关 " prop="reportingAndExaminationAndApprovalAuthority" @dblclick.native="judge('转报审批机关', 'reportingAndExaminationAndApprovalAuthority')">
                  <el-input clearable :maxlength="50" v-model="ruleForm.reportingAndExaminationAndApprovalAuthority" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入转报审批机关名称" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="县级转报审批机关 " prop="nameOfCountyLevelTransferExaminationAndApprovalAuthority" @dblclick.native="judge('县级转报审批机关', 'nameOfCountyLevelTransferExaminationAndApprovalAuthority')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="50" v-model="ruleForm.nameOfCountyLevelTransferExaminationAndApprovalAuthority" placeholder="请输入县级转报审批机关名称" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="市级转报审批机关 " prop="nameOfMunicipalTransferExaminationAndApprovalAuthorities" @dblclick.native="judge('市级转报审批机关', 'nameOfMunicipalTransferExaminationAndApprovalAuthorities')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="50" v-model="ruleForm.nameOfMunicipalTransferExaminationAndApprovalAuthorities" placeholder="请输入市级转报审批机关名称" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="省级转报审批机关 " prop="nameOfProvincialTransferExaminationAndApprovalAuthority" @dblclick.native="judge('省级转报审批机关', 'nameOfProvincialTransferExaminationAndApprovalAuthority')">
                  <el-input clearable  :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="50" v-model="ruleForm.nameOfProvincialTransferExaminationAndApprovalAuthority" placeholder="请输入省级转报审批机关名称" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="县级法定办结时限 " prop="statutoryCompletionTimeLimitAtCountyLevel" @dblclick.native="judge('县级法定办结时限', 'statutoryCompletionTimeLimitAtCountyLevel')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.statutoryCompletionTimeLimitAtCountyLevel" placeholder="请输入县级法定办结时限" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="市级法定办结时限 " prop="municipalStatutoryDeadlineForCompletion" @dblclick.native="judge('市级法定办结时限', 'municipalStatutoryDeadlineForCompletion')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.municipalStatutoryDeadlineForCompletion" placeholder="请输入市级法定办结时限" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="省级法定办结时限 " prop="provincialStatutoryCompletionTimeLimit" @dblclick.native="judge('省级法定办结时限', 'provincialStatutoryCompletionTimeLimit')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.provincialStatutoryCompletionTimeLimit" placeholder="请输入省级法定办结时限" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="县级承诺办结时限 " prop="timeLimitForFulfillingCountyLevelCommitments" @dblclick.native="judge('县级承诺办结时限', 'timeLimitForFulfillingCountyLevelCommitments')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.timeLimitForFulfillingCountyLevelCommitments" placeholder="请输入县级承诺办结时限" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="市级承诺办结时限 " prop="municipalCommitmentCompletionTimeLimit" @dblclick.native="judge('市级承诺办结时限', 'municipalCommitmentCompletionTimeLimit')">
                  <el-input clearable :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.municipalCommitmentCompletionTimeLimit" placeholder="请输入市级承诺办结时限" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="联办机构 " prop="jointAgency" @dblclick.native="judge('联办机构', 'jointAgency')">
                  <el-input clearable :maxlength="1000" :readonly="watchPic" v-model="ruleForm.jointAgency" placeholder="请输入联办机构"></el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="联办机构适用情形 " prop="applicationOfIointOrganizations" >
                  <el-input v-model="ruleForm.applicationOfIointOrganizations" :maxlength="255" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请输入联办机构适用情形" ></el-input>
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row>
                <el-col :span="24" style="margin-top: -17px">
                  <el-form-item  label="联办机构适用情形 " prop="applicationOfJointOrganizations" @dblclick.native="judge('联办机构适用情形', 'applicationOfJointOrganizations')">
                    <el-input :readonly="watchPic" :maxlength="255" v-model="ruleForm.applicationOfJointOrganizations" clearable placeholder="请输入联办机构适用情形"  ></el-input>
                  </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="省级承诺办结时限 " prop="provincialCommitmentDeadline" @dblclick.native="judge('省级承诺办结时限', 'provincialCommitmentDeadline')">
                  <el-input v-model="ruleForm.provincialCommitmentDeadline" :maxlength="100" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请输入省级承诺办结时限" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="年审或年检 " prop="annualExaminationOrAnnualInspection" @dblclick.native="judge('年审或年检', 'annualExaminationOrAnnualInspection')">
                  <el-select allow-create filterable :maxlength="50" v-model="ruleForm.annualExaminationOrAnnualInspection" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请选择年审或年检" >
                    <el-option v-for="item in selectList.yearCare"  :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="年审或年检次数 " prop="numberOfAnnualReviews" @dblclick.native="judge('年审或年检次数', 'numberOfAnnualReviews')">
                  <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="10" v-model="ruleForm.numberOfAnnualReviews" clearable placeholder="请输入年审或年检次数 示例：'12''" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="是否收费 " prop="charge" @dblclick.native="judge('是否收费', 'charge')" class="item_radio">
                  <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.charge"  @change="changeRadio">
                    <el-radio label="是"></el-radio>
                    <el-radio label="否"></el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="收费标准 " prop="chargingStandard" @dblclick.native="judge('收费标准', 'chargingStandard')">
                  <el-input v-model="ruleForm.chargingStandard" :maxlength="2000" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入收费标准" :disabled="according"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="收费依据 " prop="chargingBasis" @dblclick.native="judge('收费依据', 'chargingBasis')">
                  <el-input v-model="ruleForm.chargingBasis" :maxlength="2000" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" placeholder="请输入收费依据" :disabled="according"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="支付方式 " prop="paymentMethod" @dblclick.native="judge('支付方式', 'paymentMethod')">
                  <el-select class="tag-size" allow-create multiple filterable :maxlength="10" :disabled="according||watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.paymentMethod" clearable placeholder="请选择支付方式">
                    <el-option v-for="(item,index) in method" :key="index" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- dbb 去掉 -->
              <!-- <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="是否中介服务" prop="intermediaryService" @dblclick.native="judge('是否中介服务', 'intermediaryService')" class="item_radio">
                  <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.intermediaryService">
                    <el-radio label="是"></el-radio>
                    <el-radio label="否"></el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
               <el-col :span="12" style="margin-top: -16.5px">
                <el-form-item label="是否支持物流快递" prop="isSupportLogistics" @dblclick.native="judge('是否支持物流快递', 'isSupportLogistics')" class="item_radio">
                  <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.isSupportLogistics">
                    <el-radio label="是"></el-radio>
                    <el-radio label="否"></el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col> -->
  <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="申请材料递送方式 " prop="methodOfDeliveryOfApplicationMaterials" @dblclick.native="judge('申请材料递送方式', 'methodOfDeliveryOfApplicationMaterials')">
                  <el-select class="tag-size" multiple allow-create filterable :maxlength="255" v-model="ruleForm.methodOfDeliveryOfApplicationMaterials" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请选择申请材料递送方式" >
                    <el-option v-for="item in selectList.methodMater"  :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
                <!-- dbb  去掉 -->
              <!-- <el-col :span="12" style="margin-top: -16.5px">
                <el-form-item label="是否支持网上支付" prop="supportOnlinePayment" @dblclick.native="judge('是否支持网上支付', 'supportOnlinePayment')" class="item_radio">
                  <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.supportOnlinePayment">
                    <el-radio label="是"></el-radio>
                    <el-radio label="否"></el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col> -->

            </el-row>
            <el-row>

              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="是否有网上服务系统 " prop="onlineServiceSystem" @dblclick.native="judge('是否有网上服务系统', 'onlineServiceSystem')" class="item_radio">
                  <el-radio-group :disabled="watchPic" v-model="ruleForm.onlineServiceSystem"  @change="changeyes">
                    <el-radio label="是"></el-radio>
                    <el-radio label="否"></el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="网上服务系统网址 " prop="webSiteOfOnlineServiceSystem" @dblclick.native="judge('网上服务系统网址', 'webSiteOfOnlineServiceSystem')">
                  <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.webSiteOfOnlineServiceSystem" placeholder="请输入网上服务系统网址 示例'http://www.baidu.com'" :disabled="accordingUrl"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>

              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理结果 " prop="handlingResult" @dblclick.native="judge('办理结果', 'handlingResult')">
                  <el-tooltip placement="top" v-if="ruleForm.handlingResult">
                    <div slot="content" :maxlength="100" style="width:400px;height:100%;" >{{ruleForm.handlingResult}}</div>
                    <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.handlingResult" clearable placeholder="请输入办理结果" ></el-input>
                  </el-tooltip>
                  <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.handlingResult" clearable placeholder="请输入办理结果" ></el-input>
                </el-form-item>
              </el-col>
                <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="办理形式 " prop="handlingForm" @dblclick.native="judge('办理形式', 'handlingForm')">
                  <el-select allow-create filterable :maxlength="50" v-model="ruleForm.handlingForm" clearable placeholder="请选择办理形式" :disabled="watchPic">
                    <el-option v-for="item in selectList.handForm"  :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>

              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="网上申请形式 " prop="onlineApplicationForm" @dblclick.native="judge('网上申请形式', 'onlineApplicationForm')">
                  <el-select allow-create filterable :maxlength="10" v-model="ruleForm.onlineApplicationForm" :disabled="watchPic" clearable placeholder="请选择网上申请形式" >
                    <el-option v-for="item in selectList.onlineApp" :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
                <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="事项星级 " prop="eventStar" @dblclick.native="judge('事项星级', 'eventStar')">
                  <el-select allow-create :maxlength="100" filterable v-model="ruleForm.eventStar" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请选择事项星级" >
                    <!-- <el-option v-for="item in selectList.matStar"  :key="item.id" :label="item.name" :value="item.name"></el-option> -->
                    <el-option title="未实现网上办理" label="1星" value="1"></el-option>
                    <el-option title="已实现了网上申报功能，但尚未实现统一认证、统一查询" label="2星" value="2"></el-option>
                    <el-option title="网上预审、窗口报批、办结取件（统一认证）；到现场次数为2次" label="3星" value="3"></el-option>
                    <el-option title="网上申报、信任在先、办结核验（统一认证）；到现场次数为1次" label="4星" value="4"></el-option>
                    <el-option title="网上申报、全程在线、办结送件（统一认证）；到现场次数为0次" label="5星" value="5"></el-option>
                  </el-select>
                  <!-- <el-rate v-model="ruleForm.mattersStar"></el-rate> -->
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>

              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理时间或地点类型 " prop="typeOfProcessingTimeOrPlace" >
                  <el-input v-model="ruleForm.typeOfProcessingTimeOrPlace" disabled></el-input>
                </el-form-item>
              </el-col>

            <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理时间 " prop="processingTime" @dblclick.native="judge('办理时间', 'processingTime')">
                  <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.processingTime" clearable placeholder="请输入办理时间 比如 上午：9:00-12:00 下午14:00-18:00"></el-input>
                  <!-- <el-date-picker type="date" placeholder="请选择办理时间" clearable v-model="ruleForm.processingTime" style="width: 100%;"></el-date-picker> -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <!-- dbb  12改24  美观期间 -->
              <el-col :span="24" style="margin-top: -17px">
                <el-form-item label="办理地点 " prop="placeOfProcessing" @dblclick.native="judge('办理地点', 'placeOfProcessing')">
                  <el-tooltip placement="top" v-if="ruleForm.placeOfProcessing">
                    <div slot="content" style="width:400px;height:100%;" >{{ruleForm.placeOfProcessing}}</div>
                    <el-input :readonly="watchPic" :maxlength="200" v-model="ruleForm.placeOfProcessing" clearable placeholder="请输入办理地点" ></el-input>
                  </el-tooltip>
                  <el-input v-else :readonly="watchPic" v-model="ruleForm.placeOfProcessing" clearable placeholder="请输入办理地点" ></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>
              <el-col :span="24" style="margin-top: -17px" class="flow-map">
                <el-form-item label="办理时间或地点附件 " prop="addendumToProcessingTimeOrPlace" @dblclick.native="judge('办理时间或地点附件', 'addendumToProcessingTimeOrPlace')" style="border:0.2px solid #dcdfec ;height:36px;">
                  <ImgUpload @webFileFun="webFile($event, 'addendumToProcessingTimeOrPlace')" :webFiles="copyObj?(copyObj.objectDataMap[1]||[]):[]" v-if="ruleForm.addendumToProcessingTimeOrPlace" :watchPic="watchPic" :otherPosition="true" :imgHandle="false" :isMini="true" :files="ruleForm.addendumToProcessingTimeOrPlace?JSON.parse(ruleForm.addendumToProcessingTimeOrPlace).imgs:[]" :imageNum='40' :multiple="true"
              @upload="uploadimgs( $event)"></ImgUpload>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理窗口 " prop="processingWindow" @dblclick.native="judge('办理窗口', 'processingWindow')">
                  <el-input :readonly="watchPic" :maxlength="200" v-model="ruleForm.processingWindow" clearable placeholder="请输入办理窗口" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="数量限制 " prop="quantitativeRestriction" @dblclick.native="judge('数量限制', 'quantitativeRestriction')">
                  <el-input :readonly="watchPic" :maxlength="255" v-model="ruleForm.quantitativeRestriction" clearable placeholder="请输入数量限制" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="办理查询 " prop="enquiry" @dblclick.native="judge('办理查询', 'enquiry')">
                  <el-tooltip placement="top" v-if="ruleForm.enquiry">
                    <div slot="content" style="width:400px;height:100%;" >{{ruleForm.enquiry}}</div>
                    <el-input :readonly="watchPic" :maxlength="1000" v-model="ruleForm.enquiry" clearable placeholder="请输入办理查询" ></el-input>
                  </el-tooltip>
                  <el-input v-else :readonly="watchPic" v-model="ruleForm.enquiry" clearable placeholder="请输入办理查询" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="常见问题 " prop="commonProblem" @dblclick.native="judge('常见问题', 'commonProblem')">
                  <el-tooltip placement="top" v-if="ruleForm.commonProblem">
                    <div slot="content" style="width:400px;height:100%;" >{{ruleForm.commonProblem}}</div>
                    <el-input :readonly="watchPic" :maxlength="1000" v-model="ruleForm.commonProblem" clearable placeholder="请输入常见问题"  ></el-input>
                  </el-tooltip>
                  <el-input v-else :readonly="watchPic" v-model="ruleForm.commonProblem" clearable placeholder="请输入常见问题"  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="备注说明 " prop="remarks" @dblclick.native="judge('备注说明', 'remarks')">
                  <el-tooltip placement="top" v-if="ruleForm.remarks">
                    <div slot="content" style="width:400px;height:100%;" >{{ruleForm.remarks}}</div>
                    <el-input :readonly="watchPic" :maxlength="255" v-model="ruleForm.remarks" clearable placeholder="请输入备注说明" ></el-input>
                  </el-tooltip>
                  <el-input v-else :readonly="watchPic" v-model="ruleForm.remarks" clearable placeholder="请输入备注说明" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="咨询途径 " prop="consultationApproach" @dblclick.native="judge('咨询途径', 'consultationApproach')">
                  <el-tooltip placement="top" v-if="ruleForm.consultationApproach">
                    <div slot="content" style="width:400px;height:100%;" >{{ruleForm.consultationApproach}}</div>
                    <el-input :readonly="watchPic" :maxlength="255" v-model="ruleForm.consultationApproach" clearable placeholder="请输入咨询途径"></el-input>
                  </el-tooltip>
                  <el-input v-else :readonly="watchPic" v-model="ruleForm.consultationApproach" clearable placeholder="请输入咨询途径"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="监督电话 " prop="serviceSupportHotline" @dblclick.native="judge('监督电话', 'serviceSupportHotline')">
                  <el-input :readonly="watchPic" :maxlength="50" v-model="ruleForm.serviceSupportHotline" clearable placeholder="请输入监督电话" minlength="12"></el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="事项排序 " prop="itemSequencing" >
                  <el-input :readonly="watchPic" :maxlength="20" v-model="ruleForm.itemSequencing" clearable placeholder="请输入事项排序" disabled></el-input>
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row>
              <el-col :span="24" style="margin-top: -16px">
                <el-form-item label="受理申请条件 " prop="acceptanceConditions" @dblclick.native="judge('受理申请条件', 'acceptanceConditions')" class="textareaLabel">
                  <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="1000" v-model="ruleForm.acceptanceConditions" clearable placeholder="请输入受理申请条件" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" style="margin-top: -16px">
                <el-form-item label="设定依据 " prop="settingBasis" class="textareaLabel" @dblclick.native="judge('设定依据', 'settingBasis')">
                  <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="1000" v-model="ruleForm.settingBasis" clearable placeholder="请输入设定依据" type="textarea" style="border-radius: 0px;height: 34px;;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
             <el-row>
              <el-col :span="24" style="margin-top: -17px">
                <el-form-item label="材料提交说明 " prop="materialSubmissionStatement" class="textareaLabel" @dblclick.native="judge('材料提交说明', 'materialSubmissionStatement')">
                  <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.materialSubmissionStatement" clearable placeholder="请输入材料提交说明" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="margin-top: -17px">
                <el-form-item label="行使层级 " prop="exerciseHierarchy" @dblclick.native="judge('行使层级', 'exerciseHierarchy')">
                  <el-input disabled v-model="ruleForm.exerciseHierarchy" clearable placeholder="请输入行使层级"></el-input>
                  <!-- <el-select class="tag-size" allow-create :maxlength="10" filterable multiple v-model="ruleForm.exerciseHierarchy" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请选择行使层级" >
                    <el-option v-for="item in selectList.exerHier" :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select> -->
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12" style="margin-top: -16px">
                <el-form-item label="咨询电话 " prop="advisoryPhone" @dblclick.native="judge('咨询电话', 'advisoryPhone')">
                  <el-input :readonly="watchPic" v-model="ruleForm.advisoryPhone" clearable placeholder="请输入咨询电话"  ></el-input>
                </el-form-item>
              </el-col> -->
            </el-row>
          </el-col>
        </el-row>
        <div class="btn-submit" @click="formSubmit" v-if="!watchPic">保存</div>
      </div>
		</el-form>

    <!-- 评审 -->
    <JudgeDialog title="办事指南评审" :itemSee="itemSee" :visible="editState&&judgeShow" :judge="judgeForm" @cancel="judgeCancel"></JudgeDialog>
  </div>
</template>
<script>
import {mapState, mapMutations, mapActions} from 'vuex';
import { administrativeService } from '@/api/service';
import { validatSpace,validatorSpace } from '@/utils/validate';
import ImgUpload from '@/components/uploadPicture';
import JudgeDialog from '@/components/judgeDialog'
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
        this.bsznVersionsh = null
        let obj = Object.assign({},val.businessGuideAuditAndForwardingCategoryOne,val.businessGuideAuditAndForwardingCategoryOnePrivate)
        this.formRegroup(obj)
      }
    },
    state: function(newVal, oldVal) {
      if(newVal) {
      }
    },
    'itemDetail.id' (val) {
      if (val) {
        this.getAllAudit(val)
        this.getAllOtherList()
      }
    },
    'itemInfo.businessGuideState' (val) {
      this.allState(val)
    }
  },
  computed: {
    ...mapState([
      'itemDetail', 'copyObj'
    ]),
    itemInfo () {
      let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
      this.ruleForm.majorTypesOfEvents = itemDetail.eventType.toString();
      // this.ruleForm.itemSmallType = itemDetail.itemSmallType
      // this.ruleForm.itemCatalogue = itemDetail.itemCatalogue
      // this.ruleForm.itemName = itemDetail.eventName
      // this.ruleForm.serviceObject = itemDetail.serviceObject
      // this.ruleForm.exerciseHierarchy = itemDetail.exerciseHierarchy
      // this.ruleForm.basicCoding = itemDetail.directoryEncoding
      // this.ruleForm.codingImplementation = itemDetail.codingImplementation
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
    const validateNumber=(rule, value, callback)=>{
      var srt=/^[0-9]*$/;
      if (value === '' ||value === null || value === undefined) {
        callback(new Error('请输入次数'));
      }else if (!srt.test(value)) {
        callback(new Error("请输入数字"));
      }else{
        callback();
      }
    }
    // const validateUrl = (rule, value, callback) => {
    //   var art =/^((https|http|ftp|rtsp|mms){0,1}(:\/\/){0,1})www\.(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/;
    //   if (value === '' ||value === null || value === undefined) {
    //     callback(new Error('请输入网址'));
    //   }else if (!art.test(value)) {
    //     callback(new Error("网址格式错误,请输入正确的网址"));
    //   }else{
    //     callback();
    //   }
    // };
    return {
      bsznVersionsh:null,
      // 评审
      judgeShow: false,
      judgeForm: {},
      accordingUrl:false,
      according:false,
      eventState: '未完善',
      eventClass: 'btn-not-perfect',
      method:['微信','支付宝','刷卡'],
      ruleForm:{
        majorTypesOfEvents: '',
        itemSmallType: '',
        itemCatalogue: '',
        firstInstanceOrgan: '',
        codingImplementation: '',
        basicCoding: '',
        nameOfMunicipalFirstInspectionAuthority: '',
        itemName: '',
        nameOfMunicipalFirstInspectionAuthority: '',
        nameOfPreliminaryInspectionOrgansAtCountyLevel: '',
        courtOfFinalAppeal: '',
        nameOfCentralFinalAppealAuthority: '',
        nameOfProvincialFinalAppealAuthority: '',
        serviceObject: '',
        serviceTopic: '',
        nameOfMunicipalFinalAppealAuthority: '',
        reportingAndExaminationAndApprovalAuthority:'',
        nameOfCountyLevelTransferExaminationAndApprovalAuthority:'',
        nameOfMunicipalTransferExaminationAndApprovalAuthorities: '',
        nameOfProvincialTransferExaminationAndApprovalAuthority: '',
        statutoryCompletionTimeLimitAtCountyLevel: '',
        municipalStatutoryDeadlineForCompletion: '',
        provincialStatutoryCompletionTimeLimit: '',
        timeLimitForFulfillingCountyLevelCommitments: '',
        municipalCommitmentCompletionTimeLimit: '',
        jointAgency: '',
        applicationOfJointOrganizations: '',
        provincialCommitmentDeadline: '',
        vincialLevel: '',
        annualExaminationOrAnnualInspection: '',
        numberOfAnnualReviews: '',
        charge: '',
        chargingStandard: '',
        isSupportLogistics: '',
        paymentMethod: [],
        chargingBasis: '',
        intermediaryService: '',
        supportOnlinePayment: '',
        methodOfDeliveryOfApplicationMaterials: '',
        onlineServiceSystem: '',
        handlingResult: '',
        webSiteOfOnlineServiceSystem: '',
        handlingForm: '',
        eventStar: '',
        onlineApplicationForm: '',
        typeOfProcessingTimeOrPlace:'手动填写',
        processingTime: '',
        placeOfProcessing: '',
        processingWindow: '',
        addendumToProcessingTimeOrPlace: '',
        enquiry: '',
        quantitativeRestriction: '',
        acceptanceConditions: '',
        commonProblem: '',
        remarks: '',
        settingBasis: '',
        serviceSupportHotline: '',
        consultationApproach: '',
        materialSubmissionStatement: '参见材料分组',
        itemSequencing: '不用填写',
        exerciseHierarchy: '',
        sourcesChannelDescription: '',//来源渠道说明
        sources: '' , // 来源渠道
        materialType: '' ,//材料类型
        advisoryPhone: ''
      },
      rules:{
        majorTypesOfEvents: [
          {required: true,  message: ' ', trigger: 'blur'}
        ],
        itemSmallType: [
          {required: true, message: ' ', trigger: 'change' }
        ],
        itemCatalogue: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        itemName: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        serviceTopic: [
          { required: true, message: ' ', trigger: 'change' }
        ],
        serviceObject: [
          // { required: true,validator: checkobject, trigger: 'change' }
        ],
        timeLimitType: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        commitmenTimeLimit: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        charge: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        onlineServiceSystem: [
          { required: true, message: ' ', trigger: 'blur' }
        ],
        onlineApplicationForm: [
          { required: true, validator:checkName, trigger: 'change' }
        ],
        handlingForm: [
          { required: true, validator:checkName, trigger: 'change' }
        ],
        numberOfVisitsToTheFieldHall: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        processingTime: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        placeOfProcessing:
        [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        intermediaryService: [
          { required: true, message: ' ', trigger: 'blur' }
        ],
        commonProblem: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        acceptanceConditions:[
          { required: true,validator:checkName, trigger: 'blur' }
        ],
        settingBasis: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        handlingBasis: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        advisoryAddress: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        complaintChannels: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        serviceSupportHotline: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        complaintAddres: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        materialSubmissionStatement:[
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        exerciseHierarchy: [
          // { required: true, validator: checkobject, trigger: 'change' }
        ],
        annualExaminationOrAnnualInspection: [
          { required: true, validator:checkName, trigger: 'change' }
        ],
        numberOfAnnualReviews:[
          // { required: true, validator: validateNumber, trigger: 'blur' }
          { required: true, validator:checkName, trigger: 'blur' }
        ],
        webSiteOfOnlineServiceSystem: [
          // { required: true, validator: validateUrl, trigger: 'blur' }
        ],
        quantitativeRestriction: [
          { required: true, validator:checkName, trigger: 'blur' }
        ],
      },
      selectList: {
        yearCare: [],
        bigPro:[],
        samllPro: [],
        matStar:[],
        exerHier: [],
        handForm: [],
        materType: [],
        onlineApp: [],
        serObj: [],
        sour: [],
        methodMater: [],
        servTheme: [],
        empList:[],
      }
    }
  },
  mounted(){
    let itemDetail = this.itemDetail || localStorage.itemDetail ? JSON.parse(localStorage.itemDetail) : {}
    this.allState(itemDetail.businessGuideState)
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
    changeyes(tes){
      if(tes === '否'){
        this.accordingUrl = true
      }else{
        this.accordingUrl = false
      }
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
    uploadimgs (img) {
      this.ruleForm.addendumToProcessingTimeOrPlace= img
    },
    async getAllAudit(id){
      this.ruleForm.addendumToProcessingTimeOrPlace = ''
      this.bsznVersionsh = null
      let res = await administrativeService.getAllAudit(id);
      if(res.code === 0){
        if(res.obj) {
          let obj = Object.assign({},res.obj.businessGuideAuditAndForwardingCategoryOne,res.obj.businessGuideAuditAndForwardingCategoryOnePrivate)
          this.formRegroup(obj)
        } else {
          this.$refs.ruleForm.resetFields()
          this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
        }
      } else {
        this.$refs.ruleForm.resetFields()
        this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
      }
    },
    formRegroup(obj) {
      // obj.exerciseHierarchy = obj.exerciseHierarchy?obj.exerciseHierarchy.split(','):[];
      obj.methodOfDeliveryOfApplicationMaterials = obj.methodOfDeliveryOfApplicationMaterials?obj.methodOfDeliveryOfApplicationMaterials.split(','):[];
      obj.paymentMethod= obj.paymentMethod?obj.paymentMethod.split(','):[]
      this.accordingUrl = obj.onlineServiceSystem === '否';
      this.according = obj.charge === '否';
      obj.addendumToProcessingTimeOrPlace = obj.addendumToProcessingTimeOrPlace || '{"imgs":[]}'
      this.ruleForm = obj
      this.bsznVersionsh = obj.version
    },
    async auditSave(state){
      let param = {
        businessGuideAuditAndForwardingCategoryOne:{
          ...this.ruleForm,
          eventId:this.itemInfo.id,
          // exerciseHierarchy:this.ruleForm.exerciseHierarchy.toString(),
          paymentMethod: this.ruleForm.paymentMethod.toString(),
          methodOfDeliveryOfApplicationMaterials: this.ruleForm.methodOfDeliveryOfApplicationMaterials.toString(),
          state,
        },
        businessGuideAuditAndForwardingCategoryOnePrivate:{
          ...this.ruleForm,
          eventId:this.itemInfo.id,
          departmentalMattersId:this.itemInfo.id,
          state,
          version:this.bsznVersionsh
        }
      }
      let res = await administrativeService.auditSave(param);
        if(res.code === 0){
          this.bsznVersionsh=null;
          this.$message.success(res.msg);
          this.itemInfo.businessGuideState = state
          localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
          this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
          this.getAllAudit(this.itemInfo.id)
        } else{
          this.$message.error(res.msg);
        }
      },
      saveFiling(state){
        if(this.itemInfo.region === '1'||this.itemInfo.region === '2'){
          this.auditSave(state)
        } else {
          this.auditSaveFiling(state)
        }
      },
    //保存
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
    async auditSaveFiling(state){
      let param = {
        ...this.ruleForm,
        eventId:this.itemInfo.id,
        departmentalMattersId:this.itemInfo.id,
        state,
      }
      let src = await administrativeService.auditSaveFiling(param);
      if(res.code === 0){
        this.$message.success(res.msg);
        this.bsznVersionsh=null
        this.itemInfo.businessGuideState = state
        localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
        this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
        this.getAllAudit(this.itemInfo.id)
      }else{
        this.$message.error(res.msg);
      }
    },
    // 获取其他list内容的接口
    async getAllOtherList () {
      let res = await administrativeService.getAll(2);
      this.SET_SEARCHOBJ(null)
      if(res.code === 0){
        this.SET_SEARCHOBJ(res.obj)
        // 年审或年检
        this.selectList.yearCare = res.obj.annualExaminationOrAnnualInspection || []
        // // 事项星级
        // this.selectList.matStar = res.obj.eventStar || []
        // // 行使层级
        // this.selectList.exerHier = res.obj.exerciseHierarchy || []
        // // 事项小类型
        // this.selectList.samllPro = res.obj.itemSmallType || []
        // 大事项
        this.selectList.bigPro = res.obj.majorTypesOfEvents || []
        // 申请材料递送方式
        this.selectList.methodMater = res.obj.methodOfDeliveryOfApplicationMaterials || []
        // 网上申请形式
        this.selectList.onlineApp = res.obj.onlineApplicationForm || []
        // 服务主题
        this.selectList.servTheme = res.obj.serviceTopic || []
        // // 服务对象
        // this.selectList.serObj = res.obj.serviceObject || []
        // 办理形式
        this.selectList.handForm = res.obj.handlingForm || []
      }
    }
  },
  beforeMount () {
    this.getAllAudit(this.itemInfo.id)
    this.getAllOtherList()
  }
}
</script>

<style lang="scss" scoped>
  .container{
		background:#fff;
		height: 100%
	}
	.demo-ruleForm{
		height: 100%;
		padding-top: 20px;
    padding-right: 20px
	}
  .item_radio{
    border: 0.2px solid #dcdfec
  }
  .event-state{
    height:27px
  }
  .model-form {
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
  }
  .tag-size /deep/ .el-tag{
    font-size: 14px;
    color: #606266;
  }
</style>
