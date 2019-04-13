package com.runonce.model.eventversionone;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxc
 * @date 2019-03-01 14:39:25
 * @description 情形引导子项
 */
@Data
@Table(name="situation_child_and_dm_model")
public class SituationChildAndDmModel implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty(value="ID")
    @Column(name = "id")
    private String id;

    /**
     * 情形引导子项Id
     */
    @ApiModelProperty(value="情形引导子项Id")
    @Column(name = "situationChildId")
    private String situationChildId;

    /**
     * 事项id
     */
    @ApiModelProperty(value="事项id")
    @Column(name = "departmentalMattersId")
    private String departmentalMattersId;


}