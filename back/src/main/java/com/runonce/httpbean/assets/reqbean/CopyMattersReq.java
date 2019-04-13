package com.runonce.httpbean.assets.reqbean;

import lombok.Data;

@Data
public class CopyMattersReq {

    private String copyMattersIdFrom;//原事项ID
    private String copyMattersIdTo;//现在事项ID
    private Integer eventType;



}
