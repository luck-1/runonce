package com.runonce.dao.system.mapper;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.dao.MyMapper;
import com.runonce.model.system.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 字典数据处理层
 * @author Exrick
 */
@Mapper
public interface DictMapper extends MyMapper<Dict> {

}