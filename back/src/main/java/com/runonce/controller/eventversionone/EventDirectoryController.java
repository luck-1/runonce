package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.dao.system.DepartmentDao;
import com.runonce.model.http.EventDirectoryParam;
import com.runonce.model.system.Department;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.EventDirectory;
import  com.runonce.service.eventversionone.EventDirectoryService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
* 三级四同目录
*
* Created by xuxueli on '2019-01-16 12:00:31'.
*/

@Api(description = "三级四同目录")
@RestController
@RequestMapping("eventDirectory")
public class  EventDirectoryController {

    @Autowired
    private EventDirectoryService eventDirectoryService;

    @Resource
    private DepartmentDao departmentDao;

    @PostMapping("add")
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody @Valid EventDirectory eventDirectory,BindingResult bindingResult) {
        //输入校验
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        return eventDirectoryService.add(eventDirectory);
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除")
    public ReturnCode delete(@RequestBody List<String> ids) {

        if(ids!=null&&ids.size()>0) {

            for (int i = 0; i < ids.size(); i++) {
               Integer count= eventDirectoryService.checkDirectoryIsUsed(ids.get(i));
               if(count != null && count > 0){
                   return new ReturnCode.Builder().error().msg("删除失败，此目录已有事项引用，请删除事项再进行操作").build();
               }
                eventDirectoryService.deleteById(ids.get(i));
            }
        }
	  	return new ReturnCode.Builder().success().msg("删除成功").build();
    }

 	@PutMapping("update")
    @ApiOperation(value = "更新")
    public ReturnCode update(@RequestBody EventDirectory  eventDirectory,BindingResult bindingResult) {
        //输入校验
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
	 	return eventDirectoryService.updateEventDirectory( eventDirectory);
    }

    @PostMapping("list")
    @ApiOperation(value = "查询")
    public ReturnCode list(@RequestBody EventDirectoryParam eventDirectoryParam ) {
        ReturnCode returnCode;
        try{
            returnCode = eventDirectoryService.findEventDirectory(eventDirectoryParam);
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnCode.Builder().failed().msg("三同四从目录查询失败").build();
        }
        return returnCode;
    }

    @GetMapping("catalog/{deptId}")
    @ApiOperation(value = "查询")
    public ReturnCode catalog(@PathVariable String  deptId ) {
        List<EventDirectory> list = eventDirectoryService.findCatalogList(deptId);
        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
    }

    @GetMapping("findNameCount")
    @ApiOperation(value = "事项名重复检查")
    public ReturnCode findNameCount(@RequestParam String eventName,@RequestParam String id){
        //判断事项是否是市级
//        if(Integer.parseInt(departmentDao.findByIdAndDelFlag(deptId,0).getRegion()) < 3){
            //根据事项名称，去查市级得数据里面有没有三级四同目录
            if(eventDirectoryService.findGroupIdCount(eventName,id)){
                return new ReturnCode.Builder().success().msg("成功").build();
            }else{
                return new ReturnCode.Builder().failed().msg("事项名已存在").build();
            }
//        }else{
//            return new ReturnCode.Builder().success().msg("成功").build();
//        }
    }

}