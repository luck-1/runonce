package com.runonce.controller.base;

import java.util.List;

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

import com.runonce.model.base.ServiceObject;
import  com.runonce.service.base.ServiceObjectService;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/

@RestController
@RequestMapping("serviceObject")
@Api(description = "服务对象 ")
public class  ServiceObjectController {

    @Autowired
    private ServiceObjectService serviceObjectService;

    @PostMapping("add")
    @ApiOperation(value = "添加 ")
    public ReturnCode add(@RequestBody ServiceObject  serviceObject) {
        serviceObjectService.save( serviceObject);
       return new ReturnCode.Builder().success().msg("添加成功").object(serviceObject).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除 ")
    public ReturnCode delete(@RequestParam String id) {
	    serviceObjectService.deleteById(id);
	  	return new ReturnCode.Builder().success().msg("删除成功").build();
    }

 	@PutMapping("update")
    @ApiOperation(value = "更改")
    public ReturnCode update(@RequestBody ServiceObject  serviceObject) {
	    serviceObjectService.update( serviceObject);
	 	return new ReturnCode.Builder().success().msg("更新成功").object(serviceObject).build();
    }

   @GetMapping("detail")
   @ApiOperation(value = "查询 ")
    public ReturnCode detail(@RequestParam String id) {
        ServiceObject serviceObject = serviceObjectService.findById(id);
        return new ReturnCode.Builder().success().msg("查询成功").object(serviceObject).build();
    }

    @PostMapping("list")
    @ApiOperation(value = "查询所有 ")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ServiceObject> list = serviceObjectService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
    }

}
