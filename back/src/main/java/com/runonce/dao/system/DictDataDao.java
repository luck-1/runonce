package com.runonce.dao.system;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.DictData;

import java.util.List;

/**
 * 字典数据数据处理层
 * @author Exrick
 */
public interface DictDataDao extends MyBaseJpaDao<DictData,String> {


    /**
     * 通过dictId和状态获取
     * @param dictId
     * @param status
     * @return
     */
    List<DictData> findByDictIdAndStatusOrderBySortOrder(String dictId, Integer status);

    /**
     * 通过dictId删除
     * @param dictId
     */
    void deleteByDictId(String dictId);
}