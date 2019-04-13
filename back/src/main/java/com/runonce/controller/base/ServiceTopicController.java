package com.runonce.controller.base;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.base.ServiceTopic;
import  com.runonce.service.base.ServiceTopicService;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:37:24'.
*/

@RestController
@RequestMapping("serviceTopic")
@Api(description = " 服务主题")
public class  ServiceTopicController {

    @Autowired
    private ServiceTopicService serviceTopicService;

    @PostMapping("add")
    @ApiOperation(value = "添加 ")
    public ReturnCode add(@RequestBody ServiceTopic  serviceTopic) {
        serviceTopicService.save( serviceTopic);
       return new ReturnCode.Builder().success().msg("添加成功").object(serviceTopic).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除 ")
    public ReturnCode delete(@RequestParam String id) {
	    serviceTopicService.deleteById(id);
	  	return new ReturnCode.Builder().success().msg("删除成功").build();
    }

 	@PutMapping("update")
    @ApiOperation(value = "更改")
    public ReturnCode update(@RequestBody ServiceTopic  serviceTopic) {
	    serviceTopicService.update( serviceTopic);
	 	return new ReturnCode.Builder().success().msg("更新成功").object(serviceTopic).build();
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询 ")
    public ReturnCode detail(@RequestParam String id) {
        ServiceTopic serviceTopic = serviceTopicService.findById(id);
        return new ReturnCode.Builder().success().msg("查询成功").object(serviceTopic).build();
    }

    @PostMapping("list")
    @ApiOperation(value = "查询所有子项")
    public ReturnCode list() {
        List<ServiceTopic> list = serviceTopicService.findAll();
        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
    }

    @RequestMapping(value = "/sendTopic", method = RequestMethod.GET)
    @ApiOperation(value = "推送所有服务主题")
    public ReturnCode sendPlace() {
        return serviceTopicService.sendTopic();
    }



}