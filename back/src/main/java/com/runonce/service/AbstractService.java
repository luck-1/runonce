package com.runonce.service;

import com.runonce.ReturnCode;
import com.runonce.dao.MyMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * 鍩轰簬閫氱敤MyBatis Mapper鎻掍欢鐨凷ervice鎺ュ彛鐨勫疄鐜�
 */
@Service
public abstract class AbstractService<T> {

    @Autowired
    protected MyMapper<T> mapper;

    private Class<T> modelClass;    // 褰撳墠娉涘瀷鐪熷疄绫诲瀷鐨凜lass

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T model) { mapper.insertSelective(model);
    }

    public void save(List<T> models) {
        mapper.insertList(models);
    }

    public void deleteById(String id) {
        mapper.deleteByPrimaryKey(id);
    }

    public ReturnCode deleteByIds(String ids) {
        mapper.deleteByIds(ids);
        return null;
    }

    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    public T findById(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    @SuppressWarnings("unchecked")
	public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("鏌ヨ鎿嶄綔寮傚父!");
        }
    }

    /**
     * 按map查询
     * @param map
     * @return
     * @throws TooManyResultsException
     */
    public List<T> findByList(Map<String,Object> map) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            map.forEach((k,v)->{
                Field field = null;
                try {
                    field = modelClass.getDeclaredField(k);
                    field.setAccessible(true);
                    field.set(model, v);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

            return mapper.select(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("鏌ヨ鎿嶄綔寮傚父!");
        }
    }

    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    public List<T> findAll() {
        return mapper.selectAll();
    }
}
