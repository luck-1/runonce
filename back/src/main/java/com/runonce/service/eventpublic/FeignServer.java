package com.runonce.service.eventpublic;

import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ImportDictReq;
import com.runonce.httpbean.assets.reqbean.ImportServiceTopicReq;
import com.runonce.httpbean.assets.reqbean.SendPlaceParam;

import com.runonce.httpbean.assets.reqbean.ServiceTopic;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "import",url = "${callNumber.url}")
public interface FeignServer {


    /**
     * 发送地点所有部门
     * @param sendPlaceParam
     * @return ReturnCode
     */
    @RequestMapping(value = "/manage/import/allGroup",method = RequestMethod.POST)
    ReturnCode sendPlace(@RequestBody SendPlaceParam sendPlaceParam);

    /**
     * 发送所有
     * @param importServiceTopicReq
     * @return ReturnCode
     */
    @RequestMapping(value = "/manage/import/topic",method = RequestMethod.POST)
    ReturnCode sendTopic(@RequestBody ImportServiceTopicReq importServiceTopicReq);


    /**
     * 发送所有字典
     * @param importDictReqList
     * @return ReturnCode
     */
    @RequestMapping(value = "/manage/import/dict",method = RequestMethod.POST)
    ReturnCode sendDict(List<ImportDictReq> importDictReqList);
}
