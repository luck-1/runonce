package com.runonce.model.http;

import com.runonce.model.eventpublic.BusinessFileRelation;
import com.runonce.model.eventversionone.AuditRules;
import com.runonce.model.eventversionone.MaterialGrouping;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2018/12/11 0011 上午 11:11
 */
@Data
@Table(name="material_grouping")
public class MaterialGroupAddParam  extends MaterialGrouping implements Serializable{

//    /**
//     * 样表文件对象
//     */
//    private List<BusinessFileRelation> sampleTableMap;
//    /**
//     * 空表文件对象
//     */
//    private List<BusinessFileRelation> emptyTableMap;

    private List<AuditRules> Acceptance; //受理人员审批要点
    private List<AuditRules> Approval;//审核人员审批要点
    @Transient
    private String MaterialGroupingBeforeId; //材料分组之前ID


}
