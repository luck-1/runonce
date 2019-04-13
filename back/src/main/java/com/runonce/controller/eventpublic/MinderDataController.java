package com.runonce.controller.eventpublic;

import java.util.List;

import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventpublic.MinderData;
import com.runonce.service.eventpublic.MinderDataService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by xuxueli on '2018-12-12 19:56:37'.
 */

@RestController
    @RequestMapping("MinderData")
@Api(description = "申请类型思维导图接口")
public class MinderDataController {

    @Autowired
    private MinderDataService minderDataService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;


    @ApiOperation(value = "导图数据新增")
    @PostMapping("add")
    public ReturnCode add(@RequestBody @Valid MinderDataReq minderDataReq, BindingResult bindingResult, HttpServletRequest request) {
        //验证是否有错，如果有错就输出错误信息
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        ReturnCode code = minderDataService.saveInfo(minderDataReq,request);
        return code;
    }

    @ApiOperation(value = "获取办事情形数据")
    @GetMapping("worksituationdata/{eventId}")
    public ReturnCode getWorkSitutionData(@PathVariable("eventId") String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项id为空").object(eventId).build();
        }
        String situtionJsonData = null;
        try {
            situtionJsonData = minderDataService.getWorkSitutionData(eventId);
            if (situtionJsonData == null) {
                return new ReturnCode.Builder().failed().msg("申请类型未完善").object(situtionJsonData).build();
            }
        } catch (Exception e) {
            if (e instanceof CustomException) {
                CustomException userException = (CustomException) e;
                return ResultUtil.failed(userException.getCode(), userException.getMessage());
            } else {
                e.printStackTrace();
                return new ReturnCode.Builder().failed().msg("生成办事情形失败").object(situtionJsonData).build();

            }
        }
        return new ReturnCode.Builder().success().msg("生成办事情形成功").object(situtionJsonData).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        minderDataService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @ApiOperation(value = "导图数据更新")
    @PostMapping("update")
    public ReturnCode update(@RequestBody @Valid MinderData minderData) {
        minderDataService.update(minderData);
        return new ReturnCode.Builder().msg("更新成功").object(minderData).success().build();
    }

    @ApiOperation(value = "导图数据查询")
    @GetMapping("select/{id}/{type}")
    public ReturnCode selectByEventId(@PathVariable("id") String eventId, @PathVariable("type") Integer type) {
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项id为空").build();
        }
        if (type == null) {
            return new ReturnCode.Builder().failed().msg("查询类型为空").build();
        }
        // 事项存在校验
        DepartmentalMatters departmentalMatters = departmentalMattersService.findById(eventId);
        if (departmentalMatters == null) {
            return new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }
        MinderDataReq minderDataReq = null;
        eventId = departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        minderDataReq = minderDataService.selectByEventId(eventId, type);
        return new ReturnCode.Builder().success().msg("导图数据查询成功").object(minderDataReq).build();
    }

    @GetMapping("detail/{id}")
    public ReturnCode detail(@PathVariable String id) {
        MinderData minderData = minderDataService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(minderData).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MinderData> list = minderDataService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
