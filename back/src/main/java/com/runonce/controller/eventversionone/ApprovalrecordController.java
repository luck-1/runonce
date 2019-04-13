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

import com.runonce.model.eventversionone.Approvalrecord;
import  com.runonce.service.eventversionone.ApprovalrecordService;

/**
* 审批记录
*
* Created by xuxueli on '2019-01-22 09:46:27'.
*/

@RestController
@RequestMapping("approvalrecord")
public class  ApprovalrecordController {

    @Autowired
    private ApprovalrecordService approvalrecordService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody Approvalrecord  approvalrecord) {
        approvalrecordService.save( approvalrecord);
       return new ReturnCode.Builder().msg("添加成功").success().object(approvalrecord).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    approvalrecordService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody Approvalrecord  approvalrecord) {
	    approvalrecordService.update( approvalrecord);
	 	return new ReturnCode.Builder().msg("更新成功").object(approvalrecord).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        Approvalrecord approvalrecord = approvalrecordService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(approvalrecord).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Approvalrecord> list = approvalrecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
