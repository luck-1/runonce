package com.runonce.model.eventversiontwo;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Data
@Table(name="material_grouping_two")
public class MaterialGroupingTwo implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 材料id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 事项id
    */
    @Column(name = "eventId")
    private String eventId;

    /**
    * 材料类别
    */
    @Column(name = "type")
    private String type;

    /**
    * 材料编号
    */
    @Column(name = "number")
    private String number;

    /**
    * 输入或输出
    */
    @Column(name = "io")
    private String io;

    /**
    * 名称
    */
    @Column(name = "name")
    private String name;

    /**
    * 原件提供
    */
    @Column(name = "originalScriptProvide")
    private Object originalScriptProvide;

    /**
    * 复印件提供
    */
    @Column(name = "copyProvide")
    private Object copyProvide;

    /**
    * 电子件提供
    */
    @Column(name = "electronicProvide")
    private Object electronicProvide;

    /**
    * 审核输出原件
    */
    @Column(name = "originalScriptOutput")
    private Object originalScriptOutput;

    /**
    * 审核输出电子件
    */
    @Column(name = "electronicOutput")
    private Object electronicOutput;

    /**
    * 材料要求份数
    */
    @Column(name = "count")
    private int count;

    /**
    * 材料提供方式
    */
    @Column(name = "provideWay")
    private String provideWay;

    /**
    * 备注
    */
    @Column(name = "remarks")
    private String remarks;

    /**
    * 本市
    */
    @Column(name = "localCity")
    private Object localCity;

    /**
    * 非本市
    */
    @Column(name = "nonLocalCity")
    private Object nonLocalCity;

    /**
    * 是否精简
    */
    @Column(name = "isStreamlining")
    private Object isStreamlining;


}