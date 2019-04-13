package com.runonce.controller.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideFilingOneReq;
import com.runonce.model.eventversionone.BusinessGuideFilingOnePrivate;
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
import com.runonce.model.eventversionone.BusinessGuideFilingOne;
import  com.runonce.service.eventversionone.BusinessGuideFilingOneService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 *
 *
 * Created by xuxueli on '2018-12-10 11:49:40'.
 */

@RestController
@RequestMapping("businessGuideFilingOne")
@Api(description = "备案类 - V1.0  ")
public class  BusinessGuideFilingOneController {

    @Autowired
    private BusinessGuideFilingOneService businessGuideFilingOneService;

    @PostMapping("save")
    @ApiOperation(value = "保存 ")
    public ReturnCode save(@RequestBody @Valid BusinessGuideFilingOneReq  businessGuideFilingOneReq, BindingResult bindingResult, HttpServletRequest request) {

        if(businessGuideFilingOneReq == null){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }

        BusinessGuideFilingOne businessGuideFilingOne =businessGuideFilingOneReq.getBusinessGuideFilingOne() ;
        BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate = businessGuideFilingOneReq.getBusinessGuideFilingOnePrivate();

        if (businessGuideFilingOne == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）公共部分为空").build();
        }
        if (businessGuideFilingOnePrivate == null) {
            return new ReturnCode.Builder().failed().msg("（省市区县街办）私有部分为空").build();
        }

//        //输入校验
//        if (businessGuideFilingOne.getState() == 2  && bindingResult.hasErrors()){
//            businessGuideFilingOne.setState(1);
//            businessGuideFilingOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        //保存
        return businessGuideFilingOneService.add(businessGuideFilingOne,businessGuideFilingOnePrivate,request);
    }

    @PostMapping("findByEventId")
    @ApiOperation(value = " 从事件ID查询")
    public ReturnCode findByEventId(@RequestParam String eventId) {
        if(eventId == null || eventId.equals("")){
            return new ReturnCode.Builder().failed().msg("事件Id为空").build();
        }
        BusinessGuideFilingOneReq businessGuideFilingOneReq = businessGuideFilingOneService.findByEventId(eventId);
        if(businessGuideFilingOneReq == null){
            return new ReturnCode.Builder().success().msg("没有查询到信息").build();
        }
        return new ReturnCode.Builder().success().msg("查询成功").object(businessGuideFilingOneReq).build();
    }


    @PostMapping("savePrivate")
    @ApiOperation(value = "保存Private表信息")
    public ReturnCode savePrivate(@RequestBody BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate, BindingResult bindingResult, HttpServletRequest request) {
//        //验证是否有错，如果有错就输出错误信息
//        if (businessGuideFilingOnePrivate.getState().equals(2) && bindingResult.hasErrors() ) {
//            businessGuideFilingOnePrivate.setState(1);
////            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        businessGuideFilingOneService.savePrivate(businessGuideFilingOnePrivate,request);
        return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideFilingOnePrivate).build();
    }
}
