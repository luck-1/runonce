package com.runonce.service.impl.eventpublic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.ApplicationTypeFieldDao;
import com.runonce.dao.eventversionone.ApplicationTypeValueDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.enums.IResultEnumRole;
import com.runonce.enums.SheetNumEnum;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import com.runonce.model.eventpublic.BusinessFileRelation;
import com.runonce.model.eventversionone.ApplicationTypeField;
import com.runonce.model.eventversionone.ApplicationTypeValue;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.MindeRoot;
import com.runonce.model.http.MinderNode;
import com.runonce.model.http.MinderNodeData;
import com.runonce.service.eventversionone.VersionmanagementService;
import com.runonce.service.impl.eventversionone.DepartmentalMattersServiceImpl;
import com.runonce.util.SysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.runonce.service.eventpublic.MinderDataService;
import com.runonce.dao.eventpublic.MinderDataDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventpublic.MinderData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xuxueli on '2018-12-12 19:56:38'.
 */
@Service
public class MinderDataServiceImpl extends AbstractService<MinderData> implements MinderDataService {

    @Resource
    private MinderDataDao minderDataDao;

    @Resource
    private ApplicationTypeFieldDao applicationTypeFieldDao;

    @Resource
    private ApplicationTypeValueDao applicationTypeValueDao;

    @Resource
    private BusinessFileRelationServiceImpl businessFileRelationService;

    @Autowired
    private DepartmentalMattersServiceImpl departmentalMattersService;

    @Autowired
    private VersionmanagementService versionmanagementService;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    private String eventId;

    // 二级申请类型
//	private List<ApplicationTypeField> secondLevelFields;

    // 最下级申请类型
//	private List<ApplicationTypeField> LeafFields;

    // 现有二级节点
//	private MinderNode currentSecondNode;
    // 数据最终node
//	private MinderNode finalMinderNode;

    // 临时节点存储list
//	private List<MinderNode> tempNodeList;
    // 上一个二级节点的节点list
//	private List<MinderNode> lastNodeList;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode saveInfo(MinderDataReq minderDataReq, HttpServletRequest request) {
        MinderData model = minderDataReq.getMinderData();

        // 事项存在校验
        DepartmentalMatters event = departmentalMattersService.findById(model.getEventId());
        if (event == null) {
            return new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }
        Condition mindCondition = new Condition(MinderData.class);
        Example.Criteria mindCriteria = mindCondition.createCriteria();
        mindCriteria.andEqualTo("eventId", model.getEventId());
        mindCriteria.andEqualTo("minderType", model.getType());
        mapper.deleteByCondition(mindCondition);
        if (model.getType() == 1) {
            // 导图数据解析，并关联材料
            String dataString = model.getMinderData();
            System.out.println(dataString);
            JSONObject jsonobject = JSONObject.parseObject(dataString);
            MindeRoot dataObj = (MindeRoot) JSONObject.toJavaObject(jsonobject, MindeRoot.class);
            eventId = model.getEventId();

            /**
             *  更新申请类型字段记录以及申请类型值记录
             */
            // 删除原有的
            Condition fieldCondition = new Condition(ApplicationTypeField.class);
            Example.Criteria fieldCriteria = fieldCondition.createCriteria();
            fieldCriteria.andEqualTo("eventId", model.getEventId());
            List<ApplicationTypeField> fields = applicationTypeFieldDao.selectByCondition(fieldCondition);

            if (fields != null && fields.size() > 0) {
                for (ApplicationTypeField field : fields) {
                    Condition valueCondition = new Condition(ApplicationTypeValue.class);
                    Example.Criteria valueCriteria = valueCondition.createCriteria();
                    valueCriteria.andEqualTo("fieldId", field.getId());
                    applicationTypeValueDao.deleteByCondition(valueCondition);
                }
            }
            applicationTypeFieldDao.deleteByCondition(fieldCondition);

            // 新增申请类型记录以及关联材料记录
            parseData(dataObj.getRoot(), "-1", false, 1, 1,"1");
        }


        // 导图数据新增
        model.setId(SysUtil.getUUID());
        model.setMinderType(model.getType());
        mapper.insertSelective(model);

        // 思维导图保存
        BusinessFileRelation file = new BusinessFileRelation();
        file.setId(SysUtil.getUUID());
        file.setBusinessId(model.getEventId());
        // 申请类型状态更新
        String msg = "";
        Integer sheetNum = null;
        if (model.getType() == 1) {
            file.setBusinessType(3);
            event.setApplicationTypeState(2);
            msg = "申请类型";
            sheetNum = SheetNumEnum.APPLICATION_TYPE_FIELD.getId();
        } else if (model.getType() == 2) {
            file.setBusinessType(4);
            event.setBusinessSituationState(2);
            msg = "办事情形";
            sheetNum = SheetNumEnum.WORK_SITUATION.getId();
        }
        file.setFilePath(model.getPicPath());

        //乐观锁
        Integer versionNum = versionmanagementService.getSheetVersion(event.getId(), sheetNum);
        if (!versionNum.equals(minderDataReq.getVersion())) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
        }
        //乐观锁保存
        int count = versionmanagementService.updateSheetVersion(event.getId(), sheetNum);
        if (count == 0) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
        }
        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectPidById(event.getId());
        //更新父部门状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);
        businessFileRelationService.save(file);
        departmentalMattersService.update(event);

        return new ReturnCode.Builder().success().msg(msg + "导图保存成功").build();
    }

    @Override
    public MinderDataReq selectByEventId(String eventId, Integer type) {

        MinderDataReq minderDataReq = new MinderDataReq();
        Integer sheetNum = null;
        if (type == 1) {

            sheetNum = SheetNumEnum.APPLICATION_TYPE_FIELD.getId();
        } else if (type == 2) {

            sheetNum = SheetNumEnum.WORK_SITUATION.getId();
        }
        Integer versionNum = versionmanagementService.getSheetVersion(eventId, sheetNum);
        if (versionNum == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }
        minderDataReq.setVersion(versionNum);

        Condition condition = new Condition(MinderData.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("eventId", eventId);
        criteria.andEqualTo("minderType", type);
        List<MinderData> minderDataList = findByCondition(condition);
        if (minderDataList == null || minderDataList.size() == 0) {
            MinderData minderData=new MinderData();

            minderData.setMinderData("{\"root\": {\"data\": {\"id\": \"brxk7c59q740\",\"created\": 1549963495904,\"text\": \"事项\"},\"children\": []},\"template\": \"right\",\"theme\": \"fresh-blue\",\"version\": \"1.4.43\"}");
               // minderData.setMinderData("{}");
          //  minderData.setMinderData("{id:null, minderType:null, minderName:null, eventId:null, minderData:null, picPath:null, type:null}");
            minderDataReq.setMinderData(minderData);
            return minderDataReq;
        }
//		MinderData minderData = findBy("eventId",eventId);
        List<BusinessFileRelation> businessFileRelationList = null;
        if (type == 1) {
            Condition condition1 = new Condition(BusinessFileRelation.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andEqualTo("businessId", eventId);
            criteria1.andEqualTo("businessType", 3);
            businessFileRelationList = businessFileRelationService.findByCondition(condition1);
//			businessFileRelation = businessFileRelationService.findBy("businessId",eventId);
        } else if (type == 2) {
            Condition condition1 = new Condition(BusinessFileRelation.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andEqualTo("businessId", eventId);
            criteria1.andEqualTo("businessType", 4);
            businessFileRelationList = businessFileRelationService.findByCondition(condition1);
        }
        MinderData minderData = minderDataList.get(0);
        if (businessFileRelationList != null && businessFileRelationList.size() > 0) {
            minderData.setPicPath(businessFileRelationList.get(0).getFilePath());
        }
//        MinderDataReq minderDataReq = new MinderDataReq();

        minderDataReq.setMinderData(minderData);


        return minderDataReq;
    }

    @Transactional(rollbackFor = Exception.class)
    public void parseData(MinderNode node, String parentId, boolean isRelatedMaterials, int sort, int level,String fieldType) {
        if (StringUtils.isEmpty(node.getData().getLeafNode())) { // 不为叶子结点
            if (!isRelatedMaterials) {// 不为关联材料
                ApplicationTypeField field = new ApplicationTypeField();
                field.setId(SysUtil.getUUID());
                field.setEventId(eventId);
                field.setFieldName(node.getData().getText());
                field.setParentId(parentId);
                if(fieldType!=null&&"1".equals(fieldType)){
                    field.setFieldType("0");
                }else{
                    field.setFieldType("1");
                }
                field.setSortNumber(sort);
                field.setLevel(level);
//				if(node.getChildren() == null || node.getChildren().size() == 0){
//					field.setLeafNode(true);
//				}else{
//					field.setLeafNode(false);
//				}
                field.setLeafNode(false);
                applicationTypeFieldDao.insert(field);
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    for (int i = 0; i < node.getChildren().size(); i++) {
                        parseData(node.getChildren().get(i), field.getId(), false, i + 1, level + 1,field.getFieldType());
                    }
                }
            } else {
                ApplicationTypeValue value = new ApplicationTypeValue();
                value.setId(SysUtil.getUUID());
                value.setFieldId(parentId);
//				value.setMateriaId(node.getData().getId());// 暂定dataId为所关联材料id
                value.setMateriaId(node.getData().getMaterialId());
                value.setValue(true);
                applicationTypeValueDao.insert(value);
            }
        } else if (node.getData().getLeafNode().equals("leafNode")) {
            ApplicationTypeField field = new ApplicationTypeField();
            field.setId(SysUtil.getUUID());
            field.setEventId(eventId);
            field.setFieldName(node.getData().getText());
            field.setLeafNode(true);
            field.setFieldType(node.getData().getFieldType());
            field.setParentId(parentId);
            field.setSortNumber(sort);
            field.setLevel(level);
            applicationTypeFieldDao.insert(field);
            if (node.getChildren() != null && node.getChildren().size() > 0) {
                for (MinderNode item : node.getChildren()) {
                    parseData(item, field.getId(), true, 1, level + 1,field.getFieldType());
                }
            }
        }
    }

    /**
     * 只查一次数据库
     * 通过事项id查询申请类型并生成办事情形导图数据
     *
     * @param eventId 事项id
     * @return
     */
    @Override
    public String getWorkSitutionData(String eventId) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        // 获取所有申请类型
        Condition condition = new Condition(ApplicationTypeField.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("eventId", eventId);
        condition.setOrderByClause("sortNumber desc");

        //查询申请类型得所有字段
        List<ApplicationTypeField> fieldList = applicationTypeFieldDao.selectByCondition(condition);

        if (fieldList == null || fieldList.size() == 0) {
            return null;
        }
        //  throw new CustomException(new ReturnCode.Builder().failed().msg("申请类型不符合办事情形生成条件").build());

        //获取树顶节点
        List<ApplicationTypeField> applicationTypeFieldSon = fieldList.stream().filter(applicationTypeField -> "-1".equals(applicationTypeField.getParentId())).collect(Collectors.toList());

        //记录数顶节点名称
        String topName = "";
        if (applicationTypeFieldSon != null && applicationTypeFieldSon.size() > 0) {
            topName = applicationTypeFieldSon.get(0).getFieldName();
        }

        //将List转为树结构
        getMinderDataNode(fieldList, applicationTypeFieldSon);

        //获取所有得二级树
     /*   List<ApplicationTypeField> cc = new ArrayList<>();
        if (applicationTypeFieldSon != null && applicationTypeFieldSon.size() > 0 && applicationTypeFieldSon.get(0).getApplicationTypeFields() != null && applicationTypeFieldSon.get(0).getApplicationTypeFields().size() > 0) {
            for (ApplicationTypeField aa :
                    applicationTypeFieldSon.get(0).getApplicationTypeFields()) {
                cc.add(aa);
            }
        }*/

        List<ApplicationTypeField> curr = new ArrayList<>();

        curr = findSamllTree(curr, applicationTypeFieldSon);
        // makeCzTree(curr, applicationTypeFieldSon);

        //ApplicationTypeField树结构向 minderNode树结构转换
        List<MinderNode> minderNodes = new ArrayList<>();
        if (curr != null && curr.size() > 0) {
            getNodeTree(curr, minderNodes);
        } else {
            throw new CustomException(new ReturnCode.Builder().failed().msg("申请类型不符合办事情形生成条件").build());
        }

        //链接树根
        MinderNode minderNode = new MinderNode();
        MinderNodeData m = new MinderNodeData();
        m.setText(topName);
        m.setId(SysUtil.getUUID());
        minderNode.setData(m);
        minderNode.setChildren(minderNodes);
        minderNode.setData(m);

        //制作返回数据格式
        MindeRoot root = new MindeRoot();
        root.setRoot(minderNode);
        root.setTemplate("right");
        root.setTheme("fresh-blue");
        root.setVersion("1.4.43");
        String jsonString = JSON.toJSONString(root, SerializerFeature.DisableCircularReferenceDetect);
        return jsonString;
    }


    /**
     * 将另一个树链接到当前树的根节点
     *
     * @param cruuNode
     * @param tree
     */
    public void makeTree(List<ApplicationTypeField> cruuNode, List<ApplicationTypeField> tree) {
        if (cruuNode != null && cruuNode.size() > 0) {
            for (ApplicationTypeField a :
                    cruuNode) {
                if (a != null) {
                    if (a.getLeafNode()) {
                        a.setApplicationTypeFields(tree);
                    } else {
                        makeTree(a.getApplicationTypeFields(), tree);
                    }
                }
            }
        }
    }


    /**
     * 递归
     * 将List转换为树结构
     *
     * @param fieldList
     * @param currList
     */
    public void getMinderDataNode(List<ApplicationTypeField> fieldList, List<ApplicationTypeField> currList) {
        if (currList != null && currList.size() > 0) {
            for (ApplicationTypeField curr :
                    currList) {
                if (curr != null) {
                    List<ApplicationTypeField> applicationTypeFieldSon = fieldList.stream().filter(applicationTypeField -> curr.getId().equals(applicationTypeField.getParentId())).collect(Collectors.toList());
                    curr.setApplicationTypeFields(applicationTypeFieldSon);
                    getMinderDataNode(fieldList, applicationTypeFieldSon);
                }
            }
        }
    }


    /**
     * 递归方法
     * ApplicationTypeField树结构向 minderNode树结构转换
     *
     * @param applicationTypeField
     * @param minderNodeList
     */
    public void getNodeTree(List<ApplicationTypeField> applicationTypeField, List<MinderNode> minderNodeList) {
        if (applicationTypeField != null && applicationTypeField.size() > 0) {
            for (int i = 0; i < applicationTypeField.size(); i++) {
                ApplicationTypeField a = applicationTypeField.get(i);
                MinderNode minderNode = new MinderNode();
                MinderNodeData minderNodeData = new MinderNodeData();
                minderNodeData.setId(a.getId());
                minderNodeData.setText(a.getFieldName());
                minderNode.setData(minderNodeData);
                minderNode.setChildren(new ArrayList<>());
                getNodeTree(a.getApplicationTypeFields(), minderNode.getChildren());
                minderNodeList.add(minderNode);
            }
        }
    }


    //制造从组树
    public List<ApplicationTypeField> makeCzTree(List<ApplicationTypeField> curr, List<ApplicationTypeField> cc) {
        //原本的树
        //循环将二级树  倒序从最后一个链接到自一个
        // List<ApplicationTypeField> curr = new ArrayList<>();
        if (cc != null && cc.size() > 0) {
            //处理树只有一个二级节点的情况
            if (cc.size() == 1) {
                curr = cc.get(0).getApplicationTypeFields();
            } else {
                List<ApplicationTypeField> tree = new ArrayList<>();
                for (int i = 0; i < cc.size(); i++) {
                    if (i + 1 == cc.size()) {
                        break;
                    }
                    if (i == 0) {
                        tree = cc.get(i).getApplicationTypeFields();
                    }
                    curr = cc.get(i + 1).getApplicationTypeFields();
                    makeTree(curr, tree);
                    if (i != cc.size()) {
                        tree = curr;
                    }
                }
            }
        } else {
             throw new CustomException(new ReturnCode.Builder().failed().msg("申请类型不符合办事情形生成条件").build());
        }

        return curr;
    }


    //  递归找小树
    public List<ApplicationTypeField> findSamllTree(List<ApplicationTypeField> curr, List<ApplicationTypeField> cc) {

        if (cc != null && cc.size() > 0) {
            for (ApplicationTypeField ss : cc) {
                if (ss != null) {
                    if (ss.getApplicationTypeFields() != null && ss.getApplicationTypeFields().size() > 0) {
                        if ("1".equals(ss.getApplicationTypeFields().get(0).getFieldType())) {
                            cc = ss.getApplicationTypeFields();
                            curr = makeCzTree(curr, cc);
                            if (curr != null && curr.size() > 0) {
                                ss.setApplicationTypeFields(curr);
                                for (ApplicationTypeField a : curr) {
                                    List<ApplicationTypeField> ccc = new ArrayList<>();
                                    ccc.add(a);
                                    findSamllTree(new ArrayList<ApplicationTypeField>(), ccc);
                                }
                            }
                        } else {
                            if (cc != null && cc.size() > 0) {
                                for (ApplicationTypeField a : cc) {
                                     findSamllTree(new ArrayList<ApplicationTypeField>(), a.getApplicationTypeFields());
                                }
                            }
                        }
                    }
                }
            }
        }
        return curr;
    }
}
