package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.model.eventversionone.SituationChildAndDmModel;
import com.runonce.service.eventversionone.SituationChildAndDmModelService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxc
 * @date 2019-03-01 14:39:25
 * @description 情形引导子项关联事项
 */
@RestController
@RequestMapping("situationChildAndDmModel")
@Api(description = "情形引导子项关联事项")
public class SituationChildAndDmModelController {

    @Autowired
    private SituationChildAndDmModelService situationChildAndDmModelService;

    @PostMapping("add")
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody SituationChildAndDmModel situationChildAndDmModel) {
        situationChildAndDmModelService.save( situationChildAndDmModel);
        return new ReturnCode.Builder().success().msg("添加成功").object(situationChildAndDmModel).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除")
    public ReturnCode delete(@RequestParam String id) {
        situationChildAndDmModelService.deleteById(id);
	  	return new ReturnCode.Builder().success().msg("删除成功").build();
    }

 	@PutMapping("update")
 	@ApiOperation(value = "更新")
    public ReturnCode update(@RequestBody SituationChildAndDmModel  situationChildAndDmModel) {
        situationChildAndDmModelService.update( situationChildAndDmModel);
	 	return new ReturnCode.Builder().success().msg("更新成功").object(situationChildAndDmModel).build();
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询单个")
	public ReturnCode detail(@RequestParam String id) {
        SituationChildAndDmModel situationChildAndDmModel = situationChildAndDmModelService.findById(id);
	    return new ReturnCode.Builder().success().msg("查询成功").object(situationChildAndDmModel).build();
	}

    @PostMapping("list")
    @ApiOperation(value = "查询所有")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SituationChildAndDmModel> list = situationChildAndDmModelService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
    }

}