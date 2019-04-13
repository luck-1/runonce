package com.runonce.service.impl.base;
import javax.annotation.Resource;

import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ImportServiceTopicReq;
import com.runonce.service.eventpublic.FeignServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.base.ServiceTopicService;
import com.runonce.dao.base.ServiceTopicDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.ServiceTopic;

import java.util.ArrayList;
import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:37:24'.
*/
@Service
public class ServiceTopicServiceImpl extends AbstractService<ServiceTopic> implements ServiceTopicService  {

	private final FeignServer feignServer;


	@Resource
	private ServiceTopicDao serviceTopicDao;

    @Autowired
    public ServiceTopicServiceImpl(FeignServer feignServer) {
        this.feignServer = feignServer;
    }

    @Override
	public List<ServiceTopic> findByPid(List<String> pids){

		List<String> list= new ArrayList<>();
		List<String> list1;

		for(int i=0;i<pids.size();i++){
			list1 = serviceTopicDao.findByPid(pids.get(i));
			if(i == 0){
				list = list1;
			}
			list.retainAll(list1);
		}
		List<ServiceTopic> result= new ArrayList<>();
		for(int i=0;i<list.size();i++){
			ServiceTopic topic = serviceTopicDao.selectByCode(list.get(i));
			result.add(topic);
		}
		return result;
	}

	/**
	 * 发送所有服务主题
	 * @return ReturnCode
	 */
	@Override
	public ReturnCode sendTopic() {
        ImportServiceTopicReq importServiceTopicReq = new ImportServiceTopicReq();
		List<com.runonce.httpbean.assets.reqbean.ServiceTopic> serviceTopicList = serviceTopicDao.sendTopic();
        importServiceTopicReq.setServiceTopicList(serviceTopicList);
        List<com.runonce.httpbean.assets.reqbean.ObjectTopic> objectTopicList = serviceTopicDao.sendObjectTopic();
        importServiceTopicReq.setObjectTopicList(objectTopicList);

		return feignServer.sendTopic(importServiceTopicReq);
	}




}