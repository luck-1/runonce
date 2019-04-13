package com.runonce.controller.eventversionone;

import java.util.List;


import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.model.eventversionone.Prequalification;
import com.runonce.model.http.ProcessDescriptionParam;
import com.runonce.model.http.ProcessDescriptionVersionParam;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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

import com.runonce.model.eventversionone.ProcessDescription;
import com.runonce.service.eventversionone.ProcessDescriptionService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by xuxueli on '2018-12-10 11:49:40'.
 */

@RestController
@RequestMapping("processdescription")
@Api(description = "现有流程说明")
public class ProcessDescriptionController {

    @Autowired
    private ProcessDescriptionService processDescriptionService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @PostMapping("add")
    @ApiOperation(value = "新增数据")
    public ReturnCode add(@RequestBody ProcessDescription processDescription, HttpServletRequest request) {
        processDescription.setId(SysUtil.getUUID());
        processDescriptionService.save(processDescription);
        //更新父的状态
        departmentalMattersService.updateFatherState(processDescription.getEventId());
        return new ReturnCode.Builder().success().msg("添加成功").object(processDescription).build();
    }

    @PostMapping("save")
    @ApiOperation(value = "保存数据")
    public ReturnCode save(@RequestBody ProcessDescriptionParam processDescriptionParam, HttpServletRequest request) {
//        //验证是否有错，如果有错就输出错误信息
//        if(bindingResult.hasErrors()){
//            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
        if (processDescriptionParam == null) {
            return new ReturnCode.Builder().failed().msg("参数为空").build();
        }
        if (processDescriptionParam.getProcessDescriptionList() == null && processDescriptionParam.getProcessDescriptionList().size() == 0) {
            if (StringUtils.isEmpty(processDescriptionParam.getEventId())) {
                return new ReturnCode.Builder().failed().msg("事项id为空").build();
            }
            processDescriptionParam.setState(1);
        } else {
            for (ProcessDescription item : processDescriptionParam.getProcessDescriptionList()) {
                if (StringUtils.isEmpty(item.getEventId())) {
                    return new ReturnCode.Builder().failed().msg("事项id为空").build();
                }
            }
        }
        return processDescriptionService.saveInfo(processDescriptionParam,request);
    }

    @PostMapping("adds")
    @ApiOperation(value = "新增批量数据")
    public ReturnCode adds(@RequestBody List<ProcessDescription> list) {
        for (ProcessDescription prequalification : list) {
            prequalification.setId(SysUtil.getUUID());
            processDescriptionService.save(list);
        }
        return new ReturnCode.Builder().success().msg("添加成功").object(list).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除单条数据")
    public ReturnCode delete(@RequestParam String id) {
        ProcessDescription processDescription = processDescriptionService.findById(id);
        if (processDescription == null) {
            return new ReturnCode.Builder().paramError().msg("删除失败").build();
        } else {
            processDescriptionService.deleteById(id);
            return new ReturnCode.Builder().success().msg("删除成功").build();
        }
    }

    @DeleteMapping("deletes")
    @ApiOperation(value = "删除批量数据")
    public ReturnCode deletes(@RequestParam String ids) {
        List<ProcessDescription> list = processDescriptionService.findByIds(ids);

        if (null == list || list.size() == 0) {
            return new ReturnCode.Builder().paramError().msg("删除失败").build();
        } else {
            processDescriptionService.deleteByIds(ids);
            return new ReturnCode.Builder().success().msg("删除成功").build();
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "更新单条数据")
    public ReturnCode update(@RequestBody ProcessDescription processDescription) {
        processDescriptionService.update(processDescription);
        return new ReturnCode.Builder().success().msg("更新成功").object(processDescription).build();
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询单条数据")
    public ReturnCode detail(@RequestParam String id) {
        ProcessDescription processDescription = processDescriptionService.findById(id);
        if (processDescription == null) {
            return new ReturnCode.Builder().paramError().msg("查询结果不存在").build();
        } else {
            return new ReturnCode.Builder().object(processDescription).success().msg("查询成功").build();
        }
    }

    @GetMapping("details")
    @ApiOperation(value = "查询批量数据")
    public ReturnCode details(@RequestParam String ids) {
        List<ProcessDescription> list = processDescriptionService.findByIds(ids);
        if (null == list || list.size() == 0) {
            return new ReturnCode.Builder().paramError().msg("查询结果不存在").build();
        } else {
            return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
        }
    }

    /**
     * 根据流程id查询现有流程
     *
     * @param eventId
     * @return
     */
    @GetMapping("eventId")
    @ApiOperation(value = "根据事项ID查询结果集")
    public ReturnCode eventId(@RequestParam String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().paramError().msg("事项id为空").build();
        }
        List<ProcessDescription> list = null;

        eventId = departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        list = processDescriptionService.findByEventId(eventId);

        ProcessDescriptionVersionParam processDescriptionVersionParam = new ProcessDescriptionVersionParam();
        processDescriptionVersionParam.setList(list);
        Integer version = versionmanagementDao.getSheetVersion(eventId,5);
        if(version == null){
            version = 1;
        }
        processDescriptionVersionParam.setVersion(version);
        return new ReturnCode.Builder().success().msg("现有流程查询成功").object(processDescriptionVersionParam).build();
    }

    @PostMapping("list")
    @ApiOperation(value = "分页")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ProcessDescription> list = processDescriptionService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        if (null == list || list.size() == 0) {
            return new ReturnCode.Builder().paramError().msg("查询结果不存在").build();
        } else {
            return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
        }
    }

}
