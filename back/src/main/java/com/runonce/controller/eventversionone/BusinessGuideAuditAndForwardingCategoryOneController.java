package com.runonce.controller.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideAuditAndForwardingCategoryOneReq;
import com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOnePrivate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOne;
import  com.runonce.service.eventversionone.BusinessGuideAuditAndForwardingCategoryOneService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 *
 *
 * Created by xuxueli on '2018-12-10 11:49:40'.
 */

@RestController
@RequestMapping("businessGuideAuditAndForwardingCategoryOne")
@Api(description = "审核转报类 - V1.0 ")
public class  BusinessGuideAuditAndForwardingCategoryOneController {

    @Autowired
    private BusinessGuideAuditAndForwardingCategoryOneService businessGuideAuditAndForwardingCategoryOneService;

    @PostMapping("save")
    @ApiOperation(value = "保存 ")
    public ReturnCode save(@RequestBody @Valid BusinessGuideAuditAndForwardingCategoryOneReq businessGuideAuditAndForwardingCategoryOneReq, BindingResult bindingResult, HttpServletRequest request) {

        if(businessGuideAuditAndForwardingCategoryOneReq == null ){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne = businessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOne();
        BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate = businessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOnePrivate();

        if (businessGuideAuditAndForwardingCategoryOne == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）公共部分为空").build();
        }
        if (businessGuideAuditAndForwardingCategoryOnePrivate == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）私有部分为空").build();
        }

//        //输入校验
//        if (businessGuideAuditAndForwardingCategoryOne.getState() == 2  && bindingResult.hasErrors()){
//            businessGuideAuditAndForwardingCategoryOne.setState(1);
//            businessGuideAuditAndForwardingCategoryOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        //保存
        return businessGuideAuditAndForwardingCategoryOneService.add( businessGuideAuditAndForwardingCategoryOne,businessGuideAuditAndForwardingCategoryOnePrivate,request);
    }

    @PostMapping("findByEventId")
    @ApiOperation(value = " 从事件ID查询")
    public ReturnCode findByEventId(@RequestParam String eventId) {
        if(eventId == null || eventId.equals("")){
            return new ReturnCode.Builder().failed().msg("事件Id为空").build();
        }
        BusinessGuideAuditAndForwardingCategoryOneReq businessGuideAuditAndForwardingCategoryOneReq = businessGuideAuditAndForwardingCategoryOneService.findByEventId(eventId);
        if(businessGuideAuditAndForwardingCategoryOneReq == null){
            return new ReturnCode.Builder().success().msg("没有查询到信息").build();
        }
        return new ReturnCode.Builder().success().msg("查询成功").object(businessGuideAuditAndForwardingCategoryOneReq).build();
    }

    @PostMapping("savePrivate")
    @ApiOperation(value = "保存Private表信息")
    public ReturnCode savePrivate(@RequestBody BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate, BindingResult bindingResult, HttpServletRequest request) {
//        //验证是否有错，如果有错就输出错误信息
//        if (businessGuideAuditAndForwardingCategoryOnePrivate.getState().equals(2) && bindingResult.hasErrors() ) {
//            businessGuideAuditAndForwardingCategoryOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        businessGuideAuditAndForwardingCategoryOneService.savePrivate(businessGuideAuditAndForwardingCategoryOnePrivate,request);
        return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideAuditAndForwardingCategoryOnePrivate).build();
    }

}
