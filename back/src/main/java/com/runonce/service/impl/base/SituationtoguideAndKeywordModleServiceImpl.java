package com.runonce.service.impl.base;
import javax.annotation.Resource;

import com.runonce.dao.base.KeywordTableDao;
import com.runonce.model.base.KeywordTable;
import org.springframework.stereotype.Service;
import com.runonce.service.base.SituationtoguideAndKeywordModleService;
import com.runonce.dao.base.SituationtoguideAndKeywordModleDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.SituationtoguideAndKeywordModle;

import java.util.ArrayList;
import java.util.List;

/**
* 情形引导关键字中间表
*
* Created by xuxueli on '2019-02-27 14:10:47'.
*/
@Service
public class SituationtoguideAndKeywordModleServiceImpl extends AbstractService<SituationtoguideAndKeywordModle> implements SituationtoguideAndKeywordModleService  {

	@Resource
	private SituationtoguideAndKeywordModleDao situationtoguideAndKeywordModleDao;

}
