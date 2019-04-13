package com.runonce.model.eventversionone;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxc
 * @date 2019/3/18
 * @description 中央业务指导（实施）部门代码
 */
@Data
@Table(name="central_business_guidance_department_code")
public class CentralBusinessGuidanceDepartmentCode implements Serializable {
    private static final long serialVersionUID = 42L;

    @Id
    @ApiModelProperty(value="id")
    @Column(name = "id")
    private String id;


    @ApiModelProperty(value="中央业务指导（实施）部门代码")
    @Column(name = "code")
    private String code;


    @ApiModelProperty(value="中央业务指导（实施）部门名称（简称）")
    @Column(name = "name")
    private String name;


    @ApiModelProperty(value="中央业务指导（实施）部门名称（全称）")
    @Column(name = "fillName")
    private String fillName;

}