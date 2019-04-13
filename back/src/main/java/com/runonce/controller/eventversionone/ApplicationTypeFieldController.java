package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.model.http.RelatedMaterialsParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.ApplicationTypeField;
import  com.runonce.service.eventversionone.ApplicationTypeFieldService;

import javax.validation.Valid;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:49:40'.
*/

@RestController
@RequestMapping("applicationTypeField")
public class  ApplicationTypeFieldController {

    @Autowired
    private ApplicationTypeFieldService applicationTypeFieldService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody @Valid List<RelatedMaterialsParam>  applicationTypeFieldList) {
        applicationTypeFieldService.saveInfo(applicationTypeFieldList);
       return new ReturnCode.Builder().msg("添加成功").success().object(applicationTypeFieldList).build();
    }

    @GetMapping("delete/{id}")
    public ReturnCode delete(@PathVariable String id) {
	    applicationTypeFieldService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PostMapping("update")
    public ReturnCode update(@RequestBody ApplicationTypeField  applicationTypeField) {
	    applicationTypeFieldService.update( applicationTypeField);
	 	return new ReturnCode.Builder().msg("更新成功").object(applicationTypeField).success().build();
    }

   @GetMapping("detail/{id}")
    public ReturnCode detail(@PathVariable String id) {
        ApplicationTypeField applicationTypeField = applicationTypeFieldService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(applicationTypeField).success().build();
    }

    /**
     * 叶子结点元素关联材料接口
     * @return
     */
    @PostMapping("relatedmaterials")
    public ReturnCode relatedMaterials(@RequestBody RelatedMaterialsParam relatedMaterialsParam){
        applicationTypeFieldService.relatedMaterials(relatedMaterialsParam);
        return new ReturnCode.Builder().msg("关联成功").object(relatedMaterialsParam).success().build();
    }
    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApplicationTypeField> list = applicationTypeFieldService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
