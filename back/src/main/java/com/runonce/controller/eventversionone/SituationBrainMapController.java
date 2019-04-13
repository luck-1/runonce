package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import com.runonce.httpbean.assets.reqbean.SituationBrainMapReq;
import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.eventversionone.DepartmentalMatters;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.SituationBrainMap;
import  com.runonce.service.eventversionone.SituationBrainMapService;

/**
* 情形引导-脑图
*
* Created by xuxueli on '2019-02-28 14:53:56'.
*/

@RestController
@RequestMapping("situationBrainMap")
public class  SituationBrainMapController {

    @Autowired
    private SituationBrainMapService situationBrainMapService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody SituationBrainMapReq situationBrainMapReq) {
        situationBrainMapService.saveInfo(situationBrainMapReq);
       return new ReturnCode.Builder().msg("添加成功").success().object(situationBrainMapReq).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    situationBrainMapService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody SituationBrainMap  situationBrainMap) {
	    situationBrainMapService.update( situationBrainMap);
	 	return new ReturnCode.Builder().msg("更新成功").object(situationBrainMap).success().build();
    }

   @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        SituationBrainMap situationBrainMap = situationBrainMapService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(situationBrainMap).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SituationBrainMap> list = situationBrainMapService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }


    @ApiOperation(value = "导图数据查询")
    @GetMapping("select/{id}/{type}")
    public ReturnCode selectByEventId(@PathVariable("id") String situationId, @PathVariable("type") Integer type) {
        if (StringUtils.isEmpty(situationId)) {
            return new ReturnCode.Builder().failed().msg("事项id为空").build();
        }
        if (type == null) {
            return new ReturnCode.Builder().failed().msg("查询类型为空").build();
        }

        SituationBrainMapReq situationBrainMapReq = null;

        situationBrainMapReq = situationBrainMapService.selectByEventId(situationId, type);
        return new ReturnCode.Builder().success().msg("导图数据查询成功").object(situationBrainMapReq).build();
    }
}
