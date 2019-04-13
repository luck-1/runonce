package com.runonce.controller.eventversionone;

import java.util.ArrayList;
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

import com.runonce.model.eventversionone.DefaultSystemDockingPic;
import  com.runonce.service.eventversionone.DefaultSystemDockingPicService;

/**
* 
*
* Created by xuxueli on '2019-01-04 10:40:30'.
*/

@RestController
@RequestMapping("defaultSystemDockingPic")
public class  DefaultSystemDockingPicController {

    @Autowired
    private DefaultSystemDockingPicService defaultSystemDockingPicService;

    @GetMapping("selectall")
    public ReturnCode selectAll(){
        List<DefaultSystemDockingPic> list = new ArrayList<>();
        try {
            list = defaultSystemDockingPicService.selectAll();
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnCode.Builder().failed().msg("系统对接图查询失败").object(list).build();
        }
        return new ReturnCode.Builder().success().msg("系统对接图查询成功").object(list).build();
    }


    @PostMapping("add")
    public ReturnCode add(@RequestBody DefaultSystemDockingPic  defaultSystemDockingPic) {
        defaultSystemDockingPicService.save( defaultSystemDockingPic);
       return new ReturnCode.Builder().msg("添加成功").success().object(defaultSystemDockingPic).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    defaultSystemDockingPicService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody DefaultSystemDockingPic  defaultSystemDockingPic) {
	    defaultSystemDockingPicService.update( defaultSystemDockingPic);
	 	return new ReturnCode.Builder().msg("更新成功").object(defaultSystemDockingPic).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        DefaultSystemDockingPic defaultSystemDockingPic = defaultSystemDockingPicService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(defaultSystemDockingPic).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DefaultSystemDockingPic> list = defaultSystemDockingPicService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
