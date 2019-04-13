package com.runonce.controller.eventpublic;


import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.JointSectoralMattersReq;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventpublic.JointSectoralMatterReq;
import com.runonce.model.http.EventDistributionParam;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import com.runonce.service.eventpublic.JointSectoralMattersService;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.system.DepartmentService;
import com.runonce.util.ResultUtil;
import com.runonce.util.SysUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("jointSectoralMatters")
@Api(description = "部门事项分配库管理")
public class JointSectoralMattersController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JointSectoralMattersService jointSectoralMattersService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody JointSectoralMattersReq jointSectoralMattersReq, HttpServletRequest request) {

        EventsTable jointSectoralMattersReturn = jointSectoralMattersService.add(jointSectoralMattersReq, request);

        return new ReturnCode.Builder().msg("添加成功").success().object(jointSectoralMattersReturn).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        jointSectoralMattersService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    public ReturnCode update(@RequestBody JointSectoralMattersReq jointSectoralMattersReq) {
        JointSectoralMattersReq jointSectoralMattersReturn = jointSectoralMattersService.update(jointSectoralMattersReq);
        return new ReturnCode.Builder().msg("更新成功").object(jointSectoralMattersReturn).success().build();
    }


    /**
     * 按部门id查询已分配的事项
     *
     * @return
     */
    @PostMapping("selectbydeptid")
    public ReturnCode selectDistributedEvents(@RequestBody JointSectoralMatterReq jointSectoralMatterReq) {
        //验证是否有错，如果有错就输出错误信息
        PageInfo<EventsTable> events = jointSectoralMattersService.selectDistributedEvents(jointSectoralMatterReq);

        return new ReturnCode.Builder().msg("事项分配查询成功").object(events).success().build();
    }


}
