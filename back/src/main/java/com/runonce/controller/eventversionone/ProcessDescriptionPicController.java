package com.runonce.controller.eventversionone;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ProcessDescriptionPicReq;
import com.runonce.model.eventversionone.ProcessDescriptionPic;
import com.runonce.model.http.ProcessDataParam;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.eventversionone.ProcessDescriptionPicService;
import com.runonce.util.OssClientUtil;
import com.runonce.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:49:40'.
*/

@RestController
@RequestMapping("processdescriptionpic")
@Api(description = "现有流程说明图片")
public class ProcessDescriptionPicController {
    /** log对象 */
    private final Logger Log = LoggerFactory.getLogger(ProcessDescriptionPicController.class);

    @Autowired
    private ProcessDescriptionPicService processDescriptionPicService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @PostMapping("add")
    @ApiOperation(value = "新增数据")
    public ReturnCode add(@RequestBody  ProcessDescriptionPicReq  processDescriptionPicReq,HttpServletRequest request,BindingResult bindingResult) {
        //验证是否有错，如果有错就输出错误信息
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        if (processDescriptionPicReq == null){
            return new ReturnCode.Builder().failed().msg("参数为空").build();
        }

        return processDescriptionPicService.saveInfo(processDescriptionPicReq,request);
    }

    @PostMapping("saveimage")
    @ApiOperation(value = "流程绘制转换图片")
    public  ReturnCode saveImage(ProcessDataParam param,HttpServletRequest request) throws IOException {

        if(param!=null){
            param.setFormat("png");
        }

      String str=  processDescriptionPicService.generatePngByXml( param, request);

        return new ReturnCode.Builder().success().msg("成功").object(str).build();
    }



    @PostMapping("adds")
    @ApiOperation(value = "新增批量数据")
    public ReturnCode adds(@RequestBody List<ProcessDescriptionPic>  list) {
        for(ProcessDescriptionPic prequalification : list) {
            prequalification.setId(SysUtil.getUUID());
            processDescriptionPicService.save(list);
        }
        return new ReturnCode.Builder().success().msg("添加成功").object(list).build();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除单条数据")
    public ReturnCode delete(@RequestParam String id) {
        ProcessDescriptionPic ProcessDescriptionPic = processDescriptionPicService.findById(id);
        if(ProcessDescriptionPic==null){
            return new ReturnCode.Builder().paramError().msg("删除失败").build();
        }else{
            processDescriptionPicService.deleteById(id);
            return new ReturnCode.Builder().success().msg("删除成功").build();
        }
    }
    @DeleteMapping("deletes")
    @ApiOperation(value = "删除批量数据")
    public ReturnCode deletes(@RequestParam String ids) {
        List<ProcessDescriptionPic> list = processDescriptionPicService.findByIds(ids);

        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().success().msg("删除失败").build();
        }else{
            processDescriptionPicService.deleteByIds(ids);
            return new ReturnCode.Builder().success().msg("删除成功").build();
        }
    }
 	@PutMapping("update")
    @ApiOperation(value = "更新单条数据")
    public ReturnCode update(@RequestBody ProcessDescriptionPic  ProcessDescriptionPic) {
        processDescriptionPicService.update(ProcessDescriptionPic);
	 	return new ReturnCode.Builder().success().msg("更新成功").build();
    }

   @GetMapping("detail")
    @ApiOperation(value = "查询单条数据")
    public ReturnCode detail(@RequestParam String id) {
        ProcessDescriptionPic ProcessDescriptionPic = processDescriptionPicService.findById(id);
        if(ProcessDescriptionPic==null){
            return new ReturnCode.Builder().success().msg("查询结果不存在").build();
        }else{
            return new ReturnCode.Builder().object(ProcessDescriptionPic).success().msg("查询成功").build();
        }
    }


    @GetMapping("details")
    @ApiOperation(value = "查询批量数据")
    public ReturnCode details(@RequestParam String ids) {
        List<ProcessDescriptionPic> list = processDescriptionPicService.findByIds(ids);
        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().success().msg("查询结果不存在").build();
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
        ProcessDescriptionPicReq processDescriptionPicReq = processDescriptionPicService.findByEventId(eventId);
        if(processDescriptionPicReq==null){
            return new ReturnCode.Builder().success().msg("查询结果不存在").build();
        }else{
            return new ReturnCode.Builder().object(processDescriptionPicReq).success().msg("查询成功").build();
        }
    }
    @PostMapping("list")
    @ApiOperation(value = "分页")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ProcessDescriptionPic> list = processDescriptionPicService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        if(null == list || list.size() ==0 ){
            return new ReturnCode.Builder().success().msg("查询结果不存在").build();
        }else{
            return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
        }
    }

    @PostMapping("generatepngbyxml")
    @ApiOperation(value = "根据xml数据生成png图片")
    public ReturnCode generatePngByXml(@RequestBody @Valid ProcessDataParam param,BindingResult bindingResult,HttpServletRequest request){
        //验证是否有错，如果有错就输出错误信息
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        Log.info("根据xml数据生成png图片, xml数据为 =》"+param.toString());
        String filePath = null;
        try{
            filePath = processDescriptionPicService.generatePng(param,request);
        }catch (Exception e){
            Log.error("xml生成png异常, xml数据为 =》"+param.toString() + "异常信息 =>"+e);
            return new ReturnCode.Builder().failed().msg("xml生成png异常").object("").build();
        }
        return new ReturnCode.Builder().success().msg("xml生成png成功").object(filePath).build();
    }
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ReturnCode upload(@RequestBody MultipartFile file) {
        String filepath = "";
        if(file == null){
            return new ReturnCode.Builder().failed().msg("文件为空").object(filepath).build();
        }
        try {
            filepath = OssClientUtil.uploadFileByInputStream(file.getInputStream(),"",1);
        } catch (IOException e) {
            e.printStackTrace();
            return new ReturnCode.Builder().failed().msg("文件保存失败").object(filepath).build();
        }
        return new ReturnCode.Builder().success().msg("文件保存成功").object(filepath).build();

    }

}
