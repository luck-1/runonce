package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOne;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOnePrivate;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author swq
 * @date 2019/1/17 0017
 * @description
 */
@Data
public class BusinessGuideAdministrativeLicensingOneReq {

    @Valid
    private BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne;

    @Valid
    private BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate;
}
