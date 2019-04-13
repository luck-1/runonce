package com.runonce.controller.eventpublic;

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

import com.runonce.model.eventpublic.BusinessFileRelation;
import  com.runonce.service.eventpublic.BusinessFileRelationService;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:36:41'.
*/

@RestController
@RequestMapping("businessFileRelation")
public class  BusinessFileRelationController {

    @Autowired
    private BusinessFileRelationService businessFileRelationService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody BusinessFileRelation  businessFileRelation) {
        businessFileRelationService.save( businessFileRelation);
       return new ReturnCode.Builder().msg("添加成功").success().object(businessFileRelation).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    businessFileRelationService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody BusinessFileRelation  businessFileRelation) {
	    businessFileRelationService.update( businessFileRelation);
	 	return new ReturnCode.Builder().msg("更新成功").object(businessFileRelation).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        BusinessFileRelation businessFileRelation = businessFileRelationService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(businessFileRelation).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<BusinessFileRelation> list = businessFileRelationService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
