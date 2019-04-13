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

import com.runonce.model.eventversiontwo.MaterialReviewRules;
import  com.runonce.service.eventversiontwo.MaterialReviewRulesService;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/

@RestController
@RequestMapping("materialReviewRules")
public class  MaterialReviewRulesController {

    @Autowired
    private MaterialReviewRulesService materialReviewRulesService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody MaterialReviewRules  materialReviewRules) {
        materialReviewRulesService.save( materialReviewRules);
       return new ReturnCode.Builder().msg("添加成功").success().object(materialReviewRules).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    materialReviewRulesService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody MaterialReviewRules  materialReviewRules) {
	    materialReviewRulesService.update( materialReviewRules);
	 	return new ReturnCode.Builder().msg("更新成功").object(materialReviewRules).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        MaterialReviewRules materialReviewRules = materialReviewRulesService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(materialReviewRules).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MaterialReviewRules> list = materialReviewRulesService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
