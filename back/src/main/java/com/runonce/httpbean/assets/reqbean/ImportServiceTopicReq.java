package com.runonce.httpbean.assets.reqbean;


import lombok.Data;

import java.util.List;

/**
 * @author swq
 * @date 2019/4/11 0011
 * @description
 */
@Data
public class ImportServiceTopicReq {


    private List<ServiceTopic> serviceTopicList;

    private List<ObjectTopic> objectTopicList;

}
