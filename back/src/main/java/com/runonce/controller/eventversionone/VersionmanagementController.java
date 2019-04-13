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

import com.runonce.model.eventversionone.Versionmanagement;
import  com.runonce.service.eventversionone.VersionmanagementService;

/**
* 事项sheet页版本管理
*
* Created by xuxueli on '2019-01-28 15:06:51'.
*/

@RestController
@RequestMapping("versionmanagement")
public class  VersionmanagementController {

    @Autowired
    private VersionmanagementService versionmanagementService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody Versionmanagement  versionmanagement) {
        versionmanagementService.save( versionmanagement);
       return new ReturnCode.Builder().msg("添加成功").success().object(versionmanagement).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    versionmanagementService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody Versionmanagement  versionmanagement) {
	    versionmanagementService.update( versionmanagement);
	 	return new ReturnCode.Builder().msg("更新成功").object(versionmanagement).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        Versionmanagement versionmanagement = versionmanagementService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(versionmanagement).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Versionmanagement> list = versionmanagementService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
