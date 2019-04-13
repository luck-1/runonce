package com.runonce.controller.eventversionone;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.runonce.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.runonce.model.eventversionone.CentralBusinessGuidanceDepartmentCode;
import com.runonce.service.eventversionone.CentralBusinessGuidanceDepartmentCodeService;

/**
 * @author lxc
 * @date 2019/3/18
 * @description 中央业务指导（实施）部门代码
 */
@RestController
@Api(description = "中央业务指导（实施）部门代码")
@RequestMapping("centralBusinessGuidanceDepartmentCode")
public class  CentralBusinessGuidanceDepartmentCodeController {

    @Autowired
    private CentralBusinessGuidanceDepartmentCodeService centralBusinessGuidanceDepartmentCodeService;

    @GetMapping("list")
    @ApiOperation(value = "查询所有")
    public ReturnCode list() {
        List<CentralBusinessGuidanceDepartmentCode> list = centralBusinessGuidanceDepartmentCodeService.findAll();
        return new ReturnCode.Builder().object(list).success().msg("查询成功").build();
    }

}