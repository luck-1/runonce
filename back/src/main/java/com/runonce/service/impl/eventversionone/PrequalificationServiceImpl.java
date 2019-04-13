package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.PrequalificationParam;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.PrequalificationService;
import com.runonce.dao.eventversionone.PrequalificationDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.Prequalification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by xuxueli on '2018-12-10 11:31:13'.
 */
@Service
public class PrequalificationServiceImpl extends AbstractService<Prequalification> implements PrequalificationService {

    @Resource
    private PrequalificationDao prequalificationDao;

    @Autowired
    private DepartmentalMattersServiceImpl departmentalMattersService;

    @Override
    public List<Prequalification> findByEventId(String eventId) {
        return prequalificationDao.findByEventId(eventId);
    }

    @Override
    public void deleteByEventId(String eventId) {
        prequalificationDao.deleteByEventId(eventId);
    }

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode saveInfo(PrequalificationParam prequalificationParam, HttpServletRequest request) {
        // 事项存在校验
        DepartmentalMatters event = departmentalMattersService.findById(prequalificationParam.getEventId());
        if (event == null) {
            return new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }
        //数据库中存储的版本号
        Integer version1 = versionmanagementDao.getSheetVersion(prequalificationParam.getEventId(),4);
        if(version1 == null ){
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }
        //前端得到的版本号（点击配置按钮时的版本）
        Integer version2 = prequalificationParam.getVersion();

        if(version1.equals(version2)){
            // 按事项id删除记录
            deleteByEventId(prequalificationParam.getEventId());
            // 审核记录新增
            if (prequalificationParam.getPrequalificationList() != null && prequalificationParam.getPrequalificationList().size() > 0) {
                for (Prequalification item : prequalificationParam.getPrequalificationList()) {
                    item.setId(SysUtil.getUUID());
                    mapper.insert(item);
                }
            }
        }else{
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
        }
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(prequalificationParam.getEventId(),4);
        //更新修改用户
        event.setOperator(WebTokenUtil.getUserByWebToken(request));
        // 事项相关状态修改
        event.setPrequalificationState(prequalificationParam.getState());
        departmentalMattersService.update(event);
        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectPidById(event.getId());
        //更新父部门状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);
        return new ReturnCode.Builder().success().msg("资格预审保存成功").build();
    }
}
