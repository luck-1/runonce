package com.runonce.dao.system.mapper;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.dao.MyMapper;
import com.runonce.model.system.DictData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典数据数据处理层
 * @author Exrick
 */
@Mapper
public interface DictDataMapper extends MyMapper<DictData> {



}