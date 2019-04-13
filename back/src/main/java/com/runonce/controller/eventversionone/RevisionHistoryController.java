package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.RevisionHistoryDao;
import com.runonce.model.system.Role;
import com.runonce.service.system.RoleService;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.runonce.dao.eventversionone.RevisionHistoryDao;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.RevisionHistory;
import com.runonce.service.eventversionone.RevisionHistoryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.runonce.dao.eventversionone.DepartmentalMattersDao;

/**
 * 变更履历
 * <p>
 * Created by xuxueli on '2019-02-19 10:28:47'.
 */

@RestController
@RequestMapping("revisionhistory")
public class RevisionHistoryController {

    @Resource
    private RevisionHistoryService revisionhistoryService;
    @Resource
    private RoleService roleService;

    @Resource
    private RevisionHistoryDao revisionHistoryDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @PostMapping("add")
    public ReturnCode add(@RequestBody @Valid RevisionHistory revisionhistory, HttpServletRequest request, BindingResult bindingResult) {

        //输入校验
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        String userId = WebTokenUtil.getUserByWebToken(request);
        revisionhistory.setWho(userId);
        revisionhistory.setState(2);
        revisionhistoryService.add(revisionhistory);
        return new ReturnCode.Builder().msg("添加成功").success().object(revisionhistory).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        revisionhistoryService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    public ReturnCode update(@RequestBody RevisionHistory revisionhistory) {
        revisionhistoryService.updateInfo(revisionhistory);
        return new ReturnCode.Builder().msg("更新成功").object(revisionhistory).success().build();
    }

    @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        RevisionHistory revisionhistory = revisionhistoryService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(revisionhistory).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<RevisionHistory> list = revisionhistoryService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @GetMapping("getByDmId")
    public ReturnCode getByDmId(@RequestParam String id) {
        List<RevisionHistory> revisionhistory = revisionhistoryService.findByDmId(id);
        return new ReturnCode.Builder().msg("查询成功").object(revisionhistory).success().build();
    }

    //外面按钮权限
    @GetMapping("curriculumVitae")
    public ReturnCode curriculumVitae(@RequestParam String dmId, HttpServletRequest request) {
        RevisionHistory revisionHistory = revisionHistoryDao.selectLastInfo(dmId);
        if (revisionHistory != null) {
            return new ReturnCode.Builder().success().object(true).msg("正常访问").build();
        }
        return new ReturnCode.Builder().success().object(false).msg("用户无访问权限").build();
    }


    //超管//审批状态未2  而且是最新
    @GetMapping("SuperButton")
    public ReturnCode SuperButton(@RequestParam String id, HttpServletRequest request) {
        String userId = WebTokenUtil.getUserByWebToken(request);
        List<Role> role = roleService.findByUserRole(userId);
        if (role != null && role.size() > 0) {
            //超级管理员
            if (role.get(0).getDataType() == 0) {
                //查询当前履历是不是最新的  而且是1.4
                RevisionHistory revisionHistory = revisionHistoryDao.selectRevisionhistoryById(id);
                if (revisionHistory != null) {
                    if (revisionHistory.getState() == 2) {
                        Integer region = departmentalMattersDao.SelectDmRegion(revisionHistory.getDmId());
                        if (region <= 2) {
                            return new ReturnCode.Builder().success().object(true).msg("正常访问").build();
                        }
                    }
                }
                //部门管理员
            } else if (role.get(0).getDataType() == 1) {

                //查询当前履历是不是最新的  而且是1.4
                RevisionHistory revisionHistory = revisionHistoryDao.selectRevisionhistoryById(id);
                if (revisionHistory != null) {
                    if (revisionHistory.getState() == 2) {
                        Integer region = departmentalMattersDao.SelectDmRegion(revisionHistory.getDmId());
                        if (region > 2) {
                            return new ReturnCode.Builder().object(true).success().msg("正常访问").build();
                        }
                    }
                }
            }
        }
        return new ReturnCode.Builder().msg("用户无访问权限").success().object(false).build();
    }

    @GetMapping("ComerButton")
    public ReturnCode ComerButton(@RequestParam String dmId, HttpServletRequest request) {
        String userId = WebTokenUtil.getUserByWebToken(request);
        List<Role> role = roleService.findByUserRole(userId);
        if (role != null && role.size() > 0) {
            if (role.get(0).getDataType() == 2 || role.get(0).getDataType() == 1) {
                RevisionHistory revisionHistory = revisionHistoryDao.selectLastInfo(dmId);
                if (revisionHistory == null) {
                    return new ReturnCode.Builder().success().object(true).build();
                } else if (revisionHistory.getState() == 1 || revisionHistory.getState() == 4) {
                    return new ReturnCode.Builder().success().object(true).build();
                }
            }
        }
        return new ReturnCode.Builder().msg("用户无访问权限").success().object(false).build();
    }
}
