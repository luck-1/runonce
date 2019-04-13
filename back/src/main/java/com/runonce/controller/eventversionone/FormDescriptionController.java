package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.model.eventversionone.MaterialGrouping;
import com.runonce.model.http.FormDescriptionParam;
import com.runonce.model.http.FormThatParam;
import com.runonce.model.http.FormThatStateParm;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.FormDescription;
import com.runonce.service.eventversionone.FormDescriptionService;

import javax.servlet.http.HttpServletRequest;

/**
 * 表单说明
 * <p>
 * Created by xuxueli on '2018-12-18 13:17:37'.
 */

@RestController
@RequestMapping("formDescription")
@Api(description = "表单填写说明 ")
public class FormDescriptionController {

    @Autowired
    private FormDescriptionService formDescriptionService;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;


    @PostMapping("showText")
    @ApiOperation(value = "显示图片文字 ")
    public ReturnCode showText(@RequestParam String file) {
        if (file == null || file.equals("")) {
            return new ReturnCode.Builder().failed().msg("路径为空").build();
        }
        JSONObject reg = formDescriptionService.showPictureText(file);
        if (reg == null) {
            return new ReturnCode.Builder().failed().msg("文件不存在").build();
        }
        return new ReturnCode.Builder().success().msg("查询成功").object(reg.toString()).build();
    }

    @GetMapping("findMaterial/{eventId}")
    @ApiOperation(value = "显示所有材料")
    public ReturnCode materialQuery(@PathVariable String eventId) {


        eventId = departmentalMattersService.getMappingId(eventId);
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }
        if (eventId == null || eventId.equals("")) {
            return new ReturnCode.Builder().failed().msg("事件ID不允许为空").build();
        }
        FormThatStateParm formThatStateParm = formDescriptionService.findPhoto1(eventId);
        return new ReturnCode.Builder().success().msg("查询成功").object(formThatStateParm).build();
    }

    @PostMapping("save")
    @ApiOperation(value = "保存 ")
    public ReturnCode save(@RequestBody FormDescriptionParam formDescriptionParam,HttpServletRequest request) {
        if(formDescriptionParam == null){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        return formDescriptionService.add(formDescriptionParam,request);
    }


    @DeleteMapping("delete")
    @ApiOperation(value = "删除 ")
    public ReturnCode delete(@RequestParam String id) {
        if (id == null || id.equals("")) {
            return new ReturnCode.Builder().failed().msg("ID为空").build();
        }
        formDescriptionService.deleteById(id);
        return new ReturnCode.Builder().success().msg("删除成功").build();
    }

    @PostMapping("findFormThat")
    @ApiOperation(value = "查询表单说明")
    public ReturnCode findFormThat(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, @RequestBody FormThatParam formThatParam) {

        String eventId = departmentalMattersService.getMappingId(formThatParam.getEventId());
        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("事项映射Id为空").build();
        }

        formThatParam.setEventId(eventId);
        PageHelper.startPage(page, size);
        List<FormDescription> list = formDescriptionService.findMaterial(formThatParam);
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().success().msg("查询成功").object(pageInfo).build();
    }
}