package com.runonce.model.http;

import com.runonce.model.eventversionone.AuditRules;
import lombok.Data;

import java.util.List;

@Data
public class AuditRulesRes {
    private List<AuditRules> Acceptance; //受理人员审批要点
    private List<AuditRules> Approval;//审核人员审批要点
}
