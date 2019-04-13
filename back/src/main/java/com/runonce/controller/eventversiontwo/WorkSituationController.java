package com.runonce.controller.eventversiontwo;

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

import com.runonce.model.eventversiontwo.WorkSituation;
import  com.runonce.service.eventversiontwo.WorkSituationService;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:33:33'.
*/

@RestController
@RequestMapping("workSituation")
public class  WorkSituationController {

    @Autowired
    private WorkSituationService workSituationService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody WorkSituation  workSituation) {
        workSituationService.save( workSituation);
       return new ReturnCode.Builder().msg("添加成功").success().object(workSituation).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    workSituationService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody WorkSituation  workSituation) {
	    workSituationService.update( workSituation);
	 	return new ReturnCode.Builder().msg("更新成功").object(workSituation).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        WorkSituation workSituation = workSituationService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(workSituation).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<WorkSituation> list = workSituationService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
