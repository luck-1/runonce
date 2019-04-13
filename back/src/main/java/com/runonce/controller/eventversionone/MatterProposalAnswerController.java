package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.util.WebTokenUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.runonce.model.eventversionone.MatterProposalAnswer;
import com.runonce.service.eventversionone.MatterProposalAnswerService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 回复表
 * <p>
 */

@RestController
@RequestMapping("matterProposalAnswer")
@Api(description = "回复表")
public class MatterProposalAnswerController {

    @Autowired
    private MatterProposalAnswerService matterProposalAnswerService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody MatterProposalAnswer matterProposalAnswer, HttpServletRequest request) {

        String userId = WebTokenUtil.getUserByWebToken(request);
        matterProposalAnswer.setAnswererId(userId);
        return matterProposalAnswerService.saveInfo(matterProposalAnswer);
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        matterProposalAnswerService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    public ReturnCode update(@RequestBody MatterProposalAnswer matterProposalAnswer) {
        matterProposalAnswerService.update(matterProposalAnswer);
        return new ReturnCode.Builder().msg("更新成功").object(matterProposalAnswer).success().build();
    }

    @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        MatterProposalAnswer matterProposalAnswer = matterProposalAnswerService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(matterProposalAnswer).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MatterProposalAnswer> list = matterProposalAnswerService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

}
