package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.PrequalificationTwoService;
import com.runonce.dao.eventversiontwo.PrequalificationTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.PrequalificationTwo;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class PrequalificationTwoServiceImpl extends AbstractService<PrequalificationTwo> implements PrequalificationTwoService  {

	@Resource
	private PrequalificationTwoDao prequalificationTwoDao;
	@Override
	public List<PrequalificationTwo> findByEventId(String id) {
		List<PrequalificationTwo> list=prequalificationTwoDao.findByEventId(id);
		return list;
	}

	@Override
	public void save(List<PrequalificationTwo> list) {
		for (PrequalificationTwo PrequalificationTwo:list){
			prequalificationTwoDao.deleteByEventId(PrequalificationTwo.getEventId());
		}
		if (list.size()==1&& StringUtils.isEmpty(list.get(0).getAuditMode()) && StringUtils.isEmpty(list.get(0).getAuditOperation()) && StringUtils.isEmpty(list.get(0).getAuditOperationPic()) && StringUtils.isEmpty(list.get(0).getAuditSpecification()) && StringUtils.isEmpty(list.get(0).getItemNumber()) && StringUtils.isEmpty(list.get(0).getPretrialContent()) && StringUtils.isEmpty(list.get(0).getRemarks())){
			prequalificationTwoDao.deleteByEventId(list.get(0).getEventId());
		}else {
			for (PrequalificationTwo PrequalificationTwo:list){
				PrequalificationTwo.setId(SysUtil.getUUID());
				mapper.insertSelective(PrequalificationTwo);
			}
		}
	}
}
