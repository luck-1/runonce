package com.runonce.service.system.impl;
import com.runonce.ReturnCode;
import com.runonce.dao.base.ServiceObjectDao;
import com.runonce.dao.system.DictDao;
import com.runonce.httpbean.assets.reqbean.ImportDictReq;
import com.runonce.model.base.ServiceObject;
import com.runonce.model.system.Dict;
import com.runonce.service.eventpublic.FeignServer;
import com.runonce.service.system.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典接口实现
 *
 * @author Exrick
 */
@Service
@Transactional
public class DictServiceImpl implements DictService {

    private final DictDao dictDao;
    private final FeignServer feignServer;

    @Resource
    private ServiceObjectDao serviceObjectDao;

    @Autowired
    public DictServiceImpl(DictDao dictDao, FeignServer feignServer) {
        this.dictDao = dictDao;
        this.feignServer = feignServer;
    }

    @Override
    public DictDao getRepository() {
        return dictDao;
    }

    @Override
    public Dict findByType(String type) {

        List<Dict> list = dictDao.findByType(type);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Dict> findByTitleOrTypeLike(String key) {

        return dictDao.findByTitleOrTypeLike(key);
    }

    /**
     * 推送所有字典
     *
     * @return ReturnCode
     */
    @Override
    public ReturnCode sendDict() {

        List<ServiceObject> serviceObjectList = serviceObjectDao.selectAll();
        List<ImportDictReq> importDictReqList = new ArrayList<>();

        ImportDictReq importDictReq = new ImportDictReq();
        importDictReq.setFDictName("服务对象");
        importDictReq.setFDictDescription("服务对象");
        importDictReq.setFDictType("service_object");
        for (int i = 0; i < serviceObjectList.size(); i++) {
            ImportDictReq.DictData dictData = new ImportDictReq.DictData();
            dictData.setFDataName(serviceObjectList.get(i).getName());
            dictData.setFDataValue(serviceObjectList.get(i).getCode().toString());
            dictData.setFDataDescription(serviceObjectList.get(i).getRemarks());
            dictData.setFSortOrder(i);
            importDictReq.getDictData().add(dictData);
        }

        importDictReqList.add(importDictReq);
        return feignServer.sendDict(importDictReqList);
    }
}