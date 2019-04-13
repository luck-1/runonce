package com.runonce.service.impl.eventversionone;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.*;
import com.runonce.dao.eventversiontwo.QuantitativeRenderingDao;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.dao.system.mapper.UserMapper;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.Paging;
import com.runonce.httpbean.assets.reqbean.*;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventpublic.BusinessFileRelation;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.eventversionone.*;
import com.runonce.model.eventversiontwo.QuantitativeRendering;
import com.runonce.model.http.*;
import com.runonce.model.system.Department;
import com.runonce.model.system.Role;
import com.runonce.model.system.User;
import com.runonce.service.AbstractService;
import com.runonce.service.eventpublic.EventsTableService;
import com.runonce.service.eventpublic.MinderDataService;
import com.runonce.service.eventversionone.*;
import com.runonce.service.eventversiontwo.QuantitativeRenderingService;
import com.runonce.service.impl.eventpublic.BusinessFileRelationServiceImpl;
import com.runonce.service.system.DepartmentService;
import com.runonce.service.system.UserService;
import com.runonce.service.system.impl.DepartmentServiceImpl;
import com.runonce.util.OssClientUtil;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import com.runonce.util.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import com.runonce.dao.system.mapper.DepartmentMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author swq
 * 部门事项
 * <p>
 * Created by xuxueli on '2018-12-12 16:38:30'.
 */
@Service
public class DepartmentalMattersServiceImpl extends AbstractService<DepartmentalMatters> implements DepartmentalMattersService {

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private DepartmentService departmentService;

    @Autowired
    private EventsTableService eventsTableService;

    @Resource
    private RevisionHistoryDao revisionhistoryDao;

    @Resource
    private RevisionHistoryService revisionHistoryService;

    /**
     * 行政许可
     */
    @Resource
    private BusinessGuideAdministrativeLicensingOneDao businessGuideAdministrativeLicensingOneDao;
    /**
     * 备案类
     */
    @Resource
    private BusinessGuideFilingOneDao businessGuideFilingOneDao;

    /**
     * 量化呈现
     */
    @Resource
    private QuantitativeRenderingDao quantitativeRenderingDao;

    /**
     * 思维导图
     */
    @Autowired
    private MinderDataService minderDataService;

    @Resource
    private BusinessFileRelationServiceImpl businessFileRelationService;

    /**
     * 材料分组
     */
    @Autowired
    private MaterialGroupingService materialGroupingService;

    /**
     * 行政许可类
     */
    @Resource
    private BusinessGuideAdministrativeLicensingOneService BusinessGuideAdministrativeLicensingOneService;
    /**
     * 审核转报类
     */
    @Autowired
    private BusinessGuideAuditAndForwardingCategoryOneService businessGuideAuditAndForwardingCategoryOneService;
    /**
     * 备案类
     */
    @Autowired
    private BusinessGuideFilingOneService businessGuideFilingOneService;
    /**
     * 其他服务类
     */
    @Autowired
    private BusinessGuideOtherServicesOneService businessGuideOtherServicesOneService;

    /**
     * 资格预审
     */
    @Autowired
    private PrequalificationService prequalificationService;

    /**
     * 量化呈现
     */
    @Autowired
    private QuantitativeRenderingService quantitativeRenderingService;

    /**
     * 系统对接service
     */
    @Autowired
    private SystemDockingService systemDockingService;

    /**
     * 现有流程
     */
    @Autowired
    private ProcessDescriptionService processDescriptionService;

    /**
     * 流程绘制
     */
    @Autowired
    private ProcessDescriptionPicService processDescriptionPicService;
    /**
     * 审查细则
     */
    @Autowired
    private AuditRulesService auditRulesService;


    @Autowired
    private FormDescriptionServiceImpl formDescriptionServiceImpl;

//    private List<String> departmentIds;\

    @Resource
    private MatterproposalDao matterproposalDao;

    @Autowired
    private UserService userService;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @Resource
    private RevisionHistoryDao revisionHistoryDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private TasktodoService tasktodoService;

    @Resource
    private UserMapper userMapper;

    @Override
    public Map selectDepartmentalMattersListByParam(SelectDepartmentalMattersListParam param, HttpServletRequest request) {
        // 解析webtocken，获取用户所属部门以及角色
        User user = userService.findByUserId(WebTokenUtil.getUserByWebToken(request));
        List<Role> roles = user.getRoles();
        int highestLevelRole = 2;
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                if (role.getDataType() < highestLevelRole) {
                    highestLevelRole = role.getDataType();
                }
            }
        }
        List<Department> AllDept = departmentService.getAll();
        List<String> departmentIds = new ArrayList<>();
        switch (highestLevelRole) {
            case 0: {
                departmentIds = getBelongDept("0", AllDept, departmentIds);
            }
            break;
            case 1: {
                departmentIds = getBelongDept(user.getDepartmentId(), AllDept, departmentIds);
                departmentIds.add(user.getDepartmentId());
            }
            break;
            case 2: {
                departmentIds.add(user.getDepartmentId());
            }
            break;
            default:
                break;
        }

        if (departmentIds.size() <= 0) {
            departmentIds = null;
        }

        List<DepartmentalMattersRes> result;

        if (param.getDeptIdList() != null && param.getDeptIdList().size() > 0) {
            departmentIds = param.getDeptIdList();
        }

        if (param.getExamineAndApproveStateList() == null) {
            param.setExamineAndApproveStateList(new ArrayList<>());
        }

        long resultCount;
        //是否树结构
        if (param.getShowTree()) {

            Paging paging = new Paging();
            paging.setPageIndex(param.getPage());
            paging.setPageSize(param.getSize());

            //查询条件存在时
            if (StringUtils.isEmpty(param.getEventName())
                    && StringUtils.isEmpty(param.getEventType())
                    && StringUtils.isEmpty(param.getState())
                    && param.getExamineAndApproveStateList() == null && StringUtils.isEmpty(param.getDmId())) {
                //查询列表返回树结构
                resultCount = departmentalMattersDao.selectTreeByCount(departmentIds);
                //查询列表返回树结构
                result = departmentalMattersDao.selectTreeByList(departmentIds, paging);
            } else {

                //条件查询列表返回树结构
                resultCount = departmentalMattersDao.selectTreeByConditionCount(departmentIds, param);
                //条件查询列表返回树结构
                result = departmentalMattersDao.selectByConditionList(departmentIds, param, paging);
            }

//            //筛选树结构数据
//            result = result.stream().map(data -> {
//                if (data.getIsMin()) {
//                }
//            }).collect(Collectors.toList());

        } else {

            PageHelper.startPage(param.getPage(), param.getSize());

            //查询列表返回事项
            List<DepartmentalMattersRes> departmentalMattersResList = departmentalMattersDao.selectEventIdListByCondition(departmentIds, param);

            //分页实体
            PageInfo<DepartmentalMattersRes> thePage = new PageInfo<>(departmentalMattersResList);
            resultCount = thePage.getTotal();
            result = thePage.getList();
        }


        Map<String, Object> map = new HashMap<>();
        map.put("total", resultCount);
        map.put("list", result);
        return map;
    }

    /**
     * 获取子部门id
     */
    public List<String> getBelongDept(String departmentId, List<Department> allDept, List<String> departmentIds) {
        //List<Department> list = departmentService.findByParentIdOrderBySortOrder(departmentId, true,null);
        List<Department> list = allDept.stream().filter(a -> a.getParentId().equals(departmentId)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {
            for (Department item : list) {
                departmentIds.add(item.getId());
            }
            for (Department item : list) {
                departmentIds = getBelongDept(item.getId(), allDept, departmentIds);
            }
        }
        return departmentIds;
    }

    @Override
    public List<DepartmentalMattersRes> selectByDeptId(String deptid) {
        List<DepartmentalMattersRes> res = departmentalMattersDao.selectByDeptId(deptid);
        return res;
    }

    /**
     * 获取映射id
     *
     * @param departmentalMatterId
     * @return
     */
    @Override
    public String getMappingId(String departmentalMatterId) {
        String mappingId = departmentalMattersDao.selectMappingIdById(departmentalMatterId);
        return mappingId;
    }

    /**
     * 更新状态
     *
     * @param id
     * @param state
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(String id, String state) {
        departmentalMattersDao.updateStateById(id, state);
    }


    /**
     * 更新审核状态
     *
     * @param departmentalMattersId
     * @param isApproval
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExamineAndApproveState(String departmentalMattersId, Integer isApproval) {
        List<Matterproposal> matterProposalList = matterproposalDao.selectByIdAndIsApproval(departmentalMattersId, isApproval);

        //不等于10条则 不更新
        if (matterProposalList.size() != 10) {
            return;
        }
        //对结果集抽出State 并去重
        List<Integer> stateList = matterProposalList.stream().map(Matterproposal::getState).distinct().collect(Collectors.toList());
        Integer state = null;
        switch (isApproval) {
            case SystemBean.DEPT_APPROVAL:
                //判断去重后结果是否只有一个
                if (stateList.size() == 1) {
                    state = stateList.get(0);
                    if (state == null) {
                        return;
                    }
                    if (state == 1) {
                        state = SystemBean.DEPT_LEADER_AGREE;
                    }
                }
                break;

            case SystemBean.EXAMINE_APPROVAL:
                if (stateList.size() == 1) {
                    state = stateList.get(0);
                    if (state == null) {
                        return;
                    }
                    if (state == 1) {
                        state = SystemBean.REVIEW_AGREE;
                    }

                }
                break;
            default:
                return;
        }
        if (state != null) {
            //部门审批
            DepartmentalMatters departmentalMatters = new DepartmentalMatters();
            departmentalMatters.setId(departmentalMattersId);
            departmentalMatters.setExamineAndApproveState(state);
            //只有一个时更新dm表的审核状态
            departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatters);
            //查询父id
            String departmentalMattersPId = departmentalMattersDao.selectPidById(departmentalMattersId);
            //循环更新父级的审核状态
            updateFatherExamineAndApproveState(departmentalMattersPId);

            //如果评审通过
            //变更履历新增一条数据
            if (state == 7) {
                DepartmentalMatters departmentalMattersNow = new DepartmentalMatters();
                departmentalMattersNow.setId(departmentalMattersId);
                DepartmentalMatters departmentalMattersReturn = departmentalMattersDao.selectOne(departmentalMattersNow);
                Integer count = revisionhistoryDao.selectCountByDmId(departmentalMattersId);
                if (count == null || count == 0) {

                    RevisionHistory revisionHistory = new RevisionHistory();
                    revisionHistory.setChangeContent("新做成");
                    revisionHistory.setState(1);
                    revisionHistory.setDmId(departmentalMattersId);
                    revisionHistory.setWho(departmentalMattersReturn.getOperator());
                    //查找事项上一个操作人ID
                    revisionHistoryService.add(revisionHistory);
                } else {

                    RevisionHistory revisionHistory = new RevisionHistory();
                    revisionHistory.setState(1);
                    revisionHistory.setId(departmentalMattersReturn.getChangeOfResumeVersion());
                    revisionHistory.setWho(departmentalMattersReturn.getOperator());
                    //查找事项上一个操作人ID
                    revisionHistoryService.updateInfo(revisionHistory);
                }
            }
        }
    }

//    /**
//     * 更新审核状态
//     * @param departmentalMatters
//     */
//    @Override
//    public void updateExamineAndApproveState(DepartmentalMatters departmentalMatters ) {
//
//        departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatters);
//    }

    @Override
    public DepartmentalMatters findState(String eventId) {
        DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(eventId);
        return departmentalMatters;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean isCommit(DepartmentalMatters departmentalMatters) {
        DepartmentalMatters findDepartmentalMatters = departmentalMattersDao.selectOne(departmentalMatters);

        Integer i1 = findDepartmentalMatters.getBusinessGuideState();
        Integer i2 = findDepartmentalMatters.getMaterialGroupingState();
        Integer i3 = findDepartmentalMatters.getApplicationTypeState();
        Integer i4 = findDepartmentalMatters.getProcessMapState();
        Integer i5 = findDepartmentalMatters.getProcessDescriptionState();
        Integer i6 = findDepartmentalMatters.getPrequalificationState();
        Integer i7 = findDepartmentalMatters.getBusinessSituationState();
        Integer i8 = findDepartmentalMatters.getFormThatState();
        Integer i9 = findDepartmentalMatters.getPresentQuantitativeState();
        Integer i10 = findDepartmentalMatters.getSystemDockingState();


        if (findDepartmentalMatters.getState() == 3) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项已提交，请勿重复提交。").build());
        }

        //全部已完善
        if (i1 == 2 && i2 == 2 && i3 == 2 && i4 == 2 && i5 == 2 && i6 == 2 && i7 == 2 && i8 == 2 && i9 == 2 && i10 == 2) {
            findDepartmentalMatters.setState(3);
            //设置提交时间
            findDepartmentalMatters.setPublicityTime(new Date(System.currentTimeMillis()));
            //设置审核状态为 等待部门审核
            findDepartmentalMatters.setExamineAndApproveState(SystemBean.WAIT_DEPT_CONFIRM);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 3);
            findDepartmentalMatters.setDeptProposalEndTime(calendar.getTime());
            //更新状态
            departmentalMattersDao.updateByPrimaryKeySelective(findDepartmentalMatters);
            //获取事项id 是否有父
            String departmentalMattersPid = departmentalMattersDao.selectPidById(findDepartmentalMatters.getId());
            //更新父部门状态
            updateFatherState(departmentalMattersPid);

            for (int i = 1; i < (10 + 1); i++) {
                Matterproposal matterproposal = new Matterproposal();
                matterproposal.setId(SysUtil.getUUID());
                matterproposal.setIsApproval(SystemBean.DEPT_APPROVAL);
                matterproposal.setState(3);
                matterproposal.setDepartmentalMattersId(findDepartmentalMatters.getId());
                matterproposal.setSheetNum(i);
                matterproposalDao.insert(matterproposal);
            }
            //事项名称
            String eventName = getEventName(findDepartmentalMatters.getId());
            //找到该给谁发
            List<String> userIds = departmentService.findDeptGly(findDepartmentalMatters.getDeptId());
            //发送审批通知
            tasktodoService.sendTodo(findDepartmentalMatters.getId(), eventName, 1, userIds, "事项已提交,请审核");
            return true;
        }
        return false;
    }


    @Override
    public String getEventName(String dmId) {

        DepartmentalMatters departmentalMatters = new DepartmentalMatters();
        departmentalMatters.setId(dmId);
        DepartmentalMatters departmentalMattersReturn = departmentalMattersDao.selectOne(departmentalMatters);
        EventsTable eventsTable = eventsTableService.findById(departmentalMattersReturn.getEventId());
        return eventsTable.getEventName();

    }


    @Override
    public String findFatherId(String id) {
        return departmentalMattersDao.selectPidById(id);
    }

    /**
     * 更新本事项状态 保存操作后调用
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStade(String eventId) {
        //更新本事项状态
        DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(eventId);

        if (departmentalMatters != null) {
            Integer state = departmentalMatters.getState();

            Integer i1 = departmentalMatters.getBusinessGuideState();
            Integer i2 = departmentalMatters.getMaterialGroupingState();
            Integer i3 = departmentalMatters.getApplicationTypeState();
            Integer i4 = departmentalMatters.getProcessMapState();
            Integer i5 = departmentalMatters.getProcessDescriptionState();
            Integer i6 = departmentalMatters.getPrequalificationState();
            Integer i7 = departmentalMatters.getFormThatState();
            Integer i8 = departmentalMatters.getBusinessSituationState();
            Integer i9 = departmentalMatters.getPresentQuantitativeState();
            Integer i10 = departmentalMatters.getSystemDockingState();

            if (i1.equals(state) && i2.equals(state) && i3.equals(state) && i4.equals(state) && i5.equals(state)
                    && i6.equals(state) && i7.equals(state) && i8.equals(state) && i9.equals(state) && i10.equals(state)) {
                //更新本部门状态
                departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
            }
        }

    }


    /**
     * 事项复制
     *
     * @param copyMattersReq
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean copyMatters(CopyMattersReq copyMattersReq, HttpServletRequest request) throws Exception {

        String copyMattersIdFrom = copyMattersReq.getCopyMattersIdFrom();//原事项ID
        String copyMattersIdTo = copyMattersReq.getCopyMattersIdTo();//现在事项ID
        Integer eventType = copyMattersReq.getEventType();//事项类型(1.行政许可 2.审核转报 3.备案类 4.其他服务类)
        versionmanagementDao.updateAllSheetVersion(copyMattersIdTo);

        //办事指南
        switch (eventType) {
            case 1:
                //行政许可
                BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq = BusinessGuideAdministrativeLicensingOneService.findByEventId(copyMattersIdFrom);

                if (businessGuideAdministrativeLicensingOneReq != null) {
                    BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne = businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne();
                    BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate = businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOnePrivate();
                    if (businessGuideAdministrativeLicensingOne != null) {
                        businessGuideAdministrativeLicensingOne.setEventId(copyMattersIdTo);
                        businessGuideAdministrativeLicensingOne.setId(null);
                        BusinessGuideAdministrativeLicensingOneService.deleteByEventId(copyMattersIdTo);

                        if (businessGuideAdministrativeLicensingOnePrivate == null) {
                            businessGuideAdministrativeLicensingOnePrivate = new BusinessGuideAdministrativeLicensingOnePrivate();
                        }
                        businessGuideAdministrativeLicensingOnePrivate.setDepartmentalMattersId(copyMattersIdTo);
                        businessGuideAdministrativeLicensingOnePrivate.setState(businessGuideAdministrativeLicensingOne.getState());
                        businessGuideAdministrativeLicensingOnePrivate.setVersion(1);
                        BusinessGuideAdministrativeLicensingOneService.add(businessGuideAdministrativeLicensingOne, businessGuideAdministrativeLicensingOnePrivate, request);
                    }
                }
                break;
            case 2:
                //审核转报
                BusinessGuideAuditAndForwardingCategoryOneReq businessGuideAuditAndForwardingCategoryOneReq = businessGuideAuditAndForwardingCategoryOneService.findByEventId(copyMattersIdFrom);

                if (businessGuideAuditAndForwardingCategoryOneReq != null) {
                    BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne = businessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOne();
                    BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate = businessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOnePrivate();

                    if (businessGuideAuditAndForwardingCategoryOne != null) {

                        businessGuideAuditAndForwardingCategoryOne.setEventId(copyMattersIdTo);
                        businessGuideAuditAndForwardingCategoryOne.setId(null);
                        businessGuideFilingOneService.deleteByEventId(copyMattersIdTo);

                        if (businessGuideAuditAndForwardingCategoryOnePrivate == null) {
                            businessGuideAuditAndForwardingCategoryOnePrivate = new BusinessGuideAuditAndForwardingCategoryOnePrivate();
                        }
                        businessGuideAuditAndForwardingCategoryOnePrivate.setDepartmentalMattersId(copyMattersIdTo);

                        businessGuideAuditAndForwardingCategoryOnePrivate.setState(businessGuideAuditAndForwardingCategoryOne.getState());
                        businessGuideAuditAndForwardingCategoryOnePrivate.setVersion(1);
                        businessGuideAuditAndForwardingCategoryOneService.add(businessGuideAuditAndForwardingCategoryOne, businessGuideAuditAndForwardingCategoryOnePrivate, request);
                    }
                }
                break;
            case 3:
                //备案类
                BusinessGuideFilingOneReq businessGuideFilingOneReq = businessGuideFilingOneService.findByEventId(copyMattersIdFrom);

                if (businessGuideFilingOneReq != null) {
                    BusinessGuideFilingOne businessGuideFilingOne = businessGuideFilingOneReq.getBusinessGuideFilingOne();


                    BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate = businessGuideFilingOneReq.getBusinessGuideFilingOnePrivate();

                    if (businessGuideFilingOne != null) {

                        businessGuideFilingOne.setEventId(copyMattersIdTo);
                        businessGuideFilingOne.setId(null);
                        businessGuideFilingOneService.deleteByEventId(copyMattersIdTo);

                        if (businessGuideFilingOnePrivate == null) {
                            businessGuideFilingOnePrivate = new BusinessGuideFilingOnePrivate();
                        }
                        businessGuideFilingOnePrivate.setDepartmentalMattersId(copyMattersIdTo);
                        businessGuideFilingOnePrivate.setState(businessGuideFilingOne.getState());
                        businessGuideFilingOnePrivate.setVersion(1);
                        businessGuideFilingOneService.add(businessGuideFilingOne, businessGuideFilingOnePrivate, request);
                    }
                }


                break;
            case 4:
                //其他服务类
                BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq = businessGuideOtherServicesOneService.findByEventId(copyMattersIdFrom);

                if (businessGuideOtherServicesOneReq != null) {
                    BusinessGuideOtherServicesOne businessGuideOtherServicesOne = businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOne();


                    BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate = businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOnePrivate();

                    if (businessGuideOtherServicesOne != null) {

                        businessGuideOtherServicesOne.setEventId(copyMattersIdTo);
                        businessGuideOtherServicesOne.setId(null);
                        businessGuideOtherServicesOne.setResultSample(OssClientUtil.copyImage(businessGuideOtherServicesOne.getResultSample()));
                        businessGuideOtherServicesOneService.deleteByEventId(copyMattersIdTo);

                        if (businessGuideOtherServicesOnePrivate == null) {
                            businessGuideOtherServicesOnePrivate = new BusinessGuideOtherServicesOnePrivate();
                        }
                        businessGuideOtherServicesOnePrivate.setState(businessGuideOtherServicesOne.getState());
                        businessGuideOtherServicesOnePrivate.setDepartmentalMattersId(copyMattersIdTo);
                        businessGuideOtherServicesOnePrivate.setVersion(1);
                        businessGuideOtherServicesOneService.add(businessGuideOtherServicesOne, businessGuideOtherServicesOnePrivate, request);
                    }
                }
                break;
            default:
                break;
        }


        //材料分组
        List<MaterialGroupAddParam> minderDataList = new ArrayList<>();
        minderDataList = materialGroupingService.selectByEventId(copyMattersIdFrom).getList();
        if (minderDataList != null && minderDataList.size() > 0) {
            MaterialGroupParam materialGroupParam = new MaterialGroupParam();
            //  materialGroupingService.deleteByEventId(copyMattersIdTo);
            for (MaterialGroupAddParam materialGroupAddParam :
                    minderDataList) {
                //
                materialGroupAddParam.setMaterialGroupingBeforeId(materialGroupAddParam.getId());
                materialGroupAddParam.setEventId(copyMattersIdTo);
                materialGroupAddParam.setSampleTable(OssClientUtil.copyImage(materialGroupAddParam.getSampleTable()));
                materialGroupAddParam.setEmptyTable(OssClientUtil.copyImage(materialGroupAddParam.getEmptyTable()));
                materialGroupAddParam.setId(SysUtil.getUUID());

                // BeanUtils.copyProperties(materialGroupAddParam, materialGroupParam);

            }
            materialGroupParam.setMaterialGroupingParamList(minderDataList);
            materialGroupParam.setEventId(copyMattersIdTo);
            materialGroupParam.setVersion(1);
            materialGroupingService.saveInfo(materialGroupParam, request);
        }

        //审查细则
        if (minderDataList != null && minderDataList.size() > 0) {

            for (MaterialGroupAddParam materialGroupAddParam :
                    minderDataList) {

                List<AuditRules> auditRulesList = auditRulesService.selectByMaterialId(materialGroupAddParam.getMaterialGroupingBeforeId(), 1);

                if (auditRulesList != null && auditRulesList.size() > 0) {
                    for (AuditRules auditRules :
                            auditRulesList) {
                        auditRules.setMaterialId(materialGroupAddParam.getId());
                        auditRules.setId(SysUtil.getUUID());
                        auditRulesService.save(auditRules);
                    }


                }

                List<AuditRules> auditRulesList2 = auditRulesService.selectByMaterialId(materialGroupAddParam.getMaterialGroupingBeforeId(), 2);
                if (auditRulesList2 != null && auditRulesList2.size() > 0) {
                    for (AuditRules auditRules :
                            auditRulesList2) {
                        auditRules.setMaterialId(materialGroupAddParam.getId());
                        auditRules.setId(SysUtil.getUUID());
                        auditRulesService.save(auditRules);
                    }


                }

            }

        }


        //申请类型
        MinderDataReq minderDataReq = minderDataService.selectByEventId(copyMattersIdFrom, 1);
        MinderData minderData = minderDataReq.getMinderData();
        if (minderData != null) {
            minderData.setId(SysUtil.getUUID());
            minderData.setEventId(copyMattersIdTo);
            minderData.setPicPath(OssClientUtil.copyImage(minderData.getPicPath()));
            String getMinderData = minderData.getMinderData();
            if (minderDataList != null && minderDataList.size() > 0 && getMinderData != null) {

                for (MaterialGroupAddParam materialGroupAddParam :
                        minderDataList) {
                    getMinderData = getMinderData.replace(materialGroupAddParam.getMaterialGroupingBeforeId(), materialGroupAddParam.getId());

                }
            }
            minderData.setType(1);
            minderData.setMinderData(getMinderData);
            MinderDataReq saveMinderData = new MinderDataReq();
            saveMinderData.setMinderData(minderData);
            saveMinderData.setVersion(1);
            minderDataService.saveInfo(saveMinderData, request);
        }
        //办事情形
        MinderDataReq workSituationReq = minderDataService.selectByEventId(copyMattersIdFrom, 2);

        MinderData workSituation = workSituationReq.getMinderData();
        if (workSituation != null) {
            workSituation.setEventId(copyMattersIdTo);
            workSituation.setPicPath(OssClientUtil.copyImage(workSituation.getPicPath()));
            workSituation.setId(SysUtil.getUUID());
            workSituation.setType(2);
            MinderDataReq saveMinderDataReq2 = new MinderDataReq();
            saveMinderDataReq2.setMinderData(workSituation);
            saveMinderDataReq2.setVersion(1);
            minderDataService.saveInfo(saveMinderDataReq2, request);
        }
        //表单说明
        FormThatParam formThatParam = new FormThatParam();
        formThatParam.setEventId(copyMattersIdFrom);
        List<FormDescription> formDescriptionList = formDescriptionServiceImpl.findMaterial(formThatParam);
        if (minderDataList != null && minderDataList.size() > 0) {
            for (MaterialGroupAddParam materialGroupAddParam :
                    minderDataList) {
                FormDescriptionParam formDescriptionParam = new FormDescriptionParam();
                List<FormDescription> list = new ArrayList<>();
                if (formDescriptionList != null && formDescriptionList.size() > 0) {

                    for (FormDescription formDescription :
                            formDescriptionList) {

                        if (formDescription.getMaterialId() != null && formDescription.getMaterialId().equals(materialGroupAddParam.getMaterialGroupingBeforeId())) {

                            formDescription.setMaterialId(materialGroupAddParam.getId());
                            formDescription.setId(SysUtil.getUUID());
                            list.add(formDescription);
                        }

                    }
                }

                if (list != null && list.size() > 0) {

                    for (FormDescription s : list) {
                        s.setId(null);
                    }
                    formDescriptionParam.setList(list);
                    formDescriptionParam.setMaterialId(materialGroupAddParam.getId());
                    formDescriptionParam.setVersion(1);
                    formDescriptionParam.setEventId(copyMattersIdTo);
                    formDescriptionServiceImpl.add(formDescriptionParam, request);
                }
            }


        }


        //资格预审
        List<Prequalification> prequalificationList = prequalificationService.findByEventId(copyMattersIdFrom);
        if (prequalificationList != null && prequalificationList.size() > 0) {
            PrequalificationParam prequalificationParam = new PrequalificationParam();
            for (Prequalification prequalification :
                    prequalificationList) {

                prequalification.setId(SysUtil.getUUID());
                if ("平台查询".equals(prequalification.getAuditMode())) {

                    prequalification.setAuditOperation(OssClientUtil.copyImage(prequalification.getAuditOperation()));

                }
                prequalification.setEventId(copyMattersIdTo);


            }
            prequalificationParam.setPrequalificationList(prequalificationList);
            prequalificationParam.setEventId(copyMattersIdTo);
            prequalificationParam.setVersion(1);
            prequalificationService.saveInfo(prequalificationParam, request);


        }

        //量化呈现
        QuantitativeRendering quantitativeRendering = quantitativeRenderingService.findBy("eventId", copyMattersIdFrom);
        if (quantitativeRendering != null) {
            quantitativeRendering.setEventId(copyMattersIdTo);
            quantitativeRendering.setId("");
            quantitativeRendering.setRenderPicture(OssClientUtil.copyImage(quantitativeRendering.getRenderPicture()));
            quantitativeRendering.setDockingModePicture(OssClientUtil.copyImage(quantitativeRendering.getDockingModePicture()));

            quantitativeRenderingService.saveInfo(quantitativeRendering, 1, request);
        }
        //系统对接方案
        ReturnCode returnCode = systemDockingService.selectByEventId(copyMattersIdFrom);
        if (returnCode != null) {
            SystemDocking result = (SystemDocking) returnCode.getObj();
            if (result != null) {
                result.setId(SysUtil.getUUID());
                result.setEventId(copyMattersIdTo);
                result.setPicPath(OssClientUtil.copyImage(result.getPicPath()));
                // systemDockingService.deleteById();
                result.setVersion(1);
                systemDockingService.saveInfo(result, request);
            }
        }

        //现有流程

        List<ProcessDescription> ProcessDescriptionList = processDescriptionService.findByEventId(copyMattersIdFrom);
        if (ProcessDescriptionList != null && ProcessDescriptionList.size() > 0) {
            ProcessDescriptionParam processDescriptionParam = new ProcessDescriptionParam();
            for (ProcessDescription processDescription :
                    ProcessDescriptionList) {
                processDescription.setId(SysUtil.getUUID());
                processDescription.setEventId(copyMattersIdTo);

            }
            processDescriptionParam.setEventId(copyMattersIdTo);
            processDescriptionParam.setProcessDescriptionList(ProcessDescriptionList);
            processDescriptionParam.setVersion(1);
            processDescriptionService.saveInfo(processDescriptionParam, request);
        }

        //流程绘制
        ProcessDescriptionPicReq processDescriptionPicReq = processDescriptionPicService.findByEventId(copyMattersIdFrom);
        List<ProcessDescriptionPic> processDescriptionPicList = processDescriptionPicReq.getProcessDescriptionPicList();
        if (processDescriptionPicList != null && processDescriptionPicList.size() > 0) {
            processDescriptionPicList.forEach(processDescriptionPic -> {
                processDescriptionPic.setId(SysUtil.getUUID());
                processDescriptionPic.setEventId(copyMattersIdTo);
                processDescriptionPic.setPicbase(OssClientUtil.copyImage(processDescriptionPic.getPicbase()));
                processDescriptionPicService.deleteByEventId(copyMattersIdTo);
                // processDescriptionPic.set
                processDescriptionPicService.save(processDescriptionPic);
            } );
        }


        //状态修改
        DepartmentalMatters departmentalMatter = new DepartmentalMatters();
        departmentalMatter.setId(copyMattersIdTo);
        departmentalMatter.setApplicationTypeState(2);
        departmentalMatter.setBusinessGuideState(2);
        departmentalMatter.setMaterialGroupingState(2);
        departmentalMatter.setApplicationTypeState(2);
        departmentalMatter.setPrequalificationState(2);
        departmentalMatter.setProcessDescriptionState(2);
        departmentalMatter.setProcessMapState(2);
        departmentalMatter.setBusinessSituationState(2);
        departmentalMatter.setFormThatState(2);
        departmentalMatter.setPresentQuantitativeState(2);
        departmentalMatter.setSystemDockingState(2);
        departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatter);

        return true;
    }

    /**
     * 只根据条件查询事项(不根据用户权限)
     *
     * @param param
     * @return
     */
    @Override
    public Map selectAllByParam(SelectDepartmentalMattersListParam param) {
        List<DepartmentalMattersRes> result;
        long resultCount;


        List<String> departmentIds = null;
        if (param.getDeptIdList() != null && param.getDeptIdList().size() > 0) {
            departmentIds = param.getDeptIdList();
        }

        if (param.getExamineAndApproveStateList() == null) {
            param.setExamineAndApproveStateList(new ArrayList<>());
        }

        //是否树结构
        if (param.getShowTree()) {
            Paging paging = new Paging();
            paging.setPageIndex(param.getPage());
            paging.setPageSize(param.getSize());
            //查询条件存在时
            if (StringUtils.isEmpty(param.getEventName())
                    && StringUtils.isEmpty(param.getEventType())
                    && StringUtils.isEmpty(param.getState())
                    && param.getExamineAndApproveStateList() == null) {
                //查询列表返回树结构
                resultCount = departmentalMattersDao.selectTreeByCount(departmentIds);
                //查询列表返回树结构
                result = departmentalMattersDao.selectTreeByList(departmentIds, paging);
            } else {
                //条件查询列表返回树结构
                resultCount = departmentalMattersDao.selectTreeByConditionCount(departmentIds, param);
                //条件查询列表返回树结构
                result = departmentalMattersDao.selectByConditionList(departmentIds, param, paging);
            }

        } else {

            PageHelper.startPage(param.getPage(), param.getSize());

            //查询列表返回事项
            List<DepartmentalMattersRes> departmentalMattersResList = departmentalMattersDao.selectEventIdListByCondition(departmentIds, param);

            //分页实体
            PageInfo<DepartmentalMattersRes> thePage = new PageInfo<>(departmentalMattersResList);
            resultCount = thePage.getTotal();
            result = thePage.getList();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", resultCount);
        map.put("list", result);
        return map;
    }

    /**
     * 发起审批
     *
     * @param departmentalMattersId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void launchedExamineAndApprove(String departmentalMattersId, String userId) {

        DepartmentalMatters departmentalMatters = new DepartmentalMatters();
        departmentalMatters.setId(departmentalMattersId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 3);
        departmentalMatters.setExamineProposalEndTime(calendar.getTime());
        departmentalMatters.setExamineAndApproveState(SystemBean.WAIT_REVIEW);
        departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatters);


        //查询是否有评审记录 如果有则不新创记录
        List<Matterproposal> matterproposalList = matterproposalDao.selectByIdAndIsApproval(departmentalMattersId, 2);
        if (matterproposalList != null && matterproposalList.size() > 0) {
        } else {
            for (int i = 1; i < (10 + 1); i++) {
                Matterproposal matterproposal = new Matterproposal();
                matterproposal.setId(SysUtil.getUUID());
                matterproposal.setIsApproval(SystemBean.EXAMINE_APPROVAL);
                matterproposal.setState(3);
                matterproposal.setDepartmentalMattersId(departmentalMattersId);
                matterproposal.setSheetNum(i);
                matterproposalDao.insert(matterproposal);
            }
        }
        //查询父id
        String departmentalMattersPId = departmentalMattersDao.selectPidById(departmentalMattersId);
        //循环更新父级的审核状态
        updateFatherExamineAndApproveState(departmentalMattersPId);

        //通知所有评审员进行评审
        sendToReview(departmentalMattersId);
    }

    /**
     * 发送给
     *
     * @param dmId
     * @return
     */
    public Boolean sendToReview(String dmId) {

        //查询所有的论证员
        List<String> userIds = departmentService.selectReviewGly();
        String eventName = getEventName(dmId);

        if (userIds != null && userIds.size() > 0) {
            tasktodoService.sendTodo(dmId, eventName, 3, userIds, "事项已提交,请论证");
            return true;
        }
        return false;
    }


    /**
     * 事项提交后梳理员可修改的状态
     *
     * @param departmentalMattersId
     * @return
     */
    @Override
    public Map getRevisable(String departmentalMattersId, Role role) {

        boolean isEdit = false;
        boolean isQuestion = false;
        //查询事项审核状态
        DepartmentalMatters departmentalMatters = departmentalMattersDao.selectByPrimaryKey(departmentalMattersId);

        //判断事项是否存在
        if (departmentalMatters == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("未找到该事项数据").build());
        }

        //判断该事项是否提交
        if (departmentalMatters.getState() != 3) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项未提交").build());
        }

        //判断该事项停留在那个审核阶段
        if (departmentalMatters.getExamineAndApproveState() == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("事项未发起审批").build());
        }
        //角色
        switch (role.getDataType()) {
            case CommonConstant.DATA_TYPE_ALL:
                break;
            case CommonConstant.DATA_TYPE_DEPT_CUSTOM:

                if (SystemBean.WAIT_DEPT_CONFIRM <= departmentalMatters.getExamineAndApproveState() && departmentalMatters.getExamineAndApproveState() <= SystemBean.DEPT_LEADER_AGREE) {
                    isQuestion = true;
                }

                break;
            case CommonConstant.DATA_TYPE_CARDING:
                int count = 0;
                //部门审批
                if (SystemBean.WAIT_DEPT_CONFIRM <= departmentalMatters.getExamineAndApproveState() && departmentalMatters.getExamineAndApproveState() <= SystemBean.DEPT_LEADER_AGREE) {
                    //部门审批
                    count = matterproposalDao.selectCountByIdAndIsApprovalAndState(departmentalMattersId, SystemBean.DEPT_APPROVAL, SystemBean.MATTER_PROPOSAL_REFUSE);

                    RevisionHistory revisionHistory = revisionHistoryDao.selectLastInfo(departmentalMattersId);

                    if (revisionHistory != null && revisionHistory.getState() == 3) {
                        count = 1;
                    }

                } else if (SystemBean.WAIT_REVIEW <= departmentalMatters.getExamineAndApproveState() && departmentalMatters.getExamineAndApproveState() <= SystemBean.REVIEW_REFUSE) {
                    //审核审批
                    count = matterproposalDao.selectCountByIdAndIsApprovalAndState(departmentalMattersId, SystemBean.EXAMINE_APPROVAL, SystemBean.MATTER_PROPOSAL_REFUSE);
                }
                if (count > 0) {
                    isEdit = true;
                }
                break;
            case CommonConstant.DATA_TYPE_EXAMINE:
                if (SystemBean.WAIT_REVIEW <= departmentalMatters.getExamineAndApproveState() && departmentalMatters.getExamineAndApproveState() <= SystemBean.REVIEW_REFUSE) {
                    isQuestion = true;
                }
                break;
            default:
                break;
        }


        Map map = new HashMap();

        //是否可修改
        map.put("isEdit", isEdit);
        //是否可评论
        map.put("isQuestion", isQuestion);
        return map;
    }


    /**
     * 根据事项查询所有相关图片
     */
    public EventPictures selecrEventPictures(String eventId) {
        EventPictures eventPictures = new EventPictures();
        // 确定事项类型
        DepartmentalMatters matters = departmentalMattersDao.selectByPrimaryKey(eventId);
        if (StringUtils.isEmpty(eventId)) {
            return null;
        }
        EventsTable event = eventsTableService.findById(matters.getEventId());
        if (event == null) {
            return null;
        }
        eventPictures.setEventName(event.getEventName());
        // 办事指南图片路径
        switch (event.getEventType()) {
            case 1: {
                //行政许可
                BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq = businessGuideAdministrativeLicensingOneDao.findByEventId(eventId);
                BusinessGuideAdministrativeLicensingOne guide = businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne();
                if (guide != null) {
                    eventPictures.setProcessingFlowPic(guide.getProcessFlowChart());
                }
            }
            break;
//            case 2:{
//                // 审核转报
//            }
//            break;
            case 3: {
                // 备案类
                BusinessGuideFilingOneReq guide = businessGuideFilingOneDao.findByEventId(eventId);
                if (guide != null) {
                    eventPictures.setProcessingFlowPic(guide.getBusinessGuideFilingOne().getProcessFlowChart());
                }
            }
            break;
//            case 4:{
//                // 其他服务类
//            }
//            break;
        }
        // 材料分组对象

        // 量化呈现
        QuantitativeRendering qr = new QuantitativeRendering();
        qr.setEventId(eventId);
        QuantitativeRendering quantitativeRendering = quantitativeRenderingDao.selectOne(qr);
        if (quantitativeRendering != null) {
            eventPictures.setQuantitativeRendering(quantitativeRendering);
        }
        // 办事情形图
        Condition condition = new Condition(BusinessFileRelation.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("businessId", eventId);
        criteria.andEqualTo("businessType", 4);
        List<BusinessFileRelation> businessFileRelationList = businessFileRelationService.findByCondition(condition);
        if (businessFileRelationList != null && businessFileRelationList.size() > 0) {
            eventPictures.setSituationPic(businessFileRelationList.get(0).getFilePath());
        }
        // 资格预审list

        return eventPictures;
    }

    /**
     * @描述 更新父集状态
     * @参数 [param, userId]
     * @返回值 void
     * @创建人 swq
     * @创建时间 2018/7/18 0018
     * @修改人和其它信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFatherState(String departmentalMattersPid) {
        //循环直到没有父
        while (true) {
            if (departmentalMattersPid != null) {
                if (!"0".equals(departmentalMattersPid)) {
                    // 查询父项的所有子状态
                    List<Map> mapList = departmentalMattersDao.selectSonState(departmentalMattersPid);

                    //更新
                    if (mapList != null && mapList.size() == 1) {
                        String dId = null;
                        if (mapList.get(0).containsKey("id")) {
                            dId = mapList.get(0).get("id").toString();

                        }
                        Integer dState = null;

                        if (mapList.get(0).containsKey("state")) {
                            dState = Integer.parseInt(mapList.get(0).get("state").toString());
                        }
                        if (dId != null && dState != null) {
                            //更新父状态
                            departmentalMattersDao.updateFatherState(dId, dState);
                        }
                    }

                    //获取事项id 是否有父
                    departmentalMattersPid = departmentalMattersDao.selectPidById(departmentalMattersPid);
                    continue;
                }
            } else {
                break;
            }
        }
    }

    /**
     * 父级事项状态条件更新
     *
     * @param departmentalMattersPid
     * @param state
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFatherState(String departmentalMattersPid, Integer state) {
        //循环直到没有父
        while (true) {
            if (departmentalMattersPid != null && state != null) {
                if (!"0".equals(departmentalMattersPid)) {
                    //更新父状态
                    departmentalMattersDao.updateFatherState(departmentalMattersPid, state);

                }
                //获取事项id 是否有父
                departmentalMattersPid = departmentalMattersDao.selectPidById(departmentalMattersPid);
            } else {
                break;
            }
        }
    }


    /**
     * @描述 更新审核父集状态
     * @参数 [param, userId]
     * @返回值 void
     * @创建人 swq
     * @创建时间 2018/7/18 0018
     * @修改人和其它信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFatherExamineAndApproveState(String departmentalMattersPId) {
        //循环直到没有父
        while (true) {
            if (departmentalMattersPId != null) {
                if (!"0".equals(departmentalMattersPId)) {
                    // 查询父项的所有子状态
                    List<Map> mapList = departmentalMattersDao.selectSonState(departmentalMattersPId);
                    String dId = null;
                    //更新
                    if (mapList != null && mapList.size() == 1) {

                        if (mapList.get(0).containsKey("id")) {
                            dId = mapList.get(0).get("id").toString();

                        }
                        Integer dExamineAndApproveState = null;

                        if (mapList.get(0).containsKey("examineAndApproveState")) {
                            dExamineAndApproveState = Integer.parseInt(mapList.get(0).get("examineAndApproveState").toString());
                        }
                        if (dId != null && dExamineAndApproveState != null) {
                            DepartmentalMatters departmentalMatters = new DepartmentalMatters();
                            departmentalMatters.setId(dId);
                            departmentalMatters.setExamineAndApproveState(dExamineAndApproveState);
                            //更新父状态
                            departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatters);
                        }
                    } else {
                  /*      if (mapList.get(0).containsKey("id")) {
                            dId = mapList.get(0).get("id").toString();

                        }
                        Integer intMin = (Integer) mapList.get(0).get("examineAndApproveState");

                        for (int i = 0; i < mapList.size(); i++) {
                            if (intMin > (Integer) mapList.get(i).get("examineAndApproveState")) {

                                intMin=(Integer) mapList.get(i).get("examineAndApproveState");
                            }

                        }
                        DepartmentalMatters departmentalMatters = new DepartmentalMatters();
                        departmentalMatters.setId(dId);
                        departmentalMatters.setExamineAndApproveState(intMin);
                        //更新父状态
                        departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatters);*/

                    }

                    //获取事项id 是否有父
                    departmentalMattersPId = departmentalMattersDao.selectPidById(departmentalMattersPId);
                    continue;
                }
            } else {
                break;
            }
        }
    }

    /**
     * 更新父级审核状态
     *
     * @param departmentalMattersPid
     * @param examineAndApproveState
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFatherExamineAndApproveState(String departmentalMattersPid, Integer examineAndApproveState) {

        //循环直到没有父
        while (true) {
            if (departmentalMattersPid != null) {
                if (!"0".equals(departmentalMattersPid)) {
                    if (departmentalMattersPid != null) {
                        DepartmentalMatters departmentalMatters = new DepartmentalMatters();
                        departmentalMatters.setId(departmentalMattersPid);
                        departmentalMatters.setExamineAndApproveState(examineAndApproveState);
                        //更新父状态
                        departmentalMattersDao.updateByPrimaryKeySelective(departmentalMatters);
                    }
                }

                //获取事项id 是否有父
                departmentalMattersPid = departmentalMattersDao.selectPidById(departmentalMattersPid);
                continue;

            } else {
                break;
            }
        }
    }

    @Override
    public DealCount findDealCount() {
        DealCount dealCount = departmentalMattersDao.findDealCount();
        if (dealCount != null) {
            if (dealCount.getAllDeal() == null) {
                dealCount.setAllDeal(0);
            }
            if (dealCount.getAccomplishDeal() == null) {
                dealCount.setAccomplishDeal(0);
            }
            dealCount.setStayDeal(dealCount.getAllDeal() - dealCount.getAccomplishDeal());
        }
        return dealCount;
    }

    @Override
    public EventLevel findEventLevel() {
        EventLevel eventLevel = departmentalMattersDao.findEventLevel();
        return eventLevel;
    }

    @Override
    public List<Map<String, Object>> findEveryAreaCount() {
        List<Map<String, Object>> list = new ArrayList<>();
        //所有地点
        List<Department> allArea = departmentMapper.getAllByIsLocation(true);
        //所有部门
        List<Department> allDept = departmentMapper.getAllByIsLocation(false);
        for (int i = 0; i < allArea.size(); i++) {
            //部门总事项个数
            Integer allEventCount = 0;
            //部门已完成事项个数
            Integer finishEventCount = 0;

            List<Department> departmentList = new ArrayList<>();

            //departmentList 返回所有的子部门
            userService.getBelongDept(allArea.get(i).getId(), allDept, departmentList);

            //查询所有子部门的事项
            for (int j = 0; j < departmentList.size(); j++) {
                allEventCount += departmentalMattersDao.findAllEventCountByArea(departmentList.get(j).getId());
                finishEventCount += departmentalMattersDao.findFinishEventCountByArea(departmentList.get(j).getId());
            }

            Map<String, Object> eventCountMap = new HashMap<>();
            eventCountMap.put("cityName", allArea.get(i).getTitle());
            eventCountMap.put("allEventCount", allEventCount);
            eventCountMap.put("finishEventCount", finishEventCount);

            list.add(eventCountMap);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeDept(List<String> ids, String newDeptId, int type) {
        //转移到的部门
        Department newDepartment = departmentMapper.selectByPrimaryKey(newDeptId);
        if (newDepartment == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("数据异常：要转移到的部门不存在").build());
        }
        //用户转移
        if (type == 0) {
            ids.forEach(id -> {
                User user = userMapper.selectByPrimaryKey(id);
                if (user != null) {
                    user.setDepartmentId(newDeptId);
                    userMapper.updateByPrimaryKey(user);
                } else {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("数据异常：未找到到被转移用户").build());
                }
            });
        } else if (type == 1) {
            //转移到的部门层级
            String region = newDepartment.getRegion();
            //事项转移
            ids.forEach(eventId -> {
                String id = departmentalMattersDao.findIdByEventId(eventId);
                //id对应dm对象
                DepartmentalMatters findDepartmentalMatters = departmentalMattersDao.selectByPrimaryKey(id);
                if (findDepartmentalMatters == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("数据异常：被转移事项不存在").build());
                }
                //被转移事项部门
                Department department = departmentMapper.selectByPrimaryKey(findDepartmentalMatters.getDeptId());
                if (department == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("数据异常：被转移事项部门不存在").build());
                }
                //要转移部门层级与新部门层级相等
                if (region.equals(department.getRegion())) {
                    String groupId = findDepartmentalMatters.getGroupId();
                    //子项集合
                    List<DepartmentalMatters> departmentalMattersList = departmentalMattersDao.findByGroupId(groupId);
                    //循环更改
                    departmentalMattersList.forEach(departmentalMatters -> {
                        //更改部门ID
                        departmentalMatters.setDeptId(newDeptId);
                        departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
                    });
                } else {
                    String message = "\"" + department.getTitle() + "\" 与转以到的部门层级不相同";
                    throw new CustomException(new ReturnCode.Builder().failed().msg(message).build());
                }
            });
        } else {
            throw new CustomException(new ReturnCode.Builder().failed().msg("转移类型错误").build());
        }
    }

    /**
     * 推送所有目录及完成事项
     * @return ReturnCode
     */
    @Override
    public ReturnCode sendAllClasses() {

      List<ImportClassesReq> importClassesReqList =    departmentalMattersDao.sendAllClasses();

        return null;
    }

    /**
     * 推送单个事项
     * @param departmentalMattersId 事项id
     * @return ReturnCode
     */
    @Override
    public ReturnCode sendOneClasses(String departmentalMattersId) {


        return null;
    }

//    /**
//     * 定时任务，每天凌晨1分1秒执行
//     */
//    //测试使用 查询时间 间隔
//    final long LOOP_TIME = (1000 * 60) * 1;//1分钟
//    //测试使用 公示时间
//    final long PUBLIC_TIME = (1000 * 60) * 3;//3分钟
//    //测试使用 公示完可修改时间
//    final long PERSISTENCE_TIME = (1000 * 60) * 2;//2分钟
//
//    @Scheduled(fixedRate = LOOP_TIME)//定时任务，每天凌晨1分1秒执行
//
////    //查询间隔时间
////    final long LOOP_TIME = (1000*60) * 5;//5分钟
////    //公示时间
////    final long PUBLIC_TIME = (1000*60*60*24) * 3;//3天
////    //公示完可修改时间
////    final long PERSISTENCE_TIME = (1000*60*60*24) * 2;//2天
////    @Scheduled(cron = "01 01 00 * * ?")//定时任务，每天凌晨1分1秒执行
//    public void publicTask() {
//        List<DepartmentalMatters> list = departmentalMattersDao.findAllSubmited();
//
//        Logger log = LoggerFactory.getLogger(getClass());
//
//        if (list.isEmpty()) {
//            return;
//        }
//        for (int i = 0; i < list.size(); i++) {
//            DepartmentalMatters departmentalMatters = list.get(i);
//            if (departmentalMatters == null) {
//                continue;
//            }
//            //已提交状态
//            if (departmentalMatters.getState() == 3) {
//                //公示时间为空
//                if (departmentalMatters.getPublicityTime() == null || departmentalMatters.getPublicityTime().equals("")) {
//                    continue;
//                } else {
//                    //提交时间
//                    final long START_TIME = departmentalMatters.getPublicityTime().getTime();
//                    //在公示时间内
//                    if ((System.currentTimeMillis() - START_TIME) >= PUBLIC_TIME) {
//                        departmentalMatters.setState(4);
//                        departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
//                    }
//                }
//            } else if (departmentalMatters.getState() == 4) {  //进入公示完 可修改 时间
//                //公示完成时间  可修改时间开始
//                final long FINISH_PUBLIC_TIME = departmentalMatters.getPublicityTime().getTime() + PUBLIC_TIME;
//
//                if ((System.currentTimeMillis() - FINISH_PUBLIC_TIME) >= PERSISTENCE_TIME) {
//                    departmentalMatters.setState(5);
//                    departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
//                }
//            }
//        }
//    }
}
