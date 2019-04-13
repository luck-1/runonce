package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import com.runonce.ReturnCode;
import com.runonce.dao.base.KeywordTableDao;
import com.runonce.dao.base.SituationtoguideAndChildModelDao;
import com.runonce.dao.base.SituationtoguideAndKeywordModleDao;
import com.runonce.dao.eventversionone.SituationToGuideChildDao;
import com.runonce.exception.CustomException;
import com.runonce.model.base.KeywordTable;
import com.runonce.model.base.SituationtoguideAndChildModel;
import com.runonce.model.base.SituationtoguideAndKeywordModle;
import com.runonce.model.eventversionone.SituationToGuideChild;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.SituationToGuideService;
import com.runonce.dao.eventversionone.SituationToGuideDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.SituationToGuide;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 情形引导
*
* @auther lxc
*/
@Service
public class SituationToGuideServiceImpl extends AbstractService<SituationToGuide> implements SituationToGuideService  {

	@Resource
	private SituationToGuideDao situationToGuideDao;

	@Resource
	private KeywordTableDao keywordTableDao;

	@Resource
	private SituationtoguideAndKeywordModleDao situationtoguideAndKeywordModleDao;

	@Resource
	private SituationtoguideAndChildModelDao situationtoguideAndChildModelDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(SituationToGuide situationToGuide){
		Integer count = situationToGuideDao.findEventNameCount(situationToGuide.getEventName(),situationToGuide.getId() );
		if(count > 0){
			throw new CustomException(new ReturnCode.Builder().failed().msg("事项名称已存在").build());
		}
		//id不存在新增，存在更改
		if(situationToGuide.getId() == null || "".equals(situationToGuide.getId())){
			situationToGuide.setId(SysUtil.getUUID());
			situationToGuideDao.insert(situationToGuide);
		}else{
			//删除中间表数据
			situationtoguideAndKeywordModleDao.deleteBySituationToGuideId(situationToGuide.getId());
			situationToGuideDao.updateByPrimaryKey(situationToGuide);
		}
		//插入中间表记录，插入关键字表数据
		List<String> keywordLst = situationToGuide.getKeyword();
		keywordLst.forEach(keyword -> {
			//去除两端空格
			keyword = keyword.trim();
			//查询是否存在该关键字
			KeywordTable keywordTable = keywordTableDao.findKeywordTableByName(keyword);
			//不存在该关键字,关键字表新增
			if(keywordTable == null){
				//插入关键字
				keywordTable = new KeywordTable();
				keywordTable.setName(keyword);
				keywordTable.setId(SysUtil.getUUID());
				keywordTableDao.insert(keywordTable);
			}
			//中间表记录
			SituationtoguideAndKeywordModle situationtoguideAndKeywordModle = new SituationtoguideAndKeywordModle();
			situationtoguideAndKeywordModle.setId(SysUtil.getUUID());
			situationtoguideAndKeywordModle.setKeywordId(keywordTable.getId());
			situationtoguideAndKeywordModle.setSituationToGuideId(situationToGuide.getId());
			situationtoguideAndKeywordModleDao.insert(situationtoguideAndKeywordModle);
		});
	}

	@Override
	public List<SituationToGuide> findByEventName(String eventName){
		//从名称模糊查询
		List<SituationToGuide> situationToGuideList = situationToGuideDao.findByEventName(eventName);
		return situationToGuideList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		//查询是否有子项
		Integer count = situationtoguideAndChildModelDao.findChildCount(id);
		if( count != null && count > 0){
			throw new CustomException(new ReturnCode.Builder().failed().msg("请先删除子项").build());
		}
		//删除父项纪录
		situationToGuideDao.deleteByPrimaryKey(id);
		//删除父项和子项的中间表记录
		situationtoguideAndChildModelDao.deleteByPid(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBySelect(List<String> ids){
		ids.forEach(id -> delete(id));
	}

	/**
	 * 情形引导父项添加子项
	 * @param pid 父项ID
	 * @param ids 要添加的子项
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addChildren(String pid,List<String> ids){
		//删除原有的
		situationtoguideAndChildModelDao.deleteByPid(pid);
		//为空直接显示添加成功
		if(ids == null || ids.size() == 0){
            return ;
        }
		//添加子项
		ids.forEach(id -> {
			SituationtoguideAndChildModel situationtoguideAndChildModel = new SituationtoguideAndChildModel();
			situationtoguideAndChildModel.setId(SysUtil.getUUID());
			situationtoguideAndChildModel.setChildId(id);
			situationtoguideAndChildModel.setPid(pid);
			situationtoguideAndChildModelDao.insert(situationtoguideAndChildModel);
		});
	}

	/**
	 * 查询情形引导子项
	 * @param pid 父项ID
	 * @return
	 */
	@Override
	public List<SituationToGuideChild> findChildren(String pid){
		//查询子项
		List<SituationToGuideChild> list = situationtoguideAndChildModelDao.findChildByPid(pid);
		return list;
	}

	@Override
	public Boolean checkHasChild(String pid){
		Integer count = situationtoguideAndChildModelDao.findChildCount(pid);
		Boolean b = false;
		if(count.equals(0)){
			b = true;
		}
		return b;
	}
}
