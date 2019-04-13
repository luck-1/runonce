package com.runonce.service.eventversionone;

import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import com.runonce.model.eventversionone.SituationChildAndDmModel;

/**
 * @author lxc
 * @date 2019-03-01 14:39:25
 * @description 情形引导子项
 */
@Component
public interface SituationChildAndDmModelService extends Service<SituationChildAndDmModel> {


    void  deleteBySituationChildId(String situationChildId);

}