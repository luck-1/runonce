package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.exception.CustomException;
import com.runonce.model.http.EventDirectoryParam;
import com.runonce.model.system.Department;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.EventDirectoryService;
import com.runonce.dao.eventversionone.EventDirectoryDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.EventDirectory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 三级四同目录
 * <p>
 * Created by xuxueli on '2019-01-16 12:00:31'.
 */
@Service
public class EventDirectoryServiceImpl extends AbstractService<EventDirectory> implements EventDirectoryService {

    @Resource
    private EventDirectoryDao eventDirectoryDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode add(EventDirectory eventDirectory) {
        if (eventDirectory.getId() == null || eventDirectory.getId().equals("")) {
            eventDirectory.setId(SysUtil.getUUID());
        }
        eventDirectoryDao.insert(eventDirectory);
        return new ReturnCode.Builder().success().msg("三级四同目录保存成功").object(eventDirectory).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode updateEventDirectory(EventDirectory eventDirectory) {
        if (eventDirectory.getId() == null || eventDirectory.getId().equals("")) {
            return new ReturnCode.Builder().failed().msg("目录ID不存在").build();
        }
        EventDirectory eventDirectory1 = eventDirectoryDao.selectByPrimaryKey(eventDirectory.getId());
        if (eventDirectory1 == null) {
            return new ReturnCode.Builder().failed().msg("该目录不存在").build();
        }
        eventDirectoryDao.updateByPrimaryKey(eventDirectory);
        return new ReturnCode.Builder().success().msg("三级四同目录更改成功").object(eventDirectory).build();
    }

    @Override
    public ReturnCode findEventDirectory(EventDirectoryParam eventDirectoryParam) {
        Integer page = eventDirectoryParam.getPage();
        Integer size = eventDirectoryParam.getSize();
        if (page == null && size == null) {
            return new ReturnCode.Builder().failed().msg("分页参数错误").build();
        }
        PageHelper.startPage(page, size);
        List<EventDirectory> list = eventDirectoryDao.findEventDirectory(
                eventDirectoryParam.getImplementatioDepartment(),
                eventDirectoryParam.getGenericName(),
                eventDirectoryParam.getProvincialName(),
                eventDirectoryParam.getBigTypesOfEvents(),
                eventDirectoryParam.getItemSubtype());
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().success().msg("三同四从目录查询成功").object(pageInfo).build();
    }

    @Override
    public Boolean findGroupIdCount(String eventName,String id) {
        int count = eventDirectoryDao.findGroupIdCount(eventName,id);
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<EventDirectory> findPrivate(String deptId){
        Department department = departmentDao.findByIdAndDelFlag(deptId,0);
        List<EventDirectory> list;
        //如果部门对应地区是市级 循环得到其所在的节点
        if(department.getRegion().equals("2")){
            //循环直到为 is_location = 1
            while (true){
                if(department.getIsLocation() == true ){
                    //如果 region > 2 返回所有县级私有三级四同
                    if(Integer.parseInt(department.getRegion()) > 2){
                        list = eventDirectoryDao.findPrivate();
                        break;
                    }else {
                        //不满足 返回所有
                        return null;
                    }
                }
                //得到父
                department = departmentDao.findByIdAndDelFlag(deptId,0);
                //得到父ID
                deptId = department.getParentId();
            }
            return list;
        }else{
            return null;
        }
    }

    @Override
    public List<EventDirectory> findCatalogList(String deptId) {

        Department department = departmentMapper.selectByPrimaryKey(deptId);
        if(department == null){
            throw new CustomException(new ReturnCode.Builder().failed().msg("数据异常：部门不存在！").build());
        }
        String region = department.getRegion();

        if(region==null){

            return null;
        }
        List<EventDirectory> returnCatalogList = findPrivate(deptId);

        if(returnCatalogList != null){
            return returnCatalogList;
        }else {
            switch (region) {
                case "1":
                    returnCatalogList = eventDirectoryDao.findCatalogListByProvince();
                    break;
                case "2":
                    returnCatalogList = eventDirectoryDao.findCatalogListByCity();
                    break;
                case "3":
                    returnCatalogList = eventDirectoryDao.findCatalogListByArea();
                    break;
                default:
                    returnCatalogList = eventDirectoryDao.findCatalogListByCurrency();
                    break;
            }
        }

        return returnCatalogList;
    }

    @Override
    public Integer checkDirectoryIsUsed(String Id) {

       Integer returnCode= eventDirectoryDao.checkDirectoryIsUsed(Id);
        return returnCode;
    }
}
