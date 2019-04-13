package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.KeywordTableService;
import com.runonce.dao.base.KeywordTableDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.KeywordTable;

/**
* 关键字
*
* Created by xuxueli on '2019-02-27 14:10:47'.
*/
@Service
public class KeywordTableServiceImpl extends AbstractService<KeywordTable> implements KeywordTableService  {

	@Resource
	private KeywordTableDao keywordTableDao;

	
}
