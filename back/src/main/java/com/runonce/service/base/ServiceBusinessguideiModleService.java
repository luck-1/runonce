package com.runonce.service.base;

import com.runonce.ReturnCode;
import com.runonce.model.base.ServiceBusinessguideiModle;
import com.runonce.service.Service;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

@Component
public interface ServiceBusinessguideiModleService extends Service<ServiceBusinessguideiModle> {

    void save(ServiceBusinessguideiModle serviceBusinessguideiModle);

    List<String > findServiceObject(String businessGuideId);

    void deleteByCondition(Condition condition);
}
