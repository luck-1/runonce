package com.runonce.controller.eventversionone;

import java.util.Date;
import java.util.List;

import com.runonce.dao.eventversionone.RevisionHistoryDao;
import com.runonce.httpbean.assets.reqbean.MatterproposalReq;
import com.runonce.httpbean.assets.reqbean.SelectSheetStateReq;
import com.runonce.httpbean.assets.resbean.MatterproposalRes;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.RevisionHistory;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.system.UserService;
import com.runonce.util.WebTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.Matterproposal;
import com.runonce.service.eventversionone.MatterproposalService;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 事项建议
 * <p>
 * Created by xuxueli on '2019-01-04 14:23:27'.
 */

@RestController
@RequestMapping("matterproposal")
@Api(description = "事项建议")
public class MatterproposalController {

    @Autowired
    private MatterproposalService matterproposalService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @Autowired
    private UserService userService;

    @Resource
    private RevisionHistoryDao revisionHistoryDao;


    @GetMapping("selectListInfo/{departmentalMattersId}")
    @ApiOperation(value = "查询审批列表详情")
    public ReturnCode selectListInfo(@PathVariable String departmentalMattersId) {
        //获取当前登录用户
        List<Matterproposal> matterProposalList = matterproposalService.selectListInfo(departmentalMattersId);
        return new ReturnCode.Builder().success().msg("查询成功").object(matterProposalList).build();
    }


//    @PostMapping("add")
//    @ApiOperation(value = "新增审批审核")
//    public ReturnCode add(@RequestBody @Valid Matterproposal  matterproposal, HttpServletRequest request , BindingResult bindingResult) {
//        //验证是否有错，如果有错就输出错误信息
//        if(bindingResult.hasErrors()){
//            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
//        //获取当前登录用户
//        String userId = WebTokenUtil.getUserByWebToken(request);
//        return  matterproposalService.add(matterproposal,userId);
//    }
//
//    @PostMapping("addLeadercomment")
//    @ApiOperation(value = "新增部门审核")
//    public ReturnCode addLeaderComment(@RequestBody @Valid Matterproposal  matterproposal, HttpServletRequest request,BindingResult bindingResult) {
//
//        //验证是否有错，如果有错就输出错误信息
//        if(bindingResult.hasErrors()){
//            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
//        //获取当前登录用户
//        String userId = WebTokenUtil.getUserByWebToken(request);
//
//        return  matterproposalService.addLeaderComment(matterproposal,userId);
//    }


    @GetMapping("savePass/{MatterProposalId}")
    @ApiOperation(value = "通过审核")
    public ReturnCode savePass(@PathVariable String MatterProposalId, HttpServletRequest request) {

        //获取当前登录用户
        String userId = WebTokenUtil.getUserByWebToken(request);

        return matterproposalService.savePass(MatterProposalId, userId);
    }

    @GetMapping("launchedExamineAndApprove/{departmentalMattersId}")
    @ApiOperation(value = "发起评审")
    public ReturnCode launchedExamineAndApprove(@PathVariable String departmentalMattersId, HttpServletRequest request) {

        //获取当前登录用户
        String userId = WebTokenUtil.getUserByWebToken(request);


        //验证是否已经发起审核审批
        Condition condition = new Condition(Matterproposal.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("departmentalMattersId", departmentalMattersId);
        criteria.andEqualTo("isApproval", SystemBean.EXAMINE_APPROVAL);
        List<Matterproposal> matterProposalList = matterproposalService.findByCondition(condition);

        RevisionHistory revisionHistory = revisionHistoryDao.selectLastInfo(departmentalMattersId);

        if (matterProposalList != null && matterProposalList.size() > 0 && !(revisionHistory != null && revisionHistory.getState() == 3)) {
            return new ReturnCode.Builder().failed().msg("该事项已发起评审，请勿重复发起").build();

        }
        //验证是否通过部门审批
        DepartmentalMatters departmentalMatters = departmentalMattersService.findById(departmentalMattersId);
        if (!SystemBean.DEPT_LEADER_AGREE.equals(departmentalMatters.getExamineAndApproveState())) {
            return new ReturnCode.Builder().failed().msg("该事项未通过部门管理员审批无法发起评审").build();
        }


        departmentalMattersService.launchedExamineAndApprove(departmentalMattersId, userId);
        return new ReturnCode.Builder().success().msg("发起评审成功").build();
    }


    @DeleteMapping("delete")
    @ApiOperation(value = "删除")
    public ReturnCode delete(@RequestParam String id) {
        matterproposalService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    @ApiOperation(value = "修改")
    public ReturnCode update(@RequestBody Matterproposal matterproposal) {
        matterproposalService.update(matterproposal);
        return new ReturnCode.Builder().msg("更新成功").object(matterproposal).success().build();
    }

    @GetMapping("detail")
    @ApiOperation(value = "根据id查询")
    public ReturnCode detail(@RequestParam String id) {
        Matterproposal matterproposal = matterproposalService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(matterproposal).success().build();
    }

    @PostMapping("list")
    @ApiOperation(value = "查询全部")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Matterproposal> list = matterproposalService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @PostMapping("findbyproperty")
    @ApiOperation(value = "根据类型查询")
    public ReturnCode findByProperty(@RequestBody Matterproposal matterproposal) {

        List<Matterproposal> list = matterproposalService.findByProperty(matterproposal);

        return new ReturnCode.Builder().msg("查询成功").object(list).success().build();
    }

    @PostMapping("findbypropertyLeader")
    @ApiOperation(value = "根据类型查询部门经理意见")
    public ReturnCode findbypropertyLeader(@RequestBody Matterproposal matterproposal) {

        List<Matterproposal> list = matterproposalService.findbypropertyLeader(matterproposal);

        return new ReturnCode.Builder().msg("查询成功").object(list).success().build();
    }


    //全查事项所有的评审信息
    @PostMapping("findAllByCondition")
    @ApiOperation(value = "全查事项所有的评审信息")
    public ReturnCode findAllByCondition(@RequestBody MatterproposalReq matterproposalReq) {

        Date startTime = matterproposalReq.getStartTime();
        Date endTime = matterproposalReq.getEndTime();
        Date now = new Date();
        if (startTime != null && endTime == null) {
            if (startTime.after(now)) {

                return new ReturnCode.Builder().msg("查询失败 请检查时间区间条件").error().build();
            }
        }
        if (endTime != null && startTime == null) {
            if (endTime.before(now)) {

                return new ReturnCode.Builder().msg("查询失败 请检查时间区间条件").error().build();
            }
        }


        PageInfo<MatterproposalRes> list = matterproposalService.findAllByCondition(matterproposalReq);

        return new ReturnCode.Builder().msg("查询成功").object(list).success().build();

    }

    /**
     * @return
     */
    @PostMapping("selectSheetState")
    @ApiOperation(value = "查询sheet页状态")
    public ReturnCode selectSheetState(@RequestBody SelectSheetStateReq SelectSheetStateReq) {

        Matterproposal matterproposal = matterproposalService.selectSheetState(SelectSheetStateReq);

        return new ReturnCode.Builder().msg("查询成功").object(matterproposal).success().build();

    }


}
