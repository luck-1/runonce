package com.runonce.service.eventversionone;
import com.runonce.model.http.RelatedMaterialsParam;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.ApplicationTypeField;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface ApplicationTypeFieldService extends Service<ApplicationTypeField> {


    void relatedMaterials(RelatedMaterialsParam relatedMaterialsParam);

    void saveInfo(List<RelatedMaterialsParam> modelList);
}
