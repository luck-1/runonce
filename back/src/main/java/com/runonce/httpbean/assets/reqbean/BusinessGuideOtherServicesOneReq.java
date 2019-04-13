package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventversionone.BusinessGuideOtherServicesOne;
import com.runonce.model.eventversionone.BusinessGuideOtherServicesOnePrivate;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author swq
 * @date 2019/1/17 0017
 * @description
 */
@Data
public class BusinessGuideOtherServicesOneReq {

    @Valid
    private BusinessGuideOtherServicesOne businessGuideOtherServicesOne;

    @Valid
    private BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate;

}
