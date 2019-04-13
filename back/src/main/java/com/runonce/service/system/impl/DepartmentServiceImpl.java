package com.runonce.service.system.impl;

import com.runonce.ReturnCode;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.Group;
import com.runonce.httpbean.assets.reqbean.Place;
import com.runonce.httpbean.assets.reqbean.SendPlaceParam;
import com.runonce.model.system.Role;
import com.runonce.model.system.User;
import com.runonce.service.eventpublic.FeignServer;
import com.runonce.service.system.UserService;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.model.system.Department;
import com.runonce.service.system.DepartmentService;
import com.runonce.util.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门接口实现
 *
 * @author Exrick
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final FeignServer feignServer;

    private final DepartmentDao departmentDao;

    private final UserService userService;
    @Resource
    private DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentServiceImpl(FeignServer feignServer, DepartmentDao departmentDao, UserService userService) {
        this.feignServer = feignServer;
        this.departmentDao = departmentDao;
        this.userService = userService;
    }


    @Override
    public DepartmentDao getRepository() {
        return departmentDao;
    }

    @Override
    public List<Department> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter, String userId, Boolean IsLocation) {
        List<Department> ReturndDpartments = new ArrayList<>();
        if (IsLocation != null) {
            List<Department> departments = departmentDao.findByIsLocationOrderBySortOrder(IsLocation);

            getBelongDept(parentId, departments, ReturndDpartments);
            return ReturndDpartments;
        } else {
            return departmentDao.findByParentIdOrderBySortOrder(parentId);

        }

    }

    @Override
    public List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status) {

        return departmentDao.findByParentIdAndStatusOrderBySortOrder(parentId, status);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Department> findAll(Boolean openDataFilter, String userId, String isLocation) {

        //未开启权限
        if (!openDataFilter) {
            return departmentDao.findAll();
        }
        List<Department> departmentList = new ArrayList<>();

        switch (isLocation) {
            //true
            case "1":
                departmentList = userService.getLocationIdList(userId, openDataFilter);
                break;
            //false
            case "2":
                departmentList = departmentDao.findByDelFlagAndIsLocationOrderBySortOrder(0, true);

                break;
            //null
            case "3":
                departmentList = departmentDao.findAll();
                break;
                default:

        }

        return departmentList;
    }

    /**
     * 批量删除
     * @param idList
     */
    @Override
    public void deleteByIdIn(List<String> idList) {
        departmentDao.deleteByIdIn(idList);
    }

    @Override
    public int countByTitleAndParentIdAndDelFlag(String title, String parentId) {
        int count = departmentDao.countByTitleAndParentIdAndDelFlag(title, parentId, 0);
        return count;
    }

    @Override
    public int countByTitleAndParentIdAndDelFlagAndIdNot(String title, String parentId, String id) {

        int count = departmentDao.countByTitleAndParentIdAndDelFlagAndIdNot(title, parentId, 0, id);


        return count;
    }

    /**
     * 查询是否有子部门
     *
     * @param parentIdList
     * @return
     */
    @Override
    public int countByParentIdInAndDelFlag(List<String> parentIdList) {

        int count = departmentDao.countByParentIdInAndDelFlag(parentIdList, 0);

        return count;
    }

    /**
     * 获取可选部门下拉菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map> getDropList(String userId, boolean openDataFilter) {


        User user = userService.findByUserId(userId);

        int flag = 2;
        for (Role r : user.getRoles()) {
            if (r.getDataType() < flag) {
                flag = r.getDataType();
            }
        }

        List<Map> departmentMapList = new ArrayList<>();
        //不开启数据权限
        if (!openDataFilter) {
            return null;
        }

        //所有权限
        if (flag == CommonConstant.DATA_TYPE_ALL) {
            departmentMapList = departmentMapper.selectByIdOrParentId(null, "0");
        }
        //所有部门和部门子部门
        if (flag == CommonConstant.DATA_TYPE_DEPT_CUSTOM) {
            departmentMapList = departmentMapper.selectByIdOrParentId(null, user.getDepartmentId());
            //  return departmentIds;
        }
        //当前部门
        if (flag == CommonConstant.DATA_TYPE_CARDING) {


            departmentMapList = departmentMapper.selectByIdOrParentId(user.getDepartmentId(), null);
        }


        return departmentMapList;
    }


    /**
     * 获取下拉子菜单
     *
     * @param parentId
     * @param openDataFilter
     * @return
     */
    @Override
    public List<Map> getDropSonList(String parentId, boolean openDataFilter) {


        List<Map> departmentMapList = departmentMapper.selectByIdOrParentId(null, parentId);
        return departmentMapList;
    }

    public void getBelongDept(String deptId, List<Department> allDept, List<Department> departmentsIdList) {

        List<Department> list = allDept.stream().filter(a -> a.getParentId().equals(deptId)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {
            for (Department item : list) {
                departmentsIdList.add(item);
            }
            for (Department item : list) {
                getBelongDept(item.getId(), allDept, departmentsIdList);
            }
        }
    }

    @Override
    public List<String> findDeptGly(String deptId) {
        Department department = departmentDao.getOne(deptId);
        String departmentPid = department.getParentId();
        List<String> userIds;
        //查找部门
        userIds = departmentMapper.selectDeptGly(deptId);
        boolean isTrue = false;
        if (userIds != null && userIds.size() > 0) {
        } else {
            isTrue = true;
        }
        while (isTrue) {
            //找父部门
            Department departmentP = departmentDao.getOne(departmentPid);
            //将ID放置到List
            // deptIdList.add(departmentP.getId());
            //继续查找父
            departmentPid = departmentP.getParentId();
            //直到找到地点
            userIds = departmentMapper.selectDeptGly(departmentP.getId());
            if (userIds != null && userIds.size() > 0 || departmentP.getIsLocation() == true) {
                isTrue = false;
            }
        }

        return userIds;
    }

    /**
     * 查询超级管理员
     *
     * @return
     */
    @Override
    public List<String> selectGly() {
        return departmentMapper.selectGly();
    }

    /**
     * 查询论证员
     *
     * @return
     */
    @Override
    public List<String> selectReviewGly() {
        return departmentMapper.selectReviewGly();
    }

    /**
     * 查询所有的地点
     * @return
     */
    @Override
    public List<String> selectAllLocation() {
        return null;
    }

    /**
     * 地点(部门)下是否有子部门（事项）
     * @param deptId
     * @return
     */
    @Override
    public void checkHasChildren(String deptId){
        //部门下是否有子部门
        Integer count = departmentMapper.findChildrenById(deptId);
        if (count > 0){
            throw new CustomException(new ReturnCode.Builder().failed().msg("请先删除子部门").build());
        }
        //部门下的事项
        count = departmentMapper.findEventById(deptId);
        if(count > 0){
            throw new CustomException(new ReturnCode.Builder().failed().msg("请先删除该部门下的事项").build());
        }
    }

    @Override
    public ReturnCode sendPlace(String areaCode){
        Place department = departmentMapper.sendPlace(areaCode);
        List<Group> groupList = departmentMapper.sendGroup(areaCode);
        SendPlaceParam sendPlaceParam = new SendPlaceParam();
        sendPlaceParam.setPlace(department);
        sendPlaceParam.setGroupList(groupList);
        return feignServer.sendPlace(sendPlaceParam);
    }
}