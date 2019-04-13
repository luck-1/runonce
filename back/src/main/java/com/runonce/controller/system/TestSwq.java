package com.runonce.controller.system;

import com.runonce.ReturnCode;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author swq
 * @date 2019/1/15 0015
 * @description
 */
@RestController
@Api(description = "施测试")
@RequestMapping("/xboot/testSwq")
@Transactional(rollbackFor = Exception.class)
public class TestSwq {
    @Autowired
    private DepartmentalMattersService departmentalMattersService;


    @RequestMapping(value = "/login/{id}/{isApproval}", method = RequestMethod.GET)
    @ApiOperation(value = "更新父")
    public ReturnCode test(@PathVariable String id, @PathVariable Integer isApproval) {
//        departmentalMattersService.updateExamineAndApproveState(id, isApproval);
        return new ReturnCode.Builder().msg("1111").success().build();
    }


}
