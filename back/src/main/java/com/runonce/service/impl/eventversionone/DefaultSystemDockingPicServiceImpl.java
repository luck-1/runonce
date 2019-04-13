package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.DefaultSystemDockingPicService;
import com.runonce.dao.eventversionone.DefaultSystemDockingPicDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.DefaultSystemDockingPic;

import java.util.ArrayList;
import java.util.List;

/**
* 
*
* Created by xuxueli on '2019-01-04 10:40:30'.
*/
@Service
public class DefaultSystemDockingPicServiceImpl extends AbstractService<DefaultSystemDockingPic> implements DefaultSystemDockingPicService  {

	@Resource
	private DefaultSystemDockingPicDao defaultSystemDockingPicDao;


	@Override
	public List<DefaultSystemDockingPic> selectAll() {
		List<DefaultSystemDockingPic> list = new ArrayList<>();
		list = defaultSystemDockingPicDao.selectAllRecord();
		return list;
	}
}
