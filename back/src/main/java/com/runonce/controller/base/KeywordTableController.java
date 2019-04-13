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

import com.runonce.model.base.KeywordTable;
import  com.runonce.service.base.KeywordTableService;

/**
* 关键字
*
* Created by xuxueli on '2019-02-27 14:10:47'.
*/

@RestController
@RequestMapping("keywordTable")
public class  KeywordTableController {

    @Autowired
    private KeywordTableService keywordTableService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody KeywordTable  keywordTable) {
        keywordTableService.save( keywordTable);
       return new ReturnCode.Builder().msg("添加成功").success().object(keywordTable).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    keywordTableService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody KeywordTable  keywordTable) {
	    keywordTableService.update( keywordTable);
	 	return new ReturnCode.Builder().msg("更新成功").object(keywordTable).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        KeywordTable keywordTable = keywordTableService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(keywordTable).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<KeywordTable> list = keywordTableService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
