package com.runonce.controller.eventversionone;

import java.util.ArrayList;
import java.util.List;

import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.model.http.MaterialGroupParam;
import com.runonce.model.http.MaterialGroupVersionParam;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;

import com.runonce.model.eventversionone.MaterialGrouping;
import  com.runonce.service.eventversionone.MaterialGroupingService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:49:40'.
*/
@Api(description = "材料分组")
@RestController
@RequestMapping("materialGrouping")
public class
MaterialGroupingController {

    @Autowired
    private MaterialGroupingService materialGroupingService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;


    /**
     * 材料暂存
     * @param materialGroupParam
     * @return
     */
    @ApiOperation(value = "材料保存")
    @PostMapping("save")
    public ReturnCode save(@RequestBody @Valid MaterialGroupParam materialGroupParam, BindingResult bindingResult,HttpServletRequest request) {
        //验证是否有错，如果有错就输出错误信息
        if(bindingResult.hasErrors()){
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        if(materialGroupParam == null){
            return new ReturnCode.Builder().failed().msg("参数为空").object(materialGroupParam).build();
        }
        if(StringUtils.isEmpty(materialGroupParam.getEventId())){
            return new ReturnCode.Builder().failed().msg("事项id为空").object(materialGroupParam).build();
        }
        if(materialGroupParam.getMaterialGroupingParamList() == null ||  materialGroupParam.getMaterialGroupingParamList().size() == 0){
            materialGroupParam.setState(1);
        }else{
            for(MaterialGroupAddParam item : materialGroupParam.getMaterialGroupingParamList()){
//                ReturnCode code  = validae(item,bindingResult);
//                if(code.getCode() == -1 ){
//                    return code;
//                }else{
//                    item = (MaterialGroupAddParam)code.getObj();
//                }
//                if(item.getOrderNum() == null){
//                    return new ReturnCode.Builder().msg("材料序号为空").failed().object(materialGroupParam).build();
//                }
                if(StringUtils.isEmpty(item.getEventId())){
                    return new ReturnCode.Builder().failed().msg("事项id为空").object(materialGroupParam).build();
                }
//                if(materialGroupParam.getState() == 2 && item.getPreAcceptance() == null){
//                    return new ReturnCode.Builder().failed().msg("是否预受理为空").object(materialGroupParam).build();
//                }
            }
        }
//        if(materialGroupParam.getState() == 1){
//            materialGroupingService.saveInfo(materialGroupParam);
//        }else{// 需验证
//            materialGroupingService.saveInfo(materialGroupParam);
//        }
        return materialGroupingService.saveInfo(materialGroupParam,request);
    }

//    /**
//     * 材料对象验证
//     * @param param
//     * @param bindingResult
//     * @return
//     */
//    public ReturnCode validae(MaterialGroupAddParam param, BindingResult bindingResult){
//        //验证是否有错，如果有错就输出错误信息
//        if(bindingResult.hasErrors()){
//            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
//        }
//        return new ReturnCode.Builder().success().object(param).build();
//    }
    /**
     * 材料新增
     * @param materialGroupParam
     * @return
     */
    @ApiOperation(value = "材料新增")
    @PostMapping("add")
    public ReturnCode add(@RequestBody @Valid MaterialGroupParam materialGroupParam,HttpServletRequest request) {
        materialGroupingService.saveInfo(materialGroupParam,request);
       return new ReturnCode.Builder().msg("添加成功").success().object(materialGroupParam).build();
    }
    @ApiOperation(value = "材料删除")
    @GetMapping("delete/{id}")
    public ReturnCode delete(@PathVariable @NotNull String id) {
	    materialGroupingService.deleteById(id);
	  	return new ReturnCode.Builder().msg("删除成功").success().build();
    }
    @ApiOperation(value = "材料更新")
 	@PostMapping("update")
    public ReturnCode update(@RequestBody @Valid MaterialGroupAddParam  materialGrouping) {
	    materialGroupingService.update(materialGrouping);
	 	return new ReturnCode.Builder().msg("更新成功").object(materialGrouping).success().build();
    }
    @ApiOperation(value = "材料查询(单个)")
   @GetMapping("detail/{id}")
    public ReturnCode detail(@PathVariable @NotNull String id) {
        MaterialGrouping materialGrouping = materialGroupingService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(materialGrouping).success().build();
    }

    @ApiOperation(value = "按事项id查询材料")
    @GetMapping("select/{id}")
    public ReturnCode selectByEventId(@PathVariable("id") String eventId){
        if(StringUtils.isEmpty(eventId)){
            return new ReturnCode.Builder().failed().msg("事项id为空").build();
        }
//        List<MaterialGroupAddParam> minderDataList = new ArrayList<>();
        MaterialGroupVersionParam materialGroupVersionParam = new MaterialGroupVersionParam();

        eventId =  departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)){
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        materialGroupVersionParam = materialGroupingService.selectByEventId(eventId);

        return new ReturnCode.Builder().success().msg("材料分组查询成功").object(materialGroupVersionParam).build();
    }

    @ApiOperation(value = "材料查询(分页)")
    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MaterialGroupAddParam> list = materialGroupingService.findAll();
        PageInfo pageInfo = new PageInfo(list);
       return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
