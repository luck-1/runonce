package com.runonce.controller.eventversiontwo;

import java.util.List;

import com.runonce.controller.eventversionone.ProcessDescriptionPicController;
import com.runonce.httpbean.assets.reqbean.QuantitativeRenderingReq;
import com.runonce.model.Message;
import com.runonce.model.http.ProcessDataParam;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversiontwo.QuantitativeRendering;
import  com.runonce.service.eventversiontwo.QuantitativeRenderingService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/

@RestController
@RequestMapping("quantitativeRendering")
@Api(description = "量化呈现管理")
public class  QuantitativeRenderingController {
    /** log对象 */
    private final Logger Log = LoggerFactory.getLogger(ProcessDescriptionPicController.class);
    @Autowired
    private QuantitativeRenderingService quantitativeRenderingService;

    @Autowired
    private DepartmentalMattersService departmentalMattersService;
    /**
     * 保存量化呈现记录失败
     * @param
     * @param bindingResult
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "量化呈现新增")
    public ReturnCode add(@RequestBody @Valid QuantitativeRenderingReq quantitativeRenderingReq, BindingResult bindingResult, HttpServletRequest request) {
        //验证是否有错，如果有错就输出错误信息
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        if(quantitativeRenderingReq == null){
            return new ReturnCode.Builder().msg("参数为空").failed().build();
        }

        QuantitativeRendering quantitativeRendering = quantitativeRenderingReq.getQuantitativeRendering();
        Integer version = quantitativeRenderingReq.getVersion();
        ReturnCode code = quantitativeRenderingService.saveInfo(quantitativeRendering,version,request);

       return code;
    }

    /**
     * 按事项id查询量化呈现对象
     * @param eventId
     * @return
     */
    @GetMapping("select/{eventid}")
    @ApiOperation(value = "按事项id查询量化呈现对象")
    public ReturnCode selectbyeventId(@PathVariable("eventid") String eventId) {
        if(StringUtils.isEmpty(eventId)){
            return new ReturnCode.Builder().failed().msg("参数为空").build();
        }
        eventId = departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)) {
                return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        QuantitativeRenderingReq quantitativeRenderingReq = quantitativeRenderingService.findBydmId(eventId);
        return new ReturnCode.Builder().success().msg("量化呈现查询成功").object(quantitativeRenderingReq).build();
    }
    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
	    quantitativeRenderingService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }

 	@PutMapping("update")
    public ReturnCode update(@RequestBody QuantitativeRendering  quantitativeRendering) {
	    quantitativeRenderingService.update( quantitativeRendering);
	 	return new ReturnCode.Builder().msg("更新成功").object(quantitativeRendering).success().build();
    }

//   @GetMapping("detail")
//    public ReturnCode detail(@RequestParam String dmId) {
//        QuantitativeRendering quantitativeRendering = quantitativeRenderingService.findBydmId(dmId);
//        return new ReturnCode.Builder().msg("查询成功").object(quantitativeRendering).success().build();
//    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QuantitativeRendering> list = quantitativeRenderingService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @PostMapping("generatepngbyxml")
    @ApiOperation(value = "根据xml数据生成png图片")
    public ReturnCode generatePngByXml(@RequestBody @Valid ProcessDataParam param, BindingResult bindingResult,HttpServletRequest request){
        //验证是否有错，如果有错就输出错误信息
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        Log.info("根据xml数据生成png图片, xml数据为 =》"+param.toString());
        String filePath = null;
        try{
            filePath = quantitativeRenderingService.generatePngByXml(param,request);
        }catch (Exception e){
            Log.error("xml生成png异常, xml数据为 =》"+param.toString() + "异常信息 =>"+e);
            return new ReturnCode.Builder().failed().msg("xml生成png异常").object("").build();
        }
        return new ReturnCode.Builder().success().msg("xml生成png成功").object(filePath).build();
    }
}
