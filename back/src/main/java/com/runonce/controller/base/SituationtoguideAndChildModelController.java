package com.runonce.controller.base;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.runonce.model.base.SituationtoguideAndChildModel;
import  com.runonce.service.base.SituationtoguideAndChildModelService;

/**
 * @author lxc
 * @date 2019-03-01 10:22:57
 * @description 情形引导父项和子项中间表
 */
@RestController
@RequestMapping("situationtoguideAndChildModel")
@Api(description = "情形引导父项和子项中间表")
public class  SituationtoguideAndChildModelController {

    @Autowired
    private SituationtoguideAndChildModelService situationtoguideAndChildModelService;

    @PostMapping("add")
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody SituationtoguideAndChildModel  situationtoguideAndChildModel) {
        situationtoguideAndChildModelService.save( situationtoguideAndChildModel);
        return new ReturnCode.Builder().success().msg("添加成功").object(situationtoguideAndChildModel).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除")
    public ReturnCode delete(@RequestParam String id) {
	    situationtoguideAndChildModelService.deleteById(id);
	  	return new ReturnCode.Builder().success().msg("删除成功").build();
    }

 	@PutMapping("update")
 	@ApiOperation(value = "更新")
    public ReturnCode update(@RequestBody SituationtoguideAndChildModel  situationtoguideAndChildModel) {
	    situationtoguideAndChildModelService.update( situationtoguideAndChildModel);
	 	return new ReturnCode.Builder().success().msg("更新成功").object(situationtoguideAndChildModel).build();
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询单个")
	public ReturnCode detail(@RequestParam String id) {
	    SituationtoguideAndChildModel situationtoguideAndChildModel = situationtoguideAndChildModelService.findById(id);
	    return new ReturnCode.Builder().success().msg("查询成功").object(situationtoguideAndChildModel).build();
	}

    @PostMapping("list")
    @ApiOperation(value = "查询所有")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SituationtoguideAndChildModel> list = situationtoguideAndChildModelService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
    }




}