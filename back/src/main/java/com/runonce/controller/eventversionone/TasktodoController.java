package com.runonce.controller.eventversionone;

import java.util.List;
import java.util.Map;

import com.runonce.model.http.TasktodoReq;
import com.runonce.util.WebTokenUtil;
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

import com.runonce.model.eventversionone.Tasktodo;
import  com.runonce.service.eventversionone.TasktodoService;

import javax.servlet.http.HttpServletRequest;

/**
* taskToDo
*
* Created by xuxueli on '2019-02-21 11:20:11'.
*/

@RestController
@RequestMapping("tasktodo")
public class  TasktodoController {

    @Autowired
    private TasktodoService tasktodoService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody Tasktodo  tasktodo) {
        tasktodoService.save( tasktodo);
       return new ReturnCode.Builder().msg("添加成功").success().object(tasktodo).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    tasktodoService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody Tasktodo  tasktodo) {
	    tasktodoService.update( tasktodo);
	 	return new ReturnCode.Builder().msg("更新成功").object(tasktodo).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        Tasktodo tasktodo = tasktodoService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(tasktodo).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Tasktodo> list = tasktodoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @PostMapping("selectTodo")
    public ReturnCode selectTodo(@RequestBody TasktodoReq tasktodoReq, HttpServletRequest request) {
        String userId=WebTokenUtil.getUserByWebToken(request);
        PageInfo<Tasktodo> tasktodoList = tasktodoService.findByUserId(userId,tasktodoReq);
        return new ReturnCode.Builder().msg("查询成功").object(tasktodoList).success().build();
    }
    @GetMapping("clickTodo")
    public ReturnCode clickTodo(@RequestParam String id,HttpServletRequest request) {
       Map map= tasktodoService.clickTodo(id,request);
        return new ReturnCode.Builder().msg("处理成功").success().object(map).build();
    }




}
