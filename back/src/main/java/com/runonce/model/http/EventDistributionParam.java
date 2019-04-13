package com.runonce.model.http;

import com.runonce.model.eventpublic.EventsTable;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 事项分配参数
 * @Date: create at 2018/12/13 0013 下午 8:11
 */
@Data
public class EventDistributionParam implements Serializable {

    /**
     * 部门id
     */
    @NotNull
    private String deptId;
    /**
     * 事项id集合
     */
    @NotNull
    private List<List<EventsTable>> eventIdList;
}
