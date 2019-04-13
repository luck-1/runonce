package com.runonce.controller.eventversionone;

import java.util.List;
import java.util.Map;

import com.runonce.httpbean.assets.reqbean.BusinessGuideAdministrativeLicensingOneReq;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOnePrivate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOne;
import com.runonce.service.eventversionone.BusinessGuideAdministrativeLicensingOneService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by xuxueli on '2018-12-10 11:49:40'.
 */

@RestController
@RequestMapping("businessGuideAdministrativeLicensingOne")
@Api(description = "行政许可类 - V1.0 ")
public class BusinessGuideAdministrativeLicensingOneController {

    @Autowired
    private BusinessGuideAdministrativeLicensingOneService businessGuideAdministrativeLicensingOneService;


    @PostMapping("save")
    @ApiOperation(value = "保存")
    public ReturnCode save(@RequestBody @Valid BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq , BindingResult bindingResult, HttpServletRequest request) {

        if (businessGuideAdministrativeLicensingOneReq == null) {
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        //办事指南(行政许可类)公共
        BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne = businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne();
        //办事指南(行政许可类)私有
        BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate = businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOnePrivate();

        if (businessGuideAdministrativeLicensingOne == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）公共部分为空").build();
        }
        if (businessGuideAdministrativeLicensingOnePrivate == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）私有部分为空").build();
        }
//        //输入校验
//        if (businessGuideAdministrativeLicensingOne.getState() == 2 && bindingResult.hasErrors()) {
//            businessGuideAdministrativeLicensingOne.setState(1);
//            businessGuideAdministrativeLicensingOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        //保存
        return businessGuideAdministrativeLicensingOneService.add(businessGuideAdministrativeLicensingOne,businessGuideAdministrativeLicensingOnePrivate,request);
    }

    @PostMapping("savePrivate")
    @ApiOperation(value = "保存Private表信息")
    public ReturnCode savePrivate(@RequestBody BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate, BindingResult bindingResult, HttpServletRequest request) {
        //验证是否有错，如果有错就输出错误信息
//        if (businessGuideAdministrativeLicensingOnePrivate.getState().equals(2) && bindingResult.hasErrors() ) {
//            businessGuideAdministrativeLicensingOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        businessGuideAdministrativeLicensingOneService.savePrivate(businessGuideAdministrativeLicensingOnePrivate,request);
        return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideAdministrativeLicensingOnePrivate).build();
    }

    @PostMapping("findByEventId")
    @ApiOperation(value = " 从事件ID查询")
    public ReturnCode findByEventId(@RequestParam String eventId) {
        if (eventId == null || eventId.equals("")) {
            return new ReturnCode.Builder().failed().msg("事件Id为空").build();
        }
        BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq = businessGuideAdministrativeLicensingOneService.findByEventId(eventId);
        if (businessGuideAdministrativeLicensingOneReq == null) {
            return new ReturnCode.Builder().success().msg("没有查询到信息").build();
        }
        return new ReturnCode.Builder().success().msg("查询成功").object(businessGuideAdministrativeLicensingOneReq).build();
    }

    @GetMapping("getAllList")
    @ApiOperation(value = " 查询所有下拉框的值")
    public ReturnCode getAllList(@RequestParam String eventType) {
        if (eventType == null || "".equals(eventType)) {
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        Map<String, List> allList = businessGuideAdministrativeLicensingOneService.getAllList(eventType);
        return new ReturnCode.Builder().success().msg("查询成功").object(allList).build();
    }
}