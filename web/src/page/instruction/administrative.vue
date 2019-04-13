<template>
<!-- 1行政许可和备案类 -->
  <div class="model-input-form" style="position: relative;">
    <div class="event-state" :class="eventClass" v-if="eventState"><span class="text">{{eventState}}</span></div>
    <h2>{{eventType}}</h2>
    <el-form :model="ruleForm" :rules="rules" :inline="true" ref="ruleForm" label-width="150px" class="model-form">
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项大类型 " prop="majorTypesOfEvents" >
            <el-select v-model="ruleForm.majorTypesOfEvents" disabled>
              <el-option v-for="item in selectList.bigPro"  :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项小类型 " prop="itemSmallType" @dblclick.native="judge('事项小类型', 'itemSmallType')">
            <el-select v-model="ruleForm.itemSmallType" disabled clearable placeholder="请选择事项小类型"  >
              <el-option v-for="item in selectList.samllPro" :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否自贸区事项 " prop="ftaMatters" @dblclick.native="judge('是否自贸区事项', 'ftaMatters')" class="item_radio">
            <el-radio-group v-model="ruleForm.ftaMatters" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="事项目录名称 " prop="transactionDirectory" @dblclick.native="judge('事项目录名称', 'transactionDirectory')">
            <el-input clearable  :maxlength="50" v-model="ruleForm.transactionDirectory" placeholder="请输入事项名称目录" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -18px">
          <el-form-item label="办理项编码" title="业务办理项编码" prop="basicCoding">
            <el-input v-model="ruleForm.basicCoding" disabled placeholder="自动生成办理项编码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -18px">
          <el-form-item label="实施编码" title="实施清单编码" prop="codingImplementation">
            <el-input v-model="ruleForm.codingImplementation" disabled placeholder="自动生成实施编码"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="办理事项名称 " prop="itemName" @dblclick.native="judge('办理事项名称', 'itemName')">
            <el-input :maxlength="50" clearable v-model="ruleForm.itemName" placeholder="请输入办理事项名称" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="权力来源 " prop="sourceOfPower" @dblclick.native="judge('权力来源', 'sourceOfPower')">
            <el-select allow-create filterable  :maxlength="50" v-model="ruleForm.sourceOfPower" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" clearable placeholder="请选择权力来源"  >
              <el-option v-for="item in selectList.powSour"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="实施机关 " prop="implementingOrgan" @dblclick.native="judge('实施机关', 'implementingOrgan')">
            <el-input :readonly="watchPic" :maxlength="50" v-model="ruleForm.implementingOrgan" clearable placeholder="请输入实施机关"  ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="实施主体性质 " prop="natureOfImplementingSubject" @dblclick.native="judge('实施主体性质', 'natureOfImplementingSubject')" class="formitem_last">
            <el-select allow-create filterable :maxlength="50" :disabled="watchPic" v-model="ruleForm.natureOfImplementingSubject" clearable placeholder="请选择实施主体性质"  >
              <el-option v-for="item in selectList.mainPro"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="实施机构科室 " prop="implementingBody" @dblclick.native="judge('实施机构科室', 'implementingBody')">
            <el-input :readonly="watchPic" :maxlength="50" v-model="ruleForm.implementingBody" clearable placeholder="请输入实施机构科室"  ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px"  class="admin-select-let-space">
          <el-form-item label="服务对象 " prop="serviceObject" @dblclick.native="judge('服务对象', 'serviceObject')">
            <el-input disabled v-model="ruleForm.serviceObject" clearable placeholder="请输入服务对象"></el-input>
            <!-- <el-select class="tag-size" filterable disabled v-model="ruleForm.serviceObject" multiple clearable placeholder="请选择服务对象">
              <el-option v-for="item in selectList.serObj" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select> -->
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px" class="admin-select-let-space">
          <el-form-item label="服务主题 " prop="serviceTopic" @dblclick.native="judge('服务主题', 'serviceTopic')">
            <el-select allow-create :collapse-tags="!(watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2'))" filterable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.serviceTopic" clearable placeholder="请选择服务主题">
              <el-option v-for="item in selectList.servTheme"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="权限划分 " prop="permissionDivision" @dblclick.native="judge('权限划分', 'permissionDivision')">
            <el-select class="tag-size" allow-create :maxlength="50" multiple filterable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.permissionDivision" clearable placeholder="请选择权限划分"  >
              <el-option v-for="item in selectList.purDiv"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="详细权限划分 " prop="detailedPermissionDivision" @dblclick.native="judge('详细权限划分', 'detailedPermissionDivision')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="1000" v-model="ruleForm.detailedPermissionDivision" clearable placeholder="请输入详细权限划分"  ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="办件类型 " prop="typeOfOffice" @dblclick.native="judge('办件类型', 'typeOfOffice')">
            <el-select allow-create :maxlength="50" filterable :disabled="watchPic" v-model="ruleForm.typeOfOffice" clearable placeholder="请选择办件类型"  >
              <el-option v-for="item in selectList.typeOff"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="行使层级 " prop="exerciseHierarchy" @dblclick.native="judge('行使层级', 'exerciseHierarchy')">
            <el-input disabled v-model="ruleForm.exerciseHierarchy" clearable placeholder="请输入行使层级"></el-input>
            <!-- <el-select class="tag-size" allow-create filterable multiple :maxlength="20" disabled v-model="ruleForm.exerciseHierarchy" clearable placeholder="请选择行使层级"  >
              <el-option v-for="item in selectList.exerHier"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select> -->
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="时限类型 " prop="timeLimitType" @dblclick.native="judge('时限类型', 'timeLimitType')">
            <el-select allow-create filterable :maxlength="20" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.timeLimitType" clearable placeholder="请选择时限类型"  >
              <el-option v-for="item in timetype" :key="item.value" :label="item.label" :value="item.label"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="法定办结时限 " prop="statutoryDeadlines" @dblclick.native="judge('法定办结时限', 'statutoryDeadlines')">
            <el-input placeholder="请输入法定办结时限" v-model="ruleForm.statutoryDeadlines" :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="10" clearable>
              <span slot="append">{{ruleForm.timeLimitType}}</span>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item  label="法定办结时限说明 " prop="statutoryTimeLimitForCompletion" @dblclick.native="judge('法定办结时限说明', 'statutoryTimeLimitForCompletion')">
            <el-tooltip placement="top" v-if="ruleForm.statutoryTimeLimitForCompletion">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.statutoryTimeLimitForCompletion}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="1000" v-model="ruleForm.statutoryTimeLimitForCompletion" clearable placeholder="请输入法定办结时限说明"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.statutoryTimeLimitForCompletion" clearable placeholder="请输入法定办结时限说明"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="承诺办结时限 " prop="commitmentTimeLimit" class="textareaInput" @dblclick.native="judge('承诺办结时限', 'commitmentTimeLimit')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="50" v-model="ruleForm.commitmentTimeLimit" clearable placeholder="请输入承诺办结时限" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item  label="承诺办结时限说明 " prop="statementOfCommitmentDeadline" @dblclick.native="judge('承诺办结时限说明', 'statementOfCommitmentDeadline')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.statementOfCommitmentDeadline" clearable placeholder="请输入承诺办结时限说明"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item  label="联办机构适用情形 " prop="applicationOfJointOrganizations" @dblclick.native="judge('联办机构适用情形', 'applicationOfJointOrganizations')">
            <el-tooltip placement="top" v-if="ruleForm.applicationOfJointOrganizations">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.applicationOfJointOrganizations}}</div>
              <el-input :readonly="watchPic" :maxlength="255" v-model="ruleForm.applicationOfJointOrganizations" clearable placeholder="请输入联办机构适用情形"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.applicationOfJointOrganizations" clearable placeholder="请输入联办机构适用情形"  ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="联办机构 " prop="jointAgency" @dblclick.native="judge('联办机构', 'jointAgency')">
            <el-tooltip placement="top" v-if="ruleForm.jointAgency">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.jointAgency}}</div>
              <el-input :readonly="watchPic" :maxlength="1000" v-model="ruleForm.jointAgency" clearable placeholder="请输入联办机构"></el-input>
             </el-tooltip>
             <el-input v-else :readonly="watchPic" v-model="ruleForm.jointAgency" clearable placeholder="请输入联办机构"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="是否收费 " prop="charge" class="item_radio" @dblclick.native="judge('是否收费', 'charge')">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.charge" @change="changeRadio">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="收费标准 " prop="chargingStandard" @dblclick.native="judge('收费标准', 'chargingStandard')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="200" v-model="ruleForm.chargingStandard" clearable placeholder="请输入收费标准" :disabled="according" ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="收费依据 " prop="chargingBasis" @dblclick.native="judge('收费依据', 'chargingBasis')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.chargingBasis" clearable placeholder="请输入收费依据"  :disabled="according"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="支付方式 " prop="paymentMethod" @dblclick.native="judge('支付方式', 'paymentMethod')">
            <el-select class="tag-size" allow-create multiple filterable :maxlength="10" :disabled="according||watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.paymentMethod" clearable placeholder="请选择支付方式">
              <el-option v-for="(item,index) in method" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="申请材料递送方式 " prop="methodOfDeliveryOfApplicationMaterials" @dblclick.native="judge('申请材料递送方式', 'methodOfDeliveryOfApplicationMaterials')">
            <el-select class="tag-size" multiple allow-create filterable :maxlength="100" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.methodOfDeliveryOfApplicationMaterials" clearable placeholder="请选择申请材料递送方式"  >
              <el-option v-for="item in selectList.methodMater"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
         <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否有网上服务系统 " prop="onlineServiceSystem" class="item_radio" @dblclick.native="judge('是否有网上服务系统', 'onlineServiceSystem')">
            <el-radio-group :disabled="watchPic" v-model="ruleForm.onlineServiceSystem"  @change="changeyes">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="网上服务系统网址 " prop="webSiteOfOnlineServiceSystem" @dblclick.native="judge('网上服务系统网址', 'webSiteOfOnlineServiceSystem')">
            <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.webSiteOfOnlineServiceSystem" clearable :disabled="accordingUrl" placeholder="请输入网上服务系统网址 示例：'http://www.baidu.com'"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="投资项目申报地址" :maxlength="200" prop="declarationAddressOfEnterpriseInvestmentProjects"  >
            <el-input v-model="ruleForm.declarationAddressOfEnterpriseInvestmentProjects" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="委托代办 " prop="concierge" @dblclick.native="judge('委托代办', 'concierge')">
            <el-select allow-create :maxlength="30" filterable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.concierge" clearable placeholder="请选择委托代办"  >
              <el-option v-for="item in selectList.entr"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="通办范围 " prop="scopeOfOperation" @dblclick.native="judge('通办范围', 'scopeOfOperation')">
            <el-select allow-create :maxlength="30" filterable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.scopeOfOperation" clearable placeholder="请选择通办范围"  >
              <el-option v-for="item in selectList.scopeGen"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理形式 " prop="handlingForm" @dblclick.native="judge('办理形式', 'handlingForm')">
            <el-select allow-create :maxlength="30" filterable :disabled="watchPic" v-model="ruleForm.handlingForm" clearable placeholder="请选择办理形式"  >
              <el-option v-for="item in selectList.handForm"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
         <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="网上申请形式 " prop="onlineApplicationForm" @dblclick.native="judge('网上申请形式', 'onlineApplicationForm')">
            <el-select allow-create filterable :maxlength="30" :disabled="watchPic" v-model="ruleForm.onlineApplicationForm" clearable placeholder="请选择网上申请形式"  >
              <el-option v-for="item in selectList.onlineApp" :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="到现场大厅窗口次数 " prop="numberOfVisitsToTheFieldHall" @dblclick.native="judge('到现场大厅窗口次数', 'numberOfVisitsToTheFieldHall')">
            <el-select allow-create filterable :maxlength="5" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.numberOfVisitsToTheFieldHall" clearable placeholder="请选择到现场大厅窗口次数"  >
              <el-option v-for="(item,index) in options" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项星级 " prop="eventStar" @dblclick.native="judge('事项星级', 'eventStar')">
            <el-select allow-create filterable style=”cursor:pointer” :maxlength="100" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.eventStar" clearable placeholder="请选择事项星级"  >
              <el-option title="未实现网上办理" label="1星" value="1"></el-option>
              <el-option title="已实现了网上申报功能，但尚未实现统一认证、统一查询" label="2星" value="2"></el-option>
              <el-option title="网上预审、窗口报批、办结取件（统一认证）；到现场次数为2次" label="3星" value="3"></el-option>
              <el-option title="网上申报、信任在先、办结核验（统一认证）；到现场次数为1次" label="4星" value="4"></el-option>
              <el-option title="网上申报、全程在线、办结送件（统一认证）；到现场次数为0次" label="5星" value="5"></el-option>
            </el-select>
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
            <el-tooltip placement="top" v-if="ruleForm.processingTime">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.processingTime}}</div>
              <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.processingTime" clearable placeholder="请输入办理时间 比如 上午：9:00-12:00 下午14:00-18:00"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" :maxlength="100" v-model="ruleForm.processingTime" clearable placeholder="请输入办理时间 比如 上午：9:00-12:00 下午14:00-18:00"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px" class="flow-map">
          <el-form-item label="办理时间或地点附件 " prop="addendumToProcessingTimeOrPlace" @dblclick.native="judge('办理时间或地点附件', 'addendumToProcessingTimeOrPlace')" style="border:0.2px solid #dcdfec ;height:35px;">
            <ImgUpload @webFileFun="webFile($event, 'addendumToProcessingTimeOrPlace')" :webFiles="copyObj?(copyObj.objectDataMap[1]||[]):[]" v-if="ruleForm.addendumToProcessingTimeOrPlace" :watchPic="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :otherPosition="true" :imgHandle="false" :isMini="true" :files="ruleForm.addendumToProcessingTimeOrPlace?JSON.parse(ruleForm.addendumToProcessingTimeOrPlace).imgs:[]" :imageNum='40' :multiple="true"
              @upload="uploadimgs( $event)"></ImgUpload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理地点 " prop="placeOfProcessing" class="textareaInput" @dblclick.native="judge('办理地点', 'placeOfProcessing')">
            <el-tooltip placement="top" v-if="ruleForm.placeOfProcessing">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.placeOfProcessing}}</div>
              <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.placeOfProcessing" clearable placeholder="请输入办理地点" style="border-radius: 0px;height: 34px;"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.placeOfProcessing" clearable placeholder="请输入办理地点" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="办理窗口 " prop="processingWindow" @dblclick.native="judge('办理窗口', 'processingWindow')">
            <el-tooltip placement="top" v-if="ruleForm.processingWindow">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.processingWindow}}</div>
              <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.processingWindow" clearable placeholder="请输入办理窗口"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" :maxlength="100" v-model="ruleForm.processingWindow" clearable placeholder="请输入办理窗口"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <!-- <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="是否支持物流快递" prop="isSupportLogistics" class="item_radio" @dblclick.native="judge('是否支持物流快递', 'isSupportLogistics')">
            <el-radio-group :disabled="watchPic" v-model="ruleForm.isSupportLogistics">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col> -->
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="预约办理 " prop="appointmentProcessing" @dblclick.native="judge('预约办理', 'appointmentProcessing')">
            <el-select allow-create :maxlength="100" filterable :disabled="watchPic" v-model="ruleForm.appointmentProcessing" clearable placeholder="请选择预约办理" >
              <el-option v-for="item in selectList.makeApp"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
            <!-- <div class="demo-input-size">
              <el-input placeholder="请选择时间"></el-input>
              <el-input placeholder="请选择方式" size="medium"></el-input>
            </div> -->
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="数量限制 "  prop="quantitativeRestriction" @dblclick.native="judge('数量限制', 'quantitativeRestriction')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.quantitativeRestriction" clearable placeholder="请输入数量限制"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>

        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="是否有中介服务 " prop="intermediaryService" class="item_radio" @dblclick.native="judge('是否有中介服务', 'intermediaryService')">
            <el-radio-group :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.intermediaryService">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
            <!-- <el-input v-model="ruleForm.intermediaryService" clearable placeholder="请输入中介服务"  ></el-input> -->
          </el-form-item>
        </el-col>
      <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="批前公示 " prop="prePublicationPublicity" @dblclick.native="judge('批前公示', 'prePublicationPublicity')">
            <el-select allow-create filterable :maxlength="10" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.prePublicationPublicity" clearable placeholder="请选择批前公示"  >
              <el-option v-for="item in selectList.befPub"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
       <!-- dbb 原来是12  写成24  美观 -->
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="办理查询 " prop="enquiry" @dblclick.native="judge('办理查询', 'enquiry')">
            <el-tooltip placement="top" v-if="ruleForm.enquiry">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.enquiry}}</div>
              <el-input :maxlength="200" :readonly="watchPic" v-model="ruleForm.enquiry" clearable placeholder="请输入办理查询"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic" v-model="ruleForm.enquiry" clearable placeholder="请输入办理查询"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="常见问题 " prop="commonProblem" @dblclick.native="judge('常见问题', 'commonProblem')">
            <el-tooltip placement="top" v-if="ruleForm.commonProblem">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.commonProblem}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="1000" v-model="ruleForm.commonProblem" clearable placeholder="请输入常见问题"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')"  v-model="ruleForm.commonProblem" clearable placeholder="请输入常见问题"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="设定依据 " prop="settingBasis" class="textareaLabel" @dblclick.native="judge('设定依据', 'settingBasis')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.settingBasis" clearable placeholder="请输入设定依据" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="申请主体权利和义务 " prop="theRightsAndObligationsOfTheApplicant" class="textareaLabel" @dblclick.native="judge('申请主体权利和义务', 'theRightsAndObligationsOfTheApplicant')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.theRightsAndObligationsOfTheApplicant" clearable placeholder="请输入申请主体的权利和义务" type="textarea" style="border-radius: 0px;height: 34px;" ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="受理申请条件 " prop="acceptingConditions" class="textareaLabel" @dblclick.native="judge('受理申请条件', 'acceptingConditions')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.acceptingConditions" clearable placeholder="请输入受理申请条件" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="办理依据 " prop="handlingBasis" class="textareaLabel" @dblclick.native="judge('办理依据', 'handlingBasis')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.handlingBasis" clearable placeholder="请输入办理依据" type="textarea" style="border-radius: 0px;height: 34px"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="行使内容 " prop="exerciseContent" class="textareaLabel" @dblclick.native="judge('行使内容', 'exerciseContent')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.exerciseContent" clearable placeholder="请输入行使内容" type="textarea" style="border-radius: 0px;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="行政复议行政诉讼 " prop="administrativeReconsiderationAdministrativeLitigation" @dblclick.native="judge('行政复议行政诉讼', 'administrativeReconsiderationAdministrativeLitigation')">
            <el-tooltip placement="top" v-if="ruleForm.administrativeReconsiderationAdministrativeLitigation">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.administrativeReconsiderationAdministrativeLitigation}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.administrativeReconsiderationAdministrativeLitigation" clearable placeholder="请输入行政复议行政诉讼" ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.administrativeReconsiderationAdministrativeLitigation" clearable placeholder="请输入行政复议行政诉讼" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="备注说明 " prop="remarks" @dblclick.native="judge('备注说明', 'remarks')">
            <el-tooltip placement="top" v-if="ruleForm.remarks">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.remarks}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.remarks" clearable placeholder="请输入备注说明"   ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.remarks" clearable placeholder="请输入备注说明"   ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="咨询途径 " prop="consultationApproach" @dblclick.native="judge('咨询途径', 'consultationApproach')">
            <el-tooltip placement="top" v-if="ruleForm.consultationApproach">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.consultationApproach}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.consultationApproach" clearable placeholder="请输入咨询途径" ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.consultationApproach" clearable placeholder="请输入咨询途径" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="咨询地址 " prop="advisoryAddress" @dblclick.native="judge('咨询地址', 'advisoryAddress')">
            <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.advisoryAddress" clearable placeholder="请输入咨询地址 示例：'http://www.baidu.com'"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="监督电话 " prop="serviceSupportHotline" @dblclick.native="judge('监督电话', 'serviceSupportHotline')">
			<el-tooltip placement="top" v-if="ruleForm.serviceSupportHotline">
				<div slot="content" style="width:400px;height:100%;" >{{ruleForm.serviceSupportHotline}}</div>
				<el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.serviceSupportHotline" clearable placeholder="请输入监督电话" minlength="12"></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic"  v-model="ruleForm.serviceSupportHotline" clearable placeholder="请输入监督电话" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="投诉途径 " prop="complaintChannels" @dblclick.native="judge('投诉途径', 'complaintChannels')">
            <el-tooltip placement="top" v-if="ruleForm.complaintChannels">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.complaintChannels}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="255" v-model="ruleForm.complaintChannels" clearable placeholder="请输入投诉途径"   ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.complaintChannels" clearable placeholder="请输入投诉途径"   ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px">
          <el-form-item label="投诉地址 " prop="complaintAddress" @dblclick.native="judge('投诉地址', 'complaintAddress')">
            <el-input :readonly="watchPic" :maxlength="100" v-model="ruleForm.complaintAddress" clearable placeholder="请输入投诉地址 示例：'http://www.baidu.com'"></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="事项排序 " prop="itemSequencing" >
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="10" v-model="ruleForm.itemSequencing" disabled ></el-input>
          </el-form-item>
        </el-col> -->
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="材料提交说明 " prop="materialSubmissionStatement" class="textareaLabel" @dblclick.native="judge('材料提交说明', 'materialSubmissionStatement')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.materialSubmissionStatement" clearable placeholder="申请材料应包括：材料名称、材料来源（申请人自备、政府部门核发、中介机构出具等）、材料形式（原件、复印件、电子件等）、数量、规格、报送要求等；需要填报的制式材料应给出获取方式、材料样本、填写说明、填写示例等内容。具体格式：
    1. 申请人自备：
        ①*********（原件、纸质、*份、规格B4 185x260mm、加盖申请单位公章）
        .......
    2、证明材料：
        ①****出具的*****（原件、纸质、*份、其他要求）
        .......
    3、中介机构提供
        ①*********（原件、纸质、*份、其他要求）
        ②*********（复印件*份，表格在***下载）
    4、官网下载的法律法规规定的其他材料
        ①****出具的*****（复印件*份，表格在***下载）"  type="textarea" style="border-radius: 0px ;height: 34px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- dbb  去掉 -->
      <!-- <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="材料适用情形 " prop="materialApplication" @dblclick.native="judge('材料适用情形', 'materialApplication')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="50" v-model="ruleForm.materialApplication" clearable placeholder="请输入材料适用情形" ></el-input>
          </el-form-item>
        </el-col>
      </el-row> -->
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="受理通过 " prop="acceptAndApprove">
            <el-input v-model="ruleForm.acceptAndApprove" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
            <!--不明确-->
          <el-form-item label="时限(受理) " prop="timeLimitForAcceptance" >
            <el-input v-model="ruleForm.timeLimitForAcceptance" disabled></el-input>
          </el-form-item>
          </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="补正或更正 " prop="correctionOrCorrection">
            <el-input v-model="ruleForm.correctionOrCorrection" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="时限(补正或更正)" prop="timeLimitForCorrection">
            <el-input v-model="ruleForm.timeLimitForCorrection" placeholder="参见'现有流程'" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="受理结果 " prop="acceptanceResult" @dblclick.native="judge('受理结果', 'acceptanceResult')">
            <el-tooltip placement="top" v-if="ruleForm.acceptanceResult">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.acceptanceResult}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.acceptanceResult" placeholder="示例：出具回执单" clearable  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.acceptanceResult" placeholder="示例：出具回执单" clearable  ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="补正更正结果 " prop="correctionOfResults" @dblclick.native="judge('补正更正结果', 'correctionOfResults')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.correctionOfResults" placeholder="示例：手机短信通知" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="不予受理 " prop="refuseToAccept" @dblclick.native="judge('不予受理', 'refuseToAccept')">
            <el-tooltip placement="top" v-if="ruleForm.refuseToAccept">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.refuseToAccept}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.refuseToAccept" placeholder="示例：申请事项依法不需要取得行政许可的或申请事项依法不属于本行政机关职权范围的或因数量限制、政策变动等原因停止受理的。" clearable ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.refuseToAccept" placeholder="示例：申请事项依法不需要取得行政许可的或申请事项依法不属于本行政机关职权范围的或因数量限制、政策变动等原因停止受理的。" clearable ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="审查方式 " prop="modeOfExamination">
            <el-input v-model="ruleForm.modeOfExamination" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <!--不明确-->
          <el-form-item label="审查标准 " prop="reviewStandard">
            <el-input v-model="ruleForm.reviewStandard" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="审查结果 " prop="examinationResult">
            <el-input v-model="ruleForm.examinationResult" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="审查时限 " prop="timeLimitRorReview">
            <el-input v-model="ruleForm.timeLimitRorReview" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="决定时限 " prop="decisionTimeLimit">
            <el-input v-model="ruleForm.decisionTimeLimit" disabled  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="法律依据及描述(特殊) " prop="legalBasisAndDescriptionSpecial">
            <el-input v-model="ruleForm.legalBasisAndDescriptionSpecial" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="所需时限(特殊) " prop="requiredTimeLimitSpecial">
            <el-input v-model="ruleForm.requiredTimeLimitSpecial" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="计算法定办结时限内" prop="statutoryCompletionTimeLimitSpecial" class="item_radio">
            <el-input v-model="ruleForm.statutoryCompletionTimeLimitSpecial" disabled></el-input>
            <!-- <el-radio-group v-model="ruleForm.statutoryCompletionTimeLimitSpecial"  >
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group> -->
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="是否收费(特殊) " prop="chargeSpecial" class="item_radio" @dblclick.native="judge('是否收费(特殊)', 'chargeSpecial')">
            <el-radio-group v-model="ruleForm.chargeSpecial" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" @change="isChange">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="收费依据及描述(特殊) " prop="chargeBasisAndDescriptionSpecial" @dblclick.native="judge('收费依据及描述(特殊)', 'chargeBasisAndDescriptionSpecial')">
            <el-tooltip placement="top" v-if="ruleForm.chargeBasisAndDescriptionSpecial">
              <div slot="content" style="width:400px;height:100%;">{{ruleForm.chargeBasisAndDescriptionSpecial}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="2000" v-model="ruleForm.chargeBasisAndDescriptionSpecial" :disabled="isChangeBasis" clearable placeholder="请输入收费依据及描述(特殊环节)" ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.chargeBasisAndDescriptionSpecial" :disabled="isChangeBasis" clearable placeholder="请输入收费依据及描述(特殊环节)" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="结果类型 " prop="resultType" @dblclick.native="judge('结果类型', 'resultType')">
            <el-select class="tag-size" allow-create :collapse-tags="!(watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2'))" filterable multiple :maxlength="20" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.resultType" clearable placeholder="请选择结果类型"  >
              <el-option v-for="item in results"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px">
          <el-form-item label="办理流程图  " prop="processFlowChart"  ref="processFlowChart">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="10" v-model="ruleForm.processFlowChart" disabled></el-input>
            <!-- <ImgUpload v-if="ruleForm.processFlowChart" :watchPic="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :otherPosition="true" :imgHandle="false" :isMini="true" :files="ruleForm.processFlowChart?JSON.parse(ruleForm.processFlowChart).imgs:[]" :imageNum='40' :multiple="true"
              @upload="uploadImg( $event)"></ImgUpload> -->
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -16px" class="input_label">
          <el-form-item label="办理流程说明 " prop="processDescription" class="textareaLabel" @dblclick.native="judge('办理流程说明', 'processDescription')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="1000" v-model="ruleForm.processDescription" clearable placeholder="请输入办理流程说明" type="textarea" style="border-radius: 0px;height: 34.5px;"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
          <!-- 代兵兵  去掉多余 -->
        <!-- <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="是否支持网上支付" prop="supportOnlinePayment" class="item_radio" @dblclick.native="judge('是否支持网上支付', 'supportOnlinePayment')">
            <el-radio-group :disabled="watchPic" v-model="ruleForm.supportOnlinePayment">
              <el-radio label="是"></el-radio>
              <el-radio label="否"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col> -->
        <el-col :span="24" style="margin-top:-16px">
          <el-form-item label="结果名称 " prop="resultName" @dblclick.native="judge('结果名称', 'resultName')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.resultName" clearable placeholder="请输入结果名称"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: -17px" class="flow-map">
          <el-form-item label="结果样本 " prop="resultSample"  ref="resultSample" style="border:0.2px solid #dcdfec ;height:35px;" @dblclick.native="judge('结果样本', 'resultSample')">
            <ImgUpload @webFileFun="webFile($event, 'resultSample')" v-if="ruleForm.resultSample" :webFiles="copyObj?(copyObj.objectDataMap[1]||[]):[]" :watchPic="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :otherPosition="true" :imgHandle="false" :isMini="true" :files="ruleForm.resultSample?JSON.parse(ruleForm.resultSample).imgs:[]" :imageNum='40' :multiple="true"
              @upload="uploadImgs( $event)"></ImgUpload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="年审或年检 " prop="annualExaminationOrAnnualInspection" @dblclick.native="judge('年审或年检', 'annualExaminationOrAnnualInspection')">
            <el-select allow-create :maxlength="10" filterable :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.annualExaminationOrAnnualInspection" clearable placeholder="请选择年审或年检"  >
              <el-option v-for="item in selectList.yearCare"  :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="年审或年检次数 " prop="numberOfAnnualOrAnnualReviews" @dblclick.native="judge('年审或年检次数', 'numberOfAnnualOrAnnualReviews')">
            <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.numberOfAnnualOrAnnualReviews" clearable placeholder="请输入年审或年检次数  示例：'12''"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="决定公开 " prop="decidedToMakePublic" @dblclick.native="judge('决定公开', 'decidedToMakePublic')">
            <el-select allow-create filterable :maxlength="30" :disabled="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.decidedToMakePublic" clearable placeholder="请选择决定公开"  >
              <el-option v-for="item in selectList.deciOpen"  :key="item.id" :label="item.name" :value="item.name"         ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -17px">
          <el-form-item label="送达途径 " prop="serviceRoute" @dblclick.native="judge('送达途径', 'serviceRoute')">
            <el-tooltip placement="top" v-if="ruleForm.serviceRoute">
              <div slot="content" style="width:400px;height:100%;" >{{ruleForm.serviceRoute}}</div>
              <el-input :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" :maxlength="100" v-model="ruleForm.serviceRoute" clearable placeholder="请输入送达途径"  ></el-input>
            </el-tooltip>
            <el-input v-else :readonly="watchPic||(itemInfo.region!=='1'&&itemInfo.region!=='2')" v-model="ruleForm.serviceRoute" clearable placeholder="请输入送达途径"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-row>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="送达人及送达窗口 " prop="deliversUserAndWindow" @dblclick.native="judge('送达人及送达窗口', 'deliversUserAndWindow')">
            <el-input :readonly="watchPic" v-model="ruleForm.deliversUserAndWindow" clearable placeholder="请输入送达人及送达窗口"  ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="margin-top: -16px">
          <el-form-item label="咨询电话 " prop="advisoryPhone" @dblclick.native="judge('咨询电话', 'advisoryPhone')">
            <el-input :readonly="watchPic" v-model="ruleForm.advisoryPhone" clearable placeholder="请输入咨询电话"  ></el-input>
          </el-form-item>
        </el-col>
      </el-row> -->
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
  import JudgeDialog from '@/components/judgeDialog';
  import { validatSpace, validatorSpace } from '@/utils/validate'
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
          this.bsznVersionxz = null
          if(val.businessGuideAdministrativeLicensingOne) {
            // 行政许可
            let obj = Object.assign({},val.businessGuideAdministrativeLicensingOne,val.businessGuideAdministrativeLicensingOnePrivate)
            this.formRegroup(obj)
          } else {
            let obj = Object.assign({},val.businessGuideFilingOne,val.businessGuideFilingOnePrivate)
            this.formRegroup(obj)
          }
        }
      },
      state: function(newVal, oldVal) {
        if(newVal) {
        }
      },
      'itemDetail.id' (val) {
        if(val) {
          this.getSelectShow(val)
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
        this.allState(itemDetail.businessGuideState)
        this.eventType = itemDetail.eventType === 1 ? '行政许可' : '备案类';
        this.ruleForm.majorTypesOfEvents = itemDetail.eventType.toString()
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
      const validateNumber=(rule, value, callback) => {
        var srt=/^[0-9]*$/;
        if (value === '' ||value === null || value === undefined) {
          callback(new Error(' '));
        }else if (!srt.test(value)) {
          callback(new Error(" "));
        }else{
          callback();
        }
      }
      return {
        bsznVersionxz:null,//办事指南version
        isChangeBasis:false,
        accordingUrl:false,
        according:false,
        param: '',
        dialogImageUrl: '',
        dialogVisible: false,
        readonly: true,
        eventState: '未完善',
        eventClass: 'btn-not-perfect',
        eventType: '',
        auditOperationPic: '',
        ruleForm:{
          majorTypesOfEvents: '',
          itemSmallType: '',
          ftaMatters: '',
          transactionDirectory: '',
          basicCoding: '',
          codingImplementation: '',
          itemName: '',
          sourceOfPower: '',
          implementingOrgan: '',
          implementingBody: '',
          serviceObject: '',
          natureOfImplementingSubject: '',
          serviceTopic: '',
          permissionDivision: [],
          detailedPermissionDivision: '',
          typeOfOffice: '',
          exerciseHierarchy: '',
          timeLimitType: '工作日',
          statutoryDeadlines:'',
          statutoryTimeLimitForCompletion: '',
          commitmentTimeLimit: '',
          statementOfCommitmentDeadline: '',
          jointAgency: '',
          applicationOfJointOrganizations: '',
          charge: '',
          chargeSpecial: '',
          statutoryCompletionTimeLimitSpecial:'参见"材料分组"',
          chargingStandard: '',
          methodOfDeliveryOfApplicationMaterials: '',
          chargingBasis: '',
          paymentMethod: [],
          onlineServiceSystem: '',
          webSiteOfOnlineServiceSystem: '',
          concierge: '',
          scopeOfOperation: '',
          declarationAddressOfEnterpriseInvestmentProjects: '不用填写',
          handlingForm: '',
          onlineApplicationForm: '',
          numberOfVisitsToTheFieldHall: '',
          eventStar: '',
          processingTime: '',
          typeOfProcessingTimeOrPlace:'手动填写',
          processingWindow: '',
          addendumToProcessingTimeOrPlace: '',
          appointmentProcessing: '',
          placeOfProcessing: '',
          prePublicationPublicity: '',
          quantitativeRestriction: '',
          enquiry: '',
          intermediaryService: '',
          theRightsAndObligationsOfTheApplicant: '',
          commonProblem: '',
          acceptingConditions: '',
          settingBasis: '',
          administrativeReconsiderationAdministrativeLitigation: '',
          exerciseContent: '',
          remarks: '',
          handlingBasis: '',
          advisoryAddress: '',
          consultationApproach: '',
          complaintChannels: '',
          serviceSupportHotline: '',
          itemSequencing: '不用填写',
          complaintAddress: '',
          materialSubmissionStatement: '参见"材料分组"',
          materialApplication: '',
          acceptAndApprove: '参见"现有流程"',
          correctionOrCorrection: '参见"现有流程"',
          timeLimitForAcceptance: '参见"现有流程"',
          timeLimitForCorrection: '参见"现有流程"',
          correctionOfResults: '',
          modeOfExamination: '不用填写',
          refuseToAccept: '',
          examinationResult: '不用填写',
          reviewStandard: '不用填写',
          decisionTimeLimit: '参见"现有流程"',
          timeLimitRorReview: '不用填写',
          requiredTimeLimitSpecial:'参见"现有流程"',
          legalBasisAndDescriptionSpecial: '参见"现有流程"',
          chargeBasisAndDescriptionSpecial: '',
          // legalAdvisetime: '',
          resultType: [],
          processFlowChart: '参见"办理流程图"',
          resultName: '',
          processDescription: '',
          annualExaminationOrAnnualInspection: '',
          resultSample: '',
          decidedToMakePublic: '',
          numberOfAnnualOrAnnualReviews: '',
          serviceRoute: '',
          sourcesChannelDescription: '',//来源渠道说明
          sources: '',  // 来源渠道
          materialType: '', //材料类型
          acceptanceResult: '',
          deliversUserAndWindow: '',
          advisoryPhone: '',
          supportOnlinePayment: '',
          isSupportLogistics: ''
        },
        results: ['许可证','执照','其他许可证书','资格证','资质证','其他合格证书','行政机关的批准文件','证明文件','加贴标签','加盖检验印章','加盖检测印章','加盖检疫印章'],
        options: ['0 次', '1 次', '2 次', '3 次', '全程网办'],
        method:['微信','支付宝','刷卡'],
        timetype:[{
          value: '1',
          label:'自然日'
        },{
          value:'2',
          label:'工作日'
        }],
        rules:{
          majorTypesOfEvents: [
            { required: true, message: ' ', trigger: 'blur' }
          ],
          itemSmallType: [
            { required: true, message: ' ', trigger: 'change' }
          ],
          ftaMatters: [
            { required: true, message: ' ', trigger: 'blur' }
          ],
          transactionDirectory: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          acceptanceResult: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          itemName: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          sourceOfPower: [
            { required: true, validator:checkName, trigger: 'change' }
          ],
          implementingOrgan: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          implementingBody: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          //服务对象
          serviceObject: [
            // { required: true, validator: checkobject, trigger: 'change' }
          ],
          natureOfImplementingSubject: [
            { required: true, validator:checkName, trigger: 'change' }
          ],
          //服务对象
          serviceTopic: [
            { required: true, message: ' ', trigger: 'change' }
          ],
          permissionDivision: [
            { required: true, validator: checkobject, trigger: 'change' }
          ],
          advisoryAddress: [{
            required: true, validator:checkName, trigger: 'blur'
            // validator: validateUrl,
          }],
          typeOfOffice: [
            { required: true, validator:checkName, trigger: 'change' }
          ],
          exerciseHierarchy: [
            // { required: true, validator: checkobject, trigger: 'change' }
          ],
          complaintAddress: [{
            required: true, validator:checkName , trigger: 'blur'
            // validator: validateUrl
          }],
          statutoryTimeLimitForCompletion: [
            // { required: true, message: ' ', trigger: 'blur' }
          ],
          charge: [
            { required: true, message: ' ', trigger: 'blur' }
          ],
          chargingStandard: [
            // { required: true, message: ' ', trigger: 'blur' }
          ],
          chargingBasis: [
            // { required: true, message: ' ', trigger: 'blur' }
          ],
          paymentMethod: [
            // { required: true, message: ' ', trigger: 'blur' }
          ],

          webSiteOfOnlineServiceSystem: [
            // { required: true, validator: validateUrl, trigger: 'blur' }
          ],
          onlineServiceSystem: [
            { required: true, message: ' ', trigger: 'blur' }
          ],
          concierge: [
            { required: true, validator:checkName, trigger: 'change' }
          ],
          declarationAddressOfEnterpriseInvestmentProjects: [
            // { required: true, message: ' ', trigger: 'blur' }
          ],
          handlingForm: [
            { required: true, validator:checkName, trigger: 'change' }
          ],
          onlineApplicationForm:[
            { required: true, validator:checkName, trigger: 'change' }
          ],
          numberOfVisitsToTheFieldHall: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          processingTime: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          placeOfProcessing: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          appointmentProcessing: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          quantitativeRestriction: [
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
          settingBasis: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          exerciseContent: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          administrativeReconsiderationAdministrativeLitigation: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          serviceSupportHotline: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          materialSubmissionStatement:[
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          correctionOfResults: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          processFlowChart:[
            // { required: true, message: ' ', trigger: 'blur' }
          ],
          resultName: [
            { required: true, validator:checkName, trigger: 'blur' }
          ],
          resultSample:[
            // { required: true, message: ' ', trigger: 'blur' }
          ],
          annualExaminationOrAnnualInspection: [
            { required: true, validator:checkName, trigger: 'change' }
          ],
          decidedToMakePublic:[
            { required: true, validator:checkName, trigger: 'change' }
          ],
          numberOfAnnualOrAnnualReviews:[
            // { required: true, validator: validateNumber, trigger: 'blur' }
            { required: true, validator:checkName, trigger: 'blur' }
          ],
        },
        selectList: {
          bigPro:[],
          samllPro: [],
          yearCare: [],
          makeApp: [],
          entr: [],
          deciOpen: [],
          matStar: [],
          exerHier: [],
          handForm: [],
          materType: [],
          mainPro: [],
          onlineApp: [],
          purDiv: [],
          befPub: [],
          scopeGen: [],
          serObj: [],
          sour: [],
          methodMater: [],
          servTheme: [],
          typeOff: [],
          powSour: [],
          list:[],
        },
        // 评审
        judgeShow: false,
        judgeForm: {},
      }
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
      // 导入的图片
      webFile (e, field) {
        this.ruleForm[field] = e
      },
      judge (propertyName, property) {
        let endTime = this.itemInfo.examineEndTime
        if(endTime&&endTime>=new Date().getTime()) {
          this.judgeShow = true
          this.judgeForm = {
            lineNumber: null,
            id: this.itemInfo.id,
            propertyName,
            sheetNum: 1,
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
      isChange(val){
        if(val === '否'){
         this.isChangeBasis = true
        }else{
          this.isChangeBasis = false
        }
      },
      uploadImgs (imgs) {
        this.ruleForm.resultSample = imgs;
      },
      // uploadImg (img) {
      //   this.ruleForm.processFlowChart=img;
      // },
      uploadimgs (img) {
        this.ruleForm.addendumToProcessingTimeOrPlace= img
      },
      getSelectShow(id){
        if(this.itemInfo.eventType === 1){
          this.getAdministrativeShow(id);
        }else{
          this.getRecordShow(id);
        }
      },
     //备案类
      async getRecordShow(id){
        this.ruleForm.addendumToProcessingTimeOrPlace = ''
        this.ruleForm.resultSample = ''
        this.bsznVersionxz = null
        let res = await administrativeService.getRecordShow(id)
        if(res.code === 0){
          // this.ruleForm.majorTypesOfEvents = this.itemInfo.eventType.toString()
          if(res.obj) {
            let obj = Object.assign({},res.obj.businessGuideFilingOne,res.obj.businessGuideFilingOnePrivate)
            this.formRegroup(obj)
          } else {
            this.$refs.ruleForm.resetFields()
            this.ruleForm.majorTypesOfEvents = this.itemInfo.eventType.toString()
            // this.ruleForm.processFlowChart = '{"imgs":[]}'
            this.ruleForm.resultSample = '{"imgs":[]}'
            this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
          }
        } else {
          this.$refs.ruleForm.resetFields()
          // this.ruleForm.processFlowChart = '{"imgs":[]}'
          this.ruleForm.resultSample = '{"imgs":[]}'
          this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
        }
      },
       // 行政许可类
      async getAdministrativeShow(id){
        this.ruleForm.addendumToProcessingTimeOrPlace = ''
        this.ruleForm.resultSample = ''
        this.bsznVersionxz = null
        let res = await administrativeService.getAdministrativeShow(id);
        if(res.code === 0){
          // this.ruleForm.majorTypesOfEvents = this.itemInfo.eventType.toString()
          if(res.obj) {
            let obj = Object.assign({},res.obj.businessGuideAdministrativeLicensingOne,res.obj.businessGuideAdministrativeLicensingOnePrivate)
            this.formRegroup(obj)
          } else {
            this.$refs.ruleForm.resetFields()
            this.ruleForm.majorTypesOfEvents = this.itemInfo.eventType.toString()
            // this.ruleForm.processFlowChart = '{"imgs":[]}'
            this.ruleForm.resultSample = '{"imgs":[]}'
            this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
          }
        } else {
          this.$refs.ruleForm.resetFields()
          // this.ruleForm.processFlowChart = '{"imgs":[]}'
          this.ruleForm.resultSample = '{"imgs":[]}'
          this.ruleForm.addendumToProcessingTimeOrPlace = '{"imgs":[]}'
        }
      },
      formRegroup (obj) {
        // obj.exerciseHierarchy = obj.exerciseHierarchy?obj.exerciseHierarchy.split(','):[];
        obj.permissionDivision = obj.permissionDivision?obj.permissionDivision.split(','):[];
        obj.methodOfDeliveryOfApplicationMaterials = obj.methodOfDeliveryOfApplicationMaterials?obj.methodOfDeliveryOfApplicationMaterials.split(','):[];
        obj.resultType = obj.resultType?obj.resultType.split(','):[];
        obj.paymentMethod= obj.paymentMethod?obj.paymentMethod.split(','):[]
        this.according = obj.charge === '否';
        this.accordingUrl =obj.onlineServiceSystem === '否';
        this.isChangeBasis =obj.chargeSpecial === '否' ;
        this.ruleForm = obj
        // this.ruleForm.processFlowChart = this.ruleForm.processFlowChart || '{"imgs":[]}'
        this.ruleForm.resultSample = this.ruleForm.resultSample || '{"imgs":[]}'
        this.ruleForm.addendumToProcessingTimeOrPlace = this.ruleForm.addendumToProcessingTimeOrPlace || '{"imgs":[]}'
        this.bsznVersionxz = obj.version;
      },
      //备案
      recordFiling(state) {
        if(this.itemInfo.region === '1'||this.itemInfo.region === '2'){
          this.recordSave(state)
        } else {
          this.recordSaveFiling(state)
        }
      },
      async recordSave(state){
        let param = {
          businessGuideFilingOne: {
            ...this.ruleForm,
            eventId:this.itemInfo.id,
            // exerciseHierarchy:this.ruleForm.exerciseHierarchy.toString(),
            permissionDivision: this.ruleForm.permissionDivision.toString(),
            resultType: this.ruleForm.resultType.toString(),
            paymentMethod: this.ruleForm.paymentMethod.toString(),
            methodOfDeliveryOfApplicationMaterials: this.ruleForm.methodOfDeliveryOfApplicationMaterials.join(','),
            state
          },
          businessGuideFilingOnePrivate: {
            ...this.ruleForm,
            resultType: this.ruleForm.resultType.toString(),
            eventId:this.itemInfo.id,
            departmentalMattersId: this.itemInfo.id,
            paymentMethod: this.ruleForm.paymentMethod.toString(),
            state,
            version:this.bsznVersionxz
          }
        }
        let res = await administrativeService.recordSave(param);
        if(res.code === 0){
          this.itemInfo.businessGuideState = state
          localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
          this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
          this.$message.success(res.msg);
          this.bsznVersionxz=null;
          this.getSelectShow(this.itemInfo.id)
        }else{
          this.$message.error(res.msg);
        }
      },
      saveFiling(state){
        if(this.itemInfo.region === '1'||this.itemInfo.region === '2'){
          this.administrativeSave(state)
        } else {
          this.administrativeSaveFiilng(state)
        }
      },
      //行政许可
      async administrativeSave(state){
        let param = {
          businessGuideAdministrativeLicensingOne: {
            ...this.ruleForm,
            eventId:this.itemInfo.id,
            resultType: this.ruleForm.resultType.toString(),
            // exerciseHierarchy:this.ruleForm.exerciseHierarchy.toString(),
            permissionDivision: this.ruleForm.permissionDivision.toString(),
            methodOfDeliveryOfApplicationMaterials : this.ruleForm.methodOfDeliveryOfApplicationMaterials.toString(),
            paymentMethod: this.ruleForm.paymentMethod.toString(),
            state
          },
          businessGuideAdministrativeLicensingOnePrivate: {
            ...this.ruleForm,
            eventId:this.itemInfo.id,
            resultType: this.ruleForm.resultType.join(','),
            departmentalMattersId: this.itemInfo.id,
            paymentMethod: this.ruleForm.paymentMethod.toString(),
            state,
            version:this.bsznVersionxz
          }
        }
        let res = await administrativeService.administrativeSave(param);
        if(res.code === 0){
          this.itemInfo.businessGuideState = state
          localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
          this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
          this.$message.success(res.msg);
          this.bsznVersionxz=null;
          this.getSelectShow(this.itemInfo.id)
        }else{
          this.$message.error(res.msg);
        }
      },
      formSubmit(){
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            if(this.ruleForm.majorTypesOfEvents === '1'){
              this.saveFiling(2);
            }else{
              this.recordFiling(2);
            }
          }else{
            if(this.ruleForm.majorTypesOfEvents === '1'){
              this.saveFiling(1);
            }else{
              this.recordFiling(1);
            }
          }
        })
      },
      //行政许可市级以下
      async administrativeSaveFiilng(state){
        let param = {
          ...this.ruleForm,
          eventId:this.itemInfo.id,
          departmentalMattersId: this.itemInfo.id,   //部门事项id
          resultType: this.ruleForm.resultType.toString(),
          paymentMethod: this.ruleForm.paymentMethod.toString(),
          state,
          version:this.bsznVersionxz,
        };

        let res = await administrativeService.administrativeSaveFiilng(param)
        if(res.code === 0){
          this.itemInfo.businessGuideState = state
          localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
          this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
          this.$message.success(res.msg);
          this.bsznVersionxz=null;
          this.getSelectShow(this.itemInfo.id)
        }else{
          this.$message.error(res.msg);
        }
      },
      //备案类市级以下
      async recordSaveFiling(){
        let param = {
          ...this.ruleForm,
          departmentalMattersId: this.itemInfo.id,   //部门事项id
          resultType: this.ruleForm.resultType.toString(),
          paymentMethod: this.ruleForm.paymentMethod.toString(),
          state,
          version:this.bsznVersionxz
        };
        let res = await administrativeService.recordSaveFiling(param)
        if(res.code === 0){
          this.itemInfo.businessGuideState = state
          localStorage.setItem('itemDetail', JSON.stringify(this.itemInfo))
          this.SET_ITEMDETAIL({...this.itemInfo, businessGuideState: state})
          this.$message.success(res.msg);
          this.bsznVersionxz=null;
          this.getSelectShow(this.itemInfo.id)
        }else{
          this.$message.error(res.msg);
        }
      },
      // 获取其他list内容的接口
      async getAllOtherList () {
        let res = await administrativeService.getAll(this.eventType.includes('行政许可')?1:3)
        this.SET_SEARCHOBJ(null)
        if(res.code === 0){
          this.SET_SEARCHOBJ(res.obj)
          // 年审或年检
          this.selectList.yearCare = res.obj.annualExaminationOrAnnualInspection || []
          // 预约办理
          this.selectList.makeApp = res.obj.appointmentProcessing || []
          // 委托代办
          this.selectList.entr = res.obj.concierge || []
          // 决定公开
          this.selectList.deciOpen = res.obj.decidedToMakePublic || []
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
          // 实施主体性质
          this.selectList.mainPro = res.obj.natureOfImplementingSubject || []
          // 网上申请形式
          this.selectList.onlineApp = res.obj.onlineApplicationForm || []
          // 权限划分
          this.selectList.purDiv = res.obj.permissionDivision || []
          // 批前公示
          this.selectList.befPub = res.obj.prePublicationPublicity || []
          // 通办范围
          this.selectList.scopeGen = res.obj.scopeOfOperation || []
          // 服务主题
          this.selectList.servTheme = res.obj.serviceTopic || []
          // 权力来源
          this.selectList.powSour = res.obj.sourceOfPower || []
          // 办件类型
          this.selectList.typeOff = res.obj.typeOfOffice || []
          // // 服务对象
          // this.selectList.serObj = res.obj.serviceObject || []
          // 办理形式
          this.selectList.handForm = res.obj.handlingForm || []
        }
      }
    },
    beforeMount () {
      this.getAllOtherList()
      this.getSelectShow(this.itemInfo.id)
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
    .textareaInput {
     /deep/ .el-textarea__inner{
        height:35px !important;
        border-radius:0px !important;
      }
    }
	}
  .item_radio{
    border: 0.2px solid #dcdfec
  }
  .tag-size /deep/ .el-tag{
    font-size: 14px;
    color: #606266;
  }
  .event-state{
    height:27px
  }
</style>
