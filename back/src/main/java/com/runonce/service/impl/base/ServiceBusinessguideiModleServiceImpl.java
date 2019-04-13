package com.runonce.service.impl.base;

import com.runonce.dao.base.ServiceBusinessguideiModleDao;
import com.runonce.model.base.ServiceBusinessguideiModle;
import com.runonce.model.base.ServiceObject;
import com.runonce.service.AbstractService;
import com.runonce.service.base.ServiceBusinessguideiModleService;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ServiceBusinessguideiModleServiceImpl extends AbstractService<ServiceBusinessguideiModle> implements ServiceBusinessguideiModleService {

    @Resource
    private ServiceBusinessguideiModleDao serviceBusinessguideiModleDao;

    /**
     * 添加，更新数据
     * @param serviceBusinessguideiModle
     */
//    public void save(ServiceBusinessguideiModle serviceBusinessguideiModle) {
//        //获得基础数据
//        String businessGuideId = serviceBusinessguideiModle.getBusinessGuideId();
//        //获得本事件历史数据
//        List<ServiceBusinessguideiModle> list = serviceBusinessguideiModleDao.findBusinessguideiModle(businessGuideId);
//
//        //先删除，再添加
////        if(serviceBusinessguideiModle.getId() == null){
//            serviceBusinessguideiModle.setId(SysUtil.getUUID());
//            if( !(list.isEmpty() || list.size() == 0)){
//                for(int i=0;i<list.size();i++){
//                    serviceBusinessguideiModleDao.deleteByPrimaryKey(list.get(i).getId());
//                }
//            }
//            serviceBusinessguideiModleDao.insert(serviceBusinessguideiModle);
////        }else{
////            serviceBusinessguideiModleDao.updateByPrimaryKey(serviceBusinessguideiModle);
////        }
//    }

    /**
     * 查找服务主题
     * @param businessGuideId
     * @return
     */
    @Override
    public List<String> findServiceObject(String businessGuideId){
        List<String> list = serviceBusinessguideiModleDao.findServiceObject(businessGuideId);
        return list;
    }

    @Override
    public void deleteByCondition(Condition condition){
        mapper.deleteByCondition(condition);
    }
}
