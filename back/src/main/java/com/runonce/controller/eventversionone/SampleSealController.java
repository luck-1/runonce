package com.runonce.controller.eventversionone;

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

import com.runonce.model.eventversionone.SampleSeal;
import  com.runonce.service.eventversionone.SampleSealService;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:31:17'.
*/

@RestController
@RequestMapping("sampleSeal")
public class  SampleSealController {

    @Autowired
    private SampleSealService sampleSealService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody SampleSeal  sampleSeal) {
        sampleSealService.save( sampleSeal);
       return new ReturnCode.Builder().msg("添加成功").success().object(sampleSeal).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    sampleSealService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody SampleSeal  sampleSeal) {
	    sampleSealService.update( sampleSeal);
	 	return new ReturnCode.Builder().msg("更新成功").object(sampleSeal).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        SampleSeal sampleSeal = sampleSealService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(sampleSeal).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SampleSeal> list = sampleSealService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
