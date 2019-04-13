package com.runonce.model.eventpublic;

import lombok.Data;

@Data
public class JointSectoralMatterReq {

    private String deptId;
    private String eventName;
    private String eventState;
    private String eventType;
    private int currentPage;
    private int pageSize;
    private int startRow;

    public Integer getStartRow() {
        this.startRow = (this.currentPage - 1) * this.pageSize;
        return startRow;
    }






}
