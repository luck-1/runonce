package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventpublic.EventsTable;
import lombok.Data;

import java.io.Serializable;

@Data
public class JointSectoralMattersReq implements Serializable {

    private EventsTable eventsTable;
    private String deptId;
    private String groupId;
    private Integer isReproducible;

}
