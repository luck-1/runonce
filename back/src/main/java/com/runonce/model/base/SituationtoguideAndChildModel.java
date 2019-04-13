package com.runonce.model.base;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxc
 * @date 2019-03-01 10:22:57
 * @description 情形引导父项和子项中间表
 */
@Data
@Table(name="situationtoguide_and_child_model")
public class SituationtoguideAndChildModel implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 
     */
    @Id
    @ApiModelProperty(value="")
    @Column(name = "id")
    private String id;

    /**
     * 
     */
    @ApiModelProperty(value="")
    @Column(name = "pid")
    private String pid;

    /**
     * 
     */
    @ApiModelProperty(value="")
    @Column(name = "childId")
    private String childId;


}