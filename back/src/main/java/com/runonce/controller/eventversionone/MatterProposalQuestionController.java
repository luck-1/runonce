package com.runonce.controller.eventversionone;

import java.util.List;

import com.runonce.httpbean.assets.reqbean.MatterProposalQuestionReq;
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

import com.runonce.model.eventversionone.MatterProposalQuestion;
import com.runonce.service.eventversionone.MatterProposalQuestionService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author swq
 * 问题表
 */

@RestController
@RequestMapping("matterProposalQuestion")
@Api(description = "问题表（评论）")
public class MatterProposalQuestionController {

    @Autowired
    private MatterProposalQuestionService matterProposalQuestionService;

    @PostMapping("add")
    public ReturnCode add(@RequestBody MatterProposalQuestion matterProposalQuestion, HttpServletRequest  request) {

        String userId = WebTokenUtil.getUserByWebToken(request);
        matterProposalQuestion.setQuestionerId(userId);
        return matterProposalQuestionService.saveInfo(matterProposalQuestion);

    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        matterProposalQuestionService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    public ReturnCode update(@RequestBody MatterProposalQuestion matterProposalQuestion) {
        matterProposalQuestionService.update(matterProposalQuestion);
        return new ReturnCode.Builder().msg("更新成功").object(matterProposalQuestion).success().build();
    }

    @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        MatterProposalQuestion matterProposalQuestion = matterProposalQuestionService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(matterProposalQuestion).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MatterProposalQuestion> list = matterProposalQuestionService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    @PutMapping("updateState")
    public ReturnCode updateState(@RequestBody MatterProposalQuestionReq matterProposalQuestionReq) {
        matterProposalQuestionService.updateState(matterProposalQuestionReq);
        return new ReturnCode.Builder().msg("更新成功").object(matterProposalQuestionReq).success().build();
    }



}
