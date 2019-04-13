package com.runonce.controller.eventversionone;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.runonce.httpbean.assets.reqbean.CopyMattersReq;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.http.*;
import com.runonce.model.system.Role;
import com.runonce.service.system.RoleService;
import com.runonce.util.WebTokenUtil;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.service.eventversionone.DepartmentalMattersService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * 部门事项
 * <p>
 * Created by xuxueli on '2018-12-12 16:38:30'.
 */

@RestController
@RequestMapping("departmentalMatters")
public class DepartmentalMattersController {

    @Autowired
    private DepartmentalMattersService departmentalMattersService;


    @Autowired
    private RoleService roleService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody DepartmentalMatters departmentalMatters) {

        departmentalMattersService.save(departmentalMatters);
        return new ReturnCode.Builder().msg("添加成功").success().object(departmentalMatters).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        departmentalMattersService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    public ReturnCode update(@RequestBody DepartmentalMatters departmentalMatters) {
        departmentalMattersService.update(departmentalMatters);
        return new ReturnCode.Builder().msg("更新成功").object(departmentalMatters).success().build();
    }

    @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        DepartmentalMatters departmentalMatters = departmentalMattersService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(departmentalMatters).success().build();
    }
//   @GetMapping("selectbydeptid/{deptid}")
//    public ReturnCode selectByDeptId(@PathVariable("deptid") String deptid) {
//       List<DepartmentalMattersAndDepartmentRes> departmentalMatters = departmentalMattersService.selectByDeptId(deptid);
//        return new ReturnCode.Builder().msg("查询成功").object(departmentalMatters).success().build();
//    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DepartmentalMatters> list = departmentalMattersService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @ApiOperation(value = "查询办理项")
    @GetMapping("findDealCount")
    public ReturnCode findDealCount() {
        DealCount dealCount = departmentalMattersService.findDealCount();
        return new ReturnCode.Builder().success().msg("办理项查询成功").object(dealCount).build();
    }

    @ApiOperation(value = "事项级别统计")
    @GetMapping("findEventLevel")
    public ReturnCode findEventLevel() {
        EventLevel eventLevel = departmentalMattersService.findEventLevel();
        return new ReturnCode.Builder().success().msg("事项级别统计查询成功").object(eventLevel).build();
    }

    @ApiOperation(value = "各地点事项统计")
    @GetMapping("findEveryAreaCount")
    public ReturnCode findEveryAreaCount() {
        List<Map<String,Object>> list = departmentalMattersService.findEveryAreaCount();
        return new ReturnCode.Builder().success().msg("各地点事项查询成功").object(list).build();
    }

    @ApiOperation(value = "事项转移")
    @PutMapping("changeDept")
    public ReturnCode changeDept(@RequestBody ChangeDepartmentParam changeDepartmentParam) {
        if(changeDepartmentParam == null ){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        if(changeDepartmentParam.getIds() == null || changeDepartmentParam.getIds().size() == 0){
            return new ReturnCode.Builder().failed().msg("转移的事项为空").build();
        }
        if(changeDepartmentParam.getNewDeptId() == null || "".equals(changeDepartmentParam.getNewDeptId())){
            return new ReturnCode.Builder().failed().msg("转移部门为空").build();
        }
        if(changeDepartmentParam.getType() == null){
            return new ReturnCode.Builder().failed().msg("转移类型为空").build();
        }
        departmentalMattersService.changeDept(changeDepartmentParam.getIds(),changeDepartmentParam.getNewDeptId(),changeDepartmentParam.getType());
        return new ReturnCode.Builder().success().msg("各地点事项查询成功").build();
    }

    /**
     * 根据当前登录用户权限以及条件查询事项
     *
     * @param param
     * @param request
     * @return
     */
    @PostMapping("selectbyparam")
    public ReturnCode selectDepartmentalMattersListByParam(@RequestBody SelectDepartmentalMattersListParam param, HttpServletRequest request) {

        Map map = null;
        try {
            map = departmentalMattersService.selectDepartmentalMattersListByParam(param, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnCode.Builder().failed().msg("事项查询失败").object(map).build();
        }
        return new ReturnCode.Builder().success().msg("事项查询成功").object(map).build();
    }

    /**
     * 只根据条件查询事项(不根据用户权限)
     *
     * @param param
     * @return
     */
    @PostMapping("select")
    public ReturnCode selectAllByParam(@RequestBody SelectDepartmentalMattersListParam param) {
        if(param == null){
            return new ReturnCode.Builder().success().msg("事项查询成功").build();
        }
        Map map;
        try {
            map = departmentalMattersService.selectAllByParam(param);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnCode.Builder().failed().msg("事项查询失败").build();
        }
        return new ReturnCode.Builder().success().msg("事项查询成功").object(map).build();
    }

    /**
     * 修改状态
     *
     * @param id
     * @return
     */
    @GetMapping("updateState")
    public ReturnCode updateState(@RequestParam String id, @RequestParam String state) {
        departmentalMattersService.updateState(id, state);
        return new ReturnCode.Builder().msg("修改成功").success().build();
    }


    @PostMapping("submit")
    public ReturnCode submit(@RequestBody DepartmentalMatters departmentalMatters) {

        boolean returnState = departmentalMattersService.isCommit(departmentalMatters);
        if (returnState) {
            return new ReturnCode.Builder().msg("提交成功").success().build();
        } else {
            return new ReturnCode.Builder().paramError().msg("页面中有未完善信息，请完善后再做提交").build();
        }
    }

    /**
     * 事项复制
     *
     * @param CopyMattersReq
     * @return
     */
    @PostMapping("copy")
    public ReturnCode copyMatters(@RequestBody CopyMattersReq CopyMattersReq, HttpServletRequest request) throws Exception {

        boolean returnState = departmentalMattersService.copyMatters(CopyMattersReq, request);

        if (returnState) {

            return new ReturnCode.Builder().success().msg("复制成功").build();

        } else {
            return new ReturnCode.Builder().paramError().msg("复制失败").build();
        }
    }


    /**
     * 事项提交后按角色可修改的状态
     *
     * @param departmentalMattersId
     * @return
     */
    @GetMapping("getRevisable/{departmentalMattersId}")
    @ApiOperation(value = "事项提交后按角色可修改的状态")
    public ReturnCode getRevisable(@PathVariable String departmentalMattersId, HttpServletRequest request) throws Exception {
        String userId = WebTokenUtil.getUserByWebToken(request);

        //获取登录者角色
        List<Role> roleList = roleService.findByUserRole(userId);
        if (roleList == null || roleList.size() <= 0) {
            return new ReturnCode.Builder().success().msg("未找到人员角色").build();
        }
        Role role = roleList.get(0);

        if (role.getDataType() == null) {

            return new ReturnCode.Builder().success().msg("当前人员未配置角色").build();
        }

        Map map = departmentalMattersService.getRevisable(departmentalMattersId, role);

        return new ReturnCode.Builder().success().msg("查询成功").object(map).build();

    }

    @RequestMapping(value = "/allClasses", method = RequestMethod.GET)
    @ApiOperation(value = "推送所有事项")
    public ReturnCode sendAllClasses() {
        return departmentalMattersService.sendAllClasses();
    }

    @RequestMapping(value = "/oneClasses/{departmentalMattersId}", method = RequestMethod.GET)
    @ApiOperation(value = "推送单个事项")
    public ReturnCode sendOneClasses(@PathVariable String departmentalMattersId) {
        return departmentalMattersService.sendOneClasses(departmentalMattersId);
    }

}
