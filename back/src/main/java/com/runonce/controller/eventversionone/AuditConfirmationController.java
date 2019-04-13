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

import com.runonce.model.eventversionone.AuditConfirmation;
import  com.runonce.service.eventversionone.AuditConfirmationService;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:49:40'.
*/

@RestController
@RequestMapping("auditConfirmation")
public class  AuditConfirmationController {

    @Autowired
    private AuditConfirmationService auditConfirmationService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody AuditConfirmation  auditConfirmation) {
        auditConfirmationService.save( auditConfirmation);
       return new ReturnCode.Builder().msg("添加成功").success().object(auditConfirmation).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    auditConfirmationService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody AuditConfirmation  auditConfirmation) {
	    auditConfirmationService.update( auditConfirmation);
	 	return new ReturnCode.Builder().msg("更新成功").object(auditConfirmation).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        AuditConfirmation auditConfirmation = auditConfirmationService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(auditConfirmation).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AuditConfirmation> list = auditConfirmationService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
