package com.runonce.controller.eventversiontwo;

import java.util.List;

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

import com.runonce.model.eventversiontwo.ApplicationTypeValueTwo;
import  com.runonce.service.eventversiontwo.ApplicationTypeValueTwoService;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/

@RestController
@RequestMapping("applicationTypeValueTwo")
public class  ApplicationTypeValueTwoController {

    @Autowired
    private ApplicationTypeValueTwoService applicationTypeValueTwoService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody ApplicationTypeValueTwo  applicationTypeValueTwo) {
        applicationTypeValueTwoService.save( applicationTypeValueTwo);
       return new ReturnCode.Builder().msg("添加成功").success().object(applicationTypeValueTwo).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    applicationTypeValueTwoService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody ApplicationTypeValueTwo  applicationTypeValueTwo) {
	    applicationTypeValueTwoService.update( applicationTypeValueTwo);
	 	return new ReturnCode.Builder().msg("更新成功").object(applicationTypeValueTwo).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        ApplicationTypeValueTwo applicationTypeValueTwo = applicationTypeValueTwoService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(applicationTypeValueTwo).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApplicationTypeValueTwo> list = applicationTypeValueTwoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
