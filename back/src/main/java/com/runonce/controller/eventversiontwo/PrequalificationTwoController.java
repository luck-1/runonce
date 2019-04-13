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

import com.runonce.model.eventversiontwo.PrequalificationTwo;
import  com.runonce.service.eventversiontwo.PrequalificationTwoService;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/

@RestController
@RequestMapping("prequalificationTwo")
public class  PrequalificationTwoController {

    @Autowired
    private PrequalificationTwoService prequalificationTwoService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody PrequalificationTwo  prequalificationTwo) {
        prequalificationTwoService.save( prequalificationTwo);
       return new ReturnCode.Builder().msg("添加成功").success().object(prequalificationTwo).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    prequalificationTwoService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }
    @PostMapping("save")
    public  ReturnCode save(@RequestBody List<PrequalificationTwo> list){
        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().success().msg("数据不能为空").build();
        }else {
            prequalificationTwoService.save(list);
            return new ReturnCode.Builder().msg("成功").object(list).build();
        }
    }
 	@PutMapping("update")
    public ReturnCode update(@RequestBody PrequalificationTwo  prequalificationTwo) {
	    prequalificationTwoService.update( prequalificationTwo);
	 	return new ReturnCode.Builder().msg("更新成功").object(prequalificationTwo).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        PrequalificationTwo prequalificationTwo = prequalificationTwoService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(prequalificationTwo).success().build();
    }
    @GetMapping("eventId")
    public ReturnCode eventId(@RequestParam String id){
        List<PrequalificationTwo> list = prequalificationTwoService.findByEventId(id);
        return new ReturnCode.Builder().msg("成功").object(list).success().build();
    }
    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<PrequalificationTwo> list = prequalificationTwoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
