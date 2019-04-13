package com.runonce.controller.eventversionone;

import java.util.ArrayList;
import java.util.List;


import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.model.eventversionone.ProcessDescription;
import com.runonce.model.http.PrequalificationParam;
import com.runonce.model.http.PrequalificationVersionParam;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.Prequalification;
import  com.runonce.service.eventversionone.PrequalificationService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:49:40'.
*/
@Api(description = "资格预审页面")
@RestController
@RequestMapping("prequalification")
public class  PrequalificationController {

    @Autowired
    private PrequalificationService prequalificationService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @PostMapping("add")
    @ApiOperation(value = "新增单条数据")
    public ReturnCode add(@RequestBody @Valid Prequalification  prequalification) {
        prequalification.setId(SysUtil.getUUID());
        prequalificationService.save(prequalification);
        return new ReturnCode.Builder().success().msg("添加成功").object(prequalification).build();
    }
    @PostMapping("adds")
    @ApiOperation(value = "新增批量数据")
    public ReturnCode adds(@RequestBody List<Prequalification>  list) {
        for(Prequalification prequalification : list) {
            prequalification.setId(SysUtil.getUUID());
            prequalificationService.save(list);
        }
        return new ReturnCode.Builder().success().msg("添加成功").object(list).build();
    }
    @PostMapping("save")
    @ApiOperation(value = "保存数据")
    public ReturnCode save(@RequestBody PrequalificationParam prequalificationParam, HttpServletRequest request) {
        if(prequalificationParam == null ){
            return new ReturnCode.Builder().failed().msg("参数为空").build();
        }
        if(prequalificationParam.getPrequalificationList() == null && prequalificationParam.getPrequalificationList().size() == 0){
            if(StringUtils.isEmpty(prequalificationParam.getEventId())){
                return new ReturnCode.Builder().failed().msg("事项id为空").build();
            }
        }else{
            for(Prequalification item:prequalificationParam.getPrequalificationList()){
                if(StringUtils.isEmpty(item.getEventId())){
                    return new ReturnCode.Builder().failed().msg("事项id为空").build();
                }
            }
        }

//            if(prequalificationParam.getState() == 1){
//                prequalificationService.saveInfo(prequalificationParam);
//            }else{
//                // 加校验
//                prequalificationService.saveInfo(prequalificationParam);
//            }
        return  prequalificationService.saveInfo(prequalificationParam,request);
    }
    @DeleteMapping("delete")
    @ApiOperation(value = "删除单条数据")
    public ReturnCode delete(@RequestParam String id) {
        Prequalification prequalification = prequalificationService.findById(id);
        if(prequalification==null){
            return new ReturnCode.Builder().paramError().msg("删除失败").build();
        }else{
            prequalificationService.deleteById(id);
            return new ReturnCode.Builder().success().msg("删除成功").build();
        }
    }
    @DeleteMapping("deletes")
    @ApiOperation(value = "删除批量数据")
    public ReturnCode deletes(@RequestParam String ids) {
        List<Prequalification> list = prequalificationService.findByIds(ids);

        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().paramError().msg("删除失败").build();
        }else{
            prequalificationService.deleteByIds(ids);
            return new ReturnCode.Builder().success().msg("删除成功").build();
        }

    }

 	@PutMapping("update")
    @ApiOperation(value = "更新单条数据")
    public ReturnCode update(@RequestBody Prequalification  prequalification) {

	    prequalificationService.update( prequalification);
	 	return new ReturnCode.Builder().success().msg("更新成功").object(prequalification).build();
    }

   @GetMapping("detail")
   @ApiOperation(value = "查询单条数据")
    public ReturnCode detail(@RequestParam String id) {
       Prequalification prequalification = prequalificationService.findById(id);
        if(prequalification==null){
            return new ReturnCode.Builder().paramError().msg("查询结果不存在").build();
        }else{
            return new ReturnCode.Builder().success().msg("查询成功").object(prequalification).build();
        }
    }
    @GetMapping("details")
    @ApiOperation(value = "查询批量数据")
    public ReturnCode details(@RequestParam String ids) {
        List<Prequalification> list = prequalificationService.findByIds(ids);
        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().paramError().msg("查询结果不存在").build();
        }else{
            return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
        }
    }

    @GetMapping("eventId")
    @ApiOperation(value = "根据事项ID查询结果集")
    public ReturnCode eventId(@RequestParam String eventId) {

        eventId = departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        if(StringUtils.isEmpty(eventId)){
            return new ReturnCode.Builder().paramError().msg("事项id为空").build();
        }
        List<Prequalification> list = null;
        try {
            list=prequalificationService.findByEventId(eventId);
        }catch (Exception e){
            return new ReturnCode.Builder().failed().msg("资格预审查询异常").object(eventId).build();
        }
        PrequalificationVersionParam prequalificationVersionParam = new PrequalificationVersionParam();
        prequalificationVersionParam.setList(list);
        Integer version = versionmanagementDao.getSheetVersion(eventId,4);
        if(version == null){
            version = 1;
        }
        prequalificationVersionParam.setVersion(version);
        return new ReturnCode.Builder().success().msg("查询成功").object(prequalificationVersionParam).build();
    }
    @PostMapping("list")
    @ApiOperation(value = "分页")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {

        PageHelper.startPage(page, size);
        List<Prequalification> list = prequalificationService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().paramError().msg("查询结果不存在").build();
        }else{
            return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
        }
    }

}
