package com.runonce.controller.eventversionone;

import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.SystemDocking;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.eventversionone.SystemDockingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: zhaolei
 * @Descriptions: 系统对接controller
 * @Date: create at 2018/12/29 0029 下午 4:57
 */
@RestController
@RequestMapping("/systemdocking")
@Api(description = "系统对接")
public class SystemDockingController {
    Logger Log = LoggerFactory.getLogger(SystemDockingController.class);
    /**
     * 系统对接service
     */
    @Autowired
    private SystemDockingService systemDockingService;

    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @PostMapping("save")
    @ApiOperation(value = "系统对接记录保存")
    public ReturnCode save(@RequestBody @Valid SystemDocking systemDocking, BindingResult bindingResult, HttpServletRequest request){
        //验证是否有错，如果有错就输出错误信息
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        return systemDockingService.saveInfo(systemDocking,request);
    }
    @GetMapping("select/{eventId}")
    @ApiOperation(value = "按事项id查询")
    public ReturnCode selectByEventId(@PathVariable String eventId){
        if(StringUtils.isEmpty(eventId)){
            return new ReturnCode.Builder().failed().msg("参数为空").build();
        }
        eventId = departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        return systemDockingService.selectByEventId(eventId);
    }
}
