package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.model.http.AuditRulesRes;
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

import com.runonce.model.eventversionone.AuditRules;
import  com.runonce.service.eventversionone.AuditRulesService;

/**
* 审核细则
*
* Created by xuxueli on '2018-12-18 12:32:51'.
*/

@RestController
@RequestMapping("auditRules")
public class  AuditRulesController {

    @Autowired
    private AuditRulesService auditRulesService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody AuditRules  auditRules) {
        auditRulesService.save( auditRules);
       return new ReturnCode.Builder().msg("添加成功").success().object(auditRules).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    auditRulesService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody AuditRules  auditRules) {
	    auditRulesService.update( auditRules);
	 	return new ReturnCode.Builder().msg("更新成功").object(auditRules).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        AuditRules auditRules = auditRulesService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(auditRules).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AuditRules> list = auditRulesService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @GetMapping("selectByMaterialId")
    public ReturnCode selectByMaterialId(@RequestParam String MaterialId){
        AuditRulesRes auditRulesRes=new AuditRulesRes();
        auditRulesRes.setAcceptance(auditRulesService.selectByMaterialId(  MaterialId,  1));;
        auditRulesRes.setApproval(auditRulesService.selectByMaterialId(  MaterialId,  2));
        return  new ReturnCode.Builder().msg("查询成功").object(auditRulesRes).success().build();
    }

}
