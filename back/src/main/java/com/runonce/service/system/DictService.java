package com.runonce.service.system;

import com.runonce.ReturnCode;
import com.runonce.model.system.Dict;
import com.runonce.service.MyBaseJpaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典接口
 * @author Exrick
 */
@Service
public interface DictService extends MyBaseJpaService<Dict,String> {

    /**
     * 通过type获取
     * @param type
     * @return
     */
    Dict findByType(String type);

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    List<Dict> findByTitleOrTypeLike(String key);

    /**
     * 推送所有字典
     * @return ReturnCode
     */
    ReturnCode sendDict();
}