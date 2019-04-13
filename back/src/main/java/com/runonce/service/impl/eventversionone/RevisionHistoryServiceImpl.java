package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.runonce.dao.eventpublic.EventsTableDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.MatterproposalDao;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.Tasktodo;
import com.runonce.service.eventpublic.EventsTableService;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.eventversionone.TasktodoService;
import com.runonce.service.system.DepartmentService;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.RevisionHistoryService;
import com.runonce.dao.eventversionone.RevisionHistoryDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.RevisionHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 变更履历
 * <p>
 * Created by xuxueli on '2019-02-19 10:28:47'.
 */
@Service
public class RevisionHistoryServiceImpl extends AbstractService<RevisionHistory> implements RevisionHistoryService {

    @Resource
    private RevisionHistoryDao revisionhistoryDao;

    @Resource
    private MatterproposalDao matterproposalDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private EventsTableService eventsTableService;

    @Resource
    private TasktodoService tasktodoService;

    @Resource
    private DepartmentService departmentdService;


    private DepartmentMapper departmentMapper;

    @Override
    public void add(RevisionHistory revisionhistory) {

        if (revisionhistory.getId() == null || "".equals(revisionhistory.getId())) {
            revisionhistory.setId(SysUtil.getUUID());
        }
        //设置版本号
        RevisionHistory revisionHistoryMaxVersion = revisionhistoryDao.selectVersionByDmId(revisionhistory.getDmId());
        if (revisionHistoryMaxVersion != null) {

            String versionNumber = revisionHistoryMaxVersion.getVersionNumber();

            if (versionNumber != null) {

                versionNumber = versionNumber.replace(".", "");
                Integer versionNumberInt = Integer.parseInt(versionNumber);
                versionNumberInt++;
                String versionStr = versionNumberInt + "";
                String result = "";
                for (int i = 0; i < versionStr.length(); i++) {
                    result += versionStr.charAt(i) + ".";
                }
                revisionhistory.setVersionNumber(result);
            }
        } else {
            revisionhistory.setVersionNumber("1.0.0");
        }
        revisionhistory.setDateOfChange(new Date());

        //如果状态为2时  通知部门管理员
        if (revisionhistory.getState() == 2) {
            sendRevision(revisionhistory.getDmId());
        }
        revisionhistoryDao.insert(revisionhistory);

    }

    /**
     * 推送事项变更审批
     * @param dmId
     * @return
     */
    public Boolean sendRevision(String dmId) {

        String eventName = departmentalMattersService.getEventName(dmId);
        DepartmentalMatters departmentalMatters=departmentalMattersService.findById(dmId);
        Integer region = departmentalMattersDao.SelectDmRegion(dmId);
        List<String> userIds=new ArrayList<>();
        if (region > 2) {
         //查找部门管理员
         userIds=departmentdService.findDeptGly(departmentalMatters.getDeptId());
        } else {
         //查找超级管理员
         userIds = departmentdService.selectGly();
        }
        tasktodoService.sendTodo(dmId, eventName,1,userIds,"事项已申请变更,请及时审批");
        return true;

    }

    @Override
    public List<RevisionHistory> findByDmId(String id) {
        return revisionhistoryDao.findByDmId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInfo(RevisionHistory revisionhistory) {
        if (revisionhistory.getState() != 0) {
            if (revisionhistory.getState() == 3) {
                DepartmentalMatters dm = new DepartmentalMatters();
                dm.setId(revisionhistory.getDmId());
                dm.setExamineAndApproveState(1);
                dm.setChangeOfResumeVersion(revisionhistory.getId());
                departmentalMattersDao.updateByPrimaryKeySelective(dm);
                //更新事项状态
                if (revisionhistory.getDmId() != null) {
                    //获取事项id 是否有父
                    String departmentalMattersPid = departmentalMattersDao.selectPidById(revisionhistory.getDmId());
                    //更新父部门状态
                    departmentalMattersService.updateFatherExamineAndApproveState(departmentalMattersPid);
                }
                //重置sheet页审批状态
                matterproposalDao.resetState(revisionhistory.getDmId());
            }
        }
        RevisionHistory revisionHistoryDb = revisionhistoryDao.selectById(revisionhistory.getId());
        if (revisionhistory.getReasons() != null && !"".equals(revisionhistory.getReasons())) {
            revisionhistory.setChangeContent(revisionHistoryDb.getChangeContent() + "</br>" + "拒绝原因: " + revisionhistory.getReasons());
        }

        //给当事人推送消息
        String content="";
        if (revisionhistory.getState() == 3) {
            content  =  "事项变更申请审核通过，请尽快变更";
        } else if (revisionhistory.getState() == 4) {
            content= "事项变更申请审核驳回，请查看";
        }

      String eventName=  departmentalMattersService.getEventName(revisionHistoryDb.getDmId());
      List<String> userIdList=new ArrayList<>();
      userIdList.add(revisionHistoryDb.getWho());
      tasktodoService.sendTodo(revisionhistory.getDmId(),eventName,1,userIdList,content);

      revisionhistoryDao.updateByPrimaryKeySelective(revisionhistory);
    }
}
