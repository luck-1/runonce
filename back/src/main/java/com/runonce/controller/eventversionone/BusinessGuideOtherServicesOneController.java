package com.runonce.controller.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideOtherServicesOneReq;
import com.runonce.model.eventversionone.BusinessGuideOtherServicesOnePrivate;
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
import com.runonce.model.eventversionone.BusinessGuideOtherServicesOne;
import  com.runonce.service.eventversionone.BusinessGuideOtherServicesOneService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:49:40'.
*/

@RestController
@RequestMapping("businessGuideOtherServicesOne")
@Api(description = " 其他服务类 - V1.0 ")
public class  BusinessGuideOtherServicesOneController {

    @Autowired
    private BusinessGuideOtherServicesOneService businessGuideOtherServicesOneService;

    @PostMapping("save")
    @ApiOperation(value = "保存 ")
    public ReturnCode save(@RequestBody @Valid BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq, BindingResult bindingResult, HttpServletRequest request) {

        if( businessGuideOtherServicesOneReq == null){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        BusinessGuideOtherServicesOne businessGuideOtherServicesOne = businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOne();
        BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate = businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOnePrivate();
        if (businessGuideOtherServicesOne == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）公共部分为空").build();
        }
        if (businessGuideOtherServicesOnePrivate == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）私有部分为空").build();
        }

//        //输入校验
//        if (businessGuideOtherServicesOne.getState() == 2  && bindingResult.hasErrors()){
//            businessGuideOtherServicesOne.setState(1);
//            businessGuideOtherServicesOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        //保存
        return businessGuideOtherServicesOneService.add( businessGuideOtherServicesOne,businessGuideOtherServicesOnePrivate,request);
    }

    @PostMapping("findByEventId")
    @ApiOperation(value = " 从事件ID查询")
    public ReturnCode findByEventId(@RequestParam String eventId) {
        if(eventId == null || eventId.equals("")){
            return new ReturnCode.Builder().failed().msg("事件Id为空").build();
        }
        BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq = businessGuideOtherServicesOneService.findByEventId(eventId);
        if(businessGuideOtherServicesOneReq == null){
            return new ReturnCode.Builder().success().msg("没有查询到信息").build();
        }
        return new ReturnCode.Builder().success().msg("查询成功").object(businessGuideOtherServicesOneReq).build();
    }


    @PostMapping("savePrivate")
    @ApiOperation(value = "保存Private表信息")
    public ReturnCode savePrivate(@RequestBody BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate, BindingResult bindingResult,HttpServletRequest request) {
//        //验证是否有错，如果有错就输出错误信息
//        if (businessGuideOtherServicesOnePrivate.getState().equals(2) && bindingResult.hasErrors() ) {
//            businessGuideOtherServicesOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        businessGuideOtherServicesOneService.savePrivate(businessGuideOtherServicesOnePrivate,request);
        return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideOtherServicesOnePrivate).build();
    }
}
