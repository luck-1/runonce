package com.runonce.httpbean.assets.reqbean;

import com.runonce.httpbean.assets.PageBean;
import lombok.Data;

/**
 * @author swq
 * @date 2019/1/25 0025
 * @description
 */
@Data
public class MatterProposalListInfoReq {

    private String departmentalMattersId;

    private PageBean pageBean;
}
