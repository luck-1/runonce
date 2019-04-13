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

import com.runonce.model.eventversiontwo.MaterialReviewRulesContent;
import  com.runonce.service.eventversiontwo.MaterialReviewRulesContentService;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/

@RestController
@RequestMapping("materialReviewRulesContent")
public class  MaterialReviewRulesContentController {

    @Autowired
    private MaterialReviewRulesContentService materialReviewRulesContentService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody MaterialReviewRulesContent  materialReviewRulesContent) {
        materialReviewRulesContentService.save( materialReviewRulesContent);
       return new ReturnCode.Builder().msg("添加成功").success().object(materialReviewRulesContent).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    materialReviewRulesContentService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody MaterialReviewRulesContent  materialReviewRulesContent) {
	    materialReviewRulesContentService.update( materialReviewRulesContent);
	 	return new ReturnCode.Builder().msg("更新成功").object(materialReviewRulesContent).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        MaterialReviewRulesContent materialReviewRulesContent = materialReviewRulesContentService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(materialReviewRulesContent).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MaterialReviewRulesContent> list = materialReviewRulesContentService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
