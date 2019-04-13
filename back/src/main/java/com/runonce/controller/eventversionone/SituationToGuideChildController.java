package com.runonce.controller.eventversionone;

import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ConfigurationMattersReq;
import com.runonce.httpbean.assets.reqbean.IdAndNameReq;
import com.runonce.httpbean.assets.reqbean.SituationToGuideChildSelectReq;
import com.runonce.model.eventversionone.SituationToGuideChild;
import com.runonce.model.http.DepartmentalMattersRes;
import com.runonce.service.eventversionone.SituationToGuideChildService;
import com.runonce.util.WebTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * @author lxc
 * @date 2019-02-28 15:47:09
 */
@RestController
@RequestMapping("situationToGuideChildren")
@Api(description = "情形引导子项")
public class SituationToGuideChildController {

    private final SituationToGuideChildService situationToGuideChildService;

    @Autowired
    public SituationToGuideChildController(SituationToGuideChildService situationToGuideChildService) {
        this.situationToGuideChildService = situationToGuideChildService;
    }

    @PostMapping("saveFrom")
    @ApiOperation(value = "添加/修改情形引导子项")
    public ReturnCode saveFrom(@RequestBody @Valid SituationToGuideChild situationToGuideChild, BindingResult bindingResult, HttpServletRequest request) {
        if (situationToGuideChild == null) {
            return new ReturnCode.Builder().msg("输入为空").failed().build();
        }
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().msg(bindingResult.getFieldError().getDefaultMessage()).failed().build();
        }

        if (situationToGuideChild.getId() != null) {
            SituationToGuideChild selectSituationToGuideChild = situationToGuideChildService.findById(situationToGuideChild.getId());
            if (selectSituationToGuideChild == null) {
                return new ReturnCode.Builder().msg("不存在该情形引导子项，请确认。").failed().build();
            }

        }
        String userId = WebTokenUtil.getUserByWebToken(request);

        situationToGuideChild.setModifiedBy(userId);
        situationToGuideChildService.saveFrom(situationToGuideChild);
        return new ReturnCode.Builder().msg("添加成功").success().build();
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation(value = "删除单个")
    public ReturnCode delete(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            return new ReturnCode.Builder().failed().msg("请选择情形引导子项").build();
        }
        situationToGuideChildService.delete(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }


    @DeleteMapping("deleteBySelect")
    @ApiOperation(value = "批量删除")
    public ReturnCode deleteBySelect(@RequestParam List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        situationToGuideChildService.deleteBySelect(ids);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PostMapping("selectByCondition")
    @ApiOperation(value = "条件查询")
    public ReturnCode selectByCondition(@RequestBody @Valid SituationToGuideChildSelectReq situationToGuideChildSelectReq, BindingResult bindingResult, HttpServletRequest request) {
        if (situationToGuideChildSelectReq == null) {
            return new ReturnCode.Builder().msg("输入为空").failed().build();
        }
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().msg(bindingResult.getFieldError().getDefaultMessage()).failed().build();
        }
        Map<String, Object> map = situationToGuideChildService.selectByCondition(situationToGuideChildSelectReq);
        return new ReturnCode.Builder().msg("查询成功").success().object(map).build();
    }

    @PostMapping("checkDuplicateName")
    @ApiOperation(value = "名称是否重复")
    public ReturnCode checkDuplicateName(@RequestBody @Valid IdAndNameReq idAndNameReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().msg(bindingResult.getFieldError().getDefaultMessage()).failed().build();
        }
        boolean bool = situationToGuideChildService.checkDuplicateName(idAndNameReq);
        return new ReturnCode.Builder().msg("检查成功").success().object(bool).build();
    }

    @PostMapping("configurationMatters")
    @ApiOperation(value = "配置事项")
    public ReturnCode configurationMatters(@RequestBody @Valid ConfigurationMattersReq configurationMattersReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().msg(bindingResult.getFieldError().getDefaultMessage()).failed().build();
        }
        situationToGuideChildService.configurationMatters(configurationMattersReq);
        return new ReturnCode.Builder().msg("配置成功").success().build();
    }


    @GetMapping("selectConfigurationMatters/{id}")
    @ApiOperation(value = "查询配置事项")
    public ReturnCode selectConfigurationMatters(@PathVariable String id) {

        SituationToGuideChild situationToGuideChild = situationToGuideChildService.findById(id);
        if (situationToGuideChild == null) {
            return new ReturnCode.Builder().msg("不存在该情形引导子项，请确认。").failed().build();

        }
        List<DepartmentalMattersRes> departmentalMattersResList = situationToGuideChildService.selectConfigurationMatters(id);
        return new ReturnCode.Builder().msg("查询成功").success().object(departmentalMattersResList).build();
    }
}
