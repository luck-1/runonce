package com.runonce.controller.eventversionone;

import java.util.List;
import com.runonce.model.eventversionone.SituationToGuideChild;
import com.runonce.model.http.SituationToGuideAddChildrenParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.SituationToGuide;
import  com.runonce.service.eventversionone.SituationToGuideService;
import javax.validation.Valid;

/**
* 情形引导
*
* Created by xuxueli on '2019-02-27 14:09:17'.
*/

@RestController
@RequestMapping("situationToGuide")
@Api(description = "情形引导")
public class  SituationToGuideController {

    @Autowired
    private SituationToGuideService situationToGuideService;

    @PostMapping("add")
    @ApiOperation(value = "添加/更新")
    public ReturnCode add(@RequestBody @Valid SituationToGuide  situationToGuide, BindingResult bindingResult) {
        if(situationToGuide == null){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        situationToGuideService.add( situationToGuide);
        return new ReturnCode.Builder().success().msg("情形引导添加成功").build();
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation(value = "单个删除")
    public ReturnCode delete(@PathVariable String id) {
        if(id == null || "".equals(id)){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        situationToGuideService.delete(id);
        return new ReturnCode.Builder().msg("情形引导删除成功").success().build();
    }

    @DeleteMapping("deleteBySelect")
    @ApiOperation(value = "批量删除")
    public ReturnCode deleteBySelect(@RequestParam List<String> ids) {
        if(ids == null || ids.size() == 0){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
	    situationToGuideService.deleteBySelect(ids);
	  	return new ReturnCode.Builder().msg("情形引导删除成功").success().build();
    }

    @GetMapping("findByEventName")
    @ApiOperation(value = "从事件名称查询")
    public ReturnCode findByEventName(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "0") Integer size,
                                      @RequestParam String eventName) {
        PageHelper.startPage(page,size);
        List<SituationToGuide> situationToGuideList  = situationToGuideService.findByEventName(eventName);
        PageInfo pageInfo = new PageInfo(situationToGuideList);
        return new ReturnCode.Builder().success().msg("情形引导查询成功").object(pageInfo).build();
    }

    @PostMapping("addChildren")
    @ApiOperation(value = "情形引导父项添加子项")
    public ReturnCode addChildren(@RequestBody SituationToGuideAddChildrenParam situationToGuideAddChildrenParam) {
        if(situationToGuideAddChildrenParam == null ){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        String pid = situationToGuideAddChildrenParam.getPid();
        if(pid == null || "".equals(pid)){
            return new ReturnCode.Builder().failed().msg("父ID为空").build();
        }
        situationToGuideService.addChildren(pid,situationToGuideAddChildrenParam.getChildId());
        return new ReturnCode.Builder().success().msg("子项添加成功").build();
    }

    @GetMapping("findChildren/{pid}")
    @ApiOperation(value = "查询情形引导子项")
    public ReturnCode findChildren(@PathVariable("pid") String pid) {
        if(pid == null || "".equals(pid)){
            return new ReturnCode.Builder().failed().msg("父ID为空").build();
        }
        List<SituationToGuideChild> list = situationToGuideService.findChildren(pid);
        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
    }

}
