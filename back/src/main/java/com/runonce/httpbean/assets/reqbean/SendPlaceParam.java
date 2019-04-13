package com.runonce.httpbean.assets.reqbean;

import lombok.Data;
import java.util.List;

@Data
public class SendPlaceParam {

    private Place place;

    private List<Group> groupList;
}
