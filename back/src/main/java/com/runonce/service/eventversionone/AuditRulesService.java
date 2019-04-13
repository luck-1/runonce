package com.runonce.service.eventversionone;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.AuditRules;

import java.util.List;


/**
* 审核细则
*
* Created by xuxueli on '2018-12-18 12:32:51'.
*/
@Component
public interface AuditRulesService extends Service<AuditRules> {


    public  void deleteByMaterialId(String MaterialId);
    public List<AuditRules> selectByMaterialId(String MaterialId, Integer type);

}
