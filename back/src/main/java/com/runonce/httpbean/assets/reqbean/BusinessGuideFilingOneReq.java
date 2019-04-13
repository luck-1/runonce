package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventversionone.BusinessGuideFilingOne;
import com.runonce.model.eventversionone.BusinessGuideFilingOnePrivate;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author swq
 * @date 2019/1/17 0017
 * @description
 */
@Data
public class BusinessGuideFilingOneReq {

    @Valid
    private BusinessGuideFilingOne businessGuideFilingOne;

    @Valid
    private BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate;

}
