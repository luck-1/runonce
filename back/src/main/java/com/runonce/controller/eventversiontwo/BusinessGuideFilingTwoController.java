package com.runonce.controller.eventversiontwo;

import java.util.List;

import com.runonce.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversiontwo.BusinessGuideFilingTwo;
import  com.runonce.service.eventversiontwo.BusinessGuideFilingTwoService;

import javax.validation.Valid;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/

@RestController
@RequestMapping("businessGuideFilingTwo")
@Api(description="备案类 - V2.0")
public class  BusinessGuideFilingTwoController {

    @Autowired
    private BusinessGuideFilingTwoService businessGuideFilingTwoService;

    @PostMapping("save")
    @ApiOperation(value = "保存 ")
    public ReturnCode save(@RequestBody BusinessGuideFilingTwo  businessGuideFilingTwo) {
        if(businessGuideFilingTwo.getId() == null || businessGuideFilingTwo.getId().equals("")){
            businessGuideFilingTwo.setId(SysUtil.getUUID());
            businessGuideFilingTwoService.save( businessGuideFilingTwo);
        }else{
            businessGuideFilingTwoService.update( businessGuideFilingTwo);
        }
        return new ReturnCode.Builder().msg("保存成功").success().object(businessGuideFilingTwo).build();
    }

    @PostMapping("commit")
    @ApiOperation(value = "提交 ")
    public ReturnCode commit(@RequestBody @Valid BusinessGuideFilingTwo  businessGuideFilingTwo) {
        if(businessGuideFilingTwo.getEventId() == null){
            businessGuideFilingTwo.setId(SysUtil.getUUID());
            businessGuideFilingTwoService.save( businessGuideFilingTwo);
        }else{
            businessGuideFilingTwoService.update( businessGuideFilingTwo);
        }
        return new ReturnCode.Builder().msg("提交成功").success().object(businessGuideFilingTwo).build();
    }

    @DeleteMapping("deleteByEventId")
    @ApiOperation(value = " 删除")
    public ReturnCode deleteByEventId(@RequestParam String eventId) {
        if (businessGuideFilingTwoService.findByEventId(eventId) == null){
            return new ReturnCode.Builder().msg("用户不存在").failed().build();
        }else{
            businessGuideFilingTwoService.deleteByEventId(eventId);
            return new ReturnCode.Builder().msg("删除成功").success().build();
        }
    }
    @PostMapping("findByEventId")
    @ApiOperation(value = " 从事件ID查询")
    public ReturnCode findByEventId(@RequestParam String eventId) {
        BusinessGuideFilingTwo businessGuideFilingTwo = businessGuideFilingTwoService.findByEventId(eventId);
        if(businessGuideFilingTwo == null){
            return new ReturnCode.Builder().msg("没有查询到信息").failed().build();
        }
        return new ReturnCode.Builder().msg("查询成功").object(businessGuideFilingTwo).success().build();
    }

    @PostMapping("list")
    @ApiOperation(value = " 查询所有")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<BusinessGuideFilingTwo> list = businessGuideFilingTwoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
