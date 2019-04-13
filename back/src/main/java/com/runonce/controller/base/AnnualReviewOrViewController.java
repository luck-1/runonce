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
import javax.validation.*;
import com.runonce.model.base.AnnualReviewOrView;
import  com.runonce.service.base.AnnualReviewOrViewService;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/

@RestController
@RequestMapping("annualReviewOrView")
@Api(description = " 年审或年检")

public class  AnnualReviewOrViewController {

    @Autowired
    private AnnualReviewOrViewService annualReviewOrViewService;
 
    @PostMapping("add")
    @ApiOperation(value = "添加 ")
    public ReturnCode add(@RequestBody AnnualReviewOrView  annualReviewOrView) {
        annualReviewOrViewService.save( annualReviewOrView);
       return new ReturnCode.Builder().msg("添加成功").success().object(annualReviewOrView).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除 ")
    public ReturnCode delete(@RequestParam String id) {
	    annualReviewOrViewService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    @ApiOperation(value = "更改")
    public ReturnCode update(@RequestBody AnnualReviewOrView  annualReviewOrView) {
	    annualReviewOrViewService.update( annualReviewOrView);
	 	return new ReturnCode.Builder().msg("更新成功").object(annualReviewOrView).success().build();
    }

   @GetMapping("detail")
   @ApiOperation(value = "查询 ")
    public ReturnCode detail(@RequestParam String id) {
        AnnualReviewOrView annualReviewOrView = annualReviewOrViewService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(annualReviewOrView).success().build();
    }

    @PostMapping("list")
    @ApiOperation(value = "查询所有 ")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AnnualReviewOrView> list = annualReviewOrViewService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
