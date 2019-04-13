package com.runonce.service;

import com.runonce.ReturnCode;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 灞� 鍩虹鎺ュ彛锛屽叾浠朣ervice 鎺ュ彛 璇风户鎵胯鎺ュ彛
 * Created by zhh on 2017/09/18.
 */
@org.springframework.stereotype.Service
public interface Service<T> {
	
	/**
	 * 鎻掑叆鏁版嵁
     * @param model
     */
    void save(T model);
    
    /**
     * 鎵归噺鎻掑叆鏁版嵁
     * @param models
     */
    void save(List<T> models);
    
    /**
     * 閫氳繃涓婚嵉鍒櫎
     * @param id
     */
    void deleteById(String id);
    
    /**
     * 鎵归噺鍒櫎
     * @param ids eg锛歩ds -> "1,2,3,4"
     */
    ReturnCode deleteByIds(String ids);
    
    /**
     * 鏇存柊
     * @param model
     */

    void update(T model);
    
    /**
     * 閫氳繃ID鏌ユ壘
     * @param id
     * @return
     */
    T findById(String id);

    /**
     * 閫氳繃Model涓煇涓垚鍛樺彉閲忓悕绉帮紙闈炴暟鎹〃涓璫olumn鐨勫悕绉帮級鏌ユ壘,value闇�绗﹀悎unique绾︽潫
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    T findBy(String fieldName, Object value) throws TooManyResultsException;
    
    /**
     * 閫氳繃澶氫釜ID鏌ユ壘
     * @param ids eg锛歩ds -> "1,2,3,4"
     * @return
     */
    List<T> findByIds(String ids);
    
    /**
     * 鏍规嵁鏉′欢鏌ユ壘
     * @param condition
     * @return
     */
    List<T> findByCondition(Condition condition);
    
    /**
     * 鑾峰彇鎵�鏈�
     * @return
     */
    List<T> findAll();


}
