package com.runonce.service.impl.eventversionone;

import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.DefaultSystemDockingPicDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.SystemDockingDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.model.eventversionone.DefaultSystemDockingPic;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.SystemDocking;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.model.http.ProcessDataParam;
import com.runonce.service.AbstractService;
import com.runonce.service.eventversionone.SystemDockingService;
import com.runonce.util.ImageUtil;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 系统对接service实现
 * @Date: create at 2018/12/29 0029 下午 4:50
 */
@Service
public class SystemDockingServiceImpl  extends AbstractService<SystemDocking> implements SystemDockingService {

    @Resource
    private SystemDockingDao systemDockingDao;

    @Autowired
    private DepartmentalMattersServiceImpl departmentalMattersService;

    @Resource
    private DefaultSystemDockingPicDao defaultSystemDockingPicDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private VersionmanagementDao versionmanagementDao;
    /**
     * 系统对接记录保存
     * @param systemDocking
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode saveInfo(SystemDocking systemDocking, HttpServletRequest request) {
        // 事项存在校验
        DepartmentalMatters event = departmentalMattersService.findById(systemDocking.getEventId());
        if(event == null){
            return  new ReturnCode.Builder().failed().msg("该事项不存在!").object(systemDocking).build();
        }
        if("3".equals(systemDocking.getPicType())){
            DefaultSystemDockingPic picRecord = defaultSystemDockingPicDao.selectByPrimaryKeyOvrride(systemDocking.getDefaultPicId());
            if(picRecord ==null){
                return  new ReturnCode.Builder().failed().msg("默认对接图不存在!").object(systemDocking).build();
            }
        }
//        if(systemDocking.getPicType().equals("2")){
//            ProcessDataParam param = new ProcessDataParam();
//            param.setXmlData(systemDocking.getPicData());
//            param.setCodeedXmlString(systemDocking.getCodeedXmlString());
//            param.setWidth(systemDocking.getWidth());
//            param.setHeight(systemDocking.getHeight());
//            String filePath = ImageUtil.generatePng(param,request);
//            systemDocking.setPicPath(filePath);
//        }

        //数据库中存储的版本号
        Integer version = versionmanagementDao.getSheetVersion(systemDocking.getEventId(),10);
        if(version == null ){
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }
        //前端得到的版本号（点击配置按钮时的版本）
        Integer version1 = systemDocking.getVersion();
        //版本号相同，修改，不相同回滚
        if(version1.equals(version)){
            // 原有记录删除
            Condition condition = new Condition(MaterialGroupAddParam.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("eventId",systemDocking.getEventId());
            mapper.deleteByCondition(condition);
            // 纪录新增
            systemDocking.setId(SysUtil.getUUID());
            systemDockingDao.insert(systemDocking);
        }else{
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
        }
        //更新修改用户
        event.setOperator(WebTokenUtil.getUserByWebToken(request));
        // 更新事项相关状态
        event.setSystemDockingState(systemDocking.getState());
        departmentalMattersService.update(event);
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(systemDocking.getEventId(),10);
        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectPidById(systemDocking.getEventId());
        //更新父部门状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);
        return new ReturnCode.Builder().failed().success().msg("系统对接方案保存成功").object(systemDocking).build();
    }

    /**
     * 按事项id查询
     * @param eventId
     * @return
     */
    @Override
    public ReturnCode selectByEventId(String eventId) {
        Condition condition = new Condition(SystemDocking.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("eventId",eventId);
        List<SystemDocking> systemDockingList = mapper.selectByCondition(condition);
        SystemDocking result = null;
        if(systemDockingList != null &&systemDockingList.size() > 0){
            result = systemDockingList.get(0);
        }else{
            result = new SystemDocking();
        }
        Integer verson = versionmanagementDao.getSheetVersion(eventId,10);
        if(verson == null){
            verson = 1;
        }
        result.setVersion(verson);
        return new ReturnCode.Builder().failed().success().msg("系统对接查询成功").object(result).build();
    }
}
